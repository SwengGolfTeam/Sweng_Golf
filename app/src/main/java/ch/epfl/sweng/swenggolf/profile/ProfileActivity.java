package ch.epfl.sweng.swenggolf.profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;


public class ProfileActivity extends FragmentConverter {

    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.profile_activity_name);
        View inflated = inflater.inflate(R.layout.activity_profile, container, false);
        displayUserData(inflated);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Config.getUser();
    }

    private void displayUserData(View inflated) {

        user = getArguments().getParcelable("ch.epfl.sweng.swenggolf.user");
        if (user == null) {
            throw new NullPointerException("The user given to ProfileActivity can not be null");
        }

        TextView name = inflated.findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = inflated.findViewById(R.id.ivProfile);
        displayPicture(imageView, user, this.getContext());
        TextView preference = inflated.findViewById(R.id.preference1);
        preference.setText(user.getPreference());

        // add back arrow to toolbar
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.profile_activity_name);

    }

    protected static void displayPicture(ImageView imageView, User user, Context context) {
        if (!user.getPhoto().isEmpty()) {
            Uri photoUri = Uri.parse(user.getPhoto());
            Picasso.with(context).load(photoUri).into(imageView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        int id = user.equals(Config.getUser()) ? R.menu.menu_profile : R.menu.menu_empty;
        menuInflater.inflate(id, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                openDrawer();
                break;
            }
            case R.id.edit_profile : {
                replaceCentralFragment(new EditProfileActivity());
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
