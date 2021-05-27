package com.tanacom.myschool.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tanacom.myschool.ui.school.model.SchoolData;

import java.util.ArrayList;
import java.util.List;

import static com.tanacom.myschool.util.Constants.DATABASE_NAME;
import static com.tanacom.myschool.util.Constants.DATABASE_VERSION;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SchoolData.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SchoolData.TABLE_NAME);

        onCreate(db);
    }

    /**
     * @param model
     * @method Insert School Data into the database.
     */
    public long insertSchoolDB(SchoolData model) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchoolData.COLUMN_SCHOOL_NAME, model.getSchoolName());
        values.put(SchoolData.COLUMN_SCHOOL_REGION, model.getSchoolRegion());
        values.put(SchoolData.COLUMN_SCHOOL_COUNTRY, model.getSchoolCountry());
        values.put(SchoolData.COLUMN_DATE_FOUNDED, model.getDateFounded());
        values.put(SchoolData.COLUMN_SCHOOL_STATUS, model.getSchoolStatus());
        values.put(SchoolData.COLUMN_LONGITUDE, model.getSchoolLongitude());
        values.put(SchoolData.COLUMN_LATITUDE, model.getSchoolLatitude());
        values.put(SchoolData.COLUMN_SCHOOL_IMAGE, model.getSchoolImage());

        long id = db.insert(SchoolData.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**
     * @param model
     * @method Update School Data from the database.
     */
    public int updateSchoolDB(SchoolData model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SchoolData.COLUMN_SCHOOL_NAME, model.getSchoolName());
        values.put(SchoolData.COLUMN_SCHOOL_REGION, model.getSchoolRegion());
        values.put(SchoolData.COLUMN_SCHOOL_COUNTRY, model.getSchoolCountry());
        values.put(SchoolData.COLUMN_DATE_FOUNDED, model.getDateFounded());
        values.put(SchoolData.COLUMN_SCHOOL_STATUS, model.getSchoolStatus());
        values.put(SchoolData.COLUMN_LONGITUDE, model.getSchoolLongitude());
        values.put(SchoolData.COLUMN_LATITUDE, model.getSchoolLatitude());
        values.put(SchoolData.COLUMN_SCHOOL_IMAGE, model.getSchoolImage());

        // updating row
//        return db.update(SchoolData.TABLE_NAME, values, SchoolData.COLUMN_SCHOOL_ID + " = ?",
//                new String[]{String.valueOf(model.getSchoolId())});

        int id = db.update(SchoolData.TABLE_NAME, values, SchoolData.COLUMN_SCHOOL_ID + " = ?",
                new String[]{String.valueOf(model.getSchoolId())});


        db.close();
        return id;


    }


    /**
     * @param id
     * @method Delete School data from the database.
     */
    public void deleteSchoolDB(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SchoolData.TABLE_NAME, SchoolData.COLUMN_SCHOOL_ID + " = ?",
                new String[]{id});
        db.close();
    }

    /**
     * @param localId
     * @method Get a School data from the database.
     */
    public SchoolData getSchoolDB(String localId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SchoolData.TABLE_NAME,
                new String[]{
                        SchoolData.COLUMN_SCHOOL_ID,
                        SchoolData.COLUMN_SCHOOL_NAME,
                        SchoolData.COLUMN_SCHOOL_REGION,
                        SchoolData.COLUMN_SCHOOL_COUNTRY,
                        SchoolData.COLUMN_DATE_FOUNDED,
                        SchoolData.COLUMN_SCHOOL_STATUS,
                        SchoolData.COLUMN_LONGITUDE,
                        SchoolData.COLUMN_LATITUDE,
                        SchoolData.COLUMN_SCHOOL_IMAGE
                },
                SchoolData.COLUMN_SCHOOL_ID + "=?",
                new String[]{localId}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SchoolData model = new SchoolData(
                cursor.getInt(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_ID)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_NAME)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_REGION)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_COUNTRY)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_DATE_FOUNDED)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_STATUS)),
                cursor.getDouble(cursor.getColumnIndex(SchoolData.COLUMN_LATITUDE)),
                cursor.getDouble(cursor.getColumnIndex(SchoolData.COLUMN_LONGITUDE)),
                cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_IMAGE))
        );

        cursor.close();

        return model;
    }

    /**
     * @method Get all School data from the database.
     */
    public List<SchoolData> getSchoolList() {
        List<SchoolData> schoolList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SchoolData.TABLE_NAME + " ORDER BY " +
                SchoolData.COLUMN_DATE_FOUNDED + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SchoolData model = new SchoolData();
                model.setSchoolId(cursor.getInt(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_ID)));
                model.setSchoolName(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_NAME)));
                model.setSchoolRegion(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_REGION)));
                model.setSchoolCountry(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_COUNTRY)));
                model.setDateFounded(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_DATE_FOUNDED)));
                model.setSchoolStatus(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_STATUS)));
                model.setSchoolLongitude(cursor.getDouble(cursor.getColumnIndex(SchoolData.COLUMN_LONGITUDE)));
                model.setSchoolLatitude(cursor.getDouble(cursor.getColumnIndex(SchoolData.COLUMN_LATITUDE)));
                model.setSchoolImage(cursor.getString(cursor.getColumnIndex(SchoolData.COLUMN_SCHOOL_IMAGE)));

                schoolList.add(model);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        return schoolList;
    }

}
