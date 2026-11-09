package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Terrain;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.CLI;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifLieuxController extends SimplePopupController implements Initializable {
    private final Terrain terrainSelectionne;

    @FXML private TextField nom;
    @FXML private TextField adresse;
    @FXML private TextField superficie;
    @FXML private TextArea description;
    @FXML private Label capacite;

    public ModifLieuxController(JavaFXContext context, Terrain terrainSelectionne) {
        super(context);
        this.terrainSelectionne = terrainSelectionne;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    private void update() {
        if (terrainSelectionne != null) {
            nom.setText(Objects.toString(terrainSelectionne.getNom(), "-"));
            nom.setDisable(true);
            adresse.setText(Objects.toString(terrainSelectionne.getAdresse(), "-"));

            superficie.setText(terrainSelectionne.getSurface()/10000+"");
            capacite.setText(String.valueOf(terrainSelectionne.getCapacite()));
            description.setText(Objects.toString(terrainSelectionne.getDescription(), "-"));
        }
    }

    @FXML
    private void handleSave() {
        if (!verifyField()) return;

        terrainSelectionne.setSurface(Double.parseDouble(superficie.getText()));
        terrainSelectionne.setAdresse(adresse.getText());
        terrainSelectionne.setDescription(description.getText());

        context.controleur().getIhm().informerUtilisateur("Le terrain " + terrainSelectionne.getNom() + " a bien été modifié !",
                true);
        context.controleur().getIhm().requestRefresh();
        disableWindow();
    }

    @Override
    protected void onBackClick() {
        PopupUtils.openPopupIn("details_lieu.fxml", nom, "Details du lieux", new DetailsLieuxController(context, terrainSelectionne));
    }

    @FXML
    private void onResetClick() {
        update();
        resetStyle();
    }

    @Override
    protected boolean onVerifyFields() {
        boolean valid = true;

        if (adresse.getText().isEmpty()) {
            adresse.getStyleClass().add("error");
            valid = false;
        }

        if (superficie.getText().isEmpty()) {
            superficie.getStyleClass().add("error");
            valid = false;
        }

        if (nom.getText().isEmpty()) {
            nom.getStyleClass().add("error");
            valid = false;
        }

        try {

            double superficieVal = Double.parseDouble(superficie.getText());
            if (superficieVal <=0) {
                superficie.getStyleClass().add("error");
                valid = false;
            } else {
                capacite.setText((int) ((superficieVal * 10000) * 2) + "");
            }
        } catch (Exception e) {
            superficie.getStyleClass().add("error");
            valid = false;
        }

        return valid;
    }

    @Override
    protected void resetStyle() {
        nom.getStyleClass().remove("error");
        adresse.getStyleClass().remove("error");
        superficie.getStyleClass().remove("error");
        description.getStyleClass().remove("error");
    }

    @Override
    protected Scene provideScene() {
        return nom.getScene();
    }
}
