package ch.epfl.sweng.swenggolf.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.swenggolf.offer.Category;

public class LocalDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "categories.db";
    private static final String TABLE_NAME = "filters";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORIES = "categories";

    public LocalDatabase(Context context, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        super(context, DATABASE_NAME, factory, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME +"(" + COLUMN_ID +" INTEGER PRIMARY KEY," + COLUMN_CATEGORIES + "TEXT )";
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void writeCategories(List<Category> categories) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORIES, Category.categoriesToSingleString(categories));

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String readCategories() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        String categories = "";

        if(cursor.moveToFirst()){
            categories = cursor.getString(1);
            cursor.close();
        }
        db.close();

        return categories;
    }

    public void setDebug(){
        writeCategories(Arrays.asList(Category.values())); // default value
    }
}
