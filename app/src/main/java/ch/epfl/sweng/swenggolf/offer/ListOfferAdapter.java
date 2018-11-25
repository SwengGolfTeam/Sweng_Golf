package ch.epfl.sweng.swenggolf.offer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;

/**
 * Adapter of the ListOffer RecyclerView.
 */
public class ListOfferAdapter extends RecyclerView.Adapter<ListOfferAdapter.MyViewHolder> {

    private List<Offer> offerList;

    /**
     * Constructs a ListOfferAdapter for the RecyclerView.
     *
     * @param offerList the list of offers to be displayed
     */
    public ListOfferAdapter(List<Offer> offerList) {
        if (offerList == null) {
            throw new IllegalArgumentException();
        }
        this.offerList = offerList;
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
        final Offer offer = offerList.get(position);

        TextView title = (TextView) holder.getFieldOne();
        title.setText(offer.getTitle());

        // Get short description
        String description = offer.getShortDescription();
        TextView mainContent = (TextView) holder.getFieldThree();
        mainContent.setText(description);

        TextView author = (TextView) holder.getFieldTwo();
        ViewUserFiller.fillWithUsername(author, offer.getUserId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return offerList.size();
    }

    /**
     * Add a list of offers to the RecyclerView.
     *
     * @param offers a list of offers
     */
    public void add(@NonNull List<Offer> offers) {
        offerList.addAll(offers);
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