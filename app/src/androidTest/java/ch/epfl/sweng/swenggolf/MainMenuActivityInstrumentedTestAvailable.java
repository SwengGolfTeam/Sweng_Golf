package ch.epfl.sweng.swenggolf;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.FirebaseAccount;
import ch.epfl.sweng.swenggolf.main.MainMenuActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestAvailable {

    @Rule
    public final ActivityTestRule<MainMenuActivity> mMenuRule =
            new ActivityTestRule<>(MainMenuActivity.class, false, false);

    @Before
    public void setUp() {
        FirebaseAccount.test = true;
        mMenuRule.launchActivity(new Intent());
    }

    @Test
    public void usernameFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.username);
        assertThat(t.getText().toString(), is("usernameValid"));
    }

    @Test
    public void userIdFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.usermail);
        assertThat(t.getText().toString(), is("userIdValid"));
    }
}


