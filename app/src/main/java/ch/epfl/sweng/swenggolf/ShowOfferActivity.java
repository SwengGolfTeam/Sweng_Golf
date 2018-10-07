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

        TextView offer_title = findViewById(R.id.show_offer_title);
        offer_title.setText(offer.getTitle());

        TextView offer_author = findViewById(R.id.show_offer_author);
        offer_author.setText(offer.getAuthor());

        TextView offer_description = findViewById(R.id.show_offer_description);
        offer_description.setText(offer.getDescription());
    }
}
