package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Terrain;
import fr.uga.iut2.genevent.vue.IHM;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.commons.validator.routines.EmailValidator;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class NewEventController extends SimplePopupController implements Initializable {
    @FXML private TextField tfNom;
    @FXML private TextField tfBud;
    @FXML private DatePicker dpDeb;
    @FXML private DatePicker dpFin;
    @FXML private Label labErr;
    @FXML private ComboBox<String> cbLieu;
    @FXML private Button btnLieu;

    public NewEventController(JavaFXContext context) {
        super(context);
    }

    private void validateDatesDynamically() {
        String textDeb = dpDeb.getEditor().getText();
        String textFin = dpFin.getEditor().getText();

        LocalDate dateDeb = null;
        LocalDate dateFin = null;

        boolean debValid = false;
        boolean finValid = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (textDeb != null && !textDeb.trim().isEmpty()) {
            try {
                dateDeb = LocalDate.parse(textDeb, formatter);
                debValid = true;
            } catch (DateTimeParseException e) {

            }
        }

        if (textFin != null && !textFin.trim().isEmpty()) {
            try {
                dateFin = LocalDate.parse(textFin, formatter);
                finValid = true;
            } catch (DateTimeParseException e) {

            }
        }

        if (debValid) {
            dpDeb.getStyleClass().remove("error");
        } else {
            if (!dpDeb.getStyleClass().contains("error")) {
                dpDeb.getStyleClass().add("error");
            }
        }

        if (finValid) {
            dpFin.getStyleClass().remove("error");
        } else {
            if (!dpFin.getStyleClass().contains("error")) {
                dpFin.getStyleClass().add("error");
            }
        }

        if (debValid && finValid) {
            if (dateDeb.isAfter(dateFin)) {
                if (!dpDeb.getStyleClass().contains("error")) dpDeb.getStyleClass().add("error");
                if (!dpFin.getStyleClass().contains("error")) dpFin.getStyleClass().add("error");
            } else {
                dpDeb.getStyleClass().remove("error");
                dpFin.getStyleClass().remove("error");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateLieu();

        dpDeb.setConverter(new DateStringConverter(this::validateDatesDynamically, () -> {
            if (!dpDeb.getStyleClass().contains("error")) dpDeb.getStyleClass().add("error");
        }));

        dpFin.setConverter(new DateStringConverter(this::validateDatesDynamically, () -> {
            if (!dpFin.getStyleClass().contains("error")) dpFin.getStyleClass().add("error");
        }));

        dpDeb.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            validateDatesDynamically();
        });
        dpFin.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            validateDatesDynamically();
        });
    }

    private void updateLieu() {
        Collection<Terrain> values = context.controleur().getGenevent().getTerrains().values();
        cbLieu.setValue("Aucun");
        if (values.isEmpty()) {
            labErr.setText("Aucun terrain enregistré !");
            cbLieu.getItems().add("Aucun");
            return;
        }
        labErr.setText("");
        cbLieu.getItems().clear();
        cbLieu.getItems().add("Aucun");
        for (Terrain value : values) {
            cbLieu.getItems().add(value.getNom());
        }
    }

    @FXML
    private void handleAjouterClique() {
        dpDeb.commitValue();
        dpFin.commitValue();

        if (!verifyField()) return;

        Terrain terrain = "Aucun".equals(cbLieu.getValue()) ? null : context.controleur().getGenevent().getTerrains().get(cbLieu.getValue().toLowerCase());

        IHM.InfosNouvelEvenement data = new IHM.InfosNouvelEvenement(
                this.tfNom.getText().strip(),
                this.dpDeb.getValue(),
                this.dpFin.getValue(),
                terrain,
                Integer.parseInt(tfBud.getText())
        );
        boolean success = context.controleur().creerEvenement(data);
        if (success) disableWindow();
    }

    @FXML
    private void onResetClick() {
        tfNom.setText("");
        tfBud.setText("");
        dpDeb.setValue(null);
        dpFin.setValue(null);
        cbLieu.setValue(null);
        resetStyle();
    }

    @Override
    protected void onBackClick() {
        disableWindow();
    }

    @Override
    protected void resetStyle() {
        tfNom.getStyleClass().remove("error");
        dpDeb.getStyleClass().remove("error");
        dpFin.getStyleClass().remove("error");
        cbLieu.getStyleClass().remove("error");
        btnLieu.getStyleClass().remove("error");
        tfBud.getStyleClass().remove("error");
    }

    @Override
    protected boolean onVerifyFields() {
        boolean success = true;
        resetStyle();

//        if (cbLieu.getValue() == null) {
//            cbLieu.getStyleClass().add("error");
//            success = false;
//        }

        if (dpDeb.getValue() == null) {
            dpDeb.getStyleClass().add("error");
            success = false;
        }

        if (dpFin.getValue() == null) {
            dpFin.getStyleClass().add("error");
            success = false;
        }

        if (dpDeb.getValue() != null && dpFin.getValue() != null) {
            if (dpDeb.getValue().isAfter(dpFin.getValue())) {
                dpDeb.getStyleClass().add("error");
                dpFin.getStyleClass().add("error");
                success = false;
            }
        }

        if (tfNom.getText().isEmpty()) {
            tfNom.getStyleClass().add("error");
            success = false;
        }

        try {
            Integer.parseInt(tfBud.getText());
        } catch (Exception e) {
            tfBud.getStyleClass().add("error");
            success = false;
        }

        return success;
    }

    @FXML
    private void handleNewLieu() {
        context.controleur().saisirTerrain();
        updateLieu();
    }

    @Override
    protected Scene provideScene() {
        return tfNom.getScene();
    }
}
