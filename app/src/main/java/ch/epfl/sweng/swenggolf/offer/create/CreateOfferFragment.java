package ch.epfl.sweng.swenggolf.offer.create;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.ValueListener;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.statistics.UserStats;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static android.widget.Toast.LENGTH_LONG;
import static ch.epfl.sweng.swenggolf.Permission.GPS;
import static ch.epfl.sweng.swenggolf.storage.Storage.CAPTURE_IMAGE_REQUEST;
import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

/**
 * The fragment used to create offers. Note that the extras
 * must contain a string with key "username".
 */
public class CreateOfferFragment extends FragmentConverter
        implements DatePickerDialog.OnDateSetListener {

    static final long SEPARATION = 86220000L;

    static final boolean ON = true;
    static final boolean OFF = false;
    CreateHelper createHelper;
    View inflated;
    Offer offerToModify;
    Offer.Builder offerBuilder;
    long creationDate;
    long endDate;
    Location location = new Location("");
    Uri filePath = null;
    boolean creationAsked;
    Calendar now = Calendar.getInstance();
    Spinner categorySpinner;
    Uri tempPicturePath = null;
    TextView errorMessage;
    private TextView dateText;
    private int fragmentsToSkip;
    private CreateListeners createListeners;

    /**
     * Replace the central fragment by an Offer.
     *
     * @param offer the offer which will replace the central fragment
     */
    void replaceCentralFragmentWithOffer(Offer offer) {
        replaceCentralFragment(FragmentConverter.createShowOfferWithOffer(offer,
                numberOfFragmentsToSkip()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationAsked = false;
        final Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(FUTURE_FRAGMENTS_TO_SKIP)) {
            fragmentsToSkip = bundle.getInt(FUTURE_FRAGMENTS_TO_SKIP);
        }

    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createListeners = new CreateListeners(this);
        createHelper = new CreateHelper(this, createListeners);

        inflated = inflater.inflate(R.layout.activity_create_offer,
                container, false);
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.create_offer);
        errorMessage = inflated.findViewById(R.id.error_message);

        offerToModify = null;
        if (getArguments() != null) {
            offerToModify = getArguments().getParcelable(Offer.OFFER);
            offerBuilder = new Offer.Builder(offerToModify);
        } else {
            offerBuilder = new Offer.Builder();
            if (!creationAsked) {
                ValueListener<Offer.Builder> listener = createListeners.restoreOfferListener();
                Database.getInstance().read(Database.OFFERS_SAVED_PATH,
                        Config.getUser().getUserId(), listener, Offer.Builder.class);
            }

        }
        createHelper.preFillFields();

        createListeners.setListeners();
        dateText = inflated.findViewById(R.id.showDate);
        dateText.setText(Offer.dateFormat().format(endDate));

        Button b = inflated.findViewById(R.id.button_save_pattern);
        b.setOnClickListener(createListeners.savePatternListener());

        return inflated;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText title = findViewById(R.id.offer_name);
        title.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Offer.TITLE_MAX_LENGTH)});
        EditText description = findViewById(R.id.offer_description);
        description.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(Offer.DESCRIPTION_MAX_LENGTH)});
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Storage.conditionActivityResult(requestCode, resultCode, data)) {
            removeStalledPicture();
            switch (requestCode) {
                case CAPTURE_IMAGE_REQUEST: {
                    filePath = compressTemporaryFilepath();
                    break;
                }
                case PICK_IMAGE_REQUEST: {
                    filePath = compressUriFile(data.getData());
                    break;
                }
                default: {
                    return;
                }
            }
            ImageView imageView = findViewById(R.id.offer_picture);
            Picasso.with(this.getContext()).load(filePath).fit().into(imageView);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_offer, menu);
    }

    private Uri compressTemporaryFilepath() {
        File cacheDirectory = getContext().getCacheDir();
        File photo = new File(cacheDirectory, tempPicturePath.getLastPathSegment());
        Uri tmpPath = CreatePictureHelper.compressImageFromPath(getContext(),
                photo.getAbsolutePath(), "compressed");
        if (!photo.delete()) {
            String errorMsg = getString(R.string.tmp_file_suppression_error);
            Toast.makeText(this.getContext(), errorMsg, Toast.LENGTH_LONG).show();
        }
        return tmpPath;
    }

    private Uri compressUriFile(Uri data) {
        Uri path;
        Bitmap map;
        try {
            map = MediaStore.Images.Media.getBitmap(
                    this.getActivity().getContentResolver(), data);
            path = CreatePictureHelper.compressImageIntoCache(map,
                    this.getContext(), "compressed");
        } catch (IOException e) {
            Toast.makeText(this.getContext(), "Failed to retrieve picture.",
                    Toast.LENGTH_LONG).show();
            return Config.isTest() ? Uri.parse("drawable://" + R.drawable.img) : null;
        }
        return path;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (Config.onRequestPermissionsResult(requestCode, grantResults) == GPS) {
            createHelper.attachLocation();
        }
    }

    private int numberOfFragmentsToSkip() {
        int newFragmentsToSkip = offerToModify == null ? 1 : 2;
        newFragmentsToSkip += fragmentsToSkip;
        return newFragmentsToSkip;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                InputMethodManager manager =
                        (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                getActivity().onBackPressed();
                return true;
            }
            case R.id.action_choose_template:
                final Database database = Database.getInstance();
                database.getKeys(Database.OFFERS_PATTERN_PATH + "/" + Config.getUser().getUserId(),
                        createListeners.loadPattern());

                return true;
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        removeStalledPicture();
    }

    private void removeStalledPicture() {
        if (filePath != null && filePath.getLastPathSegment() != null) {
            File previous = new File(getContext().getCacheDir(),
                    filePath.getLastPathSegment());
            if (!previous.delete()) {
                Toast.makeText(this.getContext(),
                        getString(R.string.tmp_file_suppression_error), LENGTH_LONG).show();
            }
        }
    }

    /**
     * Creates an Offer by parsing the contents given by the user.
     */
    public void createOffer() {
        if (creationAsked) {
            return;
        }

        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String title = nameText.getText().toString();
        final String description = descriptionText.getText().toString();
        final Category category = Category.valueOf(categorySpinner.getSelectedItem().toString());

        if (title.length() < Offer.TITLE_MIN_LENGTH || description.length()
                < Offer.DESCRIPTION_MIN_LENGTH) {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            createHelper.createOfferObject(title, description, category);
            UserStats.updateStat(UserStats.Stats.OFFERS_CREATED, Config.getUser().getUserId(), 1);
        }

    }

    /**
     * Set the new date.
     *
     * @param calendar the corresponding calendar
     */
    private void setDate(final Calendar calendar) {
        this.endDate = calendar.getTimeInMillis() + SEPARATION;
        dateText.setText(Offer.dateFormat().format(calendar.getTime()));
    }

    /**
     * Retrieve the date of the calendar and change it if it is valid.
     *
     * @param view  The calendar view
     * @param year  the corresponding year
     * @param month the corresponding month
     * @param day   the corresponding day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);

        if (cal.before(now)) {
            Toast.makeText(CreateOfferFragment.this.getContext(),
                    getString(R.string.valid_date), Toast.LENGTH_LONG).show();
        } else {
            setDate(cal);
        }
    }

    /**
     * Launches the calendar.
     */
    public void showDatePickerDialog() {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        DialogFragment newFragment = new DatePickerFragment(endCalendar);
        newFragment.show(this.getFragmentManager(), "DatePicker");
    }

    void setCheckbox(boolean on) {
        String uri = on ? "@android:drawable/checkbox_on_background"
                : "@android:drawable/checkbox_off_background";
        int uncheckResource = getResources().getIdentifier(uri, null,
                getActivity().getPackageName());
        ImageView check = inflated.findViewById(R.id.offer_position_status);
        Drawable uncheck = getResources().getDrawable(uncheckResource);
        check.setImageDrawable(uncheck);

    }

    /**
     * Fragment which shows a calendar to pick the the date.
     */
    public static class DatePickerFragment extends DialogFragment {

        private final Calendar calendar;

        @SuppressLint("ValidFragment")
        public DatePickerFragment(Calendar calendar) {
            super();
            this.calendar = calendar;
        }

        /**
         * Create the Calendar.
         */
        public DatePickerFragment() {
            this(Calendar.getInstance());
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(this.getActivity(),
                    (DatePickerDialog.OnDateSetListener) getVisibleFragment(),
                    year, month, day);
        }

        private Fragment getVisibleFragment() {
            FragmentManager fragmentManager = this.getFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    return fragment;
                }
            }
            return null;
        }

    }

    @Override
    public void close() {
        if (offerToModify == null) {

            Offer.Builder builder = getOfferBuilder();

            if (!CreateHelper.isOfferEmpty(builder)) {
                Database database = Database.getInstance();
                database.write(Database.OFFERS_SAVED_PATH, Config.getUser().getUserId(), builder);
            }
        }
    }

    @NonNull
    Offer.Builder getOfferBuilder() {
        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);
        final String title = nameText.getText().toString();
        final String description = descriptionText.getText().toString();
        final Category category =
                Category.valueOf(categorySpinner.getSelectedItem().toString());
        return createHelper.getOfferBuilder(title, description, category);
    }

}
