package ch.epfl.sweng.swenggolf.offer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ch.epfl.sweng.swenggolf.R;


public class ShowOfferActivity extends AppCompatActivity {

    private Offer offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        offer = getIntent().getParcelableExtra("offer");

        setContents();

        // TODO implement username recognition
    }

    private void setContents() {
        TextView offerTitle = findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        TextView offerAuthor = findViewById(R.id.show_offer_author);
        offerAuthor.setText(offer.getAuthor());

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
        // TODO implement username when login effective
        intent.putExtra("username", offer.getAuthor());
        intent.putExtra("offer", offer);
        startActivity(intent);
    }
}
