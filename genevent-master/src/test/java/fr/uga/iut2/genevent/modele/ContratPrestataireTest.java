package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ContratPrestataireTest {

    private GenEvent genEvent;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
    }

    @Test
    void getPrestataire() {
        Prestataire prestataire = new Prestataire("Nom", "mail@fede.com", "0600000000", "Info", PrestataireType.AUTRE, genEvent);
        ContratPrestataire contratPrestataire = new ContratPrestataire(prestataire, (FileInfo) null, 0);
        assertEquals(prestataire, contratPrestataire.getPrestataire());
    }

    @Test
    void getContrat(@TempDir Path tempDir) throws java.io.IOException {
        Path tempFile = tempDir.resolve("contrat.pdf");
        java.nio.file.Files.writeString(tempFile, "dummy content");
        File contratFile = tempFile.toFile();
        ContratPrestataire contratPrestataire = new ContratPrestataire(new Prestataire("Nom", "mail@fede.com", "0600000000", "Info", PrestataireType.AUTRE, genEvent), contratFile, 0);
        assertNotNull(contratPrestataire.getContrat());
        assertEquals(contratFile.getName(), contratPrestataire.getContrat().getName());
        assertArrayEquals(java.nio.file.Files.readAllBytes(tempFile), contratPrestataire.getContrat().getData());
    }

    @Test
    void getCout() {
        int cout = 1000;
        ContratPrestataire contratPrestataire = new ContratPrestataire(new Prestataire("Nom", "mail@fede.com", "0600000000", "Info", PrestataireType.AUTRE, genEvent), (FileInfo) null, cout);
        assertEquals(cout, contratPrestataire.getCout());
    }

    @Test
    void setContrat(@TempDir Path tempDir) throws java.io.IOException {
        Path tempFile = tempDir.resolve("new_contrat.pdf");
        java.nio.file.Files.writeString(tempFile, "dummy content");
        File newContratFile = tempFile.toFile();
        ContratPrestataire contratPrestataire = new ContratPrestataire(new Prestataire("Nom", "mail@fede.com", "0600000000", "Info", PrestataireType.AUTRE, genEvent), (FileInfo) null, 0);
        contratPrestataire.setContrat(newContratFile);
        assertNotNull(contratPrestataire.getContrat());
        assertEquals(newContratFile.getName(), contratPrestataire.getContrat().getName());
        assertArrayEquals(java.nio.file.Files.readAllBytes(tempFile), contratPrestataire.getContrat().getData());
    }
}