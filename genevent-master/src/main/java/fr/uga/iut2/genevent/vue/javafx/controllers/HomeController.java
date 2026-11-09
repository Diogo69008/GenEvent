package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Evenement;
import fr.uga.iut2.genevent.util.PopupUtils;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public class HomeController implements Refreshable {
    private final JavaFXContext context;

    @FXML private VBox emptyEventBox;
    @FXML private VBox eventListBox;
    @FXML private VBox eventListContainer;


    @FXML private Label nextEventLabel;

    @FXML private Label cardArtistValue;
    @FXML private Label cardArtistFooter;
    @FXML private Label cardVenueValue;
    @FXML private Label cardVenueFooter;
    @FXML private Label cardBudgetValue;
    @FXML private Label cardBudgetFooter;
    @FXML private Label cardBenefitValue;
    @FXML private Label cardBenefitFooter;

    private Evenement selectedEvent = null;

    public HomeController(JavaFXContext context) {
        this.context = context;
    }

    @FXML
    private void initialize() {
        updateEventList();
    }

    @FXML
    private void handleAjoutEvent() {
        context.controleur().saisirEvenement();
        updateEventList();
    }

    public void updateEventList() {
        Collection<Evenement> events = context.controleur().getGenevent().getEvenements().values();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRANCE);

        if (events.isEmpty()) {
            emptyEventBox.setVisible(true);
            emptyEventBox.setManaged(true);
            eventListBox.setVisible(false);
            eventListBox.setManaged(false);


            nextEventLabel.setText("Aucun évènement programmé pour le moment.");
            resetIndicators();
        } else {
            emptyEventBox.setVisible(false);
            emptyEventBox.setManaged(false);
            eventListBox.setVisible(true);
            eventListBox.setManaged(true);


            Evenement prochainEvent = events.stream()
                    .min((e1, e2) -> e1.getDateDebut().compareTo(e2.getDateDebut()))
                    .orElse(null);

            if (prochainEvent != null) {
                String dateStr = prochainEvent.getDateDebut().format(formatter);
                nextEventLabel.setText(prochainEvent.getNom() + " — Débute le " + dateStr);
            }

            eventListContainer.getChildren().clear();
            for (Evenement event : events) {
                eventListContainer.getChildren().add(createEventButton(event));
            }

            if (!events.isEmpty()) {
                selectEvent(events.iterator().next());
            }
        }
    }

    private Node createEventButton(Evenement event) {
        StackPane container = new StackPane();

        HBox button = new HBox();
        button.setAlignment(Pos.CENTER_LEFT);
        button.setSpacing(14.0);
        button.setPrefHeight(64.0);
        button.setUserData(event);

        button.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-border-color: #EBF0F5; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 10px; " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 12px 18px; " +
                        "-fx-cursor: hand;"
        );

        Label iconLabel = new Label("🎪");
        iconLabel.setStyle("-fx-font-size: 22px;");

        VBox textContent = new VBox();
        textContent.setSpacing(3.0);

        Label nameLabel = new Label(event.getNom());
        nameLabel.setStyle("-fx-font-family: 'SF Pro Display', 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1D1D1F;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRANCE);
        String dateStr = event.getDateDebut().format(formatter) + "  →  " + event.getDateFin().format(formatter);
        Label dateLabel = new Label(dateStr);
        dateLabel.setStyle("-fx-font-family: 'SF Pro Text', 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #86868B;");

        textContent.getChildren().addAll(nameLabel, dateLabel);
        button.getChildren().addAll(iconLabel, textContent);

        Button closeButton = new Button("✕");
        closeButton.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: #FF3B30; " +
                        "-fx-font-size: 12px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 4px;"
        );

        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new javafx.geometry.Insets(4, 4, 0, 0));

        closeButton.setOnAction(e -> {
            e.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Suppression de l'évènement");
            alert.setHeaderText("Voulez-vous vraiment supprimer cet évènement ?");
            alert.setContentText("La suppression de cet élément est irréversible.");
            Optional<ButtonType> buttonType = alert.showAndWait();
            if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
                context.controleur().supprimerEvent(event);
            }
        });

        container.getChildren().addAll(button, closeButton);

        button.setOnMouseClicked(e -> {
            if (selectedEvent == null || !selectedEvent.equals(event)) {
                button.setStyle(
                        "-fx-background-color: #F5F5F7; " +
                                "-fx-border-color: #D2D2D7; " +
                                "-fx-border-width: 1px; " +
                                "-fx-border-radius: 10px; " +
                                "-fx-background-radius: 10px; " +
                                "-fx-padding: 12px 18px; " +
                                "-fx-cursor: hand;"
                );
            }
        });

        button.setOnMouseEntered(e -> {
            selectedEvent = event;
            updateButtonStyle(button, event);});
        button.setOnMouseExited(e -> {
            selectedEvent =null;
            updateButtonStyle(button,event);
        });

        button.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                DetailsEventController d = new DetailsEventController(context,event);
                PopupUtils.openPopup("details_event.fxml","Detail de l'évènement",d);
            }
        });

        return container;
    }

    private void updateButtonStyle(HBox button, Evenement event) {
        if (selectedEvent != null && selectedEvent.equals(event)) {
            button.setStyle(
                    "-fx-background-color: #F2F7FF; " +
                            "-fx-border-color: #0066CC; " +
                            "-fx-border-width: 1.5px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-padding: 12px 18px; " +
                            "-fx-cursor: hand;"
            );
        } else {
            button.setStyle(
                    "-fx-background-color: #FFFFFF; " +
                            "-fx-border-color: #EBF0F5; " +
                            "-fx-border-width: 1px; " +
                            "-fx-border-radius: 10px; " +
                            "-fx-background-radius: 10px; " +
                            "-fx-padding: 12px 18px; " +
                            "-fx-cursor: hand;"
            );
        }
    }

    private void selectEvent(Evenement event) {
        this.selectedEvent = event;

        for (var node : eventListContainer.getChildren()) {
            if (node instanceof HBox button) {
                Evenement nodeEvent = (Evenement) button.getUserData();
                if (nodeEvent != null) {
                    updateButtonStyle(button, nodeEvent);
                }
            }
        }
        updateIndicators(event);
    }

    private void updateIndicators(Evenement event) {

        int artistCount = event.getArtiste() != null ? event.getArtiste().size() : 0;
        cardArtistValue.setText(String.valueOf(artistCount));
        cardArtistFooter.setText(artistCount == 0 ? "Aucun artiste confirmé" : artistCount + " artiste(s) programmé(s)");


        String venueName = event.getTerrain() != null ? event.getTerrain().getNom() : "—";
        cardVenueValue.setText(venueName);
        cardVenueFooter.setText(event.getTerrain() != null ? "Infrastructure définie" : "Aucune infrastructure définie");


        String budgetStr = event.getBudget() > 0 ? event.getBudget() + " €" : "—";
        cardBudgetValue.setText(budgetStr);
        cardBudgetFooter.setText(event.getBudget() > 0 ? "Budget alloué" : "Aucun festival actif");


        if (event.getBudget() != null && event.getBudget() > 0 && event.getBillet() != null) {

            double budget = event.getBudget().doubleValue();
            double prixBillet = event.getBillet().doubleValue();


            double benefices = event.calculerBenefices(prixBillet, budget);


            cardBenefitValue.setText(String.format(Locale.FRANCE, "%,.2f €", benefices));


            if (benefices >= 0) {
                cardBenefitFooter.setText("Bénéfice théorique estimé");
            } else {
                cardBenefitFooter.setText("Déficit théorique projeté");
            }
        } else {
            cardBenefitValue.setText("—");
            cardBenefitFooter.setText("Prix billet ou budget manquant");
        }
    }

    private void resetIndicators() {
        cardArtistValue.setText("—");
        cardArtistFooter.setText("Aucun artiste confirmé");
        cardVenueValue.setText("—");
        cardVenueFooter.setText("Aucune infrastructure définie");
        cardBudgetValue.setText("—");
        cardBudgetFooter.setText("Aucun festival actif");

        cardBenefitValue.setText("—");
        cardBenefitFooter.setText("Aucun bénéfice estimé");

        selectedEvent = null;
    }

    @Override
    public void onRefresh() {
        updateEventList();
    }
}