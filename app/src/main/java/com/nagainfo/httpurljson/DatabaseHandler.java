package com.nagainfo.httpurljson;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagainfo on 12/3/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movieManager";
    private static final String TABLE = "movies";
    private static final String KEY_ID = "id";
    private static final String NAME = "name";
    private static final String RATING = "rating";
    private static final String YEAR = "year";
    private static final String IMAGE = "image";
    private static final String FAVOURITE = "favourite";
    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + NAME + " TEXT," + RATING + " TEXT," + YEAR +
                " TEXT," + IMAGE + " TEXT,"
                + FAVOURITE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, movie.getTitle());
        values.put(RATING, movie.getRating());
        values.put(YEAR, movie.getYear());
        values.put(IMAGE, movie.getThumbnailUrl());
        values.put(FAVOURITE, movie.isFav());

        // Inserting Row
        db.insert(TABLE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    //    // code to get the single contact
//    Movie getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE, new String[]{KEY_ID,
//                        NAME, RATING, YEAR, IMAGE, FAVOURITE}, KEY_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Movie movie = new Movie(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Double.parseDouble(cursor.getString(4)), Boolean.parseBoolean(cursor.getString(5)));
//        // return contact
//        return movie;
//    }

    // code to get all contacts in a list view
    public List<Movie> getAllContacts() {
        List<Movie> movieList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(NAME)));
                movie.setThumbnailUrl(cursor.getString(cursor.getColumnIndex(IMAGE)));
                movie.setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))));
                movie.setRating(Double.parseDouble(cursor.getString(cursor.getColumnIndex(RATING))));
                movie.setFav((cursor.getInt(cursor.getColumnIndex(FAVOURITE)) > 0));
                // Adding contact to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movieList;
    }

    public List<Movie> getAllLikedContacts() {
        List<Movie> movieList = new ArrayList<Movie>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE + " WHERE " + FAVOURITE + "> 0";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(NAME)));
                movie.setThumbnailUrl(cursor.getString(cursor.getColumnIndex(IMAGE)));
                movie.setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(YEAR))));
                movie.setRating(Double.parseDouble(cursor.getString(cursor.getColumnIndex(RATING))));
                movie.setFav((cursor.getInt(cursor.getColumnIndex(FAVOURITE)) > 0));
                // Adding contact to list
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        // return contact list
        return movieList;
    }

    // code to update the single contact
    public void updateContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, movie.getTitle());
        values.put(IMAGE, movie.getThumbnailUrl());
        values.put(YEAR, movie.getYear());
        values.put(RATING, movie.getRating());
        values.put(FAVOURITE, movie.isFav());
        db.update(TABLE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
//        exportDatabse(DATABASE_NAME);
        db.close();
    }


    private void exportDatabse(String databaseName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = "bkup.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    // Deleting single contact
    public void deleteContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(movie.getId())});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}