package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.FileInfo;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class LinkController extends SimplePopupController implements Initializable {
    private final BiConsumer<Integer, FileInfo> onLinked;

    @FXML private TextField price;
    @FXML private Hyperlink hyperText;

    private FileInfo selectedFile = null;
    private Integer currentPrice;

    public LinkController(JavaFXContext context, FileInfo currentContrat, Integer currentPrice, BiConsumer<Integer, FileInfo> onLinked) {
        super(context);
        this.onLinked = onLinked;
        this.selectedFile = currentContrat;
        this.currentPrice = currentPrice;
    }

    public LinkController(JavaFXContext context, BiConsumer<Integer, FileInfo> onLinked) {
        super(context);
        this.onLinked = onLinked;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateFileName();

        if (currentPrice != null) {
            price.setText(currentPrice.toString());
        }
    }

    private void updateFileName() {
        if (selectedFile != null) {
            hyperText.setText(selectedFile.getName());
        }
    }

    @FXML
    private void onFileSelector() {
        FileChooser chooser = new FileChooser();

        File theFile = chooser.showOpenDialog(hyperText.getScene().getWindow());
        if (theFile != null) {
            this.selectedFile = new FileInfo(theFile);
            hyperText.setText(selectedFile.getName());
        }
    }

    @FXML
    private void handleAdd() {
        if (!verifyField()) return;

        if (onLinked != null) {
            onLinked.accept(Integer.parseInt(price.getText()), selectedFile);
        }
        disableWindow();
    }

    @Override
    protected boolean onVerifyFields() {
        if (price.getText().isEmpty()) {
            price.getStyleClass().add("error");
            return false;
        }

        try {
            Integer.parseInt(price.getText());
        } catch (NumberFormatException e) {
            price.getStyleClass().add("error");
            return false;
        }

        return true;
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @Override
    protected void resetStyle() {
        price.getStyleClass().remove("error");
    }

    @Override
    protected Scene provideScene() {
        return hyperText.getScene();
    }
}
