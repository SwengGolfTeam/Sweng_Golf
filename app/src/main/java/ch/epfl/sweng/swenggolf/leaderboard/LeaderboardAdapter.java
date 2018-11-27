package ch.epfl.sweng.swenggolf.leaderboard;

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

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.profile.Badge;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

/**
 * Adapter for the Leaderboard RecyclerView.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.MyViewHolder> {

    private final FragmentConverter fragmentConverter;
    private List<User> userList;

    /**
     * Constructs a LeaderboardAdapter for the RecyclerView.
     *
     * @param userList the list of users to be displayed
     */
    public LeaderboardAdapter(List<User> userList, FragmentConverter fragmentConverter) {
        if (userList == null) {
            throw new IllegalArgumentException();
        }
        this.userList = userList;
        this.fragmentConverter = fragmentConverter;

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
        String pointString = "Points : " + Integer.toString(user.getPoints());
        points.setText(pointString);

        TextView userPosition = holder.userPosition;
        String positionString = "Position : "
                + Integer.toString(userList.indexOf(user) + 1);
        userPosition.setText(positionString);

        Uri photoUri = Uri.parse(user.getPhoto());
        Picasso.with(fragmentConverter.getContext()).load(photoUri).into(holder.userImage);

        ImageView badge = holder.userBadge;
        badge.setImageResource(Badge.getDrawable(user.getPoints()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentConverter.replaceCentralFragment(
                        FragmentConverter.createShowProfileWithProfile(user));
            }
        });

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

    /**
     * Representation of one row of the recyclerView.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userPoints;
        ImageView userImage;
        ImageView userBadge;
        TextView userPosition;

        /**
         * Constructor.
         *
         * @param itemView view of one item
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userPoints = itemView.findViewById(R.id.user_points);
            userPosition = itemView.findViewById(R.id.user_position);
            userImage = itemView.findViewById(R.id.user_image);
            userBadge = itemView.findViewById(R.id.user_badge);
        }
    }
}