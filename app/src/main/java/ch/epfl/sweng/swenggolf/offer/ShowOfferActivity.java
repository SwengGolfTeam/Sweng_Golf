package ch.epfl.sweng.swenggolf.offer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class ShowOfferActivity extends FragmentConverter {

    private Offer offer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        assert getArguments() != null;
        offer = getArguments().getParcelable("offer");
        View inflated = inflater.inflate(R.layout.activity_show_offer, container, false);
        if(!Config.getUser().getUserId().equals(offer.getUserId())){
            ImageView button = inflated.findViewById(R.id.button_modify_offer);
            button.setVisibility(View.INVISIBLE);
            button.setClickable(false);
        }

        setContents(inflated);
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        replaceCentralFragment(createFrag);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_show_offer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                getFragmentManager().popBackStack();
            }
            case R.id.button_modify_offer : {
                modifyOffer();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
