package ch.epfl.sweng.swenggolf;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

import ch.epfl.sweng.swenggolf.offer.ListOfferAdapter;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.profile.Badge;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.ThreeFieldsViewHolder;
import ch.epfl.sweng.swenggolf.tools.ViewUserFiller;


public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private List<User> userList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userPoints;
        ImageView userImage;
        ImageView userBadge;

        public MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userPoints = itemView.findViewById(R.id.user_points);
            userImage = itemView.findViewById(R.id.user_image);
            userBadge = itemView.findViewById(R.id.user_badge);
        }
    }

    /**
     * Constructs a LeaderboardAdapter for the RecyclerView.
     *
     * @param userList the list of offers to be displayed
     */
    public LeaderboardAdapter(List<User> userList, Context mContext) {
        if (userList == null) {
            throw new IllegalArgumentException();
        }
        this.userList = userList;
        this.mContext = mContext;
    }


    @Override
    public LeaderboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_list_row, parent, false);
        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User user = userList.get(position);

        TextView name = holder.userName;
        name.setText(user.getUserName());

        TextView points = holder.userPoints;
        points.setText(Integer.toString(user.getPoints()));

        Uri photoUri = Uri.parse(user.getPhoto());
        Picasso.with(mContext).load(photoUri).into(holder.userImage);

        ImageView badge = holder.userBadge;
        badge.setImageResource(Badge.getDrawable(user.getPoints()));

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