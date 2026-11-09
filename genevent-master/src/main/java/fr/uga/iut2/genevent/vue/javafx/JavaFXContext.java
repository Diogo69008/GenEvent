package fr.uga.iut2.genevent.vue.javafx;

import fr.uga.iut2.genevent.controleur.Controleur;

import java.util.concurrent.CountDownLatch;

/**
 * @author theo
 * @project genevent
 * @date 14/06/2026 17:13
 *
 * Context de l'application donné à chaque controller
 */
public record JavaFXContext(Controleur controleur, CountDownLatch eolBarrier) {
    public void exitAction() {
        // fermeture de l'interface JavaFX : on notifie sa fin de vie
        eolBarrier.countDown();
    }
}
