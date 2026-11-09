package fr.uga.iut2.genevent.controleur;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;


public class Controleur {

    private final GenEvent genevent;
    private final IHM ihm;

    public Controleur(GenEvent genevent) {
        this.genevent = genevent;

//        this.ihm = new CLI(this); (not implemented)
        this.ihm = new JavaFXGUI(this);
    }

    public IHM getIhm() {
        return ihm;
    }

    public void demarrer() {
        this.ihm.demarrerInteraction();
    }

    public GenEvent getGenevent() {
        return genevent;
    }

    public void saisirPrestaire() {
        this.ihm.saisirPrestaire();
    }

    public void saisirArtiste() {
        this.ihm.saisirArtiste();
    }

    public void saisirTerrain() {
        this.ihm.saisirTerrain();
    }

    public void afficherLieu(Terrain terrain) {
        this.ihm.afficherLieu(terrain);
    }

    public void afficherEvent(Evenement evenement) {
        this.ihm.afficherEvent(evenement);
    }

    public void afficherPresta(Prestataire prestataire) {
        this.ihm.afficherPresta(prestataire);
    }

    public void afficherContratPresta(ContratPrestataire contratPrestataire) {
        this.ihm.afficherContratPresta(contratPrestataire);
    }

    public void afficherContratArtiste(ContratArtiste contratArtiste) {
        this.ihm.afficherContratArtiste(contratArtiste);
    }

    public void afficherArtiste(Artiste artiste){
        this.ihm.afficherArtiste(artiste);
    }

    public void saisirEvenement() {
        this.ihm.saisirNouvelEvenement(this.genevent.getEvenements().keySet());
    }

    public boolean creerPrestataire(IHM.InfosNouvauPrestataire infos) {
        try {
            this.genevent.nouveauPrestataire(
                    infos.nom,
                    infos.mail,
                    infos.telephone,
                    infos.information,
                    infos.type
            );

            this.ihm.informerUtilisateur(
                    "Nouveau prestataire : " + infos.nom + " " + infos.telephone + " contact : " + infos.mail + ">",
                    true
            );
            this.ihm.requestRefresh();
            return true;
        } catch (Exception e) {
            this.ihm.informerUtilisateur(
                    e.getMessage(),
                    false
            );
            return false;
        }
    }

    public boolean creerTerrain(IHM.InfosNouveauTerrain infos) {
        try {
            this.genevent.nouveauTerrain(
                    infos.nom,
                    infos.adresse,
                    infos.surface,
                    infos.description
            );

            this.ihm.informerUtilisateur(
                    "Nouveau terrain : '" + infos.nom + "' à " + infos.adresse + " " + infos.surface + "hectare " + " " + infos.description,
                    true
            );
            this.ihm.requestRefresh();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.ihm.informerUtilisateur(
                    e.getMessage(),
                    false
            );
            return false;
        }
    }

    public boolean creerArtiste(IHM.InfosArtiste infos) {
        try {
                    this.genevent.nouveauArtiste(
                    infos.nomDeScene,
                    infos.nom,
                    infos.prenom,
                    infos.age,
                    infos.telephone, infos.informations
            );
            this.ihm.informerUtilisateur(
                    "Nouvel artiste : " + infos.nomDeScene + " " + infos.nom + " " + infos.prenom + " " + infos.age + "ans " + infos.telephone,
                    true
            );
            this.ihm.requestRefresh();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            this.ihm.informerUtilisateur(
                    e.getMessage(),
                    false
            );
            return false;
        }
    }

    public boolean creerEvenement(IHM.InfosNouvelEvenement infos) {
        try {
            this.genevent.nouvelEvenement(
                    infos.nom,
                    infos.dateDebut,
                    infos.dateFin,
                    infos.terrain,
                    infos.budget
            );

            this.ihm.informerUtilisateur(
                    "Nouvel événement : " + infos.nom + " de " + infos.dateDebut + " à " + infos.dateFin + " " + infos.budget + "€",
                    true
            );

            this.ihm.requestRefresh();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            this.ihm.informerUtilisateur(
                    e.getMessage(),
                    false
            );
            return false;
        }
    }

    public boolean supprimerTerrain(Terrain terrain){
        try{
            this.genevent.supprimerTerrain(terrain);
            this.ihm.requestRefresh();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerPrestataire(Prestataire presta){
        try{
            this.genevent.supprimerPrestataire(presta);
            this.ihm.requestRefresh();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerArtiste(Artiste artiste){
        try{
            this.genevent.supprimerArtiste(artiste);
            this.ihm.requestRefresh();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerEvent(Evenement event){
        try{
            this.genevent.supprimerEvenement(event);
            this.ihm.requestRefresh();
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
