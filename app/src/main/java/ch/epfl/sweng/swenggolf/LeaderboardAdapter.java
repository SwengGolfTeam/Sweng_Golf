package ch.epfl.sweng.swenggolf;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.swenggolf.offer.ListOfferAdapter;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private List<User> userList;

    public static class MyViewHolder extends ThreeFieldsViewHolder {

        public MyViewHolder(View view) {
            super(view, R.id.offer_title, R.id.offer_author, R.id.offer_description);
        }
    }

    /**
     * Constructs a LeaderboardAdapter for the RecyclerView.
     *
     * @param userList the list of offers to be displayed
     */
    public LeaderboardAdapter(List<User> userList) {
        if (userList == null) {
            throw new IllegalArgumentException();
        }
        this.userList = userList;
    }


    @Override
    public LeaderboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_list_row, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = userList.get(position);

        TextView title = (TextView) holder.getFieldOne();
        title.setText(user.getUserName());

        // Get short description
        String description = Integer.toString(user.getPoints());
        TextView mainContent = (TextView) holder.getFieldThree();
        mainContent.setText(description);

        TextView author = (TextView) holder.getFieldTwo();
        ViewUserFiller.fillWithUsername(author, user.getUserId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return userList.size();
    }

    /**
     * Add a list of users to the recycler view.
     *
     * @param users a list of offers
     */
    public void add(@NonNull List<User> users) {
        userList.addAll(users);
        notifyDataSetChanged();
    }
}