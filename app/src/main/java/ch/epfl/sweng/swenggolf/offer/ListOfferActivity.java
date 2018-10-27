package ch.epfl.sweng.swenggolf.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

public class ListOfferActivity extends AppCompatActivity {

    private ListOfferAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private TextView errorMessage;
    public static final List<Offer> offerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offer);
        errorMessage = findViewById(R.id.error_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHome = new Intent(ListOfferActivity.this, MainMenuActivity.class);
                startActivity(backHome);
            }
        });

        setRecyclerView();
    }

    @Override //TODO WTF?
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.offers_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
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
        return new ListOfferTouchListener(this, mRecyclerView, clickListener);
    }

    /**
     * Get the offers from the database.
     */
    private void prepareOfferData() {
        Database database = Database.getInstance();
        ValueListener listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                mAdapter.add(offers);
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load offers from database");
                errorMessage.setVisibility(View.VISIBLE);
            }
        };
        //database.readOffers(listener);
        List<Category> categories = Arrays.asList(Category.values());
        database.getByCategory(categories, listener);
    }

    private final ListOfferTouchListener.OnItemClickListener clickListener =
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
                        // Need to close the offer because the current offer is expanded
                        changeDescription(element, offer);
                        changeDescription(offerOpenedView, offerOpened);
                        offerOpenedView = null;
                        offerOpened = null;
                    } else {
                        element.setText(originalDescription);
                        changeDescription(offerOpenedView, offerOpened);
                        offerOpenedView = element;
                        offerOpened = offer;
                    }
                }

                private void changeDescription(TextView element, Offer offer) {
                    if (element != null && offer != null) {
                        element.setText(offer.getShortDescription());
                    }
                }
            };
}
