package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowOfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        Offer offer = getIntent().getParcelableExtra("offer");

        TextView offerTitle = findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        TextView offerAuthor = findViewById(R.id.show_offer_author);
        offerAuthor.setText(offer.getAuthor());

        TextView offerDescription = findViewById(R.id.show_offer_description);
        offerDescription.setText(offer.getDescription());
    }
}
