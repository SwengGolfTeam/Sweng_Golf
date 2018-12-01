package ch.epfl.sweng.swenggolf.messaging;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.answer.Answers;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class MessagingActivity extends FragmentConverter {
    private View inflated;
    private Offer offer;
    private User otherUser;

    // TODO
    // 1. lorsqu'on clique sur le bouton on est redirigé sur la page de discussion avec le user concerné
    // 2. essayer de faire en sorte que ce soit des couleurs différentes

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle bundle = getArguments();
        offer = bundle.getParcelable(Offer.OFFER);
        otherUser = bundle.getParcelable(User.USER);
        // TODO add fragmentToSkip stuff?
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        // recover which user was chosen
        Database db = Database.getInstance();

        setToolbar(R.drawable.ic_baseline_arrow_back_24px, offer.getTitle());
        inflated = inflater.inflate(R.layout.activity_messaging, container, false);
        LinearLayout mLayout = inflated.findViewById(R.id.messages);
        setRecyclerView();
        LayoutInflater mInflater = getLayoutInflater();
        View newReaction = mInflater.inflate(R.layout.reaction_you, mLayout, false);
        mLayout.addView(newReaction);
        return inflated;
    }

    private void setRecyclerView() {
        RecyclerView mRecyclerView = inflated.findViewById(R.id.messages_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}
