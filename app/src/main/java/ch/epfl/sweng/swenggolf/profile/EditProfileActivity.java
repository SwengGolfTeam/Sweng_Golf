package ch.epfl.sweng.swenggolf.profile;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class EditProfileActivity extends FragmentConverter {
    private User user;

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

    public void createUserView(View inflated) {
        user = Config.getUser();
        if (user != null) {
            displayElement((EditText) inflated.findViewById(R.id.edit_name), user.getUserName());
            displayElement((EditText) inflated.findViewById(R.id.edit_pref), user.getPreference());
            displayElement((EditText) inflated.findViewById(R.id.edit_description), user.getDescription());
            ImageView imageView = inflated.findViewById(R.id.ivProfile);
            ProfileActivity.displayPicture(imageView, user, this.getContext());
        }
    }

    private void displayElement(EditText editText, String elem) {
        editText.setText(elem);
        editText.setSelection(elem.length());
    }

    /**
     * Saves the changes and returns to the profile activity.
     */
    public void saveChangesAndReturn(View view) {
        // save new name
        EditText editedName = findViewById(R.id.edit_name);
        String name = editedName.getText().toString();
        user.setUserName(name);

        // save new preferences
        EditText editedPref = findViewById(R.id.edit_pref);
        String pref = editedPref.getText().toString();
        user.setPreference(pref);

        // save new preferences
        EditText editedDescription = findViewById(R.id.edit_description);
        String description = editedDescription.getText().toString();
        user.setDescription(description);

        DatabaseUser.addUser(user);

        Fragment ProfileActivity = new ProfileActivity();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ch.epfl.sweng.swenggolf.user", user);
        ProfileActivity.setArguments(bundle);
        replaceCentralFragment(new ProfileActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                replaceCentralFragment(new ProfileActivity());
                break;
            }
        }
        return super.onOptionsItemSelected(item);
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
