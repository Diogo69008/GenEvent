package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Prestataire;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import java.util.ArrayList;
import javafx.scene.layout.Priority;
import java.util.Comparator;
import java.util.List;

public class PrestataireController extends PageHelper<Prestataire> {
    private static final Comparator<Prestataire> COMPARATOR = Comparator.comparing(Prestataire::id);

    public PrestataireController(JavaFXContext context) {
        super(context, "Prestataire");
    }

    @Override
    protected Comparator<Prestataire> getComparator() {
        return COMPARATOR;
    }

    @Override
    protected List<Prestataire> getElements() {
        return new ArrayList<>(context.controleur().getGenevent().getPrestataires().values());
    }

    @Override
    protected VBox createCard(Prestataire element) {
        VBox card = new VBox();


        List<Prestataire> elements = getElements();
        if (elements.size() == 1) {
            card.setPrefWidth(400.0);
            card.setMinWidth(400.0);
        } else {
            card.setPrefWidth(280.0);
            card.setMinWidth(280.0);
        }

        card.setSpacing(14.0);
        card.getStyleClass().addAll("stat-card", "card-blue");
        card.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-padding: 24px; " +
                        "-fx-background-radius: 14px; " +
                        "-fx-border-color: #2563EB; " +
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 14px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.1), 12, 0, 0, 4);"
        );


        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(12.0);

        Label iconLabel = new Label("🤝");
        iconLabel.setStyle("-fx-font-size: 32px;");

        VBox textHeader = new VBox();
        textHeader.setSpacing(4.0);
        Label nameLabel = new Label(element.getNom());
        nameLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #111827;");
        Label typeLabel = new Label(element.getType().toString());
        typeLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #6B7280;");
        textHeader.getChildren().addAll(nameLabel, typeLabel);

        header.getChildren().addAll(iconLabel, textHeader);

        if (element.isSponsorise()) {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            header.getChildren().add(spacer);

            Label sponsorBadge = new Label("⭐ Sponsorisé");
            sponsorBadge.setStyle(
                    "-fx-background-color: #FEF3C7; " +
                            "-fx-text-fill: #D97706; " +
                            "-fx-padding: 4px 8px; " +
                            "-fx-background-radius: 99px; " +
                            "-fx-font-family: 'Segoe UI'; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;"
            );
            header.getChildren().add(sponsorBadge);
        }

        VBox infosBlock = new VBox();
        infosBlock.setSpacing(8.0);
        infosBlock.setStyle("-fx-padding: 12px; -fx-background-color: #EFF6FF; -fx-background-radius: 8px;");

        Label mailLabel = new Label("📧 " + element.getMail());
        mailLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #111827; -fx-font-weight: 500;");
        mailLabel.setWrapText(true);

        Label phoneLabel = new Label("📞 " + element.getTelephone());
        phoneLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #2563EB; -fx-font-weight: bold;");

        int nbEvents = element.getEvents().size();
        Label eventsLabel = new Label("📅 " + nbEvents + " événement(s)");
        eventsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 13px; -fx-text-fill: #1D4ED8; -fx-font-weight: 600;");

        infosBlock.getChildren().addAll(mailLabel, phoneLabel, eventsLabel);


        if (element.getInformation() != null && !element.getInformation().isEmpty()) {
            Label infoLabel = new Label("ℹ️ " + element.getInformation());
            infoLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #6B7280;");
            infoLabel.setWrapText(true);
            infosBlock.getChildren().add(infoLabel);
        }


        Button actionBtn = new Button("Voir les détails");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle(
                "-fx-background-color: #2563EB; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.2), 6, 0, 0, 2);"
        );


        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle(
                "-fx-background-color: #1D4ED8; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.3), 8, 0, 0, 3);"
        ));

        actionBtn.setOnMouseExited(e -> actionBtn.setStyle(
                "-fx-background-color: #2563EB; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 10px 16px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(37, 99, 235, 0.2), 6, 0, 0, 2);"
        ));

        actionBtn.setOnAction(e -> onElementClick(element));

        card.getChildren().addAll(header, infosBlock, actionBtn);

        return card;
    }

    @Override
    protected void onElementClick(Prestataire element) {
        context.controleur().afficherPresta(element);
    }

    @FXML
    private void handleAjoutPrestataire() {
        context.controleur().saisirPrestaire();
    }
}