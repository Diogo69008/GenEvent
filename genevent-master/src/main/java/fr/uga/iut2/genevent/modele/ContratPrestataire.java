package fr.uga.iut2.genevent.modele;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class ContratPrestataire implements Identifiable, Serializable {
    private final Prestataire prestataire;
    private FileInfo contrat;
    private int cout;

    public ContratPrestataire(Prestataire prestataire, File contrat, int cout) {
        this(prestataire, new FileInfo(contrat), cout);
    }

    public ContratPrestataire(Prestataire prestataire, FileInfo contrat, int cout) {
        this.prestataire = prestataire;
        this.contrat = contrat;
        this.cout = cout;
    }

    public Prestataire getPrestataire() {
        return prestataire;
    }

    public FileInfo getContrat() {
        return contrat;
    }

    public int getCout() {
        return cout;
    }

    public void setContrat(File contrat) {
        this.contrat = new FileInfo(contrat);
    }

    public void setContrat(FileInfo contrat) {
        this.contrat = contrat;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }

    /**
     *
     * @return l'id de du préstataire.
     */
    @Override
    public String id() {
        return prestataire.id();
    }
}
