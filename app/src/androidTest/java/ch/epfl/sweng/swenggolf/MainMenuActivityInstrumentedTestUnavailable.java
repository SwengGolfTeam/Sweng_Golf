package ch.epfl.sweng.sweng_golf;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;

import com.google.android.gms.internal.firebase_auth.zzap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ch.epfl.sweng.swenggolf.MainMenuActivity;

import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityInstrumentedTestUnavailable {
    private static final FirebaseUser fuu = new FirebaseUser() {
        @NonNull
        @Override
        public String getUid() {
            return null;
        }

        @NonNull
        @Override
        public String getProviderId() {
            return null;
        }

        @Override
        public boolean isAnonymous() {
            return false;
        }

        @Nullable
        @Override
        public List<String> getProviders() {
            return null;
        }

        @NonNull
        @Override
        public List<? extends UserInfo> getProviderData() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
            return null;
        }

        @Override
        public FirebaseUser zzp() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseApp zzq() {
            return null;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return null;
        }

        @Nullable
        @Override
        public Uri getPhotoUrl() {
            return null;
        }

        @Nullable
        @Override
        public String getEmail() {
            return null;
        }

        @Nullable
        @Override
        public String getPhoneNumber() {
            return null;
        }

        @NonNull
        @Override
        public zzap zzr() {
            return null;
        }

        @Override
        public void zza(@NonNull zzap zzap) {

        }

        @NonNull
        @Override
        public String zzs() {
            return null;
        }

        @NonNull
        @Override
        public String zzt() {
            return null;
        }

        @Nullable
        @Override
        public FirebaseUserMetadata getMetadata() {
            return null;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public boolean isEmailVerified() {
            return false;
        }
    };
    private static final FirebaseUser fua = new FirebaseUser() {
        @NonNull
        @Override
        public String getUid() {
            return "usernameValid";
        }

        @NonNull
        @Override
        public String getProviderId() {
            return null;
        }

        @Override
        public boolean isAnonymous() {
            return false;
        }

        @Nullable
        @Override
        public List<String> getProviders() {
            return null;
        }

        @NonNull
        @Override
        public List<? extends UserInfo> getProviderData() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
            return null;
        }

        @Override
        public FirebaseUser zzp() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseApp zzq() {
            return null;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return "usermailValid";
        }

        @Nullable
        @Override
        public Uri getPhotoUrl() {
            return Uri.parse("http://3.bp.blogspot.com/-ZrGtkJvEQyM/T8qUZRhyO0I/AAAAAAAAECc/uxldnxomvOo/s1600/Care+Bears+Welcome+to+Care-A-Lot+Logo.jpg");
        }

        @Nullable
        @Override
        public String getEmail() {
            return null;
        }

        @Nullable
        @Override
        public String getPhoneNumber() {
            return null;
        }

        @NonNull
        @Override
        public zzap zzr() {
            return null;
        }

        @Override
        public void zza(@NonNull zzap zzap) {

        }

        @NonNull
        @Override
        public String zzs() {
            return null;
        }

        @NonNull
        @Override
        public String zzt() {
            return null;
        }

        @Nullable
        @Override
        public FirebaseUserMetadata getMetadata() {
            return null;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public boolean isEmailVerified() {
            return false;
        }
    };

    @Rule public final ActivityTestRule<MainMenuActivity> mMenuRule = new ActivityTestRule<>(MainMenuActivity.class,false,false);

    @Before
    public void setUpApp(){
        mMenuRule.launchActivity(new Intent());
        FirebaseApp.initializeApp(mMenuRule.getActivity().getApplicationContext());
    }

    @Test
    public void testCanOpenDrawer() {
        DrawerLayout drawer = mMenuRule.getActivity().findViewById(R.id.side_menu);
        openDrawer(R.id.side_menu);
        assertTrue("drawer was closed",drawer.isDrawerOpen(GravityCompat.START));
    }

    @Test
    public void usernameFieldIsDefault(){
        TextView t = mMenuRule.getActivity().findViewById(R.id.username);
        assertThat(t.getText().toString(),is("username"));
    }

    @Test
    public void usermailFieldIsDefault() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.usermail);
        assertThat(t.getText().toString(),is("usermail"));
    }

    @Test
    public void usernameFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.username);
        openDrawer(R.id.side_menu);
        assertThat(t.getText().toString(),is("usernameValid"));
    }

    @Test
    public void usermailFieldIsOfUser() {
        TextView t = mMenuRule.getActivity().findViewById(R.id.usermail);
        assertThat(t.getText().toString(),is("usermailValid"));
    }
}


