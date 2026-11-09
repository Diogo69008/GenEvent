package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.TerrainException;

import java.io.Serializable;
import java.util.*;

public class Terrain implements Identifiable, Serializable {
    private final Map<String, Evenement> events = new HashMap<>();
    private final GenEvent genEvent;

    private String nomTerrain;
    private String adresse;
    private double surface;
    private int capacite;
    private String description;


    public Terrain(String nomTerrain, String adresse, double surface, String description, GenEvent event) {
        this.setNom(nomTerrain);
        this.setAdresse(adresse);
        this.setSurface(surface);
        this.description = description;
        this.genEvent = Objects.requireNonNull(event, "GenEvent ne peu pas être null");
    }

    public List<Evenement> getEvenements() {
        return this.events.values().stream().toList();
    }

    /**
     * @param evenement
     * @throws TerrainException si le terrain a été déja été ajouter à l'événement
     */
    void ajoutDansUnEvent(Evenement evenement) throws TerrainException {
        Objects.requireNonNull(evenement);

        if (this.events.containsKey(evenement.id())) {
            throw new TerrainException("Ce terrain est déjà ajouté pour cet événement !");
        }

        this.events.put(evenement.id(), evenement);
    }

    /**
     * @param event
     * @throws TerrainException si le terrain n'est pas assigné à l'événement
     */
    public void retirerEvent(Evenement event) throws TerrainException {
        if (!this.events.containsKey(event.id())) {
            throw new TerrainException("Cet évenement n'est pas assigné au terrain !");
        }
        this.events.remove(event.id(), event);
    }

    public Map<String, Evenement> getEvents(){
        return events;
    }

    /**
     * @return l'identifiant unique du terrain
     */
    @Override
    public String id() {
        return nomTerrain.toLowerCase();
    }

    public String getNom() {
        return nomTerrain;
    }

    public String getDescription() {
        return description;
    }

    public String getAdresse() {
        return adresse;
    }

    public double getSurface() {
        return surface;
    }

    public int getCapacite() {
        return capacite;
    }

    /**
     * @param nomTerrain le nom du terrain
     * @throws TerrainException si le nom est null ou vide
     */
    void setNom(String nomTerrain) throws TerrainException {
        if (nomTerrain == null) {
            throw new TerrainException("Le nom du terrain doit être défini !");
        }

        this.nomTerrain = nomTerrain;
    }

    public void setAdresse(String adresse) throws TerrainException {
        if (adresse == null) {
            throw new TerrainException("L'adresse du terrain doit être définie !");
        }
        this.adresse = adresse;
    }

    /**
     * @param surface la surface du terrain
     * @throws TerrainException si la surface est négative
     */
    public void setSurface(double surface) throws  TerrainException {
        // En hectares
        if (surface <= 0) {
            throw new TerrainException("La surface du terrain doit être supérieur à 0 hectares !");
        }

        this.surface = surface * 10000;
        this.capacite = (int) (this.surface * 2); // Nb de personnes par m carré
    }


    public void setDescription(String text) {
        this.description = text;
    }
}
