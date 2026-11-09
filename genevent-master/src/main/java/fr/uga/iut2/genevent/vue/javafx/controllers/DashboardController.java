package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import fr.uga.iut2.genevent.vue.javafx.DashboardPage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author theo
 * @project genevent
 * @date 14/06/2026 16:39
 */
public class DashboardController implements Initializable {
    private final JavaFXContext context;

    private final Map<String, Node> cachedPages = new HashMap<>();

    private DashboardPage currentPage = null;
    @FXML private BorderPane borderPane;
    @FXML private VBox button_container;

    public DashboardController(JavaFXContext context) {
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changePage(DashboardPage.HOME); // Default page
    }

    @FXML
    private void onHomePageSwitch() {
        changePage(DashboardPage.HOME);
    }

    @FXML
    private void onAgendaPageSwitch() {
        changePage(DashboardPage.AGENDA);
    }

    @FXML
    private void onLieuxPageSwitch() {
        changePage(DashboardPage.LIEU);
    }

    @FXML
    private void onArtistePageSwitch() {
        changePage(DashboardPage.ARTISTE);
    }

    @FXML
    private void onPrestatairePageSwitch() {
        changePage(DashboardPage.PRESTATAIRE);
    }

    public void refreshCurrentPage() {
        refreshElement(borderPane.getCenter());
    }

    private void refreshElement(Node element) {
        Object controller = element.getProperties().get("controller");
        if (controller instanceof Refreshable helper) {
            helper.onRefresh();
        }
    }

    private void changePage(DashboardPage page) {
        if (page.equals(currentPage)) return; // Don"t reload the same page

        if (this.cachedPages.containsKey(page.getPageId())) {
            Node node = this.cachedPages.get(page.getPageId());
            borderPane.setCenter(node);

            refreshElement(node);

            currentPage = page;
            updateButtons();
            return;
        }

        try {
            FXMLLoader mainViewLoader = new FXMLLoader(JavaFXGUI.class.getResource(page.getFile()));

            Object controller = page.getControllerFactory().apply(context); // Create the controller from the DashboardPage enum (Function)
            mainViewLoader.setController(controller);

            Node loaded = mainViewLoader.load();
            loaded.getProperties().put("controller", controller); // Save the controller inside node property for refresh

            cachedPages.put(page.getPageId(), loaded);

            borderPane.setCenter(loaded);
            currentPage = page;

            updateButtons();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update active buttons
     */
    private void updateButtons() {
        // Remove all active button
        for (DashboardPage value : DashboardPage.values()) {
            for (Node child : button_container.getChildren()) {
                if (child == null) continue;

                child.getStyleClass().remove("active");
            }
        }

        // Only add the active class for the current page button
        for (DashboardPage value : DashboardPage.values()) {
            for (Node child : button_container.getChildren()) {
                if (child == null) continue;

                if (value.equals(currentPage) && value.getPageId().equals(child.getId())) {
                    child.getStyleClass().add("active");
                }
            }
        }
    }
}
