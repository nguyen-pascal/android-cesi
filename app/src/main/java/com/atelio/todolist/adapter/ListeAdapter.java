package com.atelio.todolist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atelio.todolist.R;
import com.atelio.todolist.activity.MainActivity;
import com.atelio.todolist.manager.ListeManager;
import com.atelio.todolist.model.Liste;

import java.util.ArrayList;

// ListeAdapter
// NGUYEN Pascal RIL17

public class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.MyViewHolder> {
    private ArrayList<Liste> listes; // Liste des listes
    private ListeManager lm; // BDD
    private Context context; // Context

    public ListeAdapter(Context context, ArrayList<Liste> listes) {
        this.listes = listes;
        this.context = context;
    }

    // Retourne le nombre d'éléments
    @Override
    public int getItemCount() {
        return listes.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        lm = new ListeManager(context);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Liste liste = listes.get(position);
        holder.display(liste);
    }

    // RecyclerView
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;
        private Liste liste;
        private final Button button;

        public MyViewHolder(final View itemView) {
            super(itemView);

            label = ((TextView) itemView.findViewById(R.id.name));
            button = ((Button) itemView.findViewById(R.id.valider));

            // Action bouton "supprimer"
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lm.open();
                    AlertDialog dlg = new AlertDialog.Builder(context)
                            .setTitle("Voulez-vous supprimer la liste ?")
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lm.supprimerListe(liste.getId());
                                    Toast.makeText(context, "La liste a été supprimée.", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) context).rafraichirListe(); // Liste à jour
                                }
                            })

                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    }
                            )
                            .create();
                    dlg.show(); // On affiche l'AlertDialog
                }
            });

            // Action appui sur l'élément
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) context).allerListe(liste.getId()); // On va dans l'activity de la liste
                }
            });
        }

        public void display(Liste liste) {
            this.liste = liste;
            label.setText(liste.getNom());
        }
    }

}