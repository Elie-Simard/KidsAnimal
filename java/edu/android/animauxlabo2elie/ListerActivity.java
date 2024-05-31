package edu.android.animauxlabo2elie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.android.animauxlabo2elie.model.Film;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ListerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lister);

        Button buttonReturn = findViewById(R.id.button_return);

        buttonReturn.setOnClickListener(v -> {
            Intent intent = new Intent(ListerActivity.this, MainActivity.class);
            startActivity(intent);
        });

        ArrayList<Film> filmList = new ArrayList<>();
        String filmTitle = getIntent().getStringExtra("filmTitle");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("animaux.txt")));

            reader.readLine(); // On ignore la première ligne

            String[] data;
            String line = reader.readLine();
            while (line != null) {
                data = line.split(";");
                filmList.add(new Film(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3])));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Film> filteredFilms = (ArrayList<Film>) filmList.stream()
                .filter(film -> film.getTitre().equals(filmTitle))
                .collect(Collectors.toList());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FilmAdapter adapter = new FilmAdapter(filteredFilms);
        recyclerView.setAdapter(adapter);
    }
}
