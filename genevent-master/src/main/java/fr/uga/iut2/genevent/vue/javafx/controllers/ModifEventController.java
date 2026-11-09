package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.*;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModifEventController extends SimplePopupController implements Initializable {
    private final Evenement evenement;
    private final Map<String, ContratArtiste> draftArtistes = new HashMap<>();
    private final Map<String, ContratPrestataire> draftPrestataires = new HashMap<>();

    public ModifEventController(JavaFXContext context, Evenement event) {
        super(context);

        this.evenement = event;
        if (event != null) {
            this.draftArtistes.putAll(event.getArtiste());
            this.draftPrestataires.putAll(event.getPrestataires());
        }
    }

    @FXML
    private TextField tfNom, tfBud;
    @FXML
    private DatePicker dpDeb, dpFin;
    @FXML
    private ComboBox<String> cbLieu, cbArtiste, cbPrestataire;
    @FXML
    private VBox vboxArtistes, vboxPrestataires;
    @FXML
    private Label labErr, labelPrixBillet;


    private void updatePrixBillet(Integer price) {
        labelPrixBillet.setText(price != null ? price.toString() : "Non calculé");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLieu();
        if (evenement != null) {
            tfNom.setText(Objects.toString(evenement.getNom(), "-"));
            tfNom.setDisable(true); // event name cannot be modified since it is final in Evenement
            dpDeb.setValue(evenement.getDateDebut());
            dpFin.setValue(evenement.getDateFin());
            tfBud.setText(evenement.getBudget().toString());
            cbLieu.setValue(evenement.getTerrain() != null ? evenement.getTerrain().getNom() : "Aucun");

            updatePrixBillet(evenement.getBillet());

            // Add listeners to update the available artists/providers when dates change
            dpDeb.valueProperty().addListener((obs, oldVal, newVal) -> majScrollPanes());
            dpFin.valueProperty().addListener((obs, oldVal, newVal) -> majScrollPanes());

            majScrollPanes();
        }
    }

    private boolean isArtisteDispo(Artiste artiste, LocalDate deb, LocalDate fin) {
        if (deb == null || fin == null) return false;
        for (Evenement val : context.controleur().getGenevent().getEvenements().values()) {
            if (val == evenement) continue;
            if (val.getArtiste().containsKey(artiste.id())) {
                if (val.getDateDebut().isBefore(fin) && val.getDateFin().isAfter(deb)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPrestataireDispo(Prestataire prestataire, LocalDate deb, LocalDate fin) {
        if (deb == null || fin == null) return false;
        for (Evenement val : context.controleur().getGenevent().getEvenements().values()) {
            if (val == evenement) continue;
            if (val.getPrestataires().containsKey(prestataire.id())) {
                if (val.getDateDebut().isBefore(fin) && val.getDateFin().isAfter(deb)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void majScrollPanes() {
        vboxArtistes.getChildren().clear();
        vboxPrestataires.getChildren().clear();
        cbArtiste.getItems().clear();
        cbPrestataire.getItems().clear();

        LocalDate deb = dpDeb.getValue();
        LocalDate fin = dpFin.getValue();

        // Artistes
        for (ContratArtiste artisteContrat : draftArtistes.values()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);

            Label lblNom = new Label(artisteContrat.getArtiste().getNom());
            hBox.getChildren().add(lblNom);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().add(spacer);

            Button btnDiscocier = new Button("Dissocier");
            Button btnDetailsContrat = new Button("Détails Contrat");
            Button btnDetailsPresta = new Button("Détails Artiste");

            btnDiscocier.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #EF4444; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

            btnDetailsContrat.setStyle("-fx-background-color: #F0FDF4; -fx-text-fill: #16A34A; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

            btnDetailsPresta.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #2563EB; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

            hBox.getChildren().addAll(btnDiscocier, btnDetailsContrat, btnDetailsPresta);

            btnDiscocier.setOnAction(_ -> {
                draftArtistes.remove(artisteContrat.getArtiste().id());
                majScrollPanes();
            });

            btnDetailsPresta.setOnAction(_ -> {
                context.controleur().afficherArtiste(artisteContrat.getArtiste());
            });

            btnDetailsContrat.setOnAction(_ -> {
                context.controleur().afficherContratArtiste(artisteContrat);
            });

            vboxArtistes.getChildren().add(hBox);
        }

        if (deb != null && fin != null && !deb.isAfter(fin)) {
            for (Artiste arti : context.controleur().getGenevent().getArtistes().values()) {
                if (draftArtistes.containsKey(arti.id())) continue;

                if (isArtisteDispo(arti, deb, fin)) {
                    cbArtiste.getItems().add(arti.id());
                }
            }
        }

        // Prestataires
        for (ContratPrestataire prestaContrat : draftPrestataires.values()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);

            Label lblNom = new Label(prestaContrat.getPrestataire().getNom());
            hBox.getChildren().add(lblNom);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            hBox.getChildren().add(spacer);

            Button btnDiscocier = new Button("Dissocier");
            Button btnDetailsContrat = new Button("Détails Contrat");
            Button btnDetailsPresta = new Button("Détails Prestataire");

            btnDiscocier.setStyle("-fx-background-color: #FEF2F2; -fx-text-fill: #EF4444; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");
            btnDetailsContrat.setStyle("-fx-background-color: #F0FDF4; -fx-text-fill: #16A34A; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");
            btnDetailsPresta.setStyle("-fx-background-color: #E0F2FE; -fx-text-fill: #2563EB; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 5px 12px; -fx-cursor: hand; -fx-font-family: 'Segoe UI', sans-serif; -fx-font-size: 12px;");

            hBox.getChildren().addAll(btnDiscocier, btnDetailsContrat, btnDetailsPresta);

            btnDiscocier.setOnAction(_ -> {
                draftPrestataires.remove(prestaContrat.getPrestataire().id());
                majScrollPanes();
            });

            btnDetailsPresta.setOnAction(_ -> {
                context.controleur().afficherPresta(prestaContrat.getPrestataire());
            });

            btnDetailsContrat.setOnAction(_ -> {
                context.controleur().afficherContratPresta(prestaContrat);
            });

            vboxPrestataires.getChildren().add(hBox);
        }

        if (deb != null && fin != null && !deb.isAfter(fin)) {
            for (Prestataire presta : context.controleur().getGenevent().getPrestataires().values()) {
                if (draftPrestataires.containsKey(presta.id())) continue;

                if (isPrestataireDispo(presta, deb, fin)) {
                    cbPrestataire.getItems().add(presta.id());
                }
            }
        }
    }

    @Override
    protected void onBackClick() {
        PopupUtils.openPopupIn("details_event.fxml", tfNom, "Détail de l'évenement", new DetailsEventController(context, evenement));
    }

    @Override
    protected boolean onVerifyFields() {
        boolean success = true;

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
            int budget = Integer.parseInt(tfBud.getText());
            if (budget < 0) {
                tfBud.getStyleClass().add("error");
                success = false;
            } else {
                updatePrixBillet(Evenement.calculPrixBillet(budget, currentSelectedTerrain()));
            }
        } catch (Exception e) {
            tfBud.getStyleClass().add("error");
            success = false;
        }

        return success;
    }

    @Override
    protected void resetStyle() {
        tfNom.getStyleClass().remove("error");
        dpDeb.getStyleClass().remove("error");
        dpFin.getStyleClass().remove("error");
        cbLieu.getStyleClass().remove("error");
        tfBud.getStyleClass().remove("error");
        labErr.setText("");
    }

    @Override
    protected Scene provideScene() {
        return tfNom.getScene();
    }

    @FXML
    protected void handleNewLieu() {
        context.controleur().saisirTerrain();
        updateLieu();
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
    protected void onAjoutArtiste() {
        if (cbArtiste.getValue() == null) return;

        Artiste artiste = context.controleur().getGenevent().getArtistes().get(cbArtiste.getValue());
        if (artiste == null) return;

        PopupUtils.openPopup("lier_artiste.fxml", "Lier l'artiste", new LinkController(context, (cout, contrat) -> {
            draftArtistes.put(artiste.id(), new ContratArtiste(artiste, contrat, cout));
            majScrollPanes();
        }));
    }

    @FXML
    protected void onAjoutPresta() {
        if (cbPrestataire.getValue() == null) return;

        Prestataire prestataire = context.controleur().getGenevent().getPrestataires().get(cbPrestataire.getValue());
        if (prestataire == null) return;

        PopupUtils.openPopup("lier_presta.fxml", "Lier le prestataire", new LinkController(context, (cout, contrat) -> {
            draftPrestataires.put(prestataire.id(), new ContratPrestataire(prestataire, contrat, cout));
            majScrollPanes();
        }));
    }

    @FXML
    protected void onResetClick() {
        if (evenement != null) {
            tfNom.setText(Objects.toString(evenement.getNom(), "-"));
            dpDeb.setValue(evenement.getDateDebut());
            dpFin.setValue(evenement.getDateFin());
            tfBud.setText(evenement.getBudget().toString());
            cbLieu.setValue(evenement.getTerrain() != null ? evenement.getTerrain().getNom() : "Aucun");

            draftArtistes.clear();
            draftArtistes.putAll(evenement.getArtiste());
            draftPrestataires.clear();
            draftPrestataires.putAll(evenement.getPrestataires());

            majScrollPanes();
            resetStyle();
        }
    }

    private Terrain currentSelectedTerrain() {
        if (cbLieu.getValue() != null && !cbLieu.getValue().equals("Aucun")) {
            return context.controleur().getGenevent().getTerrains().get(cbLieu.getValue().toLowerCase());
        }

        return null;
    }

    @FXML
    protected void handleModifierClique() {
        if (!verifyField()) return;

        LocalDate dateDeb = dpDeb.getValue();
        LocalDate dateFin = dpFin.getValue();

        Terrain terrain = currentSelectedTerrain();
        if (terrain != null) {
            for (Evenement val : terrain.getEvenements()) {
                if (val != evenement && !val.getDateDebut().isAfter(dateFin) && !val.getDateFin().isBefore(dateDeb)) {
                    context.controleur().getIhm().informerUtilisateur("Le terrain est déjà pris pendant cette période !", false);
                    return;
                }
            }
        }
        for (ContratArtiste ca : draftArtistes.values()) {
            if (!isArtisteDispo(ca.getArtiste(), dateDeb, dateFin)) {
                context.controleur().getIhm().informerUtilisateur("L'artiste " + ca.getArtiste().getNom() + " n'est pas disponible pendant cette période !", false);
                return;
            }
        }
        for (ContratPrestataire cp : draftPrestataires.values()) {
            if (!isPrestataireDispo(cp.getPrestataire(), dateDeb, dateFin)) {
                context.controleur().getIhm().informerUtilisateur("Le prestataire " + cp.getPrestataire().getNom() + " n'est pas disponible pendant cette période !", false);
                return;
            }
        }

        try {
            evenement.setDateDebut(dateDeb);
            evenement.setDateFin(dateFin);
            evenement.setBudget(Integer.parseInt(tfBud.getText()));
            evenement.setTerrain(terrain);

            List<ContratArtiste> toRemoveArtistes = new ArrayList<>();
            for (ContratArtiste ca : evenement.getArtiste().values()) {
                if (!draftArtistes.containsKey(ca.getArtiste().id())) {
                    toRemoveArtistes.add(ca);
                }
            }
            for (ContratArtiste ca : toRemoveArtistes) {
                evenement.supprimeArtisteFromContrat(ca);
            }

            for (ContratArtiste ca : draftArtistes.values()) {
                if (!evenement.getArtiste().containsKey(ca.getArtiste().id())) {
                    evenement.ajoutArtiste(ca.getArtiste(), ca.getCout(), ca.getContrat());
                }
            }

            List<ContratPrestataire> toRemovePrestataires = new ArrayList<>();
            for (ContratPrestataire cp : evenement.getPrestataires().values()) {
                if (!draftPrestataires.containsKey(cp.getPrestataire().id())) {
                    toRemovePrestataires.add(cp);
                }
            }
            for (ContratPrestataire cp : toRemovePrestataires) {
                evenement.supprimePrestataire(cp.getPrestataire());
            }
            for (ContratPrestataire cp : draftPrestataires.values()) {
                if (!evenement.getPrestataires().containsKey(cp.getPrestataire().id())) {
                    evenement.ajoutPrestataire(cp.getPrestataire(), cp.getContrat(), cp.getCout());
                }
            }

            context.controleur().getIhm().informerUtilisateur("L'évènement " + evenement.getNom() + " a bien été modifié !", true);
            context.controleur().getIhm().requestRefresh();

            PopupUtils.openPopupIn("details_event.fxml", tfNom, "Détail de l'évenement", new DetailsEventController(context, evenement));
        } catch (Exception e) {
            e.printStackTrace();
            labErr.setText(e.getMessage());
        }
    }
}
