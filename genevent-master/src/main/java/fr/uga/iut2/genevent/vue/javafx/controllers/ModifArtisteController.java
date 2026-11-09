package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Artiste;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.util.RegexPhoneValidator;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifArtisteController extends SimplePopupController implements Initializable {
    private final Artiste artiste;

    public ModifArtisteController(JavaFXContext context, Artiste artiste) {
        super(context);
        this.artiste = artiste;
    }

    @FXML private TextField LabelNomS, LabelTel,LabelNom,LabelAge,LabelPrenom;
    @FXML private TextArea LabelInfo;

    @Override
    protected void onBackClick() {
        PopupUtils.openPopupIn("details_artiste.fxml", LabelInfo, "Details Artiste", new DetailsArtisteController(context, artiste));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    private void update() {
        if (artiste != null) {
            LabelNomS.setText(artiste.getNomDeScene());
            LabelNom.setText(artiste.getNom());
            LabelNom.setDisable(true);
            LabelInfo.setText(Objects.toString(artiste.getInformation(), ""));
            LabelAge.setText(artiste.getAge().toString());
            LabelPrenom.setText(artiste.getPrenom());
            LabelTel.setText(artiste.getTelephone());
        }
    }

    @FXML
    private void onModifClick() {

        if (!verifyField()) return;

        artiste.setNomDeScene(LabelNomS.getText());
        artiste.setAge(Integer.parseInt(LabelAge.getText()));
        artiste.setPrenom(LabelPrenom.getText());
        artiste.setTelephone(LabelTel.getText());
        artiste.setInformations(LabelInfo.getText());

        context.controleur().getIhm().informerUtilisateur("L'artiste " + artiste.getNomDeScene() + " a bien été modifié !",
                true);
        context.controleur().getIhm().requestRefresh();
        disableWindow();
    }

    @FXML
    private void onResetClick() {
        update();
        resetStyle();
    }

    @Override
    protected boolean onVerifyFields() {
        boolean success = true;

        if (LabelNomS.getText().isEmpty()) {
            LabelNomS.getStyleClass().add("error");
            success = false;
        }

        if (LabelNom.getText().isEmpty()) {
            LabelNom.getStyleClass().add("error");
            success = false;
        }

        if (LabelPrenom.getText().isEmpty()) {
            LabelPrenom.getStyleClass().add("error");
            success = false;
        }

        try {
            Integer.parseInt(LabelAge.getText());
        } catch (Exception e) {
            LabelAge.getStyleClass().add("error");
            success = false;
        }

        if (!RegexPhoneValidator.validerNumero(LabelTel.getText())) {
            LabelTel.getStyleClass().add("error");
            success = false;
        }

        return success;
    }

    @Override
    protected void resetStyle() {
        LabelAge.getStyleClass().remove("error");
        LabelInfo.getStyleClass().remove("error");
        LabelTel.getStyleClass().remove("error");
        LabelNomS.getStyleClass().remove("error");
        LabelNom.getStyleClass().remove("error");
        LabelPrenom.getStyleClass().remove("error");
    }

    @Override
    protected Scene provideScene() {
        return LabelNomS.getScene();
    }
}

