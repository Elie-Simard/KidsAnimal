package edu.android.animauxlabo2elie;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean insertFilmIfNotExists(String titre, String langue, int etoiles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titre", titre);
        values.put("langue", langue);
        values.put("etoiles", etoiles);

        long result = db.insert("films", null, values);
        db.close();
        return result != -1;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS films (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titre TEXT," +
                "langue TEXT," +
                "etoiles INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS films");
        onCreate(db);
    }
}
