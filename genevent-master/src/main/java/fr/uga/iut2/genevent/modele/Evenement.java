package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.ArtisteException;
import fr.uga.iut2.genevent.exceptions.EvenementException;
import fr.uga.iut2.genevent.exceptions.PrestataireException;
import fr.uga.iut2.genevent.exceptions.TerrainException;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


public class Evenement implements Serializable, Identifiable {
    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    private final GenEvent genevent;
    private final String nom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Terrain terrain;

    private Integer budget;
    private Integer billet; // par jour

    private final Map<String, ContratPrestataire> prestataires = new HashMap<>();
    private final Map<String, ContratArtiste> artiste = new HashMap<>();

    public Evenement(String nom,
                     LocalDate dateDebut,
                     LocalDate dateFin,
                     Integer budget,
                     Terrain terrain,

                     GenEvent genevent) throws EvenementException {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.genevent = genevent;
        this.nom = Objects.requireNonNull(nom, "Le nom doit être défini");

        setDateDebut(dateDebut);
        setDateFin(dateFin);
        setBudget(budget);
        setTerrain(terrain);
    }

    /**
     * @return l'identifiant unique de l'évènement
     */
    @Override
    public String id() {
        return nom.toLowerCase();
    }

    public String getNom() {
        return this.nom;
    }


    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Integer getBudget() {
        return budget;
    }


    /**
     * ajoute un prestataire à l'événement
     * @param prestataire
     * @param contrat
     * @param cout
     * @throws EvenementException si le prestataire est déjà assigné à l'événement
     * @throws PrestataireException si le prestataire n'est pas enregistré dans GenEvent
     */
    public void ajoutPrestataire(Prestataire prestataire, FileInfo contrat, int cout) throws EvenementException, PrestataireException {
        Objects.requireNonNull(prestataire);

        if (this.prestataires.containsKey(prestataire.id())) {
            throw new EvenementException("Ce prestataire est déjà lié à cette événement !");
        }

        if (!this.genevent.isPrestataireRegistered(prestataire)) {
            throw new EvenementException("Ce prestataire n'est pas enregistré !");
        }

        prestataire.ajoutDansUnEvent(this);
        this.prestataires.put(prestataire.id(), new ContratPrestataire(prestataire, contrat, cout));
    }

    /**
     * permet de retirer un prestataire de l'événement
     * @param prestataireId le prestataire à retirer
     * @throws EvenementException si le prestataire n'est pas assigné à l'événement
     */
    public void supprimePrestataire(String prestataireId) throws EvenementException, PrestataireException {
        if (!this.prestataires.containsKey(prestataireId)) {
            throw new EvenementException("Ce prestataire n'est pas lié à cette événement !");
        }

        supprimePrestataire(this.prestataires.get(prestataireId).getPrestataire());
    }

    /**
     * permet de retirer un artiste de l'événement
     * @param artiste
     * @throws EvenementException si l'artiste n'est pas assigné à l'événement
     * @throws ArtisteException si l'artiste n'est pas enregistré dans GenEvent
     */
    public void supprimeArtiste(Artiste artiste) throws EvenementException, ArtisteException {
        Objects.requireNonNull(artiste);

        if (!this.artiste.containsKey(artiste.id())) {
            throw new EvenementException("Cet artiste n'est pas lié à cet événement !");
        }

        if (!this.genevent.isArtisteRegistered(artiste)) {
            throw new ArtisteException("Cet artiste n'est pas enregistré !");
        }

        artiste.supprimeDansUnEvent(this);
        this.artiste.remove(artiste.id());
    }

    /**
     * permet de supprimer le contrat d'un artiste de l'événement
     * @param contrat
     * @throws EvenementException si l'artiste n'est pas assigné à l'événement
     * @throws ArtisteException si l'artiste n'est pas enregistré dans GenEvent
     */
    public void supprimeArtisteFromContrat(ContratArtiste contrat) throws EvenementException, ArtisteException {
        supprimeArtiste(contrat.getArtiste());
    }

    /**
     * permet de supprimer le contrat d'un prestataire de l'événement
     * @param contrat
     * @throws EvenementException si le prestataire n'est pas assigné à l'événement
     * @throws PrestataireException si le prestataire n'est pas enregistré dans GenEvent
     */
    public void supprimePrestataireFromContrat(ContratPrestataire contrat) throws EvenementException, ArtisteException {
        supprimePrestataire(contrat.getPrestataire());
    }


    /**permet d'ajouter un artiste à l'événement
     * @param artiste
     * @param contrat
     * @param cout
     * @throws EvenementException si l'artiste est déjà assigné à l'événement
     * @throws ArtisteException si l'artiste n'est pas enregistré dans GenEvent
     */
    public void ajoutArtiste(Artiste artiste, int cout, FileInfo contrat) throws EvenementException, PrestataireException {
        Objects.requireNonNull(artiste);

        if (this.artiste.containsKey(artiste.id())) {
            throw new EvenementException("Cet artiste est déjà lié à cet événement !");
        }

        if (!this.genevent.isArtisteRegistered(artiste)) {
            throw new EvenementException("Cet artiste n'est pas enregistré !");
        }

        artiste.ajoutDansUnEvent(this);
        this.artiste.put(artiste.id(), new ContratArtiste(artiste, contrat, cout));
    }

