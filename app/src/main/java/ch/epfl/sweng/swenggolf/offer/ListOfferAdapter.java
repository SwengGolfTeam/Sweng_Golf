package ch.epfl.sweng.swenggolf.offer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.swenggolf.R;

public class ListOfferAdapter extends RecyclerView.Adapter<ListOfferAdapter.MyViewHolder> {

    private List<Offer> offerList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected final TextView title;
        protected final TextView author;
        protected final TextView description;

        protected MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.show_offer_title);
            author = view.findViewById(R.id.offer_author);
            description = view.findViewById(R.id.offer_description);
        }
    }

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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offerList.get(position);

        holder.title.setText(offer.getTitle());

        // Get short description
        String description = offer.getShortDescription();

        holder.description.setText(description);

        holder.author.setText(offer.getAuthor());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return offerList.size();
    }

    /**
     * Add a list of offers to the recycler view.
     *
     * @param offers a list of offers
     */
    public void add(@NonNull List<Offer> offers) {
        if (offers == null) {
            throw new IllegalArgumentException("offers should not be null");
        }
        offerList.addAll(offers);
        notifyDataSetChanged();
    }
}