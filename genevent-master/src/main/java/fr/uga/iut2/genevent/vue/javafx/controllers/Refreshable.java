package fr.uga.iut2.genevent.vue.javafx.controllers;

/**
 * Interface implémenté dans les controlleurs des pages
 * qui doivent ce mettre à jour (à chaque changement / call de la méthode onRequestRefresh)
 */
public interface Refreshable {
    void onRefresh();
}
