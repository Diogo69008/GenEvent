package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.ArtisteException;
import fr.uga.iut2.genevent.exceptions.EvenementException;
import fr.uga.iut2.genevent.exceptions.PrestataireException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ArtisteTest {

    private GenEvent genEvent;
    private Artiste artiste;
    private Evenement evenementValide;

    @BeforeEach
    void setUp() throws EvenementException {
        this.genEvent = new GenEvent();
        this.artiste = new Artiste("Johnny", "Hallyday", "Jean-Philippe", 74, "0612345678", genEvent);
        this.evenementValide = new Evenement(
                "Rock en Stock",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 5),
                50000,
                null,
                genEvent
        );
    }

    @Test
    void ajoutDansUnEvent() {
        assertThrows(ArtisteException.class, () -> artiste.ajoutDansUnEvent(evenementValide));

        genEvent.getEvenements().put(evenementValide.id(), evenementValide);

        assertDoesNotThrow(() -> artiste.ajoutDansUnEvent(evenementValide));
        assertTrue(artiste.getEvents().containsKey(evenementValide.id()));

        assertThrows(ArtisteException.class, () -> artiste.ajoutDansUnEvent(evenementValide));

        assertThrows(NullPointerException.class, () -> artiste.ajoutDansUnEvent(null));
    }

    @Test
    void retirerEvent() {
        genEvent.getEvenements().put(evenementValide.id(), evenementValide);
        assertDoesNotThrow(() -> artiste.ajoutDansUnEvent(evenementValide));
        assertDoesNotThrow(() -> artiste.supprimeDansUnEvent(evenementValide));

        assertFalse(artiste.getEvents().containsKey(evenementValide.id()));
    }

    @Test
    void supprimeDansUnEvent() {
        assertThrows(NullPointerException.class, () -> artiste.supprimeDansUnEvent(null));

        assertThrows(ArtisteException.class, () -> artiste.supprimeDansUnEvent(evenementValide));

        genEvent.getEvenements().put(evenementValide.id(), evenementValide);
        assertDoesNotThrow(() -> artiste.ajoutDansUnEvent(evenementValide));

        genEvent.getEvenements().remove(evenementValide.id());
        assertThrows(ArtisteException.class, () -> artiste.supprimeDansUnEvent(evenementValide));

        genEvent.getEvenements().put(evenementValide.id(), evenementValide);
        assertDoesNotThrow(() -> artiste.supprimeDansUnEvent(evenementValide));
        assertFalse(artiste.getEvents().containsKey(evenementValide.id()));
    }

    @Test
    void getAge() {
        assertEquals(74, artiste.getAge());
    }

    @Test
    void getTelephone() {
        assertEquals("0612345678", artiste.getTelephone());
    }

    @Test
    void getNomDeScene() {
        assertEquals("Johnny", artiste.getNomDeScene());
    }

    @Test
    void getPrenom() {
        assertEquals("Jean-Philippe", artiste.getPrenom());
    }

    @Test
    void getNom() {
        assertEquals("Hallyday", artiste.getNom());
    }

    @Test
    void id() {
        assertEquals("hallyday", artiste.id());
    }

    @Test
    void setAge() {
        artiste.setAge(25);
        assertEquals(25, artiste.getAge());

        assertThrows(ArtisteException.class, () -> artiste.setAge(0));
        assertThrows(ArtisteException.class, () -> artiste.setAge(-5));
    }

    @Test
    void setNomDeScene() {
        artiste.setNomDeScene("Eminem");
        assertEquals("Eminem", artiste.getNomDeScene());

        assertThrows(ArtisteException.class, () -> artiste.setNomDeScene(""));
    }

    @Test
    void setPrenom() {
        artiste.setPrenom("Marshall");
        assertEquals("Marshall", artiste.getPrenom());

        assertThrows(ArtisteException.class, () -> artiste.setPrenom(""));
    }

    @Test
    void setNom() {
        artiste.setNom("Mathers");
        assertEquals("Mathers", artiste.getNom());

        assertThrows(ArtisteException.class, () -> artiste.setNom(""));
    }

    @Test
    void setTelephone() {
        artiste.setTelephone("0712345678");
        assertEquals("0712345678", artiste.getTelephone());

        assertThrows(ArtisteException.class, () -> artiste.setTelephone("telephone_invalide"));
    }

    @Test
    void getEvents() {
        assertNotNull(artiste.getEvents());
        assertTrue(artiste.getEvents().isEmpty());

        assertThrows(UnsupportedOperationException.class, () -> artiste.getEvents().put("clef_test", evenementValide));
    }
}