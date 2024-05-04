package edu.android.animauxlabo2elie;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.android.animauxlabo2elie.model.Animal;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/* La logique de l'application est la suivante --->

	1. Chargement des animaux au démarrage à partir du fichier "animals.txt".
	2. Initialisation du RecyclerView avec les animaux chargés.
	3. Rafraîchissement du RecyclerView lors de la sélection "Lister Animaux" dans le menu.
	4. Boîte de dialogue pour sélectionner un nom d'animal lors de la sélection "Lister par Nom" dans le menu.
	5. Boîte de dialogue pour ajouter un animal lors de la sélection "Ajouter Animal" dans le menu.
	6. Boîte de dialogue pour supprimer un animal lors de la sélection "Supprimer Animal" dans le menu.
	7. Lancement d'une nouvelle activité pour afficher les informations de l'animal sélectionné lors de la confirmation dans la boîte de dialogue "Lister par Nom".
	8. Ajout d'un nouvel animal à la liste et rafraîchissement du RecyclerView lors de la confirmation dans la boîte de dialogue "Ajouter Animal".
	9. Suppression de l'animal sélectionné de la liste et rafraîchissement du RecyclerView lors de la confirmation dans la boîte de dialogue "Supprimer Animal".
	10. Fermeture de la boîte de dialogue lors de l'annulation.
	11. Ouverture ou fermeture du menu de navigation lors du clic sur le bouton de navigation.
	12. Affichage du fragment correspondant à la sélection dans le menu de navigation.
	13. Fermeture du menu de navigation après la sélection.
	14. Affichage du bon fragment lors de la sélection dans le menu de navigation.
 */

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ArrayList<Animal> animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //onCreate est appelé lors de la création de l'activité, elle initialise toutes les variables et les vues
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animalList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Boutons de navigation(les 3 barres horizontales)
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close); //crée un bouton de navigation
        drawerLayout.addDrawerListener(actionBarDrawerToggle); //ajoute un écouteur pour écouter les événements de navigation
        actionBarDrawerToggle.syncState(); //synchronise l'état du bouton de navigation avec le menu de navigation

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //affiche le bouton de navigation

        navigationView.setNavigationItemSelectedListener(item -> { //écouteur pour écouter les événements de sélection dans le menu de navigation
            int id = item.getItemId();

            if (id == R.id.nav_lister_animaux) {
                refreshRecyclerView(animalList);
            } else if (id == R.id.nav_lister_par_nom) {
                AlertDialog.Builder builder = createListByNameDialog();
                builder.show();
            } else if (id == R.id.nav_ajouter_animal) {
                AlertDialog.Builder builder = createAddAnimalDialog();
                builder.show();
            } else if (id == R.id.nav_supprimer_animal) {
                AlertDialog.Builder builder = createDeleteAnimalDialog();
                builder.show();
            }

            DrawerLayout drawer = findViewById(R.id.drawer_layout); //récupère le menu de navigation
            drawer.closeDrawer(GravityCompat.START); //ferme le menu de navigation
            return true;
        });

        loadAnimalsFromFile();

        //Affichage de la liste
        RecyclerView recyclerView = findViewById(R.id.recyclerView); //RecyclerView est un conteneur pour afficher une liste de données, il est plus efficace que ListView car il recycle les vues, recycler les vues signifie qu'il réutilise les vues qui ne sont plus visibles pour afficher de nouvelles données
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AnimalAdapter adapter = new AnimalAdapter(animalList);
        recyclerView.setAdapter(adapter);
    }

    private void loadAnimalsFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("animaux.txt"))); //getAssets() ouvre un fichier dans le dossier assets

            reader.readLine();

            String[] data;
            String line = reader.readLine();
            while (line != null) { //BOUCLE
                data = line.split(";");
                animalList.add(new Animal(Integer.parseInt(data[0]), data[1], data[2], Integer.parseInt(data[3]), data[4], data[5]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshRecyclerView(ArrayList<Animal> animalList) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView); //récupère l'ancien RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //crée un nouveau RecyclerView
        //un adapteur est un pont entre une vue et des données, il fournit des données à une vue et gère la création de vues pour chaque élément de données
        AnimalAdapter adapter = new AnimalAdapter(animalList); //crée un nouvel adaptateur avec la nouvelle liste d'animaux
        recyclerView.setAdapter(adapter);
    }

    @NonNull
    private AlertDialog.Builder createDeleteAnimalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //MainActivity.this est une référence à l'activité actuelle, on l'appellera quand on voudra créer une boîte de dialogue
        builder.setTitle("Delete Animal");

        EditText animalID = new EditText(MainActivity.this);
        animalID.setHint("ID");
        builder.setView(animalID);

        builder.setPositiveButton("Delete", (dialog, which) -> {
            int idAnimal = Integer.parseInt(animalID.getText().toString());
            animalList.removeIf(animal -> animal.getId() == idAnimal);
            refreshRecyclerView(animalList);

            Toast.makeText(MainActivity.this, "Animal deleted", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        return builder;
    }

    @NonNull
    private AlertDialog.Builder createAddAnimalDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Animal");

        LinearLayout formLayout = new LinearLayout(MainActivity.this);
        formLayout.setOrientation(LinearLayout.VERTICAL);

        EditText animalID = new EditText(MainActivity.this);
        animalID.setHint("ID");
        formLayout.addView(animalID);

        EditText animalName = new EditText(MainActivity.this);
        animalName.setHint("Name");
        formLayout.addView(animalName);

        EditText animalClass = new EditText(MainActivity.this);
        animalClass.setHint("Class");
        formLayout.addView(animalClass);

        EditText animalLifespan = new EditText(MainActivity.this);
        animalLifespan.setHint("Lifespan");
        formLayout.addView(animalLifespan);

        EditText animalHabitat = new EditText(MainActivity.this);
        animalHabitat.setHint("Habitat");
        formLayout.addView(animalHabitat);

        EditText animalFact = new EditText(MainActivity.this);
        animalFact.setHint("Fact");
        formLayout.addView(animalFact);

        builder.setView(formLayout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            animalList.add(new Animal(Integer.parseInt(animalID.getText().toString()), animalName.getText().toString(), animalClass.getText().toString(), Integer.parseInt(animalLifespan.getText().toString()), animalHabitat.getText().toString(), animalFact.getText().toString()));
            refreshRecyclerView(animalList);


            Toast.makeText(MainActivity.this, "Animal added", Toast.LENGTH_SHORT).show();
            refreshRecyclerView(animalList);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        return builder;
    }

    @NonNull
    private AlertDialog.Builder createListByNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Animal Name");

        Spinner spinner = new Spinner(MainActivity.this);
        String[] animalNames = getAnimalNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, animalNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(spinner);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String selectedAnimalName = spinner.getSelectedItem().toString();

            if (!selectedAnimalName.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ListerActivity.class);
                intent.putExtra("animalName", selectedAnimalName);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Please select an animal name", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        return builder;
    }


    private String[] getAnimalNames() {
        return animalList.stream()
                .map(Animal::getNom)
                .toArray(String[]::new);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
