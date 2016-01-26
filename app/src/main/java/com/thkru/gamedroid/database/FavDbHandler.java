package com.thkru.gamedroid.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

class FavDbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "fav_db";
    private static final String TABLE_FAV = "favs";
    private static final String KEY_ID = "id";

    public FavDbHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_FAV + "(" + KEY_ID + " STRING PRIMARY KEY)";
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("", "onUpgrade DB old/new: " + oldVersion + "/" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        onCreate(db);
    }

    // ##### All Crud Operations #####

    void addFav(int id) {
        Log.i("", "addFav " + id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        db.insert(TABLE_FAV, null, values);
        db.close();
    }

    String getFav(int id) {
        Log.i("", "getFav " + id);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = getSingleFav(id, db);

        if (cursor != null)
            cursor.moveToFirst();

        String favid = cursor.getString(0);
        closeCursorAndDb(db, cursor);
        return favid;
    }

    private Cursor getSingleFav(int id, SQLiteDatabase db) {
        return db.query(TABLE_FAV, new String[]{KEY_ID}, KEY_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
    }

    List<Integer> getAllFavs() {
        Log.i("", "getAllFavs");
        String selectQuery = "SELECT * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Integer> favList = fillFavList(cursor);
        closeCursorAndDb(db, cursor);
        return favList;
    }

    @NonNull
    private List<Integer> fillFavList(Cursor cursor) {
        List<Integer> favList = new ArrayList<Integer>();
        if (cursor.moveToFirst()) {
            do {
                favList.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        return favList;
    }

    boolean isFav(int id) {
        return getAllFavs().contains(id);
    }

    void deleteFav(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAV, KEY_ID + " = ?", new String[]{""+id});
        db.close();
    }

    boolean containsFav(int id) {
        Log.i("", "containsFav " + id);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = getSingleFav(id, db);

        boolean exists = cursor.getCount() > 0;
        closeCursorAndDb(db, cursor);
        return exists;
    }

    int getFavCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        closeCursorAndDb(db, cursor);
        return count;
    }

    private void closeCursorAndDb(SQLiteDatabase db, Cursor cursor) {
        cursor.close();
        db.close();
    }
}