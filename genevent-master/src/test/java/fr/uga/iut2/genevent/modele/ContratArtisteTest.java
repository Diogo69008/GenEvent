package fr.uga.iut2.genevent.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ContratArtisteTest {
    private GenEvent genEvent;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
    }

    @Test
    void getArtiste() {
        Artiste artiste = new Artiste("tezcat", "diogo", "fortes", 20, "0629401114", genEvent);
        ContratArtiste contratArtiste = new ContratArtiste(artiste, (FileInfo) null, 0);
        assertEquals(artiste, contratArtiste.getArtiste());
    }

    @Test
    void getContrat(@TempDir Path tempDir) throws java.io.IOException {
        Path tempFile = tempDir.resolve("contrat.pdf");
        java.nio.file.Files.writeString(tempFile, "dummy content");
        File contratFile = tempFile.toFile();
        ContratArtiste contratArtiste = new ContratArtiste(new Artiste("tezcat", "diogo", "fortes", 20, "0629401114", genEvent), contratFile, 0);
        assertNotNull(contratArtiste.getContrat());
        assertEquals(contratFile.getName(), contratArtiste.getContrat().getName());
        assertArrayEquals(java.nio.file.Files.readAllBytes(tempFile), contratArtiste.getContrat().getData());
    }

    @Test
    void getCout() {
        int cout = 1000;
        ContratArtiste contratArtiste = new ContratArtiste(new Artiste("tezcat", "diogo", "fortes", 20, "0629401114", genEvent), (FileInfo) null, cout);
        assertEquals(cout, contratArtiste.getCout());
    }

    @Test
    void setContrat(@TempDir Path tempDir) throws java.io.IOException {
        Path tempFile = tempDir.resolve("new_contrat.pdf");
        java.nio.file.Files.writeString(tempFile, "dummy content");
        File newContratFile = tempFile.toFile();
        ContratArtiste contratArtiste = new ContratArtiste(new Artiste("tezcat", "diogo", "fortes", 20, "0629401114", genEvent), (FileInfo) null, 0);
        contratArtiste.setContrat(newContratFile);
        assertNotNull(contratArtiste.getContrat());
        assertEquals(newContratFile.getName(), contratArtiste.getContrat().getName());
        assertArrayEquals(java.nio.file.Files.readAllBytes(tempFile), contratArtiste.getContrat().getData());
    }

    @Test
    void id() {
        Artiste artiste = new Artiste("tezcat", "diogo", "fortes", 20, "0629401114", genEvent);
        ContratArtiste contratArtiste = new ContratArtiste(artiste, (FileInfo) null, 0);
        assertEquals(artiste.id(), contratArtiste.id());
    }
}