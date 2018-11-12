package ch.epfl.sweng.swenggolf.offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class ListOfferActivity extends FragmentConverter {

    private static final String LOG_LOCAL_DB = "LOCAL DATABASE";
    private ListOfferAdapter mAdapter;
    private Menu mOptionsMenu;
    protected RecyclerView.LayoutManager mLayoutManager;
    private TextView errorMessage;
    private TextView noOffers;
    public static final List<Offer> offerList = new ArrayList<>();
    private LocalDatabase localDb;
    private List<Category> checkedCategories = Arrays.asList(Category.values());

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.offers);
        View inflated = inflater.inflate(R.layout.activity_list_offer, container, false);

        localDb = new LocalDatabase(this.getContext(), null, 1);
        try {
            Log.d(LOG_LOCAL_DB, "Recover from database");
            checkedCategories = Category.singleStringToCategories(localDb.readCategories());
            Log.d(LOG_LOCAL_DB, "Recovered " + checkedCategories.toString());
        } catch (Exception e) {
            Log.d(LOG_LOCAL_DB, "Initial write with allCategories");
            localDb.writeCategories(checkedCategories); // by default is allCategories
        }

        errorMessage = inflated.findViewById(R.id.error_message);
        noOffers = inflated.findViewById(R.id.no_offers_to_show);
        setRecyclerView(inflated, checkedCategories);
        return inflated;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_list_offers, menu);
        addAllCategoriesToMenu(R.id.menu_offers);
    }

    private void addAllCategoriesToMenu(int groupId) {
        Category[] categoriesEnum = Category.values();
        for (int i = 0; i < categoriesEnum.length; i++) {
            if (checkedCategories.contains(categoriesEnum[i])) {
                mOptionsMenu.add(groupId, i, Menu.NONE, categoriesEnum[i].toString())
                        .setCheckable(true).setChecked(true);
            }
            else {
                mOptionsMenu.add(groupId, i, Menu.NONE, categoriesEnum[i].toString())
                        .setCheckable(true).setChecked(false);
            }
        }
    }

    private void onCheck(MenuItem item) {
        item.setChecked(!item.isChecked()); // true <-> false
        List<Category> listCategories = new ArrayList<>();

        for (int i = 0; i < Category.values().length; i++) {
            if (mOptionsMenu.getItem(i).isChecked()) {
                listCategories.add(Category.values()[i]);
            }
        }

        localDb.writeCategories(listCategories);
        Log.d(LOG_LOCAL_DB, "write "+ listCategories.toString());
        setRecyclerView(getView(), listCategories);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                openDrawer();
                break;
            }
            default: {
                onCheck(item);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRecyclerView(View inflated, List<Category> categories) {
        noOffers.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = inflated.findViewById(R.id.offers_recycler_view);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListOfferAdapter(offerList);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        offerList.clear();
        prepareOfferData(inflated, categories);

        mRecyclerView.addOnItemTouchListener(listOfferTouchListener(mRecyclerView));
    }

    private ListOfferTouchListener listOfferTouchListener(RecyclerView mRecyclerView) {
        return new ListOfferTouchListener(this.getContext(), mRecyclerView, clickListener);
    }

    /**
     * Get the offers from the database.
     */
    private void prepareOfferData(final View inflated, List<Category> categories) {
        Database database = Database.getInstance();
        inflated.findViewById(R.id.offer_list_loading).setVisibility(View.VISIBLE);
        ValueListener listener = new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                inflated.findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
                if (!offers.isEmpty()) {
                    noOffers.setVisibility(View.GONE);
                    mAdapter.add(offers);
                }

            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load offers from database");
                inflated.findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
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
                    Offer showOffer = offerList.get(position);
                    replaceCentralFragment(FragmentConverter.createShowOfferWithOffer(showOffer));
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
