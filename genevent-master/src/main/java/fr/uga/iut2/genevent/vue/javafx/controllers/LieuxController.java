package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Terrain;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LieuxController extends PageHelper<Terrain> {
    private static final Comparator<Terrain> COMPARATOR = Comparator.comparing(Terrain::id);

    public LieuxController(JavaFXContext context) {
        super(context, "Lieux");
    }

    @Override
    protected List<Terrain> getElements() {
        return new ArrayList<>(context.controleur().getGenevent().getTerrains().values());
    }

    @Override
    protected Comparator<Terrain> getComparator() {
        return COMPARATOR;
    }

    @Override
    protected VBox createCard(Terrain element) {
        VBox card = new VBox();

        List<Terrain> elements = getElements();
        if (elements.size() == 1) {
            card.setPrefWidth(400.0);
            card.setMinWidth(400.0);
        } else {
            card.setPrefWidth(280.0);
            card.setMinWidth(280.0);
        }

        card.setSpacing(14.0);
        card.getStyleClass().addAll("stat-card", "card-green");
        card.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-padding: 24px; " +
                        "-fx-background-radius: 14px; " +
                        "-fx-border-color: #10B981; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 14px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(16, 185, 129, 0.1), 12, 0, 0, 4);"
        );


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12.0);

        Label iconLabel = new Label("📍");
        iconLabel.setStyle("-fx-font-size: 32px;");

        VBox textHeader = new VBox();
        textHeader.setSpacing(4.0);
        Label nameLabel = new Label(element.getNom());
        nameLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #111827;");
        Label addressLabel = new Label(element.getAdresse());
        addressLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #6B7280;");
        textHeader.getChildren().addAll(nameLabel, addressLabel);

        header.getChildren().addAll(iconLabel, textHeader);


        VBox infosBlock = new VBox();
        infosBlock.setSpacing(8.0);
        infosBlock.setStyle("-fx-padding: 12px; -fx-background-color: #F0FDF4; -fx-background-radius: 8px;");

        Label surfaceLabel = new Label("📐 Surface : " + String.format("%.2f", element.getSurface() / 10000) + " hectares");
        surfaceLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #111827; -fx-font-weight: 500;");

        Label capaciteLabel = new Label("👥 Capacité : " + element.getCapacite() + " personnes");
        capaciteLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #059669; -fx-font-weight: bold;");

        int nbEvents = element.getEvenements() != null ? element.getEvenements().size() : 0;
        Label eventsLabel = new Label("📅 " + nbEvents + " événement(s)");
        eventsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #10B981; -fx-font-weight: 600;");

        infosBlock.getChildren().addAll(surfaceLabel, capaciteLabel, eventsLabel);


        if (element.getDescription() != null && !element.getDescription().isEmpty()) {
            Label descLabel = new Label(element.getDescription());
            descLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-wrap-text: true;");
            descLabel.setWrapText(true);
            infosBlock.getChildren().add(descLabel);
        }


        Button actionBtn = new Button("Voir les détails");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle(
                "-fx-background-color: #10B981; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(16, 185, 129, 0.2), 6, 0, 0, 2);"
        );


        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle(
                "-fx-background-color: #059669; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(16, 185, 129, 0.3), 8, 0, 0, 3);"
        ));

        actionBtn.setOnMouseExited(e -> actionBtn.setStyle(
                "-fx-background-color: #10B981; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(16, 185, 129, 0.2), 6, 0, 0, 2);"
        ));

        actionBtn.setOnAction(e -> onElementClick(element));

        card.getChildren().addAll(header, infosBlock, actionBtn);
        return card;
    }

    @Override
    protected void onElementClick(Terrain element) {
        context.controleur().afficherLieu(element);
    }

    @FXML
    public void initialize() {
        updateElements();
    }

    @FXML
    private void ajouterlieu() {
        context.controleur().saisirTerrain();
    }
}