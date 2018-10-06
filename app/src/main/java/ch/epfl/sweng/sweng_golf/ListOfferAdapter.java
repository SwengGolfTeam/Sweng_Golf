package ch.epfl.sweng.sweng_golf;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ListOfferAdapter extends RecyclerView.Adapter<ListOfferAdapter.MyViewHolder> {

    @SuppressWarnings("WeakerAccess") // could be useful for other classes to know
    public static final int DESCRIPTION_LIMIT = 140;
    private List<Offer> offerList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        protected final TextView title, author, description;

        protected MyViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.show_offer_title);
            author = v.findViewById(R.id.offer_author);
            description = v.findViewById(R.id.offer_description);
        }
    }

    public ListOfferAdapter(List<Offer> offerList) {
        if (offerList == null) {
            throw new NullPointerException();
        }
        this.offerList = offerList;
    }


    @Override
    public ListOfferAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_list_row
                , parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offerList.get(position);

        holder.title.setText(offer.getTitle());

        // Cut description
        String description = offer.getDescription();
        description = description.substring(0, DESCRIPTION_LIMIT) + "...";
        holder.description.setText(description);

        holder.author.setText(offer.getAuthor());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return offerList.size();
    }
}