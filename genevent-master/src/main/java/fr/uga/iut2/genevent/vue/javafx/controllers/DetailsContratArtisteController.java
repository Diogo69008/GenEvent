package fr.uga.iut2.genevent.vue.javafx.controllers;


import fr.uga.iut2.genevent.modele.ContratArtiste;
import fr.uga.iut2.genevent.modele.ContratPrestataire;
import fr.uga.iut2.genevent.modele.FileInfo;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class DetailsContratArtisteController extends SimplePopupController implements Initializable {
    private final ContratArtiste contrat;

    public DetailsContratArtisteController(JavaFXContext context, ContratArtiste contrat) {
        super(context);
        this.contrat = contrat;
    }

    @FXML private Hyperlink hyperText;
    @FXML private TextField price;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (contrat != null) {
            hyperText.setText(contrat.getContrat() != null ? contrat.getContrat().getName() : "Aucun");
            price.setText(contrat.getCout() + "");
        }
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @FXML
    private void onFileSelector() {
        if (contrat.getContrat() == null) return;

        FileInfo fileInfo = contrat.getContrat();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier");
        fileChooser.setInitialFileName(fileInfo.getName());

        File selectedFile = fileChooser.showSaveDialog(hyperText.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Files.write(selectedFile.toPath(), fileInfo.getData());
            } catch (Exception e) {
            }
        }
    }

    @FXML
    public void onModifClick(){
        PopupUtils.openPopupIn("lier_artiste.fxml", price, "Lier l'artiste",
                new LinkController(this.context, contrat.getContrat(), contrat.getCout(),
                        (integer, file) -> {
            contrat.setContrat(file);
            contrat.setCout(integer);
        }));
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
        return price.getScene();
    }
}