package com.ucs.mobileappproject03.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ucs.mobileappproject03.localization.GPSClass;
import com.ucs.mobileappproject03.localization.StepsClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BDSQLiteHelper extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    //-------tabela para GPS-------
    private static final String TABELA_GPS = "tabela_GPS";
    private static final String ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String DATA = "data";
    private static final String[] COLUNAS = {ID, LATITUDE, LONGITUDE, DATA};
    //-------tabela para GPS-------


    //-------tabela para PASSOS-------
    private static final String TABELA_PASSOS = "tabela_PASSOS";
    private static final String IDpassos = "id_passos";
    private static final String PASSOS = "passos";
    private static final String DATA_PASSOS = "data_passos";
    private static final String[] COLUNAS_PASSOS = {IDpassos, PASSOS, DATA_PASSOS};
    //-------tabela para PASSOS-------

    //constante do carajo
    private final long MILLISECONDSBYDAY = 86400000;

    public BDSQLiteHelper(Context context, String DataBaseName)
    {
        super(context, DataBaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE = "CREATE TABLE TABELA_GPS ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "latitude TEXT,"+
                "longitude TEXT,"+
                "data NUMBER)";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE_PASSOS = "CREATE TABLE TABELA_PASSOS ("+
                "id_passos INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "passos NUMBER,"+
                "data_passos NUMBER)";
        db.execSQL(CREATE_TABLE_PASSOS);
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
        gps.setData(cursor.getLong(3));

        return gps;
    }

    public ArrayList<GPSClass> getAllgps()
    {
        ArrayList<GPSClass> listaGPS = new ArrayList<GPSClass>();

        Date hoje = new Date();
        Calendar inicioDia = Calendar.getInstance();
        Calendar fimDia = Calendar.getInstance();

        inicioDia.setTime(hoje);
        inicioDia.set(Calendar.HOUR,0);
        inicioDia.set(Calendar.MINUTE,0);
        inicioDia.set(Calendar.SECOND,0);

        fimDia.setTime(hoje);

        long beginDay = inicioDia.getTime().getTime();
        long endDay = fimDia.getTime().getTime();

        String query = "SELECT * FROM " + TABELA_GPS + " WHERE " + DATA + " BETWEEN " + beginDay + " AND " + endDay;
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

    public long addSteps(StepsClass steps)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSOS, steps.getPassos());
        values.put(DATA_PASSOS, steps.getData());
        long stepsID = db.insert(TABELA_PASSOS, null, values);
        db.close();

        return stepsID;
    }

    public void deleteAllStepsRegisters() {

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TABELA_PASSOS);

        db.close();
    }

    private StepsClass cursorToSteps(Cursor cursor)
    {
        StepsClass steps = new StepsClass();
        steps.setId(Integer.parseInt(cursor.getString(0)));
        steps.setPassos(cursor.getLong(1));
        steps.setData(cursor.getLong(2));

        return steps;
    }

    public ArrayList<StepsClass> getAllSteps()
    {
        ArrayList<StepsClass> listaPassos = new ArrayList<StepsClass>();

        String query = "SELECT * FROM " + TABELA_PASSOS + " ORDER BY " + IDpassos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                StepsClass steps = cursorToSteps(cursor);
                listaPassos.add(steps);
            } while (cursor.moveToNext());
        }

        return listaPassos;
    }

    public int getTodaySteps(){
        Date hoje = new Date();
        Calendar fimDia = Calendar.getInstance();

        fimDia.setTime(hoje);

        long beginDay = Calendar.getInstance().getTime().getTime() - MILLISECONDSBYDAY;
        long endDay = fimDia.getTime().getTime();

        String query = "SELECT SUM( " + PASSOS + " ) as Total FROM " + TABELA_PASSOS + " WHERE " + DATA_PASSOS + " BETWEEN " + beginDay + " AND " + endDay;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("Total"));
        }

        return 0;
    }

    public ArrayList<StepsClass> get1WeekSteps()
    {
        ArrayList<StepsClass> listaPassos = new ArrayList<StepsClass>();

        Date hoje = new Date();
        Calendar fimDia = Calendar.getInstance();

        fimDia.setTime(hoje);

        long beginDay = Calendar.getInstance().getTime().getTime() - MILLISECONDSBYDAY*6;
        long endDay = fimDia.getTime().getTime();

        String query = "SELECT * FROM " + TABELA_PASSOS + " WHERE " + DATA_PASSOS + " BETWEEN " + beginDay + " AND " + endDay + " ORDER BY " + IDpassos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                StepsClass steps = cursorToSteps(cursor);
                listaPassos.add(steps);
            } while (cursor.moveToNext());
        }

        return listaPassos;
    }

    public ArrayList<StepsClass> get1MonthSteps()
    {
        ArrayList<StepsClass> listaPassos = new ArrayList<StepsClass>();

        Date hoje = new Date();
        Calendar fimDia = Calendar.getInstance();

        fimDia.setTime(hoje);

        long beginDay = Calendar.getInstance().getTime().getTime() - MILLISECONDSBYDAY*30;
        long endDay = fimDia.getTime().getTime();

//        String query = "SELECT FROM " + TABELA_PASSOS + " WHERE " + DATA_PASSOS + " BETWEEN " + beginDay + " AND " + endDay + " ORDER BY " + IDpassos;
        String query = "SELECT SUM( " + PASSOS + " ) FROM " + TABELA_PASSOS + " WHERE " + DATA_PASSOS + " BETWEEN " + beginDay + " AND " + endDay + " ORDER BY " + IDpassos;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                StepsClass steps = cursorToSteps(cursor);
                listaPassos.add(steps);
            } while (cursor.moveToNext());
        }

        return listaPassos;
    }
}
