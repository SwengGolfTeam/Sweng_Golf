package ch.epfl.sweng.swenggolf.offer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
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
    private Menu mOptionsMenu;
    protected RecyclerView.LayoutManager mLayoutManager;
    private TextView errorMessage;
    private TextView noOffers;
    public static final List<Offer> offerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offer);
        errorMessage = findViewById(R.id.error_message);
        noOffers = findViewById(R.id.no_offers_to_show);

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
        List<Category> allCategories = Arrays.asList(Category.values()); // by default
        setRecyclerView(allCategories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_list_offers, menu);
        addAllCategoriesToMenu(R.id.menu_offers);
        return true;
    }

    private void addAllCategoriesToMenu(int groupId){
        Category[] categoriesEnum = Category.values();
        for (int i=0; i<categoriesEnum.length; i++) {
            mOptionsMenu.add(groupId, i, Menu.NONE, categoriesEnum[i].toString()).setCheckable(true).setChecked(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.isChecked()){
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }
        List<Category> listCategories = new ArrayList<>();
        Category[] catEnum = Category.values();
        for(int i=0; i<catEnum.length; i++){
            if(mOptionsMenu.getItem(i).isChecked()){
                listCategories.add(catEnum[i]);
            }
        }
        setRecyclerView(listCategories);
        return false;
    }

    private void setRecyclerView(List<Category> categories) {
        noOffers.setVisibility(View.VISIBLE);

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
        prepareOfferData(categories);

        mRecyclerView.addOnItemTouchListener(listOfferTouchListener(mRecyclerView));
    }

    private ListOfferTouchListener listOfferTouchListener(RecyclerView mRecyclerView) {
        return new ListOfferTouchListener(this, mRecyclerView, clickListener);
    }

    /**
     * Get the offers from the database.
     */
    private void prepareOfferData(List<Category> categories) {
        Database database = Database.getInstance();
        findViewById(R.id.offer_list_loading).setVisibility(View.VISIBLE);
        ValueListener listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
                if (!offers.isEmpty()){
                    noOffers.setVisibility(View.GONE);
                    mAdapter.add(offers);
                }

            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load offers from database");
                findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        };
        database.readOffers(listener, categories);
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
