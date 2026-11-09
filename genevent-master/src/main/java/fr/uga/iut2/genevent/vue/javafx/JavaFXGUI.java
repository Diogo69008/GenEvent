package fr.uga.iut2.genevent.vue.javafx;

import fr.uga.iut2.genevent.controleur.Controleur;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * La classe JavaFXGUI est responsable des interactions avec
 * l'utilisa·teur/trice en mode graphique.
 * <p>
 * Attention, pour pouvoir faire le lien avec le
 * {@link fr.uga.iut2.genevent.controleur.Controleur}, JavaFXGUI n'est pas une
 * sous-classe de {@link javafx.application.Application} !
 * <p>
 * Le démarrage de l'application diffère des exemples classiques trouvés dans
 * la documentation de JavaFX : l'interface est démarrée à l'initiative du
 * {@link fr.uga.iut2.genevent.controleur.Controleur} via l'appel de la méthode
 * {@link #demarrerInteraction()}.
 */
public class JavaFXGUI extends IHM {
    private final JavaFXContext context;
    private DashboardController dashboardController;

    public JavaFXGUI(Controleur controleur) {
        this.context = new JavaFXContext(controleur, new CountDownLatch(1));
    }

    /**
     * Point d'entrée principal pour le code de l'interface JavaFX.
     *
     * @param primaryStage stage principale de l'interface JavaFX, sur laquelle
     *     définir des scenes.
     *
     * @throws IOException si le chargement de la vue FXML échoue.
     *
     * @see javafx.application.Application#start(Stage)
     */
    private void start(Stage primaryStage) throws IOException {
        FXMLLoader mainViewLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));

        this.dashboardController = new DashboardController(context);
        mainViewLoader.setController(dashboardController);

        Scene mainScene = new Scene(mainViewLoader.load());
        mainScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("GenEvent");
        primaryStage.setScene(mainScene);
        primaryStage.setWidth(950);
        primaryStage.setHeight(800);
        primaryStage.setMinWidth(950);
        primaryStage.setMinHeight(800);
        primaryStage.show();
    }

    @Override
    public void requestRefresh() {
        if (dashboardController != null) {
            dashboardController.refreshCurrentPage();
        }
    }

    @Override
    public void demarrerInteraction() {

        Platform.startup(() -> {
            Stage primaryStage = new Stage();
            primaryStage.setOnCloseRequest((WindowEvent t) -> context.exitAction());
            try {
                this.start(primaryStage);
            }
            catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        });


        try {
            context.eolBarrier().await();
        }
        catch (InterruptedException exc) {
            System.err.println("Erreur d'exécution de l'interface.");
            System.err.flush();
        }
    }

    @Override
    public void informerUtilisateur(String msg, boolean succes) {

        VBox container = new VBox(12);
        container.setStyle("-fx-padding: 20px; -fx-background-color: #FFFFFF;");

        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        String emoji = succes ? "✅" : "⚠️";
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 24px;");

        String titleText = succes ? "Succès!" : "Attention";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-weight: bold; -fx-font-size: 14px; " +
                (succes ? "-fx-text-fill: #10B981;" : "-fx-text-fill: #F59E0B;"));

        headerBox.getChildren().addAll(emojiLabel, titleLabel);

        Label messageLabel = new Label(msg);
        messageLabel.setWrapText(true);
        messageLabel.setStyle("-fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 13px; -fx-text-fill: #374151; -fx-line-spacing: 4;");

        container.getChildren().addAll(headerBox, messageLabel);

        Alert alert = new Alert(succes ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING);
        alert.setTitle("GenEvent");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.getDialogPane().setContent(container);
        alert.getDialogPane().setStyle("-fx-padding: 0px; -fx-background-color: #FFFFFF;");


        alert.getButtonTypes().forEach(buttonType -> {
            var button = alert.getDialogPane().lookupButton(buttonType);
            if (button != null) {
                button.setStyle("-fx-padding: 8px 24px; -fx-font-weight: bold; -fx-font-size: 12px; " +
                        (succes ? "-fx-text-fill: white; -fx-background-color: #10B981; -fx-background-radius: 6px;" :
                                "-fx-text-fill: white; -fx-background-color: #F59E0B; -fx-background-radius: 6px;"));
            }
        });

        alert.initModality(Modality.APPLICATION_MODAL);

        Window owner = Window.getWindows().stream()
                .filter(Window::isShowing)
                .findFirst()
                .orElse(null);
        if (owner != null) {
            alert.initOwner(owner);
            if (owner.getScene() != null && owner.getScene().getRoot() != null) {
                owner.getScene().getRoot().setDisable(true);
            }
            alert.setOnShown(e -> Platform.runLater(() -> {
                double x = owner.getX() + (owner.getWidth() - alert.getWidth()) / 2;
                double y = owner.getY() + (owner.getHeight() - alert.getHeight()) / 2;
                alert.setX(x);
                alert.setY(y);
            }));
        }

        alert.setOnHidden(e -> Platform.runLater(() -> {
            if (owner != null && owner.getScene() != null && owner.getScene().getRoot() != null) {
                owner.getScene().getRoot().setDisable(false);
            }
        }));

        alert.showAndWait();
    }

    @Override
    public void saisirPrestaire() {
        PopupUtils.openPopup("nouveau_prestataire_popup.fxml", "Créer une prestataire", new NewPrestataireController(context));
    }

    @Override
    public void afficherPresta(Prestataire prestataire) {
        PopupUtils.openPopup("details_prestataire.fxml", "Details d'un prestataire", new DetailsPrestataireController(context, prestataire));
    }

    @Override
    public void afficherContratPresta(ContratPrestataire contratPrestataire) {
        PopupUtils.openPopup("details_contrat_presta.fxml", "Détails contrat", new DetailsContratPrestataireController(context, contratPrestataire));
    }

    @Override
    public void afficherContratArtiste(ContratArtiste contratArtiste) {
        PopupUtils.openPopup("details_contrat_artiste.fxml","Détails contrat", new DetailsContratArtisteController(context, contratArtiste));
    }

    @Override
    public void modifArtiste(Artiste artiste){
        PopupUtils.openPopup("modif_artiste.fxml","Modifier un artiste", new ModifArtisteController(context,artiste));
    }

    @Override
    public void afficherArtiste(Artiste artiste) {
        PopupUtils.openPopup("details_artiste.fxml","Details d'un Artiste", new DetailsArtisteController(context,artiste));
    }

    @Override
    public void saisirArtiste() {
        PopupUtils.openPopup("nouveau_artiste_popup.fxml", "Créer un artiste", new NewArtisteController(context));
    }

    @Override
    public void afficherLieu(Terrain terrain) {
        PopupUtils.openPopup("details_lieu.fxml", "Afficher un Terrain", new DetailsLieuxController(context, terrain));
    }

    @Override
    public void afficherEvent(Evenement evenement) {
        PopupUtils.openPopup("details_event.fxml", "Afficher un Événement", new DetailsEventController(context, evenement));
    }

    @Override
    public void saisirTerrain() {
        PopupUtils.openPopup("nouveau_lieux_popup.fxml", "Créer un terrain", new NewLieuxController(context));
    }

    @Override
    public void saisirNouvelEvenement(Set<String> nomsExistants) {
        PopupUtils.openPopup("nouveau_event_popup.fxml", "Créer un évènement", new NewEventController(context));
    }
}