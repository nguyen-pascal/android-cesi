package com.atelio.todolist.activity;

import android.content.DialogInterface;
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
import com.atelio.todolist.adapter.TacheAdapter;
import com.atelio.todolist.manager.ListeManager;
import com.atelio.todolist.manager.TacheManager;
import com.atelio.todolist.model.Tache;

import java.util.ArrayList;

// TacheActivity
// NGUYEN Pascal RIL 17

public class TacheActivity extends AppCompatActivity {

    private Button btnAddTache; // Bouton Ajouter une tâche
    private Button btnVider; // Bouton Vider les tâches
    private RecyclerView my_recycler_view; // RecyclerView
    private ArrayList<Tache> taches; // Liste des tâches
    private TacheManager m; // BDD
    private ListeManager lm; // BDD

    private int listeId; // Id de la liste extra

    private String nomListe = ""; // Nom de la liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Récupération de l'id de la liste
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listeId = Integer.valueOf(getIntent().getStringExtra("listeId"));
        }

        btnAddTache = (Button) findViewById(R.id.btnAddList);
        btnVider = (Button) findViewById(R.id.btnVider);

        my_recycler_view = (RecyclerView) findViewById(R.id.my_recycler_view);

        lm = new ListeManager(this);
        lm.open();

        m = new TacheManager(this);
        m.open();

        // Changement du titre avec le nom de la liste
        nomListe = lm.getNomFromListe(listeId);
        setTitle(nomListe);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        rafraichirListe();

        // Action bouton vider la liste
        btnVider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (taches.size() > 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(TacheActivity.this);
                    alert.setTitle("Voulez-vous vider la liste ?");

                    alert.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            m.viderListe(listeId);
                            rafraichirListe();
                            Toast.makeText(TacheActivity.this, "La liste a été vidé", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    alert.show();
                }
            }
        });


        // Action bouton ajouter une tâche
        btnAddTache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(TacheActivity.this);

                alert.setTitle("Ajouter une tâche");

                final EditText input = new EditText(TacheActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // On vérifie qu'il y ait bien une valeur, sinon pas d'insertion
                        String value = input.getText().toString();
                        if(!value.isEmpty()) {
                            Tache tache = new Tache(0, value, false, listeId); // Par défaut, tâche non effectuée
                            m.ajouterTache(tache); // Insertion
                            rafraichirListe(); // Rafraichissement de la liste
                            Toast.makeText(TacheActivity.this, "La tâche a été ajouté.", Toast.LENGTH_SHORT).show();

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

    // Tâches actuelles
    public ArrayList<Tache> getTaches() {
        this.taches = TacheManager.cursorToArrayList(m.getTaches(listeId));
        return this.taches;
    }

    // Rafraichissement du RecyclerView
    public void rafraichirListe() {
        my_recycler_view.setAdapter(new TacheAdapter(TacheActivity.this, getTaches()));
    }
}
