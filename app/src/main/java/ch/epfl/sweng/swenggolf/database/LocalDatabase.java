package ch.epfl.sweng.swenggolf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;

public class LocalDatabase extends SQLiteOpenHelper {

    private static final String DEFAULT_DATABASE_NAME = "local.db";
    private static final String TABLE_NAME = "filters";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORIES = "categories";

    public LocalDatabase(Context context, SQLiteDatabase.CursorFactory factory,
                         int dbVersion, String databaseName) {
        super(context, databaseName, factory, dbVersion);
    }

    public LocalDatabase(Context context, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        this(context, factory, dbVersion, DEFAULT_DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQueryForCreation = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY, " + COLUMN_CATEGORIES + " TEXT NOT NULL);";
        db.execSQL(sqlQueryForCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Writes a List of Categories into the local SQLite database.
     *
     * @param categories A List of categories
     */
    public void writeCategories(List<Category> categories) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIES, Category.categoriesToSingleString(categories));

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    /**
     * Reads the saved List of categories from the Database and returns it in a String format.
     *
     * @return A String in the form "true,true,[...],false,true"
     */
    public List<Category> readCategories() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        String categories = "";

        if (cursor.moveToFirst()) {
            categories = cursor.getString(1);
            cursor.close();
        }
        db.close();

        return Category.singleStringToCategories(categories);
    }
}
