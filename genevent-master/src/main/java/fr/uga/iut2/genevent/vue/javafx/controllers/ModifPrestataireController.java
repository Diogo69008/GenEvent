package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Prestataire;
import fr.uga.iut2.genevent.modele.PrestataireType;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifPrestataireController extends SimplePopupController implements Initializable {
    private final Prestataire prestataire;

    public ModifPrestataireController(JavaFXContext context, Prestataire prestataire) {
        super(context);
        this.prestataire = prestataire;
    }

    @FXML private TextField tfNomPrestataire;
    @FXML private TextField tfMail;
    @FXML private TextField tfTelephone;
    @FXML private TextArea tfPrestaInfo;
    @FXML private ComboBox<PrestataireType> typeCombo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        update();

        for (PrestataireType value : PrestataireType.values()) {
            typeCombo.getItems().add(value);
        }
    }

    private void update(){
        if (prestataire != null) {
            tfNomPrestataire.setText(prestataire.getNom());
            tfMail.setText(prestataire.getMail());
            tfTelephone.setText(String.valueOf(prestataire.getTelephone()));
            tfPrestaInfo.setText(prestataire.getInformation());
            typeCombo.setValue(prestataire.getType());
        }
    }

    @FXML
    public void onModifClick(){
        if (!verifyField()) return;

        prestataire.setMail(tfMail.getText());
        prestataire.setTelephone(tfTelephone.getText());
        prestataire.setInformation(tfPrestaInfo.getText());
        prestataire.setType(typeCombo.getValue());
        context.controleur().getIhm().informerUtilisateur("Le prestataire " + prestataire.getNom() + " a bien été modifié !",
                true);
        context.controleur().getIhm().requestRefresh();
        disableWindow();
    }

    @Override
    protected void onBackClick() {
        PopupUtils.openPopupIn("details_prestataire.fxml", tfMail, "Details d'un Prestataire", new DetailsPrestataireController(context, prestataire));
    }

    @FXML
    private void onResetClick() {
        update();
    }

    @Override
    protected boolean onVerifyFields() {
        boolean valid = true;

        if (!RegexPhoneValidator.validerNumero(this.tfTelephone.getText())) {
            tfTelephone.getStyleClass().add("error");
            valid = false;
        }

        if (!EmailValidator.getInstance().isValid(tfMail.getText())) {
            tfMail.getStyleClass().add("error");
            valid = false;
        }

        return valid;
    }

    @Override
    protected void resetStyle() {
        tfTelephone.getStyleClass().remove("error");
        tfMail.getStyleClass().remove("error");
    }

    @Override
    protected Scene provideScene() {
        return tfNomPrestataire.getScene();
    }
}
