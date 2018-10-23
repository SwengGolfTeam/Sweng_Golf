package ch.epfl.sweng.swenggolf.offer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

    private boolean userIsCreator;
    private Offer offer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setHomeIcon(R.drawable.ic_baseline_arrow_back_24px);
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

    private void setContents(View view) {
        TextView offerTitle = view.findViewById(R.id.show_offer_title);
        offerTitle.setText(offer.getTitle());

        final TextView offerAuthor = view.findViewById(R.id.show_offer_author);
        ViewUserFiller.fillWithUsername(offerAuthor, offer.getUserId());

        TextView offerDescription = view.findViewById(R.id.show_offer_description);
        offerDescription.setText(offer.getDescription());

        if (!offer.getLinkPicture().isEmpty()) {
            ImageView offerPicture = view.findViewById(R.id.show_offer_picture);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                getFragmentManager().beginTransaction().replace(R.id.centralFragment,getFragmentManager().findFragmentByTag("list_offer")).commit();
                break;
            }
            case R.id.button_modify_offer : {
                modifyOffer();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
