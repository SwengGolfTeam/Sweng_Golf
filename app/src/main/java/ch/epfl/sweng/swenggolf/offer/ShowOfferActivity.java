package ch.epfl.sweng.swenggolf.offer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

        if (offer.getLinkPicture() != null && !offer.getLinkPicture().isEmpty()) {
            ImageView offerPicture = findViewById(R.id.show_offer_picture);
            Picasso.with(this).load(Uri.parse(offer.getLinkPicture())).into(offerPicture);
        }
    }

    public void modifyOffer(View view) {
        Intent intent = new Intent(this, CreateOfferActivity.class);
        // TODO implement username when login effective
        intent.putExtra("username", offer.getAuthor());
        intent.putExtra("offer", offer);
        startActivity(intent);
    }
}
