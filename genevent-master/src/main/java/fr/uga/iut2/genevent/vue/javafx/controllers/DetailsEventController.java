package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class DetailsEventController extends SimplePopupController implements Initializable {
    private final Evenement evenement;

    public DetailsEventController(JavaFXContext context, Evenement evenement) {
        super(context);
        this.evenement = evenement;
    }

    @FXML private Label description, prixBillet;
    @FXML private TextField nom, lieu, dateDebut, dateFin, budget;
    @FXML private VBox vboxA, vboxP;

    @FXML
    private void handleModifClick() {
        PopupUtils.openPopupIn("modif_event.fxml", nom, "Modifier l'évènement", new ModifEventController(this.context, this.evenement));
    }

    @FXML
    private void handleDeleteClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de l'évènement");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet évènement ?");
        alert.setContentText("La suppression de cet élément est irréversible.");

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {

            context.controleur().supprimerEvent(evenement);
            disableWindow();
        }
    }

    /**
     * Close the popup
     */
    @Override
    protected void onBackClick() {
        disableWindow();
    }

    /**
     * Provide the scene for the SimplePopupController
     * @return
     */
    @Override
    protected Scene provideScene() {
        return nom.getScene();
    }


    // Unused for this controller
    @Override
    protected boolean onVerifyFields() {
        return false;
    }

    @Override
    protected void resetStyle() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (evenement != null) {
            nom.setText(evenement.getNom());
            dateDebut.setText(evenement.getDateDebut().toString());
            dateFin.setText(evenement.getDateFin().toString());
            budget.setText(evenement.getBudget().toString());
            lieu.setText(evenement.getTerrain() != null ? evenement.getTerrain().getNom() : "Aucun");
            prixBillet.setText(evenement.getBillet() != null ? evenement.getBillet().toString() : "Non défini");

            for(ContratArtiste cartiste : evenement.getArtiste().values()){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(10);

                Label lblNom = new Label(cartiste.getArtiste().getNom());
                hBox.getChildren().add(lblNom);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hBox.getChildren().add(spacer);

                Button btnDetailsContrat = new Button("Contrat");
                Button btnDetailsPresta = new Button("Details");


                btnDetailsContrat.setStyle("-fx-background-color: #F0FDF4; -fx-text-fill: #16A34A; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

                btnDetailsPresta.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #2563EB; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

                hBox.getChildren().addAll(btnDetailsContrat, btnDetailsPresta);

                btnDetailsPresta.setOnAction(_ -> {
                    context.controleur().afficherArtiste(cartiste.getArtiste());
                });

                btnDetailsContrat.setOnAction(_ -> {
                    context.controleur().afficherContratArtiste(cartiste);
                });

                vboxA.getChildren().add(hBox);
            }

            for(ContratPrestataire cprest : evenement.getPrestataires().values()){
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setSpacing(10);

                Label lblNom = new Label(cprest.getPrestataire().getNom());
                hBox.getChildren().add(lblNom);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hBox.getChildren().add(spacer);

                Button btnDetailsContrat = new Button("Contrat");
                Button btnDetailsPresta = new Button("Details");

                btnDetailsContrat.setStyle("-fx-background-color: #F0FDF4; -fx-text-fill: #16A34A; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");
                btnDetailsPresta.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #2563EB; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

                hBox.getChildren().addAll(btnDetailsContrat, btnDetailsPresta);

                btnDetailsPresta.setOnAction(_ -> {
                    context.controleur().afficherPresta(cprest.getPrestataire());
                });

                btnDetailsContrat.setOnAction(_ -> {
                    context.controleur().afficherContratPresta(cprest);
                });

                vboxP.getChildren().add(hBox);
            }
        }
    }
}
