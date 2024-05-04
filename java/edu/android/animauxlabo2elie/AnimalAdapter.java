package edu.android.animauxlabo2elie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.android.animauxlabo2elie.model.Animal;

import java.util.ArrayList;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private ArrayList<Animal> animals;

    public AnimalAdapter(ArrayList<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.animal_item, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnimalViewHolder holder, int position) {
        Animal animal = animals.get(position);
        holder.animal_id.setText(String.valueOf(animal.getId()));
        holder.animal_nom.setText(animal.getNom());
        holder.animal_classe.setText(animal.getClasse());
        holder.animal_longevite.setText(String.valueOf(animal.getLongevite()));
        holder.animal_habitat.setText(animal.getHabitat());
        holder.animal_fait.setText(animal.getFait());
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        public TextView animal_id;
        public TextView animal_nom;
        public TextView animal_classe;
        public TextView animal_longevite;
        public TextView animal_habitat;
        public TextView animal_fait;

        public AnimalViewHolder(View view) {
            super(view);
            animal_id = view.findViewById(R.id.animal_id);
            animal_nom = view.findViewById(R.id.animal_nom);
            animal_classe = view.findViewById(R.id.animal_classe);
            animal_longevite = view.findViewById(R.id.animal_longevite);
            animal_habitat = view.findViewById(R.id.animal_habitat);
            animal_fait = view.findViewById(R.id.animal_fait);
        }
    }
}