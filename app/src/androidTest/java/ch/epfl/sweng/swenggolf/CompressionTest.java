package ch.epfl.sweng.swenggolf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import ch.epfl.sweng.swenggolf.main.MainMenuActivity;
import ch.epfl.sweng.swenggolf.offer.create.CreatePictureHelper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

@RunWith(AndroidJUnit4.class)
public class CompressionTest {
    @Rule
    public ActivityTestRule<MainMenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MainMenuActivity.class);

    @Test
    public void imageSizeAfterCompressionIsWithinBounds() {
        Activity activity = mActivityTestRule.getActivity();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bm = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.img, options);
        Uri compressionResult = CreatePictureHelper.compressImageIntoCache(bm,
                activity.getApplicationContext(), "test_compression");
        File result = new File(activity.getCacheDir(), compressionResult.getLastPathSegment());
        assertThat(result.exists(), is(true));
        assertThat(result.length(), lessThan((long) CreatePictureHelper.MAX_IMAGE_SIZE * 1024));
        result.delete();
    }
}
