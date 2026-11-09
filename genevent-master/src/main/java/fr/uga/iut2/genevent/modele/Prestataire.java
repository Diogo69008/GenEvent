package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.PrestataireException;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;
import fr.uga.iut2.genevent.exceptions.TerrainException;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author theo
 * @project genevent
 * @date 14/06/2026 17:32
 */
public class Prestataire implements Identifiable, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Evenement> events = new HashMap<>(); // events où ce prestataire est utilisé
    private final GenEvent genEvent;

    private String nom, mail, information;
    private String telephone;
    private PrestataireType type; // peut-être final si on dit qu'il est identifé par le type dans l'event
    private boolean sponsorise;

    public Prestataire(String nom,
                       String mail,
                       String telephone,
                       String information,
                       PrestataireType type,
                       boolean sponsorise,

                       GenEvent event) {
        this.setNom(nom);
        this.setMail(mail);
        this.telephone = telephone;
        this.information = information;
        this.setType(type);
        this.setSponsorise(sponsorise);

        this.genEvent = Objects.requireNonNull(event, "GenEvent ne peu pas être null");
    }

    public Prestataire(String nom,
                       String mail,
                       String telephone,
                       String information,
                       PrestataireType type,
                       GenEvent event) {
        this(nom, mail, telephone, information, type, false, event);
    }

    /**
     * Lie le prestataire à l'évènement passé en paramètre.
     * @param evenement
     * @throws PrestataireException si l'évènement n'existe pas ou est déja lié.
     */
    void ajoutDansUnEvent(Evenement evenement) throws PrestataireException {
        Objects.requireNonNull(evenement);

        if (this.events.containsKey(evenement.id())) {
            throw new PrestataireException("Ce prestataire est déjà ajouté pour cette événement !");
        }

        if (!this.genEvent.isEventRegistered(evenement)) {
            throw new PrestataireException("Cette événement n'a pas été enregistré !");
        }

        this.events.put(evenement.id(), evenement);
    }

    public void retirerEvent(Evenement event) throws PrestataireException {
        if (!this.events.containsKey(event.id())) {
            throw new PrestataireException("Ce prestataire n'est pas assigné à cet evenement !");
        }
        this.events.remove(event.id(), event);
    }

    /**
     * Délie le prestataire de l'évènement passé en paramètre.
     * @param evenement
     * @throws PrestataireException si l'évènement n'est pas lié au prestataire ou qu'il n'éxiste pas
     */
    void supprimeDansUnEvent(Evenement evenement) throws PrestataireException {
        Objects.requireNonNull(evenement);

        if (!this.events.containsKey(evenement.id())) {
            throw new PrestataireException("Ce prestataire n'est pas lié à cette événement !");
        }

        if (!this.genEvent.isEventRegistered(evenement)) {
            throw new PrestataireException("Cette événement n'a pas été enregistré !");
        }

        this.events.remove(evenement.id());
    }

    /**
     * convertie le nom en minuscule
     * @return le nom en minuscules.
     */
    @Override
    public String id() {
        return nom.toLowerCase();
    }

    public String getNom() {
        return nom;
    }

    public String getMail() {
        return mail;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getInformation() {
        return information;
    }

    public PrestataireType getType() {
        return type;
    }

    public boolean isSponsorise() {
        return sponsorise;
    }

    void setNom(String nom) throws PrestataireException {
        if (nom == null) {
            throw new PrestataireException("Le nom du prestataire doit être défini !");
        }

        this.nom = nom;
    }

    public void setMail(String mail) throws PrestataireException {
        if (!EmailValidator.getInstance().isValid(mail)) {
            throw new PrestataireException("L'email '" + mail + "' n'est pas valide.");
        }

        this.mail = mail;
    }

    public void setType(PrestataireType type) throws PrestataireException {
        if (type == null) {
            throw new PrestataireException("Le type de prestataire doit être défini !");
        }

        this.type = type;
    }

    public void setTelephone(String telephone) {
        if (RegexPhoneValidator.validerNumero(telephone)){
            this.telephone = telephone;
        }
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setSponsorise(boolean sponsorise) {
        this.sponsorise = sponsorise;
    }

    public Map<String, Evenement> getEvents() {
        return Collections.unmodifiableMap(events);
    }
}