    /**
     * permet de retirer un prestataire de l'événement
     * @param prestataire
     * @throws EvenementException si le prestataire n'est pas assigné à l'événement
     * @throws PrestataireException si le prestataire n'est pas enregistré dans GenEvent
     */
    public void supprimePrestataire(Prestataire prestataire) throws EvenementException, PrestataireException {
        Objects.requireNonNull(prestataire);

        if (!this.prestataires.containsKey(prestataire.id())) {
            throw new EvenementException("Ce prestataire n'est pas lié à cette événement !");
        }

        if (!this.genevent.isPrestataireRegistered(prestataire)) {
            throw new EvenementException("Ce prestataire n'est pas enregistré !");
        }

        prestataire.supprimeDansUnEvent(this);
        this.prestataires.remove(prestataire.id());
    }

    /**
     * permet de definir la date de début de l'événement
     * @param dateDebut
     * @throws EvenementException si la date de début est après la date de fin
     */
    public void setDateDebut(LocalDate dateDebut) throws EvenementException {
        if (dateDebut.isAfter(dateFin)) {
            throw new EvenementException("la date de début ne peu pas être après la date de fin !");
        }

        this.dateDebut = dateDebut;
    }

    /**
     * permet de definir la date de fin de l'événement
     * @param dateFin
     * @throws EvenementException si la date de fin est avant la date de début
     */
    public void setDateFin(LocalDate dateFin) throws EvenementException {
        if (dateDebut.isAfter(dateFin)) {
            throw new EvenementException("la date de début ne peu pas être après la date de fin !");
        }

        this.dateFin = dateFin;
    }

    /**
     * permet de definir le budget de l'événement
     * @param budget
     * @throws EvenementException si le budget n'est pas définit
     */
    public void setBudget(Integer budget) throws EvenementException {
        if (budget == null) {
            throw new EvenementException("Le budget doit être défini");
        }

        this.budget = budget;
        updatePrixBillet();
    }

    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * permet de definir le terrain de l'événement
     * @param terrain
     * @throws EvenementException si le terrain est déjà assigné à un autre événement
     */
    public void setTerrain(Terrain terrain) throws EvenementException, TerrainException {
        if (terrain == null){
            this.terrain = null;
            return;
        }

        boolean terrainPris = false;

        for (Evenement value : terrain.getEvenements()) {
            if (value != this && !value.getDateDebut().isAfter(this.dateFin) && !value.getDateFin().isBefore(this.dateDebut)) {
                terrainPris = true;
                break;
            }
        }

        if(!terrainPris){
            if (this.terrain != null && this.terrain != terrain) {
                this.terrain.retirerEvent(this);
            }
            this.terrain = terrain;
            if (!terrain.getEvenements().contains(this)) {
                terrain.ajoutDansUnEvent(this);
            }
            updatePrixBillet();
        } else {
            throw new EvenementException("Le terrain est déjà pris par un autre évènement pendant cette période !");
        }
    }

    /**
     * permet de definir le prix du billet de l'événement
     * @throws EvenementException si le prix du billet n'est pas définit
     */
    public void updatePrixBillet() {
        this.billet = calculPrixBillet(budget, terrain);
    }

    /**
     * permet de calculer le prix du billet de l'événement en fonction du budget et de la capacité du terrain
     * @param budget
     * @param terrain
     * @return le prix du billet
     */
    public static Integer calculPrixBillet(Integer budget, Terrain terrain) {
        if (budget == null || terrain == null || terrain.getCapacite() <= 0) return null;
        double coutParPlace = (double) budget / terrain.getCapacite();
        int prixCalcule = (int) Math.ceil(coutParPlace * 1.25);
        return Math.max(25, Math.min(200, prixCalcule));
    }

    public Integer getBillet() {
        return billet;
    }

    public Map<String, ContratArtiste> getArtiste() {
        return artiste;
    }

    public Map<String, ContratPrestataire> getPrestataires() {
        return prestataires;
    }

    /**
     * permet de calculer les benefices de l'événement
     * @param prixPlace
     * @param budgetInvesti
     * @return
     */
    public double calculerBenefices(double prixPlace, double budgetInvesti) {
        if (terrain == null || terrain.getCapacite() <= 0 || prixPlace <= 0 || budgetInvesti < 0) {
            return 0;
        }

        int capacite = terrain.getCapacite();
        double budgetParPlace = budgetInvesti / capacite;
        double effetBudget = 1 - Math.exp(-budgetParPlace / 40.0);
        double effetPrix = Math.exp(-(prixPlace - 25.0) / 100.0);
        double tauxRemplissage = 0.20 + (0.75 * effetBudget * effetPrix);
        tauxRemplissage = Math.min(0.95, tauxRemplissage);
        double placesVendues = capacite * tauxRemplissage;
        double recettes = placesVendues * prixPlace;
        double coutVariable = placesVendues * 10.0;
        double fraisBilletterie = recettes * 0.15;
        double coutsTotaux = budgetInvesti + coutVariable + fraisBilletterie;
        double beneficeNet = recettes - coutsTotaux;

        if (beneficeNet > (budgetInvesti * 1.5)) {
            beneficeNet = (budgetInvesti * 1.5) + (beneficeNet - (budgetInvesti * 1.5)) * 0.1;
        }
        return beneficeNet;
    }
}
