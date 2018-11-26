package ch.epfl.sweng.swenggolf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;

/**
 * Local Database which store User preferences.
 */
public class LocalDatabase extends SQLiteOpenHelper {

    private static final String COLUMN_CATEGORIES = "categories";
    private static final String COLUMN_REPUTATION = "reputation";
    private static final String DEFAULT_DATABASE_NAME = "local.db";
    private static final String TABLE_NAME_FILTERS = "filters";
    private static final String TABLE_NAME_LEVEL = "level";
    private static final String COLUMN_ID = "_id";

    /**
     * Create a new Local Database.
     *
     * @param context      the context of the application
     * @param factory      used to allow returning sub-classes of Cursor when calling query
     * @param dbVersion    the Database version
     * @param databaseName the Database name
     */
    public LocalDatabase(Context context, SQLiteDatabase.CursorFactory factory,
                         int dbVersion, String databaseName) {
        super(context, databaseName, factory, dbVersion);
    }

    /**
     * Create a new Local Database with a default name.
     *
     * @param context   the context of the application
     * @param factory   used to allow returning sub-classes of Cursor when calling query
     * @param dbVersion the Database version
     */
    public LocalDatabase(Context context, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        this(context, factory, dbVersion, DEFAULT_DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQueryForCreation = "CREATE TABLE " + TABLE_NAME_FILTERS + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY, " + COLUMN_CATEGORIES + " TEXT NOT NULL);";
        db.execSQL(sqlQueryForCreation);
        sqlQueryForCreation = "CREATE TABLE " + TABLE_NAME_LEVEL + "(" + COLUMN_ID
                + " INTEGER PRIMARY KEY, " + COLUMN_REPUTATION + " INTEGER);";
        db.execSQL(sqlQueryForCreation);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FILTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LEVEL);
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

        database.insert(TABLE_NAME_FILTERS, null, values);
        database.close();
    }

    /**
     * Writes the current reputation level into the local SQLite database.
     *
     * @param level The current level
     */
    public void writeLevel(int level) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REPUTATION, level);
        database.insert(TABLE_NAME_LEVEL, null, values);
        database.close();
    }

    /**
     * Reads the saved List of categories from the Database and returns it in a String format.
     *
     * @return A String in the form "true,true,[...],false,true"
     */
    public List<Category> readCategories() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_NAME_FILTERS + " ORDER BY "
                + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        String categories = "";

        if (cursor.moveToFirst()) {
            categories = cursor.getString(1);
            cursor.close();
        }
        db.close();

        return Category.singleStringToCategories(categories);
    }

    /**
     * Reads the saved reputation level and returns it.
     *
     * @return the level saved in the database
     */
    public int readLevel() {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "Select * FROM " + TABLE_NAME_LEVEL + " ORDER BY "
                + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        int level = 0;

        if (cursor.moveToFirst()) {
            level = cursor.getInt(1);
            cursor.close();
        }
        db.close();
        return level;
    }
}
