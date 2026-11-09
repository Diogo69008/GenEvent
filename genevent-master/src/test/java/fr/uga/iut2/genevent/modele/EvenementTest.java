package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.EvenementException;
import fr.uga.iut2.genevent.exceptions.PrestataireException;
import fr.uga.iut2.genevent.exceptions.ArtisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EvenementTest {

    private GenEvent genEvent;
    private Terrain terrain;
    private Evenement evenement;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
        terrain = new Terrain("Terrain1", "Adresse1", 2.0, "Desc", genEvent);
        genEvent.getTerrains().put(terrain.id(), terrain);
        
        evenement = new Evenement("Ev1", LocalDate.of(2026, 6, 17), LocalDate.of(2026, 6, 18), 10000, terrain, genEvent);
        genEvent.getEvenements().put(evenement.id(), evenement);
    }

    @Test
    void testConstructorDatesInvalides() {
        assertThrows(EvenementException.class, () -> {
            new Evenement("Nom", LocalDate.of(2026, 6, 18), LocalDate.of(2026, 6, 17), 10000, null, genEvent);
        });
    }

    @Test
    void getNom() {
        assertEquals("Ev1", evenement.getNom());
    }

    @Test
    void getDateDebut() {
        assertEquals(LocalDate.of(2026, 6, 17), evenement.getDateDebut());
    }

    @Test
    void getDateFin() {
        assertEquals(LocalDate.of(2026, 6, 18), evenement.getDateFin());
    }

    @Test
    void getBudget() {
        assertEquals(10000, evenement.getBudget());
    }

    @Test
    void setDateDebut() {
        evenement.setDateDebut(LocalDate.of(2026, 6, 16));
        assertEquals(LocalDate.of(2026, 6, 16), evenement.getDateDebut());
    }

    @Test
    void setDateDebutException() {
        assertThrows(EvenementException.class, () -> {
            evenement.setDateDebut(LocalDate.of(2026, 6, 19));
        });
    }

    @Test
    void setDateFin() {
        evenement.setDateFin(LocalDate.of(2026, 6, 20));
        assertEquals(LocalDate.of(2026, 6, 20), evenement.getDateFin());
    }

    @Test
    void setDateFinException() {
        assertThrows(EvenementException.class, () -> {
            evenement.setDateFin(LocalDate.of(2026, 6, 16));
        });
    }

    @Test
    void setBudget() {
        evenement.setBudget(20000);
        assertEquals(20000, evenement.getBudget());
    }

    @Test
    void setBudgetException() {
        assertThrows(EvenementException.class, () -> {
            evenement.setBudget(null);
        });
    }

    @Test
    void getTerrain() {
        assertEquals(terrain, evenement.getTerrain());
    }

    @Test
    void setTerrain() {
        Terrain terrain2 = new Terrain("Terrain2", "Adresse2", 2.0, "Desc", genEvent);
        evenement.setTerrain(terrain2);
        assertEquals(terrain2, evenement.getTerrain());
    }

    @Test
    void setTerrainOverlapException() {
        Evenement evenement2 = new Evenement("Ev2", LocalDate.of(2026, 6, 17), LocalDate.of(2026, 6, 18), 10000, null, genEvent);
        assertThrows(EvenementException.class, () -> {
            evenement2.setTerrain(terrain);
        });
    }

    @Test
    void updatePrixBillet() {
        assertEquals(25, evenement.getBillet());
        evenement.setBudget(1000000);
        assertEquals(32, evenement.getBillet());
    }

    @Test
    void getBillet() {
        assertEquals(25, evenement.getBillet());
    }

    @Test
    void getArtiste() {
        assertTrue(evenement.getArtiste().isEmpty());
    }

    @Test
    void getPrestataires() {
        assertTrue(evenement.getPrestataires().isEmpty());
    }

    @Test
    void ajoutPrestataire() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
        
        evenement.ajoutPrestataire(prestataire, null, 500);
        
        assertTrue(evenement.getPrestataires().containsKey(prestataire.id()));
    }

    @Test
    void ajoutPrestataireExceptionDuplicate() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
        
        evenement.ajoutPrestataire(prestataire, null, 500);
        
        assertThrows(EvenementException.class, () -> {
            evenement.ajoutPrestataire(prestataire, null, 500);
        });
    }

    @Test
    void ajoutPrestataireExceptionNonEnregistre() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        
        assertThrows(EvenementException.class, () -> {
            evenement.ajoutPrestataire(prestataire, null, 500);
        });
    }

    @Test
    void supprimePrestataire() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
        evenement.ajoutPrestataire(prestataire, null, 500);
        
        evenement.supprimePrestataire(prestataire);
        
        assertFalse(evenement.getPrestataires().containsKey(prestataire.id()));
    }

    @Test
    void testSupprimePrestataire() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
        evenement.ajoutPrestataire(prestataire, null, 500);
        
        evenement.supprimePrestataire(prestataire.id());
        
        assertFalse(evenement.getPrestataires().containsKey(prestataire.id()));
    }

    @Test
    void supprimePrestataireExceptionNonLie() {
        Prestataire prestataire = new Prestataire("Pres1", "pres1@test.com", "0102030405", "info", PrestataireType.RESTAURATION, genEvent);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
        
        assertThrows(EvenementException.class, () -> {
            evenement.supprimePrestataire(prestataire);
        });
    }

    @Test
    void supprimePrestataireByIdExceptionNonLie() {
        assertThrows(EvenementException.class, () -> {
            evenement.supprimePrestataire("inconnu");
        });
    }

    @Test
    void ajoutArtiste() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        genEvent.getArtistes().put(artiste.id(), artiste);
        
        evenement.ajoutArtiste(artiste, 1000, null);
        
        assertTrue(evenement.getArtiste().containsKey(artiste.id()));
    }

    @Test
    void ajoutArtisteExceptionDuplicate() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        genEvent.getArtistes().put(artiste.id(), artiste);
        
        evenement.ajoutArtiste(artiste, 1000, null);
        
        assertThrows(EvenementException.class, () -> {
            evenement.ajoutArtiste(artiste, 1000, null);
        });
    }

    @Test
    void ajoutArtisteExceptionNonEnregistre() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        
        assertThrows(EvenementException.class, () -> {
            evenement.ajoutArtiste(artiste, 1000, null);
        });
    }

    @Test
    void supprimeArtiste() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        genEvent.getArtistes().put(artiste.id(), artiste);
        evenement.ajoutArtiste(artiste, 1000, null);
        
        evenement.supprimeArtiste(artiste);
        
        assertFalse(evenement.getArtiste().containsKey(artiste.id()));
    }

    @Test
    void supprimeArtisteExceptionNonLie() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        genEvent.getArtistes().put(artiste.id(), artiste);
        
        assertThrows(EvenementException.class, () -> {
            evenement.supprimeArtiste(artiste);
        });
    }

    @Test
    void supprimeArtisteFromContrat() {
        Artiste artiste = new Artiste("NomScene", "Nom", "Prenom", 25, "0605040302", genEvent);
        genEvent.getArtistes().put(artiste.id(), artiste);
        evenement.ajoutArtiste(artiste, 1000, null);
        
        ContratArtiste contrat = evenement.getArtiste().get(artiste.id());
        assertNotNull(contrat);
        
        evenement.supprimeArtisteFromContrat(contrat);
        
        assertFalse(evenement.getArtiste().containsKey(artiste.id()));
    }

    @Test
    void calculerBenefices() {
        double prixPlace = 25.0;
        double budget = 20000.0;
        double benefice = evenement.calculerBenefices(prixPlace, budget);
        System.out.println(benefice);
        assertTrue(benefice > 0, "Le modèle grand budget devrait rester rentable.");
        System.out.println("Bénéfice simulé pour 20 000€ : " + benefice + " €");
    }
}