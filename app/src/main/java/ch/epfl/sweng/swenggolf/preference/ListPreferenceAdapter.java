package ch.epfl.sweng.swenggolf.preference;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.User;
import ch.epfl.sweng.swenggolf.UserFirebase;
import ch.epfl.sweng.swenggolf.UserLocal;

public class ListPreferenceAdapter
        extends RecyclerView.Adapter<ListPreferenceAdapter.PreferenceViewHolder> {

    private static final int DEFAULT_PICTURE = R.drawable.common_google_signin_btn_icon_dark;
    public static boolean debug = false;
    private ArrayList<User> mDataset;

    public static final User[] usersInitial = {
            new UserLocal("Anna","0","anna@mail.com","Tomatoes"),
            new UserLocal("Bob","1","bob@mail.com","Screwdriver"),
            new UserLocal("Geany","2","geany@mail.com","Comics"),
            new UserLocal("Greg","3","greg@gmail.com","Ropes"),
            new UserLocal("Fred","4","fred@gmail.com","Beverages"),
            new UserLocal("AAnna","0","aanna@mail.com","Friends"),
            new UserLocal("ABob","1","abob@mail.com","Washing machine"),
            new UserLocal("AGeany","2","ageany@mail.com","Hammer"),
            new UserLocal("AGreg","3","agreg@gmail.com","Lunch"),
            new UserLocal("AFred","4","afred@gmail.com","Cheeseburgers"),
            new UserLocal("BAnna","0","banna@mail.com","Champaign"),
            new UserLocal("BBob","1","bbob@mail.com","Mushrooms"),
            new UserLocal("BGeany","2","bgeany@mail.com","Nothing"),
            new UserLocal("BGreg","3","bgreg@gmail.com","Fries"),
            new UserLocal("BFreEricisSIstirusiwssjdsidjsidskdisjdijsmdisjd",
                    "4","fr@gmail.com","A nice sweatshirt, some hot shoes and a poncho")
    };

    /**
     * Create a new adapter for the list that fetches information about users.
     * If debug is set to true, a default list of users is used.
     */
    public ListPreferenceAdapter(){
        if(!debug){
            ValueEventListener getUserList = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> usersData = dataSnapshot.getChildren();
                    ArrayList<User> users = new ArrayList<>();
                    for (DataSnapshot user : usersData){
                        UserFirebase concreteUser = user.getValue(UserFirebase.class);
                        users.add(concreteUser);
                    }
                    mDataset = users;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Loading users error","failed to retrieve users list for preferences");
                }
            };
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            ref.addValueEventListener(getUserList);
        }
        else{
            mDataset = new ArrayList<User>(Arrays.asList(usersInitial));
        }
    }

    public class PreferenceViewHolder extends RecyclerView.ViewHolder{
        private ImageView userpic;
        private TextView username;
        private TextView preference;
        private Context context;

        /**
         * Create a container for displaying a cell of the view.
         * Container contains user picture, username and preference.
         */
        public PreferenceViewHolder(View view){
            super(view);
            context = view.getContext();
            userpic = view.findViewById(R.id.preference_userpic);
            username = view.findViewById(R.id.preference_username);
            preference = view.findViewById(R.id.preference_preference);
        }
    }

    @NonNull
    @Override
    public PreferenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.preferences_list_quad,parent,false);
        PreferenceViewHolder p = new PreferenceViewHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
        User current = mDataset.get(position);
        Picasso.with(holder.context)
                .load(current.getPhoto())
                .placeholder(DEFAULT_PICTURE)
                .fit().into(holder.userpic);
        holder.username.setText(current.getUserName());
        holder.preference.setText(current.getPreference());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
