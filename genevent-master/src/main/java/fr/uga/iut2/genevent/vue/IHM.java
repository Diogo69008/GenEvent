package fr.uga.iut2.genevent.vue;

import fr.uga.iut2.genevent.modele.*;

import java.time.LocalDate;
import java.util.Set;


public abstract class IHM {
    public static class InfosArtiste {
        public final String nomDeScene;
        public final String nom;
        public final String prenom;
        public final Integer age;
        public final String telephone;
        public String informations;


        public InfosArtiste(final String nomDeScene, final String nom, final String prenom, final Integer age, final String telephone, final String informations) {
            this.nomDeScene = nomDeScene;
            this.nom = nom;
            this.prenom = prenom;
            this.age = age;
            this.telephone =telephone;
            this.informations = informations;
        }
    }

    /**
     * Classe conteneur pour les informations saisies pour un nouvel
     * {@link fr.uga.iut2.genevent.modele.Evenement}.
     *
     * <ul>
     * <li>Tous les attributs sont `public` par commodité d'accès.</li>
     * <li>Tous les attributs sont `final` pour ne pas être modifiables.</li>
     * </ul>
     */
    public static class InfosNouvelEvenement {
        public final String nom;
        public final LocalDate dateDebut;
        public final LocalDate dateFin;
        public final Integer budget;
        public final Terrain terrain;

        public InfosNouvelEvenement(final String nom, final LocalDate dateDebut, final LocalDate dateFin, final Terrain terrain, final Integer budget) {
            this.nom = nom;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.terrain = terrain;
            this.budget = budget;
        }
    }

    public static class InfosNouveauTerrain {
        public final String nom;
        public final String adresse;
        public final double surface;
        public final String description;

        public InfosNouveauTerrain(final String nom, final String adresse, final double surface, final String description) {
            this.nom = nom;
            this.adresse = adresse;
            this.surface = surface;
            this.description = description;
        }
    }

    public static class InfosNouvauPrestataire {
        public final String nom, mail;
        public final String telephone;
        public final PrestataireType type;
        public final String information;

        public InfosNouvauPrestataire(String nom, String mail, String telephone, PrestataireType type, String information) {
            this.nom = nom;
            this.mail = mail;
            this.telephone = telephone;
            this.type = type;
            this.information = information;
        }
    }

    /**
     * Rend actif l'interface Humain-machine.
     *
     * L'appel est bloquant : le contrôle est rendu à l'appelant une fois que
     * l'IHM est fermée.
     *
     */
    public abstract void demarrerInteraction();

    /**
     * Affiche un message d'information à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     *
     * @param succes true si le message informe d'une opération réussie, false
     *     sinon.
     */
    public abstract void informerUtilisateur(final String msg, final boolean succes);

    /**
     * Fonction qui refresh la page qctuelle affiché dans le dashboard
     */
    public abstract void requestRefresh();

    public abstract void saisirPrestaire();

    public abstract void saisirArtiste();

    public abstract void afficherLieu(Terrain terrain);

    public abstract void afficherEvent(Evenement evenement);

    public abstract void saisirTerrain();

    public abstract void afficherPresta(Prestataire prestataire);

    public abstract void afficherContratPresta(ContratPrestataire contratPrestataire);

    public abstract void afficherContratArtiste(ContratArtiste contratArtiste);

    public abstract void afficherArtiste(Artiste artiste);

    public abstract void modifArtiste(Artiste artiste);

    /**
     * Récupère les informations nécessaires à la création d'un nouvel
     * {@link fr.uga.iut2.genevent.modele.Evenement}.
     *
     * @param nomsExistants L'ensemble des noms d'évenements qui ne sont plus
     *     disponibles.
     *
     */
    public abstract void saisirNouvelEvenement(final Set<String> nomsExistants);
}

