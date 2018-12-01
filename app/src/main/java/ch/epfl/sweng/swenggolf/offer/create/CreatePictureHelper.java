package ch.epfl.sweng.swenggolf.offer.create;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.exifinterface.media.ExifInterface;

public final class CreatePictureHelper {
    public static final int MAX_IMAGE_SIZE = 175 * 512;
    private static final int MAX_COMPRESS_QUALITY = 100;
    private static final int STEP_COMPRESS_QUALITY = 5;
    private static final String COMPRESSION_TAG = "compressBitmap";

    private CreatePictureHelper() {
    }

    /**
     * Creates a compressed version of a file given its path.
     *
     * @param context  the context from which the cache is taken.
     * @param filePath the path of the file to be compressed.
     * @param filename the name of the compressed file.
     * @return the Uri where the compressed file is stored.
     */
    public static Uri compressImageFromPath(Context context, String filePath, String filename) {
        // First decode with inJustDecodeBounds=true to check dimensions of image
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize(First we are going to resize the image to 800x800 image,
        // in order to not have a big but very low quality image.
        //resizing the image will already reduce the file size,
        // but after resizing we will check the file size and start to compress image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bmpPic = BitmapFactory.decodeFile(filePath, options);
        if (bmpPic == null) {
            return null;
        }

        Bitmap tmp = null;
        try {
            tmp = correctPictureOrientation(filePath, bmpPic);
        } catch (IOException e) {
            Toast.makeText(context, "Failed to restore picture original orientation",
                    Toast.LENGTH_SHORT).show();
        }
        if (tmp != null) {
            bmpPic = tmp;
        }

        return compressImageIntoCache(bmpPic, context, filename);
    }


    /**
     * <@see https://stackoverflow.com/questions/39361550/android-resize-image-to-upload-to-server/39363418/>
     */
    public static Uri compressImageIntoCache(Bitmap bmpPic, Context context, String fileName) {
        int targetQuality = computeTargetQuality(bmpPic);
        File temp = compressAndSave(context, fileName, targetQuality, bmpPic);
        //return the path of resized and compressed file
        if (temp != null) {
            return FileProvider.getUriForFile(context, "ch.epfl.sweng.swenggolf.fileprovider", temp);
        } else {
            return null;
        }
    }

    private static int computeTargetQuality(Bitmap bmpPic) {
        int compressQuality = MAX_COMPRESS_QUALITY; // quality decreasing by 5 every loop.
        int streamLength;
        do {
            ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
            Log.d(COMPRESSION_TAG, "Quality: " + compressQuality);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
            compressQuality -= STEP_COMPRESS_QUALITY;
            Log.d(COMPRESSION_TAG, "Size: " + streamLength / 1024 + " kb");
        }
        while (streamLength >= MAX_IMAGE_SIZE);
        return compressQuality;
    }

    private static File compressAndSave(Context context, String fileName, int targetQuality,
                                        Bitmap bmpPic) {
        File temp = null;
        try {
            //save the resized and compressed file to disk cache
            Log.d(COMPRESSION_TAG, "cacheDir: " + context.getCacheDir());
            temp = File.createTempFile(fileName, null, context.getCacheDir());
            FileOutputStream bmpFile = new FileOutputStream(temp);
            bmpPic.compress(Bitmap.CompressFormat.JPEG, targetQuality, bmpFile);
            bmpFile.flush();
            bmpFile.close();
        } catch (Exception e) {
            Log.e(COMPRESSION_TAG, "Error on saving file");
        }
        return temp;
    }

    /**
     * <@see https://stackoverflow.com/questions/39361550/android-resize-image-to-upload-to-server/39363418/>
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        String debugTag = "MemoryInformation";
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(debugTag, "image height: " + height + "---image width: " + width);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.d(debugTag, "inSampleSize: " + inSampleSize);
        return inSampleSize;
    }

    /**
     * <@see https://stackoverflow.com/questions/14066038/why-does-an-image-captured-using-camera-intent-gets-rotated-on-some-devices-on-a/>
     */
    private static Bitmap correctPictureOrientation(String photoPath, Bitmap photo)
            throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap;
        switch (orientation) {
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
