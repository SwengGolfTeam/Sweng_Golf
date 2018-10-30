package ch.epfl.sweng.swenggolf.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;

import static ch.epfl.sweng.swenggolf.database.DbError.NONE;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class ProfileActivity extends FragmentConverter {
    private User user;
    private static final int STAR_OFF = android.R.drawable.btn_star_big_off;
    private static final int STAR_ON = android.R.drawable.btn_star_big_on;
    private boolean isFollowing = false;
    private View inflated;
    private MenuItem button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.profile_activity_name);
        inflated = inflater.inflate(R.layout.activity_profile, container, false);
        displayUserData();
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable("ch.epfl.sweng.swenggolf.user");
        if (user == null) {
            throw new NullPointerException("The user given to ProfileActivity can not be null");
        }
    }

    private void displayUserData() {
        TextView name = inflated.findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = inflated.findViewById(R.id.ivProfile);
        displayPicture(imageView, user, this.getContext());
        TextView preference = inflated.findViewById(R.id.preference1);
        preference.setText(user.getPreference());
        TextView description = inflated.findViewById(R.id.description);
        description.setText(user.getDescription());
    }

    private void showFollowButton() {
        User currentUser = Config.getUser();
        String uid = user.getUserId();
        ValueListener<String> listener = new ValueListener<String>() {
            @Override
            public void onDataChange(String value) {
                if (value != null) {
                    setStar(true);
                } else {
                    setStar(false);
                }
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d("DbError", "Could not load the user follow list");
            }
        };

        Database.getInstance().read(Database.FOLLOWERS_PATH + "/" + currentUser.getUserId(),
                uid, listener, String.class);
    }

    private void setStar(boolean follow) {
        int star = follow ? STAR_ON : STAR_OFF;
        button.setIcon(star);
        isFollowing = follow;
    }

    protected static void displayPicture(ImageView imageView, User user, Context context) {
        if (!user.getPhoto().isEmpty()) {
            Uri photoUri = Uri.parse(user.getPhoto());
            Picasso.with(context).load(photoUri).into(imageView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        int id = user.getUserId().equals(Config.getUser().getUserId()) ? R.menu.menu_profile : R.menu.menu_other_user;
        menuInflater.inflate(id, menu);
        if(id == R.menu.menu_other_user){
            button = menu.findItem(R.id.follow);
            showFollowButton();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                openDrawer();
                return true;
            }
            case R.id.edit_profile: {
                replaceCentralFragment(new EditProfileActivity());
                return true;
            }
            case R.id.follow: {
                follow();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /**
     * Follow the user showed in the profile.
     */
    public void follow() {
        User currentUser = Config.getUser();
        if (!isFollowing) {
            addFollow(currentUser);
        } else {
            deleteFollow(currentUser);
        }
    }

    private void deleteFollow(User currentUser) {
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                if (error == NONE) {
                    setStar(false);
                }
            }
        };
        Database.getInstance().remove("/followers/" + currentUser.getUserId(),
                user.getUserId(), listener);
    }

    private void addFollow(User currentUser) {
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(DbError error) {
                if (error == NONE) {
                    button.setIcon(STAR_ON);
                    isFollowing = true;
                    Toast.makeText(ProfileActivity.this.getContext(), getResources()
                                    .getString(R.string.now_following) + " " + user.getUserName(),
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(ProfileActivity.this.getContext(), getResources()
                                    .getString(R.string.error_following) + " " + user.getUserName(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        Database.getInstance().write("/followers/" + currentUser.getUserId(), user.getUserId(),
                user.getUserId(), listener);
    }

    public boolean isFollowing() {
        return isFollowing;
    }
}
