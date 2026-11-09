package fr.uga.iut2.genevent.modele;

import fr.uga.iut2.genevent.exceptions.PrestataireException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrestataireTest {

    private GenEvent genEvent;
    private Terrain terrain;
    private Prestataire prestataire;
    private Evenement evenement;

    @BeforeEach
    void setUp() {
        genEvent = new GenEvent();
        terrain = new Terrain("Place DG", "Place du Doyen de Gauss, 38000 Grenoble", 12.0d,
                "place juste devant l'IUT2", genEvent);
        prestataire = new Prestataire("Jean Dupont", "jean.dupont@example.com", "0600000000",
                "Traiteur évènementiel", PrestataireType.AUTRE, genEvent);
        evenement = new Evenement("Nouvel An", LocalDate.of(2026, 12, 31),
                LocalDate.of(2027, 1, 1), 10000, terrain, genEvent);
        genEvent.getEvenements().put(evenement.id(), evenement);
        genEvent.getPrestataires().put(prestataire.id(), prestataire);
    }


    @Test
    void ajoutDansUnEvent() {
        prestataire.ajoutDansUnEvent(evenement);
        assertTrue(prestataire.getEvents().containsKey(evenement.id()));
        PrestataireException exception = assertThrows(PrestataireException.class, () -> prestataire.ajoutDansUnEvent(evenement));
        assertEquals("Ce prestataire est déjà ajouté pour cette événement !", exception.getMessage());
    }

    @Test
    void retirerEvent() {
        prestataire.ajoutDansUnEvent(evenement);
        prestataire.retirerEvent(evenement);
        assertFalse(prestataire.getEvents().containsKey(evenement.id()));
        PrestataireException exception = assertThrows(PrestataireException.class, () -> prestataire.retirerEvent(evenement));
        assertEquals("Ce prestataire n'est pas assigné à cet evenement !", exception.getMessage());
    }

    @Test
    void supprimeDansUnEvent() {
        prestataire.ajoutDansUnEvent(evenement);
        evenement.getPrestataires().put(prestataire.id(), new ContratPrestataire(prestataire, (FileInfo) null, 100));
        evenement.supprimePrestataire(prestataire.id());
        assertFalse(prestataire.getEvents().containsKey(evenement.id()));
    }

    @Test
    void id() {
        assertEquals("jean dupont", prestataire.id());
    }

    @Test
    void getNom() {
        assertEquals("Jean Dupont", prestataire.getNom());
    }

    @Test
    void getMail() {
        assertEquals("jean.dupont@example.com", prestataire.getMail());
    }

    @Test
    void getTelephone() {
        assertEquals("0600000000", prestataire.getTelephone());
    }

    @Test
    void getInformation() {
        assertEquals("Traiteur évènementiel", prestataire.getInformation());
    }

    @Test
    void getType() {
        assertEquals(PrestataireType.AUTRE, prestataire.getType());
    }

    @Test
    void setNom() {
        assertThrows(PrestataireException.class, () -> prestataire.setNom(null));
    }

    @Test
    void setMail() {
        assertThrows(PrestataireException.class, () -> prestataire.setMail("mail-invalide"));
        assertThrows(PrestataireException.class, () -> prestataire.setMail(""));
        assertDoesNotThrow(() -> prestataire.setMail("nouveau.mail@test.fr"));
        assertEquals("nouveau.mail@test.fr", prestataire.getMail());
    }

    @Test
    void setType() {
        assertThrows(PrestataireException.class, () -> prestataire.setType(null));
    }

    @Test
    void getEvents() {
    }
}