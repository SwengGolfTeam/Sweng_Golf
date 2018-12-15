package ch.epfl.sweng.swenggolf.offer.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.offer.Offer;
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
     */
    public ListOfferAdapter() {
        this.filteredOfferList = new ArrayList<>();
        this.offerList = new ArrayList<>();
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
     * Doesn't update the view if offers are null.
     *
     * @param offers a list of offers
     * @return whether displayed offers are empty or not.
     */
    public boolean add(@NonNull List<Offer> offers) {
        if (!offers.isEmpty()) {
            offerList.addAll(offers);
            filteredOfferList.clear();
            fillFilteredList();
            notifyDataSetChanged();
        }
        return filteredOfferList.isEmpty();
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
        for (Offer offer : offerList) {
            if (offer.getTitle().toLowerCase().contains(filter)) {
                filteredOfferList.add(offer);
            }
        }
    }

    /**
     * Filter the data to only show data which contains the filter string in their title.
     *
     * @param filter the string to filter.
     * @return whether the displayed list is empty or not.
     */
    public boolean filter(String filter) {
        this.filter = filter.toLowerCase();
        filteredOfferList.clear();
        fillFilteredList();
        notifyDataSetChanged();
        return filteredOfferList.isEmpty();
    }

    /**
     * Getter for an offer in the RecyclerView displaying this adapter.
     *
     * @param position the position of the offer in the displayed list.
     * @return the offer at the appropriate position in the view.
     */
    public Offer getOffer(int position) {
        return filteredOfferList.get(position);
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