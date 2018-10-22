package ch.epfl.sweng.swenggolf.offer;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);
        offer = getIntent().getParcelableExtra("offer");
        if (!Config.getUser().getUserId().equals(offer.getUserId())) {
            ImageView button = findViewById(R.id.button_modify_offer);
            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
        }

        setContents();
    }

    private void setContents() {
        TextView offerTitle = findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        final TextView offerAuthor = findViewById(R.id.show_offer_author);
        ViewUserFiller.fillWithUsername(offerAuthor, offer.getUserId());

        TextView offerDescription = findViewById(R.id.show_offer_description);
        offerDescription.setText(offer.getDescription());

        if (!offer.getLinkPicture().isEmpty()) {
            ImageView offerPicture = findViewById(R.id.show_offer_picture);
            Picasso.with(this).load(Uri.parse(offer.getLinkPicture())).into(offerPicture);
        }
    }

    /**
     * Launches the CreateOfferActivity using the current offer, which will trigger
     * subsequent parameters that will be used to modify it.
     *
     * @param view the view
     */
    public void modifyOffer(View view) {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        intent.putExtra("offer", offer);
        startActivity(intent);
    }

    /**
     * Launches the DeleteOfferActivity using the current offer.
     *
     * @param view the view
     */
    public void deleteOffer(View view) {
        showDeleteAlertDialog();
    }

    /**
     * Display the Alert Dialog for the delete.
     */
    public void showDeleteAlertDialog(){
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
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
    private void deleteOfferInDatabase(){
        Database database = Database.getInstance();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(ShowOfferActivity.this, R.string.offer_deleted,
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowOfferActivity.this, ListOfferActivity.class));
                } else {
                    Toast.makeText(ShowOfferActivity.this, R.string.offer_couldnt_be_deleted,
                            Toast.LENGTH_SHORT).show();
                }
            }

        };
        database.remove("/offers", offer.getUuid(), listener);
    }

}
