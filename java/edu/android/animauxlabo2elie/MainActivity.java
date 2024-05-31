package edu.android.animauxlabo2elie;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.android.animauxlabo2elie.model.Film;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Film> filmList;
    private MyDatabaseHelper dbHelper;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filmList = new ArrayList<>();
        dbHelper = new MyDatabaseHelper(this);
        loadFilmsFromDatabase();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_lister_films) {
                refreshRecyclerView();
            } else if (id == R.id.nav_ajouter_film) {
                AlertDialog.Builder builder = createAddFilmDialog();
                builder.show();
            } else if (id == R.id.nav_supprimer_film) {
                AlertDialog.Builder builder = createDeleteFilmDialog();
                builder.show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FilmAdapter adapter = new FilmAdapter(filmList);
        recyclerView.setAdapter(adapter);
    }

    private void loadFilmsFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDatabaseContract.FilmEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No films found in the database.", Toast.LENGTH_SHORT).show();
            loadFilmsFromFileToDb(db);
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseContract.FilmEntry.COLUMN_ID));
                String titre = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseContract.FilmEntry.COLUMN_TITRE));
                String langue = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseContract.FilmEntry.COLUMN_LANGUE));
                int etoiles = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseContract.FilmEntry.COLUMN_ETOILES));

                filmList.add(new Film(id, titre, langue, etoiles));
            }
        }

        cursor.close();
    }

    private void loadFilmsFromFileToDb(SQLiteDatabase db) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("animaux.txt"), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 3) {
                    String titre = data[0].trim();
                    String langue = data[1].trim();
                    int etoiles = Integer.parseInt(data[2].trim());

                    ContentValues values = new ContentValues();
                    values.put(MyDatabaseContract.FilmEntry.COLUMN_TITRE, titre);
                    values.put(MyDatabaseContract.FilmEntry.COLUMN_LANGUE, langue);
                    values.put(MyDatabaseContract.FilmEntry.COLUMN_ETOILES, etoiles);

                    long newRowId = db.insertWithOnConflict(
                            MyDatabaseContract.FilmEntry.TABLE_NAME,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE
                    );
                    if (newRowId != -1) {
                        Log.d("DB_INSERT", "Film inserted/updated: " + titre);
                    } else {
                        Log.d("DB_INSERT", "Error inserting/updating film: " + titre);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading films from file.", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshRecyclerView() {
        filmList.clear();
        loadFilmsFromDatabase();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FilmAdapter adapter = new FilmAdapter(filmList);
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private AlertDialog.Builder createDeleteFilmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Film");

        EditText filmID = new EditText(MainActivity.this);
        filmID.setHint("ID");
        builder.setView(filmID);

        builder.setPositiveButton("Delete", (dialog, which) -> {
            int idFilm = Integer.parseInt(filmID.getText().toString());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String selection = MyDatabaseContract.FilmEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(idFilm)};
            db.delete(MyDatabaseContract.FilmEntry.TABLE_NAME, selection, selectionArgs);
            refreshRecyclerView();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder;
    }

    @NonNull
    private AlertDialog.Builder createAddFilmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Film");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText filmTitle = new EditText(MainActivity.this);
        filmTitle.setHint("Title");
        layout.addView(filmTitle);

        EditText filmLangue = new EditText(MainActivity.this);
        filmLangue.setHint("Language");
        layout.addView(filmLangue);

        EditText filmEtoiles = new EditText(MainActivity.this);
        filmEtoiles.setHint("Stars");
        layout.addView(filmEtoiles);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String titreFilm = filmTitle.getText().toString();
            String langueFilm = filmLangue.getText().toString();
            int etoilesFilm = Integer.parseInt(filmEtoiles.getText().toString());

            dbHelper.insertFilmIfNotExists(titreFilm, langueFilm, etoilesFilm);
            refreshRecyclerView();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder;
    }

    @NonNull
    private AlertDialog.Builder createListByLangueDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("List by Language");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText filmLangue = new EditText(MainActivity.this);
        filmLangue.setHint("Language");
        layout.addView(filmLangue);

        builder.setView(layout);

        builder.setPositiveButton("List", (dialog, which) -> {
            String langueFilm = filmLangue.getText().toString();

            filmList.clear();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String[] columns = {"id", "titre", "langue", "etoiles"};
            String selection = "langue = ?";
            String[] selectionArgs = {langueFilm};

            Cursor cursor = db.query("films", columns, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String titre = cursor.getString(cursor.getColumnIndexOrThrow("titre"));
                String langue = cursor.getString(cursor.getColumnIndexOrThrow("langue"));
                int etoiles = cursor.getInt(cursor.getColumnIndexOrThrow("etoiles"));

                filmList.add(new Film(id, titre, langue, etoiles));
            }
            cursor.close();

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            FilmAdapter adapter = new FilmAdapter(filmList);
            recyclerView.setAdapter(adapter);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder;
    }
}
