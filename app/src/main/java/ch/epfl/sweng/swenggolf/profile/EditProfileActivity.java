package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

public class EditProfileActivity extends FragmentConverter {
    private User user;
    private int fragmentsToSkip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FUTURE_FRAGMENTS_TO_SKIP)) {
            fragmentsToSkip = bundle.getInt(FUTURE_FRAGMENTS_TO_SKIP);
        } else {
            fragmentsToSkip = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.profile_activity_name);
        View inflated = inflater.inflate(R.layout.activity_edit_profile, container, false);
        inflated.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesAndReturn(v);
            }
        });
        createUserView(inflated);
        return inflated;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText username = findViewById(R.id.edit_name);
        username.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(User.USERNAME_MAX_LENGTH)});
        EditText preferences = findViewById(R.id.edit_pref);
        preferences.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(User.PREFERENCES_MAX_LENGTH)});
        EditText infos = findViewById(R.id.edit_description);
        infos.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(User.INFOS_MAX_LENGTH)});

    }

    private void createUserView(View inflated) {
        user = Config.getUser();
        if (user != null) {
            displayElement((EditText) inflated.findViewById(R.id.edit_name),
                    user.getUserName());
            displayElement((EditText) inflated.findViewById(R.id.edit_pref),
                    user.getPreference());
            displayElement((EditText) inflated.findViewById(R.id.edit_description),
                    user.getDescription());
            ImageView imageView = inflated.findViewById(R.id.ivProfile);
            ProfileActivity.displayPicture(imageView, user, this.getContext());
        }
        inflated.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChangesAndReturn(v);
            }
        });
        inflated.findViewById(R.id.photoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilePicture(v);
            }
        });
    }

    private void displayElement(EditText editText, String elem) {
        editText.setText(elem);
        editText.setSelection(elem.length());
    }

    /**
     * Saves the changes and returns to the profile activity.
     */
    public void saveChangesAndReturn(View view) {
        // Get new user data
        EditText editedName = findViewById(R.id.edit_name);
        String name = editedName.getText().toString();
        EditText editedPref = findViewById(R.id.edit_pref);
        String pref = editedPref.getText().toString();
        EditText editedDescription = findViewById(R.id.edit_description);
        String description = editedDescription.getText().toString();
        if (name.length() < User.USERNAME_MIN_LENGTH) {
            editedName.setError(getResources()
                    .getString(R.string.username_min_length, User.USERNAME_MIN_LENGTH));

        } else { //update user data
            user.setDescription(description);
            user.setUserName(name);
            user.setPreference(pref);
            DatabaseUser.addUser(user);

            replaceCentralFragment(FragmentConverter.createShowProfileWithProfile(user,
                    fragmentsToSkip + 2));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                getFragmentManager().popBackStack();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public void changeProfilePicture(View view) {
        startActivityForResult(Storage.choosePicture(), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Storage.conditionActivityResult(requestCode, resultCode, data)) {
            Uri filePath = data.getData();

            Storage.getInstance().write(filePath, "images/user/" + user.getUserId(),
                    getChangePpListener());
        }


    }

    private OnCompleteListener<Uri> getChangePpListener() {
        return new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String link = task.getResult().toString();
                    user.setPhoto(link);
                    DatabaseUser.addUser(user);
                    updatePicture();
                } else {
                    // TODO Handle failures
                }
            }
        };
    }

    private void updatePicture() {
        ImageView imageView = findViewById(R.id.ivProfile);
        ProfileActivity.displayPicture(imageView, user, this.getContext());
    }
}
