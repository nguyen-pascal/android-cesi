package com.atelio.todolist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atelio.todolist.R;
import com.atelio.todolist.adapter.ListeAdapter;
import com.atelio.todolist.manager.ListeManager;
import com.atelio.todolist.model.Liste;

import java.util.ArrayList;

// MainActivity
// Activity de démarrage
// NGUYEN Pascal RIL 17

public class MainActivity extends AppCompatActivity {

    private Button btnCreerListe; // Bouton Créer une liste
    private Intent tacheActivity;
    private RecyclerView liste_recyclerview; // Liste
    private ArrayList<Liste> listes;
    private ListeManager lm; // BDD

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreerListe = (Button) findViewById(R.id.btnCreerListe);

        liste_recyclerview = (RecyclerView) findViewById(R.id.liste_recyclerview);

        tacheActivity = new Intent(MainActivity.this, TacheActivity.class);

        lm = new ListeManager(this);
        lm.open();

        liste_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        rafraichirListe();

        // Action bouton créer une liste
        btnCreerListe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                alert.setTitle("Nommer votre liste");

                final EditText input = new EditText(MainActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Sauvegarder", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // On vérifie qu'il y ait bien une valeur, sinon pas d'insertion
                        String value = input.getText().toString();
                        if (!value.isEmpty()) {
                            Liste liste = new Liste(0, value);
                            lm.creerListe(liste); // Insertion
                            rafraichirListe(); // Rafraichissement de la liste
                            Toast.makeText(MainActivity.this, "La liste a été créée.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });

    }

    // Liste actuelle des liste
    public ArrayList<Liste> getListes() {
        this.listes = ListeManager.cursorToArrayList(lm.getListes());
        return this.listes;
    }

    // Rafraichissement du RecyclerView
    public void rafraichirListe() {
        liste_recyclerview.setAdapter(new ListeAdapter(MainActivity.this, getListes()));
    }

    // Pour aller sur l'activity des tâches
    public void allerListe(int listeId) {
        tacheActivity.putExtra("listeId", String.valueOf(listeId));
        startActivity(tacheActivity);
    }
}
