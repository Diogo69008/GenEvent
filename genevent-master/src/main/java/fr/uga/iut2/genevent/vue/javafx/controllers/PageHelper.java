package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The class who manage the "carrouselle"
 * @param <T> The type who are paginated
 */
public abstract class PageHelper<T> implements Initializable, Refreshable {
    protected final JavaFXContext context;

    @FXML protected ScrollPane carouselScrollPane;
    @FXML protected HBox cardContainer;
    @FXML protected VBox emptyStateBox;
    @FXML protected VBox carouselContainer;
    @FXML protected Label titleLabel;

    @FXML protected Label dot1;
    @FXML protected Label dot2;
    @FXML protected Label dot3;
    @FXML protected Label dot4;

    private final String title;

    public PageHelper(JavaFXContext context, String title) {
        this.context = context;
        this.title = title;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateElements();
    }

    /**
     * Les éléments trié en fonction de getComparator
     * @return éléments trié
     */
    private List<T> getSortedElements() {
        List<T> elements = getElements();

        Comparator<T> comparator = getComparator();
        if (comparator != null) elements.sort(comparator);

        return elements;
    }

    /**
     * Comparateur qui trie les éléments de la page
     * @return le comparateur
     */
    protected Comparator<T> getComparator() {
        return null;
    }

    /**
     * Provide all the possible items for this abstract classe
     * @return The list of elements
     */
    protected abstract List<T> getElements();

    /**
     * Create the showed card
     * @param element The elemehent of the card
     * @return The VBox of the card
     */
    protected abstract VBox createCard(T element);

    /**
     *
     * @param element
     */
    protected abstract void onElementClick(T element);

    /**
     * Refresh les éléments quand on change de page / le demande
     */
    @Override
    public void onRefresh() {
        updateElements();
    }

    /**
     * Refresh all the elements of the page
     */
    protected void updateElements() {
        List<T> elements = getSortedElements();
        if (elements.isEmpty()) {
            emptyStateBox.setVisible(true);
            emptyStateBox.setManaged(true);
            carouselContainer.setVisible(false);
            carouselContainer.setManaged(false);
            titleLabel.setText(title + " (0)");
        } else {
            emptyStateBox.setVisible(false);
            emptyStateBox.setManaged(false);
            carouselContainer.setVisible(true);
            carouselContainer.setManaged(true);
            titleLabel.setText(title + " (" + elements.size() + " actifs)");

            cardContainer.getChildren().clear();


            if (elements.size() == 1) {
                cardContainer.setSpacing(0);
                cardContainer.setStyle("-fx-padding: 0;");
            } else {
                cardContainer.setSpacing(20.0);
                cardContainer.setStyle("-fx-padding: 10px 5px;");
            }

            for (T t : elements) {
                VBox card = createCard(t);
                cardContainer.getChildren().add(card);
            }


            updateCarouselVisibility(elements.size());
        }

        if (carouselScrollPane != null) {
            carouselScrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> {
                updatePaginationDots(newVal.doubleValue());
            });
            updatePaginationDots(carouselScrollPane.getHvalue());
        }
    }


    private void updateCarouselVisibility(int elementCount) {
        boolean showControls = elementCount > 1;


        carouselContainer.getChildren().forEach(node -> {
            if (node instanceof HBox hbox) {
                hbox.getChildren().forEach(child -> {
                    if (child.getId() != null && (child.getId().contains("Prev") || child.getId().contains("Next"))) {
                        child.setVisible(showControls);
                        child.setManaged(showControls);
                    }
                });
            }
        });

        if (dot1 != null) dot1.setVisible(showControls);
        if (dot1 != null) dot1.setManaged(showControls);
        if (dot2 != null) dot2.setVisible(showControls);
        if (dot2 != null) dot2.setManaged(showControls);
        if (dot3 != null) dot3.setVisible(showControls);
        if (dot3 != null) dot3.setManaged(showControls);
        if (dot4 != null) dot4.setVisible(showControls);
        if (dot4 != null) dot4.setManaged(showControls);
    }

    /**
     * Update the dots opacity
     * @param hvalue
     */
    private void updatePaginationDots(double hvalue) {
        if (dot1 == null || dot2 == null || dot3 == null || dot4 == null) return;

        dot1.setOpacity(0.30);
        dot2.setOpacity(0.30);
        dot3.setOpacity(0.30);
        dot4.setOpacity(0.30);

        switch (hvalue) {
            case double v when v < 0.20 -> dot1.setOpacity(1.0);
            case double v when v >= 0.20 && v < 0.55 -> dot2.setOpacity(1.0);
            case double v when v >= 0.55 && v < 0.85 -> dot3.setOpacity(1.0);
            default -> dot4.setOpacity(1.0);
        }
    }

    @FXML
    private void handleCarouselNext() {
        if (carouselScrollPane != null) {
            double currentHvalue = carouselScrollPane.getHvalue();
            carouselScrollPane.setHvalue(Math.min(currentHvalue + 0.35, 1.0));
        }
    }

    @FXML
    private void handleCarouselPrev() {
        if (carouselScrollPane != null) {
            double currentHvalue = carouselScrollPane.getHvalue();
            carouselScrollPane.setHvalue(Math.max(currentHvalue - 0.35, 0.0));
        }
    }
}