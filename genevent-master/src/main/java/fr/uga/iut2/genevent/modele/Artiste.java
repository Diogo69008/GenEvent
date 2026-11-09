package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.ArtisteException;
import fr.uga.iut2.genevent.exceptions.TerrainException;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Artiste implements Identifiable, Serializable {
    private static final long serialVersionUID = 1L;

    private String nomDeScene;
    private String nom;
    private String prenom;
    private Integer age;
    private String telephone;
    private String informations;
    private final Map<String, Evenement> events = new HashMap<>();
    private final GenEvent genEvent;

    public Artiste(String nomDeScene,
                       String nom,
                       String prenom,
                       Integer age,
                       String telephone,

                       GenEvent event
                       ) {
        setAge(age);
        setNom(nom);
        setNomDeScene(nomDeScene);
        setPrenom(prenom);
        setTelephone(telephone);
        this.genEvent = Objects.requireNonNull(event, "GenEvent ne peu pas être null");
    }

    public Artiste(String nomDeScene,
                   String nom,
                   String prenom,
                   Integer age,
                   String telephone,
                    String informations,
                   GenEvent event
    ) {
        setAge(age);
        setNom(nom);
        setNomDeScene(nomDeScene);
        setPrenom(prenom);
        setTelephone(telephone);
        setInformations(informations);
        this.genEvent = Objects.requireNonNull(event, "GenEvent ne peu pas être null");
    }

    /**
     * Permet de lier l'artiste à l'evenement
     * @param evenement evenement à lier
     * @throws ArtisteException
     */
    void ajoutDansUnEvent(Evenement evenement) throws ArtisteException {
        Objects.requireNonNull(evenement);

        if (this.events.containsKey(evenement.id())) {
            throw new ArtisteException("Cet artiste est déjà ajouté pour cet événement !");
        }

        if (!this.genEvent.isEventRegistered(evenement)) {
            throw new ArtisteException("Cet événement n'a pas été enregistré !");
        }

        this.events.put(evenement.id(), evenement);
    }

    /**
     * Supprime la liaison avec l'événement
     * @param evenement
     * @throws ArtisteException
     */
    void supprimeDansUnEvent(Evenement evenement) throws ArtisteException {
        Objects.requireNonNull(evenement);

        if (!this.events.containsKey(evenement.id())) {
            throw new ArtisteException("Cet artiste n'est pas lié à cet événement !");
        }

        if (!this.genEvent.isEventRegistered(evenement)) {
            throw new ArtisteException("Cet événement n'a pas été enregistré !");
        }

        this.events.remove(evenement.id());
    }

    /**
     * Modifie l'age d'un artiste
     * @param age
     * @throws ArtisteException
     */
    public void setAge(Integer age) throws ArtisteException {
        if (age > 0) {
            this.age = age;
        }else{
            throw new ArtisteException("L'age de l'artiste est inférieur à 1 !");
        }
    }

    /**
     * Modifie le nom de scène de l'artiste
     * @param nomArtiste
     * @throws ArtisteException
     */
    public void setNomDeScene(String nomArtiste) throws ArtisteException{
        if(nomArtiste != null && !nomArtiste.isEmpty()){
            this.nomDeScene = nomArtiste;
        }else{
            throw new ArtisteException("Le nom de l'artiste est vide !");
        }
    }

    /**
     * Modifie le prenom de l'ariste
     * @param prenom
     * @throws ArtisteException
     */
    public void setPrenom(String prenom) throws ArtisteException{
        if(prenom != null && !prenom.isEmpty()){
            this.prenom = prenom;
        }else{
            throw new ArtisteException("Le prenom de l'artiste est vide !");
        }
    }

    /**
     * Modifie le nom de l'artiste (uniquement pour le constructeur)
     * @param nom
     * @throws ArtisteException
     */
    void setNom(String nom) throws ArtisteException{
        if (nom == null || nom.isEmpty()) {
            throw new ArtisteException("Le nom de l'artiste est vide !");

        }

        this.nom = nom;
    }

    /**
     * Modifie le telephone de l'artiste
     * @param telephone
     * @throws ArtisteException si le téléphone n'est pas valide
     */
    public void setTelephone(String telephone) throws ArtisteException {
        if (!RegexPhoneValidator.validerNumero(telephone)) {
            throw new ArtisteException("Le numéro de téléphone est invalide!");
        }

        this.telephone = telephone;
    }

    public Integer getAge() {
        return age;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getNomDeScene() {
        return nomDeScene;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String id() {
        return nom.toLowerCase();
    }

    public String getInformation() {
        return informations;
    }

    public void setInformations(String informations) {

        this.informations = informations;
    }

    public Map<String, Evenement> getEvents() {
        return Collections.unmodifiableMap(events);
    }
}
