package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.TerrainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TerrainTest {

    private GenEvent genEvent;
    private Terrain terrain;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
        terrain = new Terrain("Stade des Alpes", "1 Avenue de Valmy, 38000 Grenoble", 2.5, "Grand stade municipal", genEvent);
    }

    @Test
    void testConstructorNomNull() {
        assertThrows(TerrainException.class, () -> {
            new Terrain(null, "Adresse", 2.0, "Description", genEvent);
        });
    }

    @Test
    void testConstructorAdresseNull() {
        assertThrows(TerrainException.class, () -> {
            new Terrain("Nom", null, 2.0, "Description", genEvent);
        });
    }

    @Test
    void testConstructorSurfaceTropPetite() {
        assertThrows(TerrainException.class, () -> {
            new Terrain("Nom", "Adresse", 0, "Description", genEvent);
        });
    }

    @Test
    void testConstructorGenEventNull() {
        assertThrows(NullPointerException.class, () -> {
            new Terrain("Nom", "Adresse", 2.0, "Description", null);
        });
    }

    @Test
    void getEvenements() {
        assertTrue(terrain.getEvenements().isEmpty());
        
        Evenement event1 = new Evenement("Concert1", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        Evenement event2 = new Evenement("Concert2", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        
        terrain.ajoutDansUnEvent(event1);
        terrain.ajoutDansUnEvent(event2);
        
        List<Evenement> list = terrain.getEvenements();
        assertEquals(2, list.size());
        assertTrue(list.contains(event1));
        assertTrue(list.contains(event2));
    }

    @Test
    void ajoutDansUnEvent() {
        Evenement event = new Evenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        
        terrain.ajoutDansUnEvent(event);
        
        List<Evenement> evenements = terrain.getEvenements();
        assertEquals(1, evenements.size());
        assertTrue(evenements.contains(event));
    }

    @Test
    void ajoutDansUnEventExceptionDouble() {
        Evenement event = new Evenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        
        terrain.ajoutDansUnEvent(event);
        
        assertThrows(TerrainException.class, () -> {
            terrain.ajoutDansUnEvent(event);
        });
    }

    @Test
    void ajoutDansUnEventNullException() {
        assertThrows(NullPointerException.class, () -> {
            terrain.ajoutDansUnEvent(null);
        });
    }

    @Test
    void retirerEvent() {
        Evenement event = new Evenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        
        terrain.ajoutDansUnEvent(event);
        assertEquals(1, terrain.getEvenements().size());
        
        terrain.retirerEvent(event);
        assertEquals(0, terrain.getEvenements().size());
    }

    @Test
    void retirerEventNonAssigneException() {
        Evenement event = new Evenement("Concert", LocalDate.now(), LocalDate.now().plusDays(1), 10000, null, genEvent);
        
        assertThrows(TerrainException.class, () -> {
            terrain.retirerEvent(event);
        });
    }

    @Test
    void retirerEventNullException() {
        assertThrows(NullPointerException.class, () -> {
            terrain.retirerEvent(null);
        });
    }

    @Test
    void id() {
        assertEquals("stade des alpes", terrain.id());
    }

    @Test
    void getNom() {
        assertEquals("Stade des Alpes", terrain.getNom());
    }

    @Test
    void getDescription() {
        assertEquals("Grand stade municipal", terrain.getDescription());
    }

    @Test
    void getAdresse() {
        assertEquals("1 Avenue de Valmy, 38000 Grenoble", terrain.getAdresse());
    }

    @Test
    void getSurface() {
        assertEquals(25000.0, terrain.getSurface());
    }

    @Test
    void getCapacite() {
        assertEquals(50000, terrain.getCapacite());
    }

    @Test
    void setNom() {
        terrain.setNom("Nouveau Nom");
        assertEquals("Nouveau Nom", terrain.getNom());
        assertEquals("nouveau nom", terrain.id());
    }

    @Test
    void setNomException() {
        assertThrows(TerrainException.class, () -> {
            terrain.setNom(null);
        });
    }

    @Test
    void setAdresse() {
        terrain.setAdresse("Nouvelle Adresse");
        assertEquals("Nouvelle Adresse", terrain.getAdresse());
    }

    @Test
    void setAdresseException() {
        assertThrows(TerrainException.class, () -> {
            terrain.setAdresse(null);
        });
    }

    @Test
    void setSurface() {
        terrain.setSurface(3.0);
        assertEquals(30000.0, terrain.getSurface());
        assertEquals(60000, terrain.getCapacite());
    }

    @Test
    void setSurfaceException() {
        assertThrows(TerrainException.class, () -> {
            terrain.setSurface(0);
        });
    }
}