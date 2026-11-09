package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewLieuxController extends SimplePopupController {
    @FXML private TextField nom;
    @FXML private TextField adresse;
    @FXML private TextField superficie;
    @FXML private TextArea description;
    @FXML private Label capacite;

    public NewLieuxController(JavaFXContext context) {
        super(context);
    }

    @FXML
    private void handleAdd() {
        if (!verifyField()) return;

        IHM.InfosNouveauTerrain data = new IHM.InfosNouveauTerrain(
                this.nom.getText().strip(),
                this.adresse.getText().strip(),
                Double.parseDouble(this.superficie.getText()),
                this.description.getText()

        );
        boolean success = context.controleur().creerTerrain(data);
        if (success) disableWindow();
    }

    @FXML
    private void onResetClick() {
        nom.setText("");
        adresse.setText("");
        superficie.setText("");
        description.setText("");
        resetStyle();
    }

    @FXML
    protected void onBackClick() {
        disableWindow();
    }

    @Override
    protected Scene provideScene() {
        return nom.getScene();
    }

    @Override
    protected void resetStyle() {
        nom.getStyleClass().remove("error");
        adresse.getStyleClass().remove("error");
        superficie.getStyleClass().remove("error");
        description.getStyleClass().remove("error");
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
}
