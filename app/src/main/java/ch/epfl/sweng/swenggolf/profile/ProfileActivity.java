package ch.epfl.sweng.swenggolf.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
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
        setHasOptionsMenu(true);
        setHomeIcon(R.drawable.ic_menu);
        View inflated = inflater.inflate(R.layout.activity_profile, container, false);
        displayUserData(inflated);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Config.getUser();
    }

    private void displayUserData(View view) {
        TextView name = view.findViewById(R.id.name);
        name.setText(user.getUserName());
        ImageView imageView = view.findViewById(R.id.ivProfile);
        displayPicture(imageView, user, this.getContext());

        // TODO count the number of offers posted+answered and display them

    }

    protected static void displayPicture(ImageView imageView, User user, Context context) {
        if (!user.getPhoto().isEmpty()) {
            Uri photoUri = Uri.parse(user.getPhoto());
            Picasso.with(context).load(photoUri).into(imageView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_profile,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                openDrawer();
                break;
            }
            case R.id.edit_profile : {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.centralFragment, new EditProfileActivity());
                transaction.commit();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
