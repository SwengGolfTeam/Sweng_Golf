package ch.epfl.sweng.swenggolf.offer.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.LocalDatabase;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.network.Network;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.ShowOfferFragment;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

/**
 * Fragment which shows the offers stored in the Database.
 */
public class ListOfferFragment extends FragmentConverter {
    public static final String DISPLAY_CLOSED_BUNDLE_KEY =
            "ch.epfl.sweng.swenggolf.listOfferActivity";
    private static final String LOG_LOCAL_DB = "LOCAL DATABASE";

    private final ListOfferTouchListener.OnItemClickListener clickListener =
            new ListOfferTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {
                    closeSoftKeyboard(ListOfferFragment.this.search);
                    Offer showOffer = mAdapter.getOffer(position);
                    ShowOfferFragment show = FragmentConverter.createShowOfferWithOffer(showOffer);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.centralFragment, show);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                @Override
                public void onLongItemClick(View view, int position) {
                    // Expands or retract the description
                    TextView descriptionView = view.findViewById(R.id.offer_description);
                    Offer currentOffer = mAdapter.getOffer(position);
                    expandOrRetractOffer(descriptionView, currentOffer);
                }

                /**
                 * Expands or retract the offer after a long touch.
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
                    } else {
                        element.setText(originalDescription);
                    }
                }
            };

    private ListOfferAdapter mAdapter;
    private Menu mOptionsMenu;
    private TextView errorMessage;
    private TextView noOffers;
    private LocalDatabase localDb;
    private List<Category> checkedCategories = new ArrayList<>(Arrays.asList(Category.values()));
    private RecyclerView mRecyclerView;
    private EditText search;
    private boolean displayClosed;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        Log.d("ListOfferCreation : ", "View created");
        final View inflated = inflater.inflate(R.layout.activity_list_offer, container, false);

        setToolbar(R.drawable.ic_menu_black_24dp, R.string.offers);
        if (getArguments() != null) {
            displayClosed = getArguments().getBoolean(DISPLAY_CLOSED_BUNDLE_KEY);
            User offersBelongTo = getArguments().getParcelable(User.USER);
            if (offersBelongTo != null) {
                setToolbar(R.drawable.ic_menu_black_24dp,
                        offersBelongTo.getUserName() + "'s offers");
            }
        }

        localDb = new LocalDatabase(this.getContext(), null, 1);
        loadCheckedCategories();

        errorMessage = inflated.findViewById(R.id.error_message);
        noOffers = inflated.findViewById(R.id.no_offers_to_show);
        setRecyclerView(inflated, checkedCategories);
        setRefreshListener(inflated);
        return inflated;
    }

    private void loadCheckedCategories() {
        try {
            Log.d(LOG_LOCAL_DB, "Recover from database");
            checkedCategories = localDb.readCategories();
            Log.d(LOG_LOCAL_DB, "Recovered " + checkedCategories.toString());
        } catch (Exception e) {
            Log.d(LOG_LOCAL_DB, "Initial write with allCategories");
            localDb.writeCategories(checkedCategories); // by default is allCategories
        }
    }

    private void setRefreshListener(final View inflated) {
        final SwipeRefreshLayout refresher = inflated.findViewById(R.id.refresh_list_offer);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData(inflated, checkedCategories);
                refresher.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mOptionsMenu = menu;
        inflater.inflate(R.menu.menu_list_offers, menu);
        HashSet<Category> previouslyCheckedCategories = new HashSet<>(checkedCategories);
        addAllCategoriesToMenu(R.id.menu_offers);
        if (!previouslyCheckedCategories.equals(new HashSet<>(checkedCategories))) {
            updateData(getView(), checkedCategories);
        }
    }

    protected void addAllCategoriesToMenu(int groupId) {
        mOptionsMenu.clear();
        loadCheckedCategories();
        Category[] categoriesEnum = Category.values();
        for (int i = 0; i < categoriesEnum.length; i++) {
            if (checkedCategories.contains(categoriesEnum[i])) {
                mOptionsMenu.add(groupId, i, Menu.NONE, categoriesEnum[i].toString())
                        .setCheckable(true).setChecked(true);
            } else {
                mOptionsMenu.add(groupId, i, Menu.NONE, categoriesEnum[i].toString())
                        .setCheckable(true).setChecked(false);
            }
        }
    }

    private void onCheck(MenuItem item) {
        item.setChecked(!item.isChecked()); // true <-> false
        checkedCategories.clear();

        for (int i = 0; i < Category.values().length; ++i) {
            if (mOptionsMenu.getItem(i).isChecked()) {
                checkedCategories.add(Category.values()[i]);
            }
        }

        localDb.writeCategories(checkedCategories);
        Log.d(LOG_LOCAL_DB, "write " + checkedCategories.toString());
        updateData(getView(), checkedCategories);
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

    private void setRecyclerView(View inflated, final List<Category> categories) {
        mRecyclerView = inflated.findViewById(R.id.offers_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListOfferAdapter();
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        updateData(inflated, categories);

        mRecyclerView.addOnItemTouchListener(
                new ListOfferTouchListener(this.getContext(), mRecyclerView, clickListener));
        setupSearch(inflated);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void updateData(View inflated, List<Category> categories) {
        mAdapter.clear();
        if (categories.isEmpty()) {
            noOffers.setVisibility(View.VISIBLE);
            inflated.findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
        } else {
            noOffers.setVisibility(View.GONE);
            DatabaseOfferConsumer dbConsumer = new DatabaseOfferConsumer() {
                @Override
                public void accept(Database db, List<Category> categories,
                                   ValueListener<List<Offer>> listener) {
                    db.readOffers(listener, categories);
                }
            };
            prepareOfferData(displayClosed, inflated, dbConsumer, categories);
        }

    }

    private void setupSearch(View inflated) {
        search = inflated.findViewById(R.id.search_bar);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int visibility = mAdapter.filter(s.toString()) ? View.VISIBLE : View.GONE;
                noOffers.setVisibility(visibility);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing
            }
        });
    }

    /**
     * Get the offers from the database.
     */
    protected void prepareOfferData(final boolean displayClosed, final View inflated,
                                    DatabaseOfferConsumer dbConsumer, List<Category> categories) {
        Database database = Database.getInstance();
        inflated.findViewById(R.id.offer_list_loading).setVisibility(View.VISIBLE);

        ValueListener listener = onOfferFetched(inflated);
        dbConsumer.accept(database, categories, listener);
        Network.checkAndDialog(getContext());
    }

    private ValueListener<List<Offer>> onOfferFetched(final View inflated) {
        return new ValueListener<List<Offer>>() {
            @Override
            public void onDataChange(List<Offer> offers) {
                errorMessage.setVisibility(View.GONE);
                inflated.findViewById(R.id.offer_list_loading).setVisibility(View.GONE);

                checkShowEmptyMessage(offers);

            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(), "Unable to load offers from database");
                inflated.findViewById(R.id.offer_list_loading).setVisibility(View.GONE);
                noOffers.setVisibility(View.GONE);
                errorMessage.setVisibility(View.VISIBLE);
            }
        };
    }

    private void checkShowEmptyMessage(List<Offer> offers) {
        offers = filterClosedOffers(offers);
        int noOffersVisibility = mAdapter.add(offers) ? View.VISIBLE : View.GONE;
        noOffers.setVisibility(noOffersVisibility);
    }

    private List<Offer> filterClosedOffers(List<Offer> toBeFiltered) {
        ArrayList<Offer> filtered = new ArrayList<>();
        for (Offer offer : toBeFiltered) {
            if (!displayClosed ^ offer.getIsClosed()) {
                filtered.add(offer);
            }
        }
        return filtered;
    }
}
