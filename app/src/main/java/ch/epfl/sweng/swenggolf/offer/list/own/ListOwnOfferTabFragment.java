package ch.epfl.sweng.swenggolf.offer.list.own;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.offer.list.DatabaseOfferConsumer;
import ch.epfl.sweng.swenggolf.offer.list.ListOfferFragment;
import ch.epfl.sweng.swenggolf.profile.User;

import static ch.epfl.sweng.swenggolf.profile.User.USER;

/**
 * Fragment which shows user own offers.
 */
public class ListOwnOfferTabFragment extends ListOfferFragment {

    private User user;

    @Override
    protected void prepareOfferData(boolean displayClosed, View inflated,
                                    DatabaseOfferConsumer dbConsumer, List<Category> categories) {

        super.prepareOfferData(displayClosed, inflated, new DatabaseOfferConsumer() {
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

        Bundle bundle = getArguments();

        if (bundle != null && bundle.getParcelable(USER) != null) {
            user = bundle.getParcelable(USER);
        } else if (bundle != null) {
            user = Config.getUser();
            bundle.putParcelable(USER, user);
        }

        return super.onCreateView(inflater, container, savedInstance);
    }

}