package ch.epfl.sweng.swenggolf.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;


public class EditProfileActivity extends FragmentConverter {
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_edit_profile, container, false);
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

    @Override
    public boolean onSelectedOptionsMenu(MenuItem item) {
        switch (item.getItemId()) {

        }
    }

    /**
     * Saves the changes and returns to the profile activity.
     */
    public void saveChangesAndReturn() {
        EditText editText = findViewById(R.id.edit_name);
        String name = editText.getText().toString();

        user.setUserName(name);
        DatabaseUser.addUser(user);

        replaceCentralFragment(new ProfileActivity());
    }

}
