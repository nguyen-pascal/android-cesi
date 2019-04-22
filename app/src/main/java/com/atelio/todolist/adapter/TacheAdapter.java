package com.atelio.todolist.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atelio.todolist.R;
import com.atelio.todolist.model.Tache;
import com.atelio.todolist.activity.TacheActivity;
import com.atelio.todolist.manager.TacheManager;

import java.util.ArrayList;

// TacheAdapter
// NGUYEN Pascal RIL17

public class TacheAdapter extends RecyclerView.Adapter<TacheAdapter.MyViewHolder> {
    private ArrayList<Tache> taches;
    private TacheManager m;
    private Context context;
    private int count = 1; // Compteur

    public TacheAdapter(Context context, ArrayList<Tache> taches) {
        this.taches = taches;
        this.context = context;
    }

    // Nombre d'élément
    @Override
    public int getItemCount() {
        return taches.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        m = new TacheManager(context);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tache tache = taches.get(position);
        holder.display(tache);

        // Selon l'état de la tâche, on modifie le style du texte (barré, opacité)
        if (tache.isDone()) {
            holder.label.setTextColor(Color.LTGRAY);
            holder.label.setPaintFlags(holder.label.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.label.setTextColor(Color.DKGRAY);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;
        private Tache tache;
        private final Button button;

        public MyViewHolder(final View itemView) {
            super(itemView);

            label = ((TextView) itemView.findViewById(R.id.name));
            button = ((Button) itemView.findViewById(R.id.valider));

            // Fenêtre de suppresion de tâche
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    m.open();

                    AlertDialog dlg = new AlertDialog.Builder(context)
                            .setTitle("Voulez-vous supprimer la tâche ?")

                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    m.supprimerTache(tache.getId()); // Suppression de la tâche
                                    Toast.makeText(context, "La tâche a été supprimée.", Toast.LENGTH_SHORT).show();
                                    ((TacheActivity) context).rafraichirListe();
                                }
                            }

                            )

                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }
                            )
                            .create();
                    dlg.show();
                }
            });

            // Barre ou débarre la tâche
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m.open();
                    m.validerTache(tache, !tache.isDone()); // Inversion de l'état
                    ((TacheActivity) context).rafraichirListe();
                }
            });
        }


        public void display(Tache tache) {
            this.tache = tache;
            label.setText(count + " : " + tache.getLabel());
            count++; // Numéro de la tâche par ordre croissant
        }
    }

}