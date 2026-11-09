package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.ArtisteException;
import fr.uga.iut2.genevent.exceptions.PrestataireException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GenEventTest {

    private GenEvent genEvent;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
    }

    @Test
    void getEvenements() {
        assertTrue(genEvent.getEvenements().isEmpty(), "La liste devrait être vide au départ");
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        Terrain terrain = genEvent.getTerrains().get("stade");
        genEvent.nouvelEvenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), terrain, 5000);
        assertEquals(1, genEvent.getEvenements().size());
        assertTrue(genEvent.getEvenements().containsKey("concert"));
    }

    @Test
    void getTerrains() {
        assertTrue(genEvent.getTerrains().isEmpty(), "La liste devrait être vide au départ");
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        assertEquals(1, genEvent.getTerrains().size());
        assertTrue(genEvent.getTerrains().containsKey("stade"));
    }

    @Test
    void getPrestataires() {
        assertTrue(genEvent.getPrestataires().isEmpty(), "La liste devrait être vide au départ");
        genEvent.nouveauPrestataire("Traiteur", "contact@test.fr", "0400000000", "Info", PrestataireType.AUTRE);
        assertEquals(1, genEvent.getPrestataires().size());
        assertTrue(genEvent.getPrestataires().containsKey("traiteur"));
    }

    @Test
    void getArtistes() {
        assertTrue(genEvent.getArtistes().isEmpty(), "La liste devrait être vide au départ");
        genEvent.nouveauArtiste("DJ Snake", "William", "Grigahcine", 35, "0600000000");
        assertEquals(1, genEvent.getArtistes().size());
        assertTrue(genEvent.getArtistes().containsKey("william"));
    }

    @Test
    void isPrestataireRegistered() {
        Prestataire pNonEnregistre = new Prestataire("Inconnu", "inconnu@test.fr", "0600000000", "Info", PrestataireType.AUTRE, genEvent);
        assertFalse(genEvent.isPrestataireRegistered(pNonEnregistre), "Le prestataire ne devrait pas être reconnu");
        genEvent.nouveauPrestataire("Traiteur", "contact@test.fr", "0400000000", "Info", PrestataireType.AUTRE);
        Prestataire pEnregistre = genEvent.getPrestataires().get("traiteur");
        assertTrue(genEvent.isPrestataireRegistered(pEnregistre), "Le prestataire devrait être reconnu");
    }

    @Test
    void isArtisteRegistered() {
        Artiste aNonEnregistre = new Artiste("Inconnu", "Nom", "Prenom", 20, "0600000000", genEvent);
        assertFalse(genEvent.isArtisteRegistered(aNonEnregistre), "L'artiste ne devrait pas être reconnu");
        genEvent.nouveauArtiste("DJ Snake", "William", "Grigahcine", 35, "0600000000");
        Artiste aEnregistre = genEvent.getArtistes().get("william");
        assertTrue(genEvent.isArtisteRegistered(aEnregistre), "L'artiste devrait être reconnu");
    }

    @Test
    void isEventRegistered() {
        Terrain t = new Terrain("Stade", "Grenoble", 2.0, "Description", genEvent);
        Evenement eNonEnregistre = new Evenement("Fantome", LocalDate.now(), LocalDate.now().plusDays(1), 1000, t, genEvent);
        assertFalse(genEvent.isEventRegistered(eNonEnregistre), "L'événement ne devrait pas être reconnu");
        genEvent.nouveauTerrain("Parc", "Grenoble", 2.0, "Description");
        Terrain terrainOfficiel = genEvent.getTerrains().get("parc");
        genEvent.nouvelEvenement("Festival", LocalDate.now(), LocalDate.now().plusDays(1), terrainOfficiel, 5000);
        Evenement eEnregistre = genEvent.getEvenements().get("festival");
        assertTrue(genEvent.isEventRegistered(eEnregistre), "L'événement devrait être reconnu");
    }

    @Test
    void nouveauPrestataire() {
        genEvent.nouveauPrestataire("Traiteur Pro", "contact@traiteur.fr", "0476000000", "Info", PrestataireType.AUTRE);
        assertTrue(genEvent.getPrestataires().containsKey("traiteur pro"));
        assertThrows(PrestataireException.class, () ->
                genEvent.nouveauPrestataire("Traiteur Pro", "autre@mail.fr", "0476111111", "Info", PrestataireType.AUTRE)
        );
    }

    @Test
    void nouveauArtiste() {
        genEvent.nouveauArtiste("Stromae", "Van Haver", "Paul", 41, "0612345678");
        assertTrue(genEvent.getArtistes().containsKey("van haver"));
        Artiste artiste = genEvent.getArtistes().get("van haver");
        assertNotNull(artiste);
        assertEquals("van haver", artiste.id());
        ArtisteException exception = assertThrows(ArtisteException.class, () ->
                genEvent.nouveauArtiste("Stromae2", "Van Haver", "Paule", 25, "0600000000")
        );
        assertEquals("Un artiste avec ce nom existe déjà !", exception.getMessage());
    }

    @Test
    void nouveauTerrain() {
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        assertTrue(genEvent.getTerrains().containsKey("stade"));
    }

    @Test
    void nouvelEvenement() {
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        Terrain terrain = genEvent.getTerrains().get("stade");

        genEvent.nouvelEvenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), terrain, 5000);
        assertTrue(genEvent.getEvenements().containsKey("concert"));
    }

    @Test
    void supprimerTerrain() {
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        Terrain t = genEvent.getTerrains().get("stade");
        assertTrue(genEvent.getTerrains().containsKey("stade")); // Vérif de pré-condition
        genEvent.supprimerTerrain(t);
        assertFalse(genEvent.getTerrains().containsKey("stade"), "Le terrain devrait être supprimé");
    }

    @Test
    void supprimerEvenement() {
        genEvent.nouveauTerrain("Stade", "Grenoble", 2.0, "Description");
        Terrain terrain = genEvent.getTerrains().get("stade");
        genEvent.nouvelEvenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), terrain, 5000);
        Evenement event = genEvent.getEvenements().get("concert");

        genEvent.supprimerEvenement(event);
        assertFalse(genEvent.getEvenements().containsKey("concert"));
        // Vérifie que le terrain n'est plus lié à cet événement
        assertFalse(terrain.getEvenements().contains(event));
    }

    @Test
    void supprimerPrestataire() {
        genEvent.nouveauPrestataire("Traiteur", "contact@test.fr", "0400000000", "Info", PrestataireType.AUTRE);
        Prestataire p = genEvent.getPrestataires().get("traiteur");

        assertTrue(genEvent.getPrestataires().containsKey("traiteur")); // Vérif de pré-condition

        genEvent.supprimerPrestataire(p);
        assertFalse(genEvent.getPrestataires().containsKey("traiteur"), "Le prestataire devrait être supprimé");
    }

    @Test
    void supprimerArtiste() {
        genEvent.nouveauArtiste("DJ Snake", "William", "Grigahcine", 35, "0600000000");
        Artiste a = genEvent.getArtistes().get("william");
        assertTrue(genEvent.getArtistes().containsKey("william")); // Vérif de pré-condition
        genEvent.supprimerArtiste(a);
        assertFalse(genEvent.getArtistes().containsKey("william"), "L'artiste devrait être supprimé");
    }
}