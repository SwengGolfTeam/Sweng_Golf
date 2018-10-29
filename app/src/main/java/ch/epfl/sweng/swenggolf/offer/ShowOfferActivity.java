package ch.epfl.sweng.swenggolf.offer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.DialogInterface;

import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;

import ch.epfl.sweng.swenggolf.tools.FragmentConverter;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.ProfileActivity;
import ch.epfl.sweng.swenggolf.storage.Storage;

import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class ShowOfferActivity extends FragmentConverter {

    private boolean userIsCreator;
    private Offer offer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.button_show_offers);
        assert getArguments() != null;
        View inflated = inflater.inflate(R.layout.activity_show_offer, container, false);
        userIsCreator = Config.getUser().getUserId().equals(offer.getUserId());
        setContents(inflated);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        offer = getArguments().getParcelable("offer");
    }

    private void setContents(View inflated) {
        TextView offerTitle = inflated.findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        final TextView offerAuthor = inflated.findViewById(R.id.show_offer_author);
        ViewUserFiller.fillWithUsername(offerAuthor, offer.getUserId());

        TextView offerDescription = inflated.findViewById(R.id.show_offer_description);
        offerDescription.setText(offer.getDescription());

        if (!offer.getLinkPicture().isEmpty()) {
            ImageView offerPicture = inflated.findViewById(R.id.show_offer_picture);
            Picasso.with(this.getContext()).load(Uri.parse(offer.getLinkPicture())).into(offerPicture);
        }
    }

    /**
     * Launches the CreateOfferActivity using the current offer, which will trigger
     * subsequent parameters that will be used to modify it.
     */
    public void modifyOffer() {
        CreateOfferActivity createFrag = new CreateOfferActivity();
        Bundle createBundle = new Bundle();
        createBundle.putParcelable("offer", offer);
        createFrag.setArguments(createBundle);
        replaceCentralFragment(createFrag);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(userIsCreator) {
            inflater.inflate(R.menu.menu_show_offer, menu);
        } else {
            inflater.inflate(R.menu.menu_empty, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
                switch (item.getItemId()) {
                    case android.R.id.home: {
                        getFragmentManager().beginTransaction().replace(R.id.centralFragment, new ListOfferActivity()).commit();
                        break;
                    }
                    case R.id.button_modify_offer: {
                        modifyOffer();
                        break;
                    }
                    case R.id.button_delete_offer: {
                        deleteOffer();
                        break;
                    }
                }
                return super.onOptionsItemSelected(item);
            }
    /**
     * Launches the DeleteOfferActivity using the current offer.
     */
    public void deleteOffer() {
        showDeleteAlertDialog();
    }

    /**
     * Display the Alert Dialog for the delete.
     */
    public void showDeleteAlertDialog() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this offer?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOfferInDatabase();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Delete the offer in the database.
     */
    private void deleteOfferInDatabase() {
        Storage storage = Storage.getInstance();
        storage.remove(offer.getLinkPicture());
        Database database = Database.getInstance();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(getContext(), R.string.offer_deleted,
                            Toast.LENGTH_SHORT).show();
                    replaceCentralFragment(new ListOfferActivity());
                }
            }

        };
        database.remove("/offers", offer.getUuid(), listener);
    }

    /**
     * Open the user profile when we click on his name.
     * @param v the view
     */
    public void openUserProfile(View v) {

        DatabaseUser.getUser(new ValueListener<User>() {
                                 @Override
                                 public void onDataChange(User user) {
                                     Bundle bundle = new Bundle();
                                     bundle.putParcelable("ch.epfl.sweng.swenggolf.user", user);
                                     Fragment profileActivity = new ProfileActivity();
                                     profileActivity.setArguments(bundle);
                                     replaceCentralFragment(profileActivity);
                                 }

                                 @Override
                                 public void onCancelled(DbError error) {
                                     Toast.makeText(ShowOfferActivity.this.getContext(),
                                             R.string.error_load_user, Toast.LENGTH_LONG).show();
                                 }
                             },
                 offer.getUserId());

    }

}
