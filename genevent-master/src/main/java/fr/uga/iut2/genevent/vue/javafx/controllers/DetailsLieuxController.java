package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Evenement;
import fr.uga.iut2.genevent.modele.Terrain;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.util.Objects;
import java.util.Optional;


public class DetailsLieuxController extends SimplePopupController {
    private final Terrain terrainSelectionne;

    public DetailsLieuxController(JavaFXContext context, Terrain terrainSelectionne) {
        super(context);
        this.terrainSelectionne = terrainSelectionne;
    }

    @FXML private TextField nom, adresse, superficie, capacite;
    @FXML private TextArea description;

    @FXML private Button btnRetour, btnSupprimer, btnModifier;

    @FXML
    private void initialize() {
        if (terrainSelectionne != null) {
            nom.setText(terrainSelectionne.getNom());
            adresse.setText(terrainSelectionne.getAdresse());

            superficie.setText(terrainSelectionne.getSurface()/10000 +" hectare " +"( "+ String.format("%.0f m²", terrainSelectionne.getSurface())+" )");
            capacite.setText(terrainSelectionne.getCapacite() + "");
            description.setText(terrainSelectionne.getDescription());
        }
    }

    @FXML
    private void onBtnSupprimerClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de lieu");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce lieu ? Ce lieu sera dissocié de tous les événements.");
        alert.setContentText("La suppression de cet élément est irréversible.");

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {

            context.controleur().supprimerTerrain(terrainSelectionne);
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

    @FXML
    public void onModifClick(){
        PopupUtils.openPopupIn("modifier_lieu_popup.fxml", nom, "Modifier le Lieu", new ModifLieuxController(this.context, this.terrainSelectionne));
    }

    // Unused for this controller
    @Override
    protected boolean onVerifyFields() {
        return false;
    }

    @Override
    protected void resetStyle() {
    }

    /**
     * Provide the scene for the SimplePopupController
     * @return
     */
    @Override
    protected Scene provideScene() {
        return nom.getScene();
    }
}
