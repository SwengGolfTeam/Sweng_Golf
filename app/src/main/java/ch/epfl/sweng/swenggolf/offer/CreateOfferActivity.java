package ch.epfl.sweng.swenggolf.offer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DbError;
import ch.epfl.sweng.swenggolf.storage.Storage;
import ch.epfl.sweng.swenggolf.tools.FragmentConverter;

import static ch.epfl.sweng.swenggolf.storage.Storage.PICK_IMAGE_REQUEST;

/**
 * The activity used to create offers. Note that the intent extras
 * must contain a string with key "username".
 */
public class CreateOfferActivity extends FragmentConverter implements DatePickerDialog.OnDateSetListener {

    private TextView errorMessage;
    private  TextView date_text;
    private Offer offerToModify;
    private boolean creationAsked;
    private Spinner categorySpinner;
    private Uri filePath = null;
    private long creationDate;
    private Calendar now = Calendar.getInstance();
    private long endDate;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEEE, dd/MM/yyyy");




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.activity_create_offer, container, false);
        setToolbar(R.drawable.ic_baseline_arrow_back_24px, R.string.create_offer);
        errorMessage = inflated.findViewById(R.id.error_message);
        now = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        creationDate = now.getTimeInMillis();
        endDate = now.getTimeInMillis();
        preFillFields(inflated);
        setupSpinner(inflated);
        date_text = inflated.findViewById(R.id.showDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        date_text.setText(DATE_FORMAT.format(endCalendar.getTime()));

        inflated.findViewById(R.id.offer_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture(v);
            }
        });
        inflated.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOffer(v);
            }
        });
        inflated.findViewById(R.id.pick_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        creationAsked = false;
    }

    private void setupSpinner(View v) {
        categorySpinner = v.findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(),
                android.R.layout.simple_list_item_1, Category.values()));
    }

    private void preFillFields(View inflated) {
        if (getArguments() != null
                && (offerToModify = getArguments().getParcelable("offer")) != null) {
            EditText title = inflated.findViewById(R.id.offer_name);
            title.setText(offerToModify.getTitle(), TextView.BufferType.EDITABLE);
            EditText description = inflated.findViewById(R.id.offer_description);
            description.setText(offerToModify.getDescription(), TextView.BufferType.EDITABLE);
            ImageView picture = inflated.findViewById(R.id.offer_picture);
            String link = offerToModify.getLinkPicture();
            if (!link.isEmpty() && !Config.isTest()) {
                Picasso.with(this.getContext()).load(Uri.parse(link)).into(picture);
            }
        }
    }

    /**
     * Allows the user to choose a picture from his gallery.
     *
     * @param view the view
     */
    public void choosePicture(View view) {
        startActivityForResult(Storage.choosePicture(), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Storage.conditionActivityResult(requestCode, resultCode, data)) {
            ImageView imageView = findViewById(R.id.offer_picture);
            filePath = data.getData();
            Picasso.with(this.getContext()).load(filePath).into(imageView);
        }
    }

    /**
     * Creates an Offer by parsing the contents given by the user.
     *
     * @param view the view
     */
    public void createOffer(View view) {
        if (creationAsked) {
            return;
        }

        EditText nameText = findViewById(R.id.offer_name);
        EditText descriptionText = findViewById(R.id.offer_description);

        final String name = nameText.getText().toString();
        final String description = descriptionText.getText().toString();
        final Category category = Category.valueOf(categorySpinner.getSelectedItem().toString());

        if (name.isEmpty() || description.isEmpty()) {
            errorMessage.setText(R.string.error_create_offer_invalid);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            createOfferObject(name, description, category);
        }

    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     */
    protected void createOfferObject(String name, String description, Category tag) {
        String uuid;
        if (offerToModify != null) {
            uuid = offerToModify.getUuid();
            creationDate = offerToModify.getCreationDate();
        } else {
            uuid = UUID.randomUUID().toString();
        }

        final Offer newOffer = new Offer(Config.getUser().getUserId(), name, description,
                "", uuid, tag, creationDate, endDate);

        if (filePath == null) {
            writeOffer(newOffer);
        } else {
            uploadImage(newOffer);
        }
    }

    private void uploadImage(final Offer offer) {
        OnCompleteListener<Uri> listener = new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String link = task.getResult().toString();
                    Offer newOffer = offer.updateLinkToPicture(link);
                    writeOffer(newOffer);
                } else {
                    // TODO Handle failures
                }
            }
        };
        Storage.getInstance().write(filePath, "images/" + offer.getUuid(), listener);
    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     */
    private void writeOffer(final Offer offer) {
        creationAsked = true;
        Database database = Database.getInstance();
        CompletionListener listener = new CompletionListener() {
            @Override
            public void onComplete(@Nullable DbError databaseError) {
                if (databaseError == DbError.NONE) {
                    Toast.makeText(CreateOfferActivity.this.getContext(), "Offer created",
                            Toast.LENGTH_SHORT).show();
                    replaceCentralFragment(FragmentConverter.createShowOfferWithOffer(offer));
                } else {
                    errorMessage.setVisibility(View.VISIBLE);
                    errorMessage.setText(R.string.error_create_offer_database);
                }
            }

        };
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer, listener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                InputMethodManager manager =
                        (InputMethodManager) getActivity()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                Fragment backFrag;
                if (offerToModify == null) {
                    backFrag = new ListOfferActivity();
                } else {
                    backFrag = FragmentConverter.createShowOfferWithOffer(offerToModify);
                }
                replaceCentralFragment(backFrag);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment {

        private final Calendar c;

        @SuppressLint("ValidFragment")
        public DatePickerFragment(Calendar c) {
            super();
            this.c = c;
        }

        public DatePickerFragment(){
            this(Calendar.getInstance());
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(this.getActivity(), (DatePickerDialog.OnDateSetListener) getVisibleFragment(), year, month, day);
        }


        private Fragment getVisibleFragment() {
            FragmentManager fragmentManager = this.getFragmentManager();
            List<Fragment> fragments = fragmentManager.getFragments();
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
            return null;
        }
    }



    /**
     * To set date on TextView
     * @param calendar
     */
    private void setDate(final Calendar calendar) {
        this.endDate = calendar.getTimeInMillis();
        date_text.setText(DATE_FORMAT.format(calendar.getTime()));

    }

    /**
     * To receive a callback when the user sets the date.
     * @param view
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        if (cal.before(now)){

            Toast.makeText(CreateOfferActivity.this.getContext(),
                    getString(R.string.valid_date), Toast.LENGTH_LONG).show();
        } else {
            setDate(cal);
        }
    }


    public void showDatePickerDialog(View v) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTimeInMillis(endDate);
        DialogFragment newFragment = new DatePickerFragment(endCalendar);
        newFragment.show(this.getFragmentManager(), "DatePicker");
    }
}
