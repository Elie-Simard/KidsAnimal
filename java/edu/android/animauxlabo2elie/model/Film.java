package edu.android.animauxlabo2elie.model;

public class Film {
    private int id;
    private String titre;
    private String langue;
    private int etoiles;

    public Film(int id, String titre, String langue, int etoiles) {
        this.id = id;
        this.titre = titre;
        this.langue = langue;
        this.etoiles = etoiles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public int getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(int etoiles) {
        this.etoiles = etoiles;
    }
}
