package edu.android.animauxlabo2elie.model;

public class Animal {
    private int id;
    private String nom;
    private String classe;
    private int longevite;
    private String habitat;
    private String fait;

    public Animal(int id, String nom, String classe, int longevite, String habitat, String fait) {
        this.id = id;
        this.nom = nom;
        this.classe = classe;
        this.longevite = longevite;
        this.habitat = habitat;
        this.fait = fait;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getLongevite() {
        return longevite;
    }

    public void setLongevite(int longevite) {
        this.longevite = longevite;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getFait() {
        return fait;
    }

    public void setFait(String fait) {
        this.fait = fait;
    }
}
