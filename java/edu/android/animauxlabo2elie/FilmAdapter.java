package edu.android.animauxlabo2elie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.android.animauxlabo2elie.model.Film;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private ArrayList<Film> filmList;

    public FilmAdapter(ArrayList<Film> filmList) {
        this.filmList = filmList;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film currentFilm = filmList.get(position);
        holder.idTextView.setText(String.valueOf(currentFilm.getId()));
        holder.titreTextView.setText(currentFilm.getTitre());
        holder.langueTextView.setText(currentFilm.getLangue());
        holder.etoilesTextView.setText(String.valueOf(currentFilm.getEtoiles()));
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView titreTextView;
        public TextView langueTextView;
        public TextView etoilesTextView;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.film_id);
            titreTextView = itemView.findViewById(R.id.film_title);
            langueTextView = itemView.findViewById(R.id.film_language);
            etoilesTextView = itemView.findViewById(R.id.film_stars);
        }
    }
}
