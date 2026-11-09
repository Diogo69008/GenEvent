package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.modele.Evenement;
import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class AgendaController extends PageHelper<Evenement> {
    private static final Comparator<Evenement> COMPARATOR = Comparator.comparing(Evenement::id);

    private static final int[] PREDEFINED_HUES = {210, 340, 45, 145, 280, 15, 190, 95, 310, 115, 255};

    @FXML private GridPane calendarGrid;
    @FXML private Label monthYearLabel;
    @FXML private TextField searchSubDate;

    private YearMonth currentYearMonth;

    public AgendaController(JavaFXContext context) {
        super(context, "Festivals");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentYearMonth = YearMonth.now();
        populateCalendar(currentYearMonth);
        super.initialize(url, resourceBundle);
    }

    @Override
    protected Comparator<Evenement> getComparator() {
        return COMPARATOR;
    }

    @FXML
    private void handleDateSelect() {
        try {
            LocalDate filter = LocalDate.parse(searchSubDate.getText(), DateStringConverter.FORMATTER);
            searchSubDate.getStyleClass().remove("error");

            this.currentYearMonth = YearMonth.from(filter);
            populateCalendar(currentYearMonth);
            updateElements();
        } catch (Exception e) {
            searchSubDate.getStyleClass().add("error");
        }
    }

    private void populateCalendar(YearMonth yearMonth) {
        calendarGrid.getChildren().clear();

        String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        monthYearLabel.setText(monthName + " " + yearMonth.getYear());

        LocalDate firstOfMonth = yearMonth.atDay(1);
        int leadDays = firstOfMonth.getDayOfWeek().getValue() - 1;
        int daysInMonth = yearMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = yearMonth.atDay(day);
            int cellIndex = leadDays + day - 1;
            int row = cellIndex / 7;
            int col = cellIndex % 7;

            VBox cell = new VBox() {
                @Override
                protected double computeMinHeight(double width) {
                    return width >= 0 ? width : super.computeMinHeight(width);
                }

                @Override
                protected double computePrefHeight(double width) {
                    return width >= 0 ? Math.max(width, super.computePrefHeight(width)) : super.computePrefHeight(width);
                }
            };
            cell.getStyleClass().add("calendar-grid-cell");
            cell.setSpacing(6.0);

            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.getStyleClass().add("calendar-day-number");
            cell.getChildren().add(dayLabel);

            for (Evenement evenement : context.controleur().getGenevent().getEvenements().values()) {
                if (!currentDate.isBefore(evenement.getDateDebut()) && !currentDate.isAfter(evenement.getDateFin())) {
                    Button festBtn = new Button(evenement.getNom());
                    festBtn.getStyleClass().add("btn-festival-event");
                    festBtn.setMaxWidth(Double.MAX_VALUE);

                    int hue = getEventHue(evenement);
                    String baseColor = "hsb(" + hue + ", 70%, 65%)";
                    String hoverColor = "hsb(" + hue + ", 75%, 55%)";
                    festBtn.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white;");
                    festBtn.setOnMouseEntered(e -> festBtn.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white;"));
                    festBtn.setOnMouseExited(e -> festBtn.setStyle("-fx-background-color: " + baseColor + "; -fx-text-fill: white;"));

                    if (currentDate.isEqual(evenement.getDateDebut())) {
                        festBtn.getStyleClass().add("festival-start");
                    } else if (currentDate.isEqual(evenement.getDateFin())) {
                        festBtn.getStyleClass().add("festival-end");
                        festBtn.setText("Fin : " + evenement.getNom());
                    } else {
                        festBtn.getStyleClass().add("festival-middle");
                        festBtn.setText("...");
                    }

                    festBtn.setOnAction(_ -> onElementClick(evenement));

                    cell.getChildren().add(festBtn);
                }
            }

            calendarGrid.add(cell, col, row);
        }
    }

    @FXML
    private void handlePreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth);
        updateElements();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth);
        updateElements();
    }

    /**
     * Call on page switch
     */
    @Override
    protected void updateElements() {
        super.updateElements();
        populateCalendar(currentYearMonth);
    }

    @Override
    protected List<Evenement> getElements() {
        if (currentYearMonth == null) {
            currentYearMonth = YearMonth.now();
        }
        LocalDate startOfMonth = currentYearMonth.atDay(1);
        LocalDate endOfMonth = currentYearMonth.atEndOfMonth();

        List<Evenement> list = new ArrayList<>();
        for (Evenement evenement : context.controleur().getGenevent().getEvenements().values()) {
            if (!evenement.getDateFin().isBefore(startOfMonth) && !evenement.getDateDebut().isAfter(endOfMonth)) {
                list.add(evenement);
            }
        }
        return list;
    }

    @Override
    protected VBox createCard(Evenement element) {
        VBox card = new VBox();
        card.setPrefWidth(230.0);
        card.setMinWidth(230.0);
        card.setSpacing(14.0);

        int hue = getEventHue(element);
        card.getStyleClass().add("stat-card");
        card.setStyle("-fx-background-color: #ffffff; " +
                     "-fx-padding: 20px; " +
                     "-fx-background-radius: 14px; " +
                     "-fx-border-color: hsb(" + hue + ", 70%, 65%); " +
                     "-fx-border-width: 2px; " +
                     "-fx-border-radius: 14px; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.05), 12, 0, 0, 4);");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(10.0);

        Label iconLabel = new Label("🎪");
        iconLabel.setStyle("-fx-font-size: 26px;");

        VBox textHeader = new VBox();
        Label nameLabel = new Label(element.getNom());
        nameLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #3D001E;");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.FRANCE);
        String dateStr = element.getDateDebut().format(formatter) + " au " + element.getDateFin().format(formatter);
        Label dateLabel = new Label(dateStr);
        dateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 11px; -fx-text-fill: #BA9A8A;");
        textHeader.getChildren().addAll(nameLabel, dateLabel);

        header.getChildren().addAll(iconLabel, textHeader);

        VBox infosBlock = new VBox();
        infosBlock.setSpacing(5.0);

        String lieuNom = element.getTerrain() != null ? element.getTerrain().getNom() : "Non défini";
        Label lieuLabel = new Label("📍 Lieu : " + lieuNom);
        lieuLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #555555;");

        Label budgetLabel = new Label("💵 Budget : " + element.getBudget() + " €");
        budgetLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #555555;");

        Label billetLabel = new Label("🎫 Billet : " + (element.getBillet() == null ? "Non défini" : element.getBillet()) + " €/jour");
        billetLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #00BA63; -fx-font-weight: bold;");

        infosBlock.getChildren().addAll(lieuLabel, budgetLabel, billetLabel);

        Button actionBtn = new Button("Voir les détails");
        actionBtn.setMaxWidth(Double.MAX_VALUE);
        actionBtn.setStyle("-fx-background-color: #FFF0F6; -fx-text-fill: #FF007F; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 8px 0;");
        actionBtn.setOnAction(e -> onElementClick(element));

        Button removeBtn = new Button("Supprimer l'événement");
        removeBtn.setMaxWidth(Double.MAX_VALUE);
        removeBtn.setStyle("-fx-background-color: #FFF0F6; -fx-text-fill: #FF007F; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-cursor: hand; -fx-padding: 8px 0;");
        removeBtn.setOnAction(e -> onEventDelete(element));

        card.getChildren().addAll(header, infosBlock, actionBtn, removeBtn);
        return card;
    }

    @Override
    protected void onElementClick(Evenement element) {
        context.controleur().afficherEvent(element);
    }

    private void onEventDelete(Evenement evenement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de l'événement");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet événement ?");
        alert.setContentText("La suppression de cet élément est irréversible.");

        Optional<ButtonType> buttonType = alert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            context.controleur().supprimerEvent(evenement);
        }
    }

    private int getEventHue(Evenement evenement) {
        List<Evenement> allEvents = new ArrayList<>(context.controleur().getGenevent().getEvenements().values());
        allEvents.sort(Comparator.comparing(Evenement::getNom));
        int index = allEvents.indexOf(evenement);
        if (index < 0) {
            index = Math.abs(evenement.getNom().hashCode());
        }
        return PREDEFINED_HUES[index % PREDEFINED_HUES.length];
    }
}
