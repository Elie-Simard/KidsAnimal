package edu.android.animauxlabo2elie;

public final class MyDatabaseContract {
    private MyDatabaseContract() {}

    public static class FilmEntry {
        public static final String TABLE_NAME = "films";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITRE = "titre";
        public static final String COLUMN_LANGUE = "langue";
        public static final String COLUMN_ETOILES = "etoiles";
    }
}
