package edu.android.animauxlabo2elie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.android.animauxlabo2elie.model.Animal;

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

        ArrayList<Animal> listeAnimaux = new ArrayList<>();
        String animalName = getIntent().getStringExtra("animalName");

        try {
            BufferedReader lire = new BufferedReader(new InputStreamReader(getAssets().open("animaux.txt")));

            lire.readLine(); // On ignore la première ligne

            String[] donnee;
            String ligne = lire.readLine();
            while (ligne != null) {
                donnee = ligne.split(";");
                listeAnimaux.add(new Animal(Integer.parseInt(donnee[0]), donnee[1], donnee[2], Integer.parseInt(donnee[3]), donnee[4], donnee[5]));
                ligne = lire.readLine();
            }
            lire.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Animal> filteredAnimals = (ArrayList<Animal>) listeAnimaux.stream()
                .filter(animal -> animal.getNom().equals(animalName))
                .collect(Collectors.toList());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AnimalAdapter adapter = new AnimalAdapter(filteredAnimals);
        recyclerView.setAdapter(adapter);
    }
}