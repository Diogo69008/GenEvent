package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Evenement;
import fr.uga.iut2.genevent.modele.Prestataire;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class DetailsPrestataireController extends SimplePopupController implements Initializable {
    private final Prestataire prestataire;

    public DetailsPrestataireController(JavaFXContext context, Prestataire prestataire) {
        super(context);
        this.prestataire = prestataire;
    }

    @FXML private TextField LabelNom;
    @FXML private TextField LabelMail;
    @FXML private TextField LabelTel;
    @FXML private TextField LabelType;
    @FXML private TextField LabelInfo;
    @FXML private Button btnSupprimer;
    @FXML private Button btnModifie;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (prestataire != null) {
            LabelNom.setText(prestataire.getNom() );
            LabelMail.setText(prestataire.getMail());
            LabelTel.setText(prestataire.getTelephone());
            LabelType.setText(prestataire.getType().toString());
            LabelInfo.setText(prestataire.getInformation());
            if (prestataire.isSponsorise()){
                btnSupprimer.setDisable(true);
                btnModifie.setDisable(true);
            }
        }
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @FXML
    private void onBtnSupprimerClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de prestataire");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce prestataire ? Ce prestataire sera dissocié de tous les événements.");
        alert.setContentText("La suppression de cet élément est irréversible.");

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {

            context.controleur().supprimerPrestataire(prestataire);

            disableWindow();
        }
    }

    @FXML
    public void onModifClick(){
        PopupUtils.openPopupIn("modif_prestataire.fxml", LabelNom, "Modifier le Prestataire", new ModifPrestataireController(this.context, this.prestataire));
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
