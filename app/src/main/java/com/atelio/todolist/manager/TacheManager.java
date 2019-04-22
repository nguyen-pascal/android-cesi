package com.atelio.todolist.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.atelio.todolist.model.Tache;
import java.util.ArrayList;

// Manager BDD pour la classe Tache
// NGUYEN Pascal RIL 17

public class TacheManager {

    private static final String TABLE_NAME = "tache";
    public static final String KEY_ID_TACHE = "id";
    public static final String KEY_NOM_TACHE = "label";
    public static final String KEY_DONE_TACHE = "done";
    public static final String KEY_LISTE_ID = "liste_id";

    public static final String CREATE_TABLE_TACHE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID_TACHE + " INTEGER primary key," +
            " " + KEY_NOM_TACHE + " TEXT NOT NULL," +
            " " + KEY_DONE_TACHE + " BOOLEAN NOT NULL DEFAULT 0," +
            " " + KEY_LISTE_ID + " INTEGER" +
            ");";

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    // Constructeur
    public TacheManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    // Ouvre l'accès à la base lecture/écriture
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    // Insertion d'une nouvelle tâche
    public long ajouterTache(Tache tache) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOM_TACHE, tache.getLabel());
        values.put(KEY_DONE_TACHE, tache.isDone());
        values.put(KEY_LISTE_ID, tache.getListeId());
        return db.insert(TABLE_NAME, null, values);
    }

    // Change l'état de la tâche
    public int validerTache(Tache tache, boolean etat) {
        ContentValues values = new ContentValues();
        values.put(KEY_DONE_TACHE, etat);
        String where = KEY_ID_TACHE + " = ?";
        String[] whereArgs = {tache.getId() + ""};
        return db.update(TABLE_NAME, values, where, whereArgs);
    }

    // Suppression des tâches par id de la liste
    public int viderListe(int listeId) {
        return db.delete(TABLE_NAME, "liste_id = " + listeId, null);
    }

    // Suppression d'une tâche par id de la tâche
    public int supprimerTache(int tacheId) {
        return db.delete(TABLE_NAME, "id = " + tacheId, null);
    }

    // Select des tâches d'une liste
    public Cursor getTaches(int listeId) {
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE liste_id = " + listeId, null);
    }

    // Cruseur de Tache
    static public ArrayList<Tache> cursorToArrayList(Cursor c) {
        ArrayList<Tache> taches = new ArrayList<Tache>();
        if (c.moveToFirst()) {
            do {
                Tache tache = new Tache(
                        c.getInt(c.getColumnIndex(TacheManager.KEY_ID_TACHE)),
                        c.getString(c.getColumnIndex(TacheManager.KEY_NOM_TACHE)),
                        c.getInt(c.getColumnIndex(TacheManager.KEY_DONE_TACHE)) > 0,
                        c.getInt(c.getColumnIndex(TacheManager.KEY_LISTE_ID))
                );
                taches.add(tache);
            }
            while (c.moveToNext());
        }
        c.close(); // Fermeture du cursor
        return taches;
    }

}