package com.ucs.mobileappproject03.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ucs.mobileappproject03.localization.GPSClass;

import java.util.ArrayList;

public class BDSQLiteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gpsDB";
    private static final String TABELA_GPS = "tabela_GPS";
    private static final String ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String DATA = "data";
    private static final String[] COLUNAS = {ID, LATITUDE, LONGITUDE, DATA};

    public BDSQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE TABELA_GPS ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "latitude TEXT,"+
                "longitude TEXT,"+
                "data TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS TABELA_GPS");
        this.onCreate(db);
    }

    public long addPosition(GPSClass gps)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LATITUDE, gps.getLatitude());
        values.put(LONGITUDE, gps.getLongitude());
        values.put(DATA, gps.getData());
        long gpsID = db.insert(TABELA_GPS, null, values);
        db.close();

        return gpsID;
    }

    public GPSClass getPosition(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABELA_GPS, // a. tabela
                COLUNAS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            GPSClass gps = cursorToGPS(cursor);
            return gps;
        }
    }

    private GPSClass cursorToGPS(Cursor cursor)
    {
        GPSClass gps = new GPSClass();
        gps.setId(Integer.parseInt(cursor.getString(0)));
        gps.setLatitude(cursor.getString(1));
        gps.setLongitude(cursor.getString(2));
        gps.setData(cursor.getString(3));

        return gps;
    }

    public ArrayList<GPSClass> getAllgps()
    {
        ArrayList<GPSClass> listaGPS = new ArrayList<GPSClass>();

        String query = "SELECT * FROM " + TABELA_GPS + " ORDER BY " + ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                GPSClass gps = cursorToGPS(cursor);
                listaGPS.add(gps);
            } while (cursor.moveToNext());
        }

        return listaGPS;
    }
}
