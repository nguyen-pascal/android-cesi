package com.atelio.todolist.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.atelio.todolist.model.Liste;
import java.util.ArrayList;

// Manager BDD pour la classe Liste
// NGUYEN Pascal RIL 17

public class ListeManager {

    private static final String TABLE_NAME = "liste";
    public static final String KEY_ID_LISTE = "id";
    public static final String KEY_NOM_LISTE = "label";

    public static final String CREATE_TABLE_LISTE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" +
            " " + KEY_ID_LISTE + " INTEGER primary key," +
            " " + KEY_NOM_LISTE + " TEXT NOT NULL" +
            ");";

    private MySQLite maBaseSQLite;
    private SQLiteDatabase db;

    // Constructeur
    public ListeManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }

    // Ouvre l'accès à la base lecture/écriture
    public void open() {
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    // Insertion d'une nouvelle liste
    public long creerListe(Liste liste) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOM_LISTE, liste.getNom());
        return db.insert(TABLE_NAME, null, values);
    }

    // Select de toutes les listes
    public Cursor getListes() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    // Récupération du nom d'une liste
    public String getNomFromListe(int id) {
        String nom = "";
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = " + id, null);
        if(c != null)
        {
            while(c.moveToNext()){
                nom = c.getString(c.getColumnIndex("label"));
            }
        }
        return nom;
    }

    // Suppression d'une liste et de tous ses tâches
    public int supprimerListe(int listeId) {
        db.delete("TACHE", "liste_id = " + listeId, null);
        return db.delete(TABLE_NAME, "id = " + listeId, null);
    }

    // Curseur de Liste
    static public ArrayList<Liste> cursorToArrayList(Cursor c) {
        ArrayList<Liste> listes = new ArrayList<Liste>();
        if (c.moveToFirst())
        {
            do {
                Liste liste = new Liste(
                        c.getInt(c.getColumnIndex(ListeManager.KEY_ID_LISTE)),
                        c.getString(c.getColumnIndex(ListeManager.KEY_NOM_LISTE))
                );
                listes.add(liste);
            }
            while (c.moveToNext());
        }
        c.close();
        return listes;
    }

}