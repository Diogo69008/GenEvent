package fr.uga.iut2.genevent.modele;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class ContratArtiste implements Identifiable, Serializable {
    private final Artiste artiste;
    private FileInfo contrat;
    private int cout;

    public ContratArtiste(Artiste artiste, File contrat, int cout) {
        this(artiste, new FileInfo(contrat), cout);
    }

    public ContratArtiste(Artiste artiste, FileInfo contrat, int cout) {
        this.artiste = artiste;
        this.contrat = contrat;
        this.cout = cout;
    }

    public Artiste getArtiste() {
        return artiste;
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
     * @return l'id de l'artiste.
     */
    @Override
    public String id() {
        return artiste.id();
    }
}
