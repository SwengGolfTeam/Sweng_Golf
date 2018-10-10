package ch.epfl.sweng.swenggolf.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.User;

public class ListPreferenceAdapter extends RecyclerView.Adapter<ListPreferenceAdapter.PreferenceViewHolder> {

    private ArrayList<User> mDataset;

    public ListPreferenceAdapter(){
        //TODO
        mDataset = new ArrayList<>();
    }

    public class PreferenceViewHolder extends RecyclerView.ViewHolder{
        public ImageView userpic;
        public TextView username;
        public TextView preference;
        public Context context;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferences_list_quad,parent,false);
        PreferenceViewHolder p = new PreferenceViewHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder, int position) {
        User current = mDataset.get(position);
        Picasso.with(holder.context).load(current.getPhoto()).error(android.R.drawable.btn_dialog).into(holder.userpic);
        holder.username.setText(current.getUsername());
        holder.preference.setText(current.getPreference());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
