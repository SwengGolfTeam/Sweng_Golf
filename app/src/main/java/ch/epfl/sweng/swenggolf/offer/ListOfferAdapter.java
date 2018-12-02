package ch.epfl.sweng.swenggolf.offer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;

/**
 * Adapter of the ListOffer RecyclerView.
 */
public class ListOfferAdapter extends RecyclerView.Adapter<ListOfferAdapter.MyViewHolder> {

    private List<Offer> filteredOfferList;
    private List<Offer> offerList;
    private String filter;

    /**
     * Constructs a ListOfferAdapter for the RecyclerView.
     *
     * @param offerList the list of offers to be displayed
     */
    public ListOfferAdapter(List<Offer> offerList) {
        if (offerList == null) {
            throw new IllegalArgumentException();
        }
        this.filteredOfferList = offerList;
        this.offerList = new ArrayList<>();
        this.offerList.addAll(offerList);
        filter = "";
        ViewUserFiller.clearMap();
    }

    @Override
    public ListOfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_list_row, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Offer offer = filteredOfferList.get(position);

        TextView title = (TextView) holder.getFieldOne();
        title.setText(offer.getTitle());

        TextView author = (TextView) holder.getFieldTwo();
        author.setText("");
        ViewUserFiller.fillWithUsername(author, offer.getUserId());

        // Get short description
        String description = offer.getShortDescription();
        TextView mainContent = (TextView) holder.getFieldThree();
        mainContent.setText(description);
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return filteredOfferList.size();
    }

    /**
     * Update the list of offers of the RecyclerView.
     *
     * @param offers a list of offers
     */
    public void add(@NonNull List<Offer> offers) {
        offerList.addAll(offers);
        filteredOfferList.clear();
        fillFilteredList();
        notifyDataSetChanged();
        Log.d("OFFER", "updating " + offers.size());
    }

    /**
     * Remove all the data in the adapter.
     */
    public void clear() {
        offerList.clear();
        filteredOfferList.clear();
        notifyDataSetChanged();
    }

    private void fillFilteredList() {
        for(Offer offer : offerList) {
            if(offer.getTitle().toLowerCase().contains(filter)) {
                filteredOfferList.add(offer);
            }
        }
    }

    /**
     * Filter the data to only show data which contains the filter string in their title.
     * @param filter the string to filter
     */
    public void filter(String filter) {
        this.filter = filter.toLowerCase();
        filteredOfferList.clear();
        fillFilteredList();
        notifyDataSetChanged();
    }

    /**
     * Create a ThreeFieldsViewHolder which represents a row in the RecyclerView.
     */
    public static class MyViewHolder extends ThreeFieldsViewHolder {

        /**
         * Create a row of the RecyclerView.
         *
         * @param view the corresponding view
         */
        public MyViewHolder(View view) {
            super(view, R.id.offer_title, R.id.offer_author, R.id.offer_description);
        }
    }
}