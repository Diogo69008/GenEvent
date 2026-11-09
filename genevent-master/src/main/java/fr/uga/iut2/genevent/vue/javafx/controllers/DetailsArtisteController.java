package fr.uga.iut2.genevent.vue.javafx.controllers;


import fr.uga.iut2.genevent.modele.Artiste;
import fr.uga.iut2.genevent.modele.Evenement;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class DetailsArtisteController extends SimplePopupController implements Initializable {
    private final Artiste artiste;

    public DetailsArtisteController(JavaFXContext context, Artiste artiste) {
        super(context);
        this.artiste = artiste;
    }

    @FXML private TextField LabelNomS;
    @FXML private TextField LabelNom;
    @FXML private TextField LabelPrenom;
    @FXML private TextField LabelAge , LabelTel;
    @FXML private TextArea taInfo;
    @FXML private TextArea LabelInfo;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (artiste != null) {
            LabelNomS.setText(artiste.getNomDeScene());
            LabelNom.setText(artiste.getNom());
            LabelTel.setText(artiste.getTelephone());
            LabelPrenom.setText(artiste.getPrenom());
            LabelAge.setText(artiste.getAge().toString());
            LabelInfo.setText(artiste.getInformation());
        }
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @FXML
    public void onDeleteClick(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de l'artiste");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet artiste ? Cet artiste sera dissocié de tous les événements.");
        alert.setContentText("La suppression de cet élément est irréversible.");

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {

            context.controleur().supprimerArtiste(artiste);
            disableWindow();
        }
    }
    @FXML
    public void onModifClick(){
        PopupUtils.openPopupIn("modif_artiste.fxml", LabelNom, "Modifier l'artiste", new ModifArtisteController(this.context, this.artiste));
    }

    @Override
    protected boolean onVerifyFields() {
        return false;
    }

    @Override
    protected void resetStyle() {
    }

    @Override
    protected Scene provideScene() {
        return LabelNom.getScene();
    }
}