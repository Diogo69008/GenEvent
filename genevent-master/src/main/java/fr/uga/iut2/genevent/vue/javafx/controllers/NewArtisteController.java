package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import static java.lang.Integer.parseInt;

public class NewArtisteController extends SimplePopupController {
    @FXML private TextField tfNomS;
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfAge;
    @FXML private TextField tfTel;
    @FXML private TextArea taInfo;


    public NewArtisteController(JavaFXContext context) {
        super(context);
    }

    @FXML
    private void handleAjouterClique() {
        if (!verifyField()) return;

        IHM.InfosArtiste info = new IHM.InfosArtiste(tfNomS.getText(),
                tfNom.getText(),
                tfPrenom.getText(),
                Integer.parseInt(tfAge.getText()),
                tfTel.getText(), taInfo.getText());

        boolean succes = context.controleur().creerArtiste(info);
        if (succes) disableWindow();
    }

    @FXML
    private void onResetClick() {
        tfNomS.setText("");
        tfNom.setText("");
        tfAge.setText("");
        tfTel.setText("");
        tfPrenom.setText("");
        taInfo.setText("");
        resetStyle();
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @Override
    protected boolean onVerifyFields() {
        boolean success = true;
        resetStyle();

        if (tfNomS.getText().isEmpty()) {
            tfNomS.getStyleClass().add("error");
            success = false;
        }

        if (tfNom.getText().isEmpty()) {
            tfNom.getStyleClass().add("error");
            success = false;
        }

        if (tfPrenom.getText().isEmpty()) {
            tfPrenom.getStyleClass().add("error");
            success = false;
        }

        try {
            Integer.parseInt(tfAge.getText());
        } catch (Exception e) {
            tfAge.getStyleClass().add("error");
            success = false;
        }

        if (!RegexPhoneValidator.validerNumero(tfTel.getText())) {
            tfTel.getStyleClass().add("error");
            success = false;
        }

        return success;
    }

    @Override
    protected void resetStyle() {
        tfNomS.getStyleClass().remove("error");
        tfNom.getStyleClass().remove("error");
        tfPrenom.getStyleClass().remove("error");
        tfAge.getStyleClass().remove("error");
        tfTel.getStyleClass().remove("error");
    }

    @Override
    protected Scene provideScene() {
        return tfNom.getScene();
    }
}
