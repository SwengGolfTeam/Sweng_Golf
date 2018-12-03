package ch.epfl.sweng.swenggolf.offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.profile.User;

/**
 * Fragment which shows user own offers.
 */
public class ListOwnOfferActivity extends ListOfferActivity {

    private final User user;

    public ListOwnOfferActivity(User user) {
        this.user = user;
    }

    public ListOwnOfferActivity() {
        this.user = Config.getUser();
    }

    @Override
    protected void prepareOfferData(View inflated,
                                    DatabaseOfferConsumer dbConsumer, List<Category> categories) {
        super.prepareOfferData(inflated, new DatabaseOfferConsumer() {
            @Override
            public void accept(Database db, List<Category> categories,
                               ValueListener<List<Offer>> listener) {
                db.readOffers(listener, categories, user.getUserId());
            }
        }, categories);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View view = super.onCreateView(inflater, container, savedInstance);
        setToolbar(R.drawable.ic_menu_black_24dp, R.string.my_offers);
        return view;
    }
}