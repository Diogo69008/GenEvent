package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Artiste;
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

public class ArtisteController extends PageHelper<Artiste> {
    private static final Comparator<Artiste> COMPARATOR = Comparator.comparing(Artiste::id);

    public ArtisteController(JavaFXContext context) {
        super(context, "Artiste");
    }

    @FXML
    private void handleAjoutArtiste() {
        context.controleur().saisirArtiste();
    }

    @Override
    protected List<Artiste> getElements() {
        return new ArrayList<>(context.controleur().getGenevent().getArtistes().values());
    }

    @Override
    protected Comparator<Artiste> getComparator() {
        return COMPARATOR;
    }

    @Override
    protected VBox createCard(Artiste element) {
        VBox card = new VBox();

        List<Artiste> elements = getElements();
        if (elements.size() == 1) {
            card.setPrefWidth(400.0);
            card.setMinWidth(400.0);
        } else {
            card.setPrefWidth(280.0);
            card.setMinWidth(280.0);
        }

        card.setSpacing(14.0);
        card.getStyleClass().addAll("stat-card", "card-yellow");
        card.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-padding: 24px; " +
                        "-fx-background-radius: 14px; " +
                        "-fx-border-color: #F59E0B; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 14px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(245, 158, 11, 0.1), 12, 0, 0, 4);"
        );


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12.0);

        Label iconLabel = new Label("🎤");
        iconLabel.setStyle("-fx-font-size: 32px;");

        VBox textHeader = new VBox();
        textHeader.setSpacing(4.0);
        Label sceneNameLabel = new Label(element.getNomDeScene());
        sceneNameLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #111827;");
        Label fullNameLabel = new Label(element.getPrenom() + " " + element.getNom());
        fullNameLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #6B7280;");
        textHeader.getChildren().addAll(sceneNameLabel, fullNameLabel);

        header.getChildren().addAll(iconLabel, textHeader);


        VBox infosBlock = new VBox();
        infosBlock.setSpacing(8.0);
        infosBlock.setStyle("-fx-padding: 12px; -fx-background-color: #FFFBEB; -fx-background-radius: 8px;");

        Label ageLabel = new Label("🎂 Âge : " + element.getAge() + " ans");
        ageLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #111827; -fx-font-weight: 500;");

        int nbEvents = element.getEvents().size();
        Label eventsLabel = new Label("📅 " + nbEvents + " événement(s) programmé(s)");
        eventsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #D97706; -fx-font-weight: bold;");

        infosBlock.getChildren().addAll(ageLabel, eventsLabel);


        if (element.getTelephone() != null && !element.getTelephone().isEmpty()) {
            Label phoneLabel = new Label("📞 " + element.getTelephone());
            phoneLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #6B7280;");
            infosBlock.getChildren().add(phoneLabel);
        }


        Button actionBtn = new Button("Voir les détails");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle(
                "-fx-background-color: #F59E0B; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(245, 158, 11, 0.2), 6, 0, 0, 2);"
        );


        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle(
                "-fx-background-color: #D97706; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(245, 158, 11, 0.3), 8, 0, 0, 3);"
        ));

        actionBtn.setOnMouseExited(e -> actionBtn.setStyle(
                "-fx-background-color: #F59E0B; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(245, 158, 11, 0.2), 6, 0, 0, 2);"
        ));

        actionBtn.setOnAction(e -> onElementClick(element));

        card.getChildren().addAll(header, infosBlock, actionBtn);
        return card;
    }

    @Override
    protected void onElementClick(Artiste element) {
        context.controleur().afficherArtiste(element);
    }
}