package ch.epfl.sweng.swenggolf.offer.create;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.epfl.sweng.swenggolf.Config;
import ch.epfl.sweng.swenggolf.R;
import ch.epfl.sweng.swenggolf.database.CompletionListener;
import ch.epfl.sweng.swenggolf.database.Database;
import ch.epfl.sweng.swenggolf.database.DatabaseUser;
import ch.epfl.sweng.swenggolf.location.AppLocation;
import ch.epfl.sweng.swenggolf.offer.Category;
import ch.epfl.sweng.swenggolf.offer.Offer;
import ch.epfl.sweng.swenggolf.storage.Storage;

import static ch.epfl.sweng.swenggolf.location.AppLocation.checkLocationPermission;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.OFF;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.ON;
import static ch.epfl.sweng.swenggolf.offer.create.CreateOfferActivity.SEPARATION;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Helps the CreateOfferactivity to set some fields.
 */
class CreateHelper {
    private static final int MAX_IMAGE_SIZE = 175 * 256;
    private static final int MAX_COMPRESS_QUALITY = 100;
    private static final int STEP_COMPRESS_QUALITY = 5;

    private final CreateOfferActivity create;
    private final CreateListeners listeners;

    /**
     * Initialize the create helper.
     *
     * @param create    the CreateOfferActivity to help
     * @param listeners the CreateListeners
     */
    protected CreateHelper(CreateOfferActivity create, CreateListeners listeners) {
        this.create = create;
        this.listeners = listeners;
    }

    /**
     * Pre-fill the date, category, location fields of the CreateOfferActivity.
     */
    void preFillFields() {

        setupSpinner();

        create.now = new GregorianCalendar(create.now.get(Calendar.YEAR),
                create.now.get(Calendar.MONTH), create.now.get(Calendar.DATE));

        create.creationDate = create.now.getTimeInMillis();
        create.endDate = create.now.getTimeInMillis() + SEPARATION;

        if (create.offerToModify != null) {

            EditText title = create.inflated.findViewById(R.id.offer_name);
            title.setText(create.offerToModify.getTitle(), TextView.BufferType.EDITABLE);

            EditText description = create.inflated.findViewById(R.id.offer_description);
            description.setText(create.offerToModify.getDescription(),
                    TextView.BufferType.EDITABLE);

            create.categorySpinner.setSelection(create.offerToModify.getTag().ordinal());

            create.location = new Location("");
            create.location.setLatitude(create.offerToModify.getLatitude());
            create.location.setLongitude(create.offerToModify.getLongitude());

            create.creationDate = create.offerToModify.getCreationDate();
            create.endDate = create.offerToModify.getEndDate();

            checkFillConditions();

        }
    }

    private void setupSpinner() {
        create.categorySpinner = create.inflated.findViewById(R.id.category_spinner);
        create.categorySpinner.setAdapter(new ArrayAdapter<>(create.getContext(),
                android.R.layout.simple_list_item_1, Category.values()));
    }

    private void checkFillConditions() {
        if (create.location.getLatitude() == 0.0 && create.location.getLongitude() == 0.0) {
            create.setCheckbox(ON);
        }

        ImageView picture = create.inflated.findViewById(R.id.offer_picture);
        String link = create.offerToModify.getLinkPicture();

        if (!link.isEmpty() && !Config.isTest()) {
            Picasso.with(create.getContext()).load(Uri.parse(link)).into(picture);
        }
    }

    /**
     * Creates the offer and pushes it to the database.
     *
     * @param name        the title of the offer
     * @param description the description of the offer
     */
    void createOfferObject(String name, String description, Category tag) {
        Offer.Builder builder;
        if (create.offerToModify != null) {
            builder = new Offer.Builder(create.offerToModify);
        } else {
            builder = new Offer.Builder();
        }

        final Offer newOffer = builder.setUserId(Config.getUser().getUserId())
                .setTitle(name).setDescription(description)
                .setTag(tag).setCreationDate(create.creationDate)
                .setEndDate(create.endDate).setLocation(create.location).build();

        if (create.filePath == null) {
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
        Storage.getInstance().write(create.filePath, "images/" + offer.getUuid(), listener);
    }

    /**
     * Write an offer into the database.
     *
     * @param offer offer to be written
     */
    private void writeOffer(final Offer offer) {
        create.creationAsked = true;
        Database database = Database.getInstance();
        CompletionListener listener = listeners.createWriteOfferListener(offer);
        database.write(Database.OFFERS_PATH, offer.getUuid(), offer, listener);
    }

    /**
     * Update the User score.
     *
     * @param offerToModify the offer to modify
     * @param offer         the offer
     */
    void updateUserScore(Offer offerToModify, Offer offer) {
        int scoreToAdd = 0;
        if (offerToModify == null) {
            scoreToAdd += offer.offerValue();
        } else {
            scoreToAdd += offerToModify.offerValueDiff(offer);
        }
        DatabaseUser.addPointsToCurrentUser(scoreToAdd);
    }

    /**
     * Attach location to the offer.
     */
    void attachLocation() {
        if (create.location.getLatitude() != 0.0 || create.location.getLongitude() != 0.0) {

            create.location = new Location("");
            create.setCheckbox(OFF);

            return;
        }

        if (checkLocationPermission(create.getActivity())) {
            AppLocation currentLocation = AppLocation.getInstance(create.getActivity());
            currentLocation.getLocation(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location l) {
                    saveLocation(l);
                }
            });
        }
    }

    private void saveLocation(Location location) {
        create.location = location;
        create.setCheckbox(ON);
    }

    public static Uri compressImageFromPath(Context context, String filePath, String filename) {
        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image, in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size, but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig= Bitmap.Config.ARGB_8888;
        Bitmap bmpPic = BitmapFactory.decodeFile(filePath,options);

        Bitmap tmp = null;
        try {
            tmp = correctPictureOrientation(filePath, bmpPic);
        } catch (IOException e) {
            Toast.makeText(context, "Failed to restore picture original orientation",
                    Toast.LENGTH_SHORT).show();
        }
        if(tmp != null) {
            bmpPic = tmp;
        }

        return compressImageIntoCache(bmpPic, context, filename);
    }



    /**
     * <@see https://stackoverflow.com/questions/39361550/android-resize-image-to-upload-to-server/39363418/>
     */
    public static Uri compressImageIntoCache(Bitmap bmpPic, Context context, String fileName){
        int targetQuality = computeTargetQuality(bmpPic);
        File temp = compressAndSave(context, fileName, targetQuality, bmpPic);
        //return the path of resized and compressed file
        if(temp != null) {
           return FileProvider.getUriForFile(context, "ch.epfl.sweng.swenggolf.fileprovider",temp);
        } else {
            return null;
        }
    }

    private static int computeTargetQuality(Bitmap bmpPic) {
        int compressQuality = MAX_COMPRESS_QUALITY; // quality decreasing by 5 every loop.
        int streamLength;
        do{
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d("compressBitmap", "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= STEP_COMPRESS_QUALITY;
            Log.d("compressBitmap", "Size: " + streamLength/1024+" kb");
        }while (streamLength >= MAX_IMAGE_SIZE);
        return compressQuality;
    }

    private static File compressAndSave(Context context, String fileName, int targetQuality,
                                        Bitmap bmpPic) {
        File temp = null;
        try {
            //save the resized and compressed file to disk cache
            Log.d("compressBitmap","cacheDir: "+context.getCacheDir());
            temp = File.createTempFile(fileName, null,context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(temp);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, targetQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e("compressBitmap", "Error on saving file");
        }
        return temp;
    }

    /**
     * <@see https://stackoverflow.com/questions/39361550/android-resize-image-to-upload-to-server/39363418/>
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag,"image height: "+height+ "---image width: "+ width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag,"inSampleSize: "+inSampleSize);
        return inSampleSize;
    }

    /**
     * <@see https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a/>
     */
    private static Bitmap correctPictureOrientation(String photoPath, Bitmap photo) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap;
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(photo, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(photo, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(photo, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = photo;
        }
        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
