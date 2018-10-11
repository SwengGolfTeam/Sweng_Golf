package ch.epfl.sweng.swenggolf.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;

public class ListOfferActivity extends Activity {
    private RecyclerView.Adapter mAdapter;
    public static final List<Offer> offerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offer);

        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.offers_recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListOfferAdapter(offerList);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        offerList.clear();
        prepareOfferData();

        mRecyclerView.addOnItemTouchListener(listOfferTouchListener(mRecyclerView));
    }

    private ListOfferTouchListener listOfferTouchListener(RecyclerView mRecyclerView) {
        return new ListOfferTouchListener(this, mRecyclerView,
                new ListOfferTouchListener.OnItemClickListener() {
                    private TextView offerOpenedView = null;
                    private Offer offerOpened = null;

                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent =
                                new Intent(ListOfferActivity.this,
                                        ShowOfferActivity.class);
                        Offer offer = offerList.get(position);
                        intent.putExtra("offer", offer);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Expands or retract the description
                        TextView descriptionView = view.findViewById(R.id.offer_description);
                        Offer currentOffer = offerList.get(position);
                        expandOrRetractOffer(descriptionView, currentOffer);
                    }

                    /**
                     * Expands or retract the offer after a long touch. Closes all other opened
                     * offers in the list.
                     *
                     * @param element the TextView containing the information about the offer
                     * @param offer the offer
                     */
                    private void expandOrRetractOffer(TextView element, Offer offer) {
                        // Fetch all necessary strings to compare and set
                        String actualDescription = element.getText().toString();
                        String originalDescription = offer.getDescription();

                        if (actualDescription.equals(originalDescription)) {
                            element.setText(offer.getShortDescription());
                            offerOpenedView = null;
                            offerOpened = null;

                        } else {
                            element.setText(originalDescription);
                            if (offerOpened != null) {
                                expandOrRetractOffer(offerOpenedView, offerOpened);
                            }
                            offerOpenedView = element;
                            offerOpened = offer;

                        }
                    }
                });
    }

    /**
     * Creates dummy data to list.
     */
    private void prepareOfferData() {
        String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
                + "Nam ut quam ornare, fringilla nunc eget, facilisis lectus."
                + "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo."
                + "Suspendisse potenti. Morbi vehicula ante nibh,"
                + " at tristique tortor dignissim non."
                + "In sit amet ligula tempus, mattis massa dictum, mollis sem."
                + "Mauris convallis sed mauris ut sodales."
                + "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat."
                + "Fusce in nibh pharetra nunc pellentesque tempor id interdum est."
                + "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";

        Offer offer = new Offer("Robin", "6-pack beers for ModStoch homework", lorem);
        offerList.add(offer);

        offer = new Offer("Eric", "Chocolate for tractor", lorem);
        offerList.add(offer);

        offer = new Offer("Ugo", "ModStoch help for food", lorem);
        offerList.add(offer);

        offer = new Offer("Elsa", "Pizzas for beer", lorem);
        offerList.add(offer);

        offer = new Offer("Seb", "Everything for a canton that doesn't suck and some "
                + "more text to overflow the box", lorem);
        offerList.add(offer);

        offer = new Offer("Markus", "My kingdom for a working DB", lorem);
        offerList.add(offer);

        mAdapter.notifyDataSetChanged();

        // TODO: Read from database and display it (with DatabaseConnection & readOffers() function)
    }
}
