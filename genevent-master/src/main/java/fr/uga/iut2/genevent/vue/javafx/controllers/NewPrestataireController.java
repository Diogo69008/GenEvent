package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.PrestataireType;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class NewPrestataireController extends SimplePopupController implements Initializable {
    @FXML private TextField tfNomPrestataire;
    @FXML private TextField tfMail;
    @FXML private TextField tfTelephone;
    @FXML private ComboBox<PrestataireType> typeCombo;
    @FXML private TextArea tfPrestaInfo;

    public NewPrestataireController(JavaFXContext context) {
        super(context);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (PrestataireType value : PrestataireType.values()) {
            typeCombo.getItems().add(value);
        }
    }

    @FXML
    private void handleAdd() {
        if (!verifyField()) return;

        IHM.InfosNouvauPrestataire data = new IHM.InfosNouvauPrestataire(
                this.tfNomPrestataire.getText().strip(),
                this.tfMail.getText().strip(),
                this.tfTelephone.getText(),
                this.typeCombo.getValue(),
                this.tfPrestaInfo.getText()
        );
        boolean success = context.controleur().creerPrestataire(data);
        if (success) disableWindow();
    }

    @Override
    protected Scene provideScene() {
        return tfNomPrestataire.getScene();
    }

    @FXML
    private void onResetClick() {
        tfNomPrestataire.setText("");
        tfMail.setText("");
        tfTelephone.setText("");
        typeCombo.setValue(null);
        tfPrestaInfo.setText("");
        resetStyle();
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @Override
    protected boolean onVerifyFields() {
        boolean valid = true;
        resetStyle();

        if (tfNomPrestataire.getText().isEmpty()) {
            tfNomPrestataire.getStyleClass().add("error");
            valid = false;
        }

        if (!RegexPhoneValidator.validerNumero(this.tfTelephone.getText())) {
            tfTelephone.getStyleClass().add("error");
            valid = false;
        }

        if (!EmailValidator.getInstance().isValid(tfMail.getText())) {
            tfMail.getStyleClass().add("error");
            valid = false;
        }

        if (typeCombo.getValue() == null) {
            typeCombo.getStyleClass().add("error");
            valid = false;
        }

        return valid;
    }

    @Override
    protected void resetStyle() {
        tfNomPrestataire.getStyleClass().remove("error");
        tfMail.getStyleClass().remove("error");
        tfTelephone.getStyleClass().remove("error");
        typeCombo.getStyleClass().remove("error");
        tfPrestaInfo.getStyleClass().remove("error");
    }
}
