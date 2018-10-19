package ch.epfl.sweng.swenggolf.offer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;


public class ListOfferAdapter extends RecyclerView.Adapter<ListOfferAdapter.MyViewHolder> {

    private List<Offer> offerList;

    public static class MyViewHolder extends ThreeFieldsViewHolder {

        public MyViewHolder(View view) {
            super(view, R.id.show_offer_title, R.id.offer_author, R.id.offer_description);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Offer offer = offerList.get(position);

        TextView title = (TextView) holder.getTitle();
        title.setText(offer.getTitle());

        // Get short description
        String description = offer.getShortDescription();
        TextView mainContent = (TextView) holder.getMainContent();
        mainContent.setText(description);

        final TextView subtitle = (TextView) holder.getSubTitle();
        Database data = Database.getInstance();
        data.read("/users",offer.getUserId(),new ValueListener<User>() {
            @Override
            public void onDataChange(User value) {
                subtitle.setText(value.getUserName());
            }

            @Override
            public void onCancelled(DbError error) {
                Log.d(error.toString(),"Failed to load user name");
            }
        },
        User.class);
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
        offerList.addAll(offers);
        notifyDataSetChanged();
    }
}