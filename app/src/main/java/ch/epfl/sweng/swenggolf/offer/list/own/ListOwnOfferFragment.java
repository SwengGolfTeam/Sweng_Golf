package ch.epfl.sweng.swenggolf.offer.list.own;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.profile.User;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

public class ListOwnOfferFragment extends FragmentConverter {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_list_own_offer,
                container, false);
        TabLayout tabs = inflated.findViewById(R.id.list_own_offer_tablayout);
        final ViewPager pager = inflated.findViewById(R.id.list_own_offer_pager);

        User user = Config.getUser();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getParcelable(User.USER) != null) {
            user = bundle.getParcelable(User.USER);
        }
        ListOwnOfferPager adapter =
                new ListOwnOfferPager(user, this.getContext(), getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        return inflated;
    }

}
