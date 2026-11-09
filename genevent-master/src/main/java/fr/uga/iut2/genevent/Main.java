package fr.uga.iut2.genevent;

import fr.uga.iut2.genevent.controleur.Controleur;
import fr.uga.iut2.genevent.modele.GenEvent;
import fr.uga.iut2.genevent.util.Persisteur;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;


public class Main {

    public static final int EXIT_ERR_LOAD = 2;
    public static final int EXIT_ERR_SAVE = 3;

    public static void main(String[] args) {
        AtomicReference<GenEvent> genevent = new AtomicReference<>(null);
        try {
            genevent.set(Persisteur.lireEtat());
            genevent.get().initialize();
        }
        catch (ClassNotFoundException | IOException ignored) {
            System.err.println("Erreur irrécupérable pendant le chargement de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_LOAD);
        }

        // Permet de tout le temps enregistré l'état, même quand l'application est fermé "bursquement"
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                Persisteur.sauverEtat(genevent.get());
            }
            catch (IOException ignored) {
                System.err.println("Erreur irrécupérable pendant la sauvegarde de l'état : fin d'exécution !");
                System.err.flush();
                System.exit(Main.EXIT_ERR_SAVE);
            }
        }));

        Controleur controleur = new Controleur(genevent.get());
        controleur.demarrer();
    }
}
