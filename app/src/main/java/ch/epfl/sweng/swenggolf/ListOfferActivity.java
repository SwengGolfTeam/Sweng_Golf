package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListOfferActivity extends Activity {
    private ListOfferAdapter mAdapter;
    private  TextView errorMessage;
    protected static final List<Offer> offerList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offer);
        errorMessage = findViewById(R.id.error_message);

        setRecyclerView();
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.offers_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ListOfferAdapter(offerList);
        // Add dividing line
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        offerList.clear();
        prepareOfferData();

        mRecyclerView.addOnItemTouchListener(listOfferTouchListener(mRecyclerView));
    }

    private ListOfferTouchListener listOfferTouchListener(RecyclerView mRecyclerView) {
        return new ListOfferTouchListener(this, mRecyclerView,
                new ListOfferTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent =
                                new Intent(ListOfferActivity.this,
                                        ShowOfferActivity.class);
                        Offer offer = offerList.get(position);
                        intent.putExtra("offer", offer);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        onItemClick(view, position);
                    }
                });
    }

    /**
     * Get the offers from the database
     */
    private void prepareOfferData() {
      DatabaseConnection db = DatabaseConnection.getInstance();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Offer> offers = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Offer offer = noteDataSnapshot.getValue(Offer.class);
                    offers.add(offer);
                }
                mAdapter.add(offers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DBERR", "Could not do things (aka load offers from database");
                errorMessage.setVisibility(View.VISIBLE);

            }
        };
        db.readOffers(listener);
    }
}
