package ch.epfl.sweng.swenggolf.profile;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;

import ch.epfl.sweng.swenggolf.tools.FragmentConverter;


public class EditProfileActivity extends FragmentConverter {
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHomeIcon(R.drawable.ic_baseline_arrow_back_24px);
        setHasOptionsMenu(true);
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

    public void createUserView(View view) {
        user = Config.getUser();
        if (user != null) {
            EditText editText = view.findViewById(R.id.edit_name);
            String userName = user.getUserName();
            editText.setText(userName);
            editText.setSelection(userName.length());

            ImageView imageView = view.findViewById(R.id.ivProfile);
            ProfileActivity.displayPicture(imageView, user, this.getContext());
        }
    }

    /**
     * Saves the changes and returns to the profile activity.
     */
    public void saveChangesAndReturn(View view) {
        EditText editText = findViewById(R.id.edit_name);
        String name = editText.getText().toString();

        user.setUserName(name);
        DatabaseUser.addUser(user);

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
}
