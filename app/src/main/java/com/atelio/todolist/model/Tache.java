package com.atelio.todolist.model;

// Classe Tache
// NGUYEN Pascal RIL 17

public class Tache {
    private int id;
    private String label; // Nom
    private boolean done; // Etat
    private int listeId; // Id de la liste

    public Tache(int id, String label, boolean done, int listeId) {
        this.id = id;
        this.label = label;
        this.done = done;
        this.listeId = listeId;
    }

    // Accesseurs et mutateurs

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getListeId() {
        return listeId;
    }

    public void setListeId(int listeId) {
        this.listeId = listeId;
    }
}
