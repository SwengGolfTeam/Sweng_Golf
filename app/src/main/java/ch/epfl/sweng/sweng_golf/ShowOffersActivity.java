package ch.epfl.sweng.sweng_golf;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShowOffersActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Offer> offerList = new ArrayList<>();;
    private String lorem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
            "Nam ut quam ornare, fringilla nunc eget, facilisis lectus." +
            "Curabitur ut nunc nec est feugiat commodo. Nulla vel porttitor justo." +
            "Suspendisse potenti. Morbi vehicula ante nibh, at tristique tortor dignissim non." +
            "In sit amet ligula tempus, mattis massa dictum, mollis sem." +
            "Mauris convallis sed mauris ut sodales." +
            "Nullam tristique vel nisi a rutrum. Sed commodo nec libero sed volutpat." +
            "Fusce in nibh pharetra nunc pellentesque tempor id interdum est." +
            "Sed rutrum mauris in ipsum consequat, nec scelerisque nulla facilisis.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offers);

        mRecyclerView = findViewById(R.id.offers_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ShowOffersAdapter(offerList);
        mRecyclerView.setAdapter(mAdapter);

        prepareOfferData();

    }

    /**
     * Creates dummy data to list.
     */
    private void prepareOfferData() {
        Offer offer = new Offer("Robin", "6-pack beers for ModStoch homework", lorem);
        offerList.add(offer);
        offer = new Offer("Eric", "Chocolate for tractor", lorem);
        offerList.add(offer);
        offer = new Offer("Ugo", "ModStoch help for food", lorem);
        offerList.add(offer);
        offer = new Offer("Elsa", "Pizzas for beer", lorem);
        offerList.add(offer);
        offer = new Offer("Seb", "Everything for a canton that doesn't suck", lorem);
        offerList.add(offer);
        offer = new Offer("Markus", "My kingdom for a working DB", lorem);
        offerList.add(offer);

        mAdapter.notifyDataSetChanged();
    }
}
