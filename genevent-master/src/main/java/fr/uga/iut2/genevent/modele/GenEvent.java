package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.EvenementException;
import fr.uga.iut2.genevent.exceptions.PrestataireException;
import fr.uga.iut2.genevent.exceptions.ArtisteException;

import fr.uga.iut2.genevent.exceptions.TerrainException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GenEvent implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    private final Map<String, Evenement> evenements = new HashMap<>();  // association qualifiée par le nom
    private final Map<String,Terrain> terrains = new HashMap<>(); // association qualifiée par le nom
    private final Map<String, Prestataire> prestataires = new HashMap<>();
    private final Map<String, Artiste> artistes = new HashMap<>();

    public void initialize(){
        Prestataire prestataire = new Prestataire("IUT 2", "contact@iut2.com", "0476284509", "Notre école", PrestataireType.INFRASTRUCTURE, true,this);
        prestataires.put(prestataire.id(), prestataire);
        prestataire = new Prestataire("Tasty Crousty", "contact@tastycrousty.com", "0753207201", "Riz et poulet", PrestataireType.RESTAURATION, true,this);
        prestataires.put(prestataire.id(), prestataire);
    }

    public Map<String, Evenement> getEvenements() {
        return this.evenements;
    }

    public Map<String, Terrain> getTerrains() {
        return terrains;
    }

    public Map<String, Prestataire> getPrestataires() {
        return prestataires;
    }

    public Map<String, Artiste> getArtistes() {
        return artistes;
    }

    /**
     * Vérifie si le préstataire est enregistré
     * @param prestataire
     * @return true si le préstataire est enregistré, false sinon
     */
    public boolean isPrestataireRegistered(Prestataire prestataire) {
        return this.prestataires.containsKey(prestataire.id());
    }

    /**
     * Vérifie si l'artiste est enregistré
     * @param artiste
     * @return true si l'artiste est enregistré, false sinon
     */
    public boolean isArtisteRegistered(Artiste artiste) {
        return this.artistes.containsKey(artiste.id());
    }

    /**
     * Vérifie si l'évènement est enregistré
     * @param event
     * @return true si l'évènement est enregistré, false sinon
     */
    public boolean isEventRegistered(Evenement event) {
        return this.evenements.containsKey(event.id());
    }

    /**
     * Crée un nouvel objet préstataire et l'ajoute dans les préstataire enregistrés.
     * @param nom
     * @param mail
     * @param telephone
     * @param information
     * @param type
     * @throws PrestataireException si le préstataire éxiste déjà
     */
    public void nouveauPrestataire(String nom, String mail, String telephone, String information, PrestataireType type) throws PrestataireException {
        if (prestataires.containsKey(nom.toLowerCase())) {
            throw new PrestataireException("Un prestataire avec ce nom existe déjà !");
        }

        Prestataire prestataire = new Prestataire(nom, mail, telephone, information, type, false,this);
        prestataires.put(prestataire.id(), prestataire);
    }

    /**
     * Crée un nouvel objet artiste et l'ajoute dans les artistes enregistrés.
     * @param nomDeScene
     * @param nom
     * @param prenom
     * @param age
     * @param telephone
     * @throws ArtisteException si l'artiste éxiste déjà
     */
    public void nouveauArtiste(String nomDeScene, String nom, String prenom, Integer age, String telephone, String informations) throws ArtisteException {
        if (artistes.containsKey(nom.toLowerCase())) {
            throw new ArtisteException("Un artiste avec ce nom existe déjà !");
        }

        Artiste artiste = new Artiste(nomDeScene, nom, prenom, age, telephone, informations,this);
        artistes.put(artiste.id(), artiste);
    }

    /**
     * Crée un nouvel objet terrain et l'ajoute dans les terrains enregistrés.
     * @param nom
     * @param adresse
     * @param surface
     * @param description
     * @throws TerrainException si le terrain éxiste déjà
     */
    public void nouveauTerrain(String nom, String adresse, double surface, String description) throws TerrainException {
        if (terrains.containsKey(nom.toLowerCase())) {
            throw new TerrainException("Un terrain avec ce nom existe déjà !");
        }

        Terrain terrain = new Terrain(nom, adresse, surface, description,this);
        terrains.put(terrain.id(), terrain);
    }

    /**
     * Crée un nouvel objet événement et l'ajoute dans les événements enregistrés.
     * @param nom
     * @param dateDebut
     * @param dateFin
     * @param terrain
     * @param budget
     * @throws EvenementException si l'événement éxiste déjà
     */
    public void nouvelEvenement(String nom, LocalDate dateDebut, LocalDate dateFin, Terrain terrain, Integer budget) throws EvenementException {
        if (evenements.containsKey(nom.toLowerCase())) {
            throw new EvenementException("Un événement avec ce nom existe déjà !");
        }

        Evenement evenement = new Evenement(nom, dateDebut, dateFin, budget, terrain, this);
        evenements.put(evenement.id(), evenement);
    }

    /**
     * Supprime le terrain des terrains enregistrés et le délie des éventuels événements auquels il est associé.
     * @param terrain
     */
    public void supprimerTerrain(Terrain terrain) {
        for(Evenement event : terrain.getEvents().values()){
            event.setTerrain(null);
        }
        terrains.remove(terrain.id());
    }


    /**
     * Supprime l'événement des événements enregistrés et le délie des éventuels terrains, artistes et préstataires auquels il est associé.
     * @param evenement
     */
    public void supprimerEvenement(Evenement evenement) {
        for (ContratArtiste contrata : new ArrayList<>(evenement.getArtiste().values())) {
            evenement.supprimeArtisteFromContrat(contrata);
        }
        for (ContratPrestataire contratp : new ArrayList<>(evenement.getPrestataires().values())) {
            evenement.supprimePrestataireFromContrat(contratp);
        }
        if (evenement.getTerrain() != null) {
            evenement.getTerrain().retirerEvent(evenement);
        }
        evenements.remove(evenement.id());
    }

    /**
     * Supprime le préstataire des préstataires enregistrés et le délie des éventuels événements auquels il est associé.
     * @param prestataire
     */
    public void supprimerPrestataire(Prestataire prestataire) {
        for(Evenement event : prestataire.getEvents().values()){
            event.supprimePrestataire(prestataire);
        }
        prestataires.remove(prestataire.id());
    }

    /**
     * Supprime l'artiste des artistes enregistrés et le délie des éventuels événements auquels il est associé.
     * @param artiste
     */
    public void supprimerArtiste(Artiste artiste) {
        for(Evenement event : artiste.getEvents().values()){
            event.supprimeArtiste(artiste);
        }

        artistes.remove(artiste.id());
    }
}
