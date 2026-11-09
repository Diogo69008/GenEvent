package fr.uga.iut2.genevent.util;

import fr.uga.iut2.genevent.vue.javafx.JavaFXGUI;
import fr.uga.iut2.genevent.vue.javafx.controllers.ModifPrestataireController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class PopupUtils {
    /**
     * Ouvre une nouvelle popup
     * @param fxml le fichier fxml
     * @param title le titre
     * @param controller le controlleur
     */
    public static void openPopup(String fxml, String title, Object controller) {
        try {

            FXMLLoader newUserViewLoader = new FXMLLoader(JavaFXGUI.class.getResource(fxml));
            newUserViewLoader.setController(controller);
            Scene newUserScene = new Scene(newUserViewLoader.load());

            Stage newUserWindow = new Stage();
            newUserWindow.setTitle(title);
            newUserWindow.initModality(Modality.APPLICATION_MODAL);

            Window owner = Window.getWindows().stream()
                    .filter(Window::isShowing)
                    .findFirst()
                    .orElse(null);
            if (owner != null) {
                newUserWindow.initOwner(owner);
                if (owner.getScene() != null && owner.getScene().getRoot() != null) {
                    owner.getScene().getRoot().setDisable(true);
                }
                newUserWindow.setOnShown(e -> Platform.runLater(() -> {
                    double x = owner.getX() + (owner.getWidth() - newUserWindow.getWidth()) / 2;
                    double y = owner.getY() + (owner.getHeight() - newUserWindow.getHeight()) / 2;
                    newUserWindow.setX(x);
                    newUserWindow.setY(y);
                }));
            }

            newUserWindow.setOnHidden(e -> Platform.runLater(() -> {
                if (owner != null && owner.getScene() != null && owner.getScene().getRoot() != null) {
                    owner.getScene().getRoot().setDisable(false);
                }
            }));

            newUserWindow.setScene(newUserScene);
            newUserWindow.showAndWait();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Ouvre une popup sur la scène d'un node
     * @param fxml fichier fxml
     * @param node l'élément qui permet de récupèrer la scène
     * @param title titre du popup
     * @param controller controlleur
     */
    public static void openPopupIn(String fxml, Node node, String title, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(JavaFXGUI.class.getResource(fxml));
            loader.setController(controller);

            Stage stage = (Stage) node.getScene().getWindow();
            stage.setTitle(title);
            stage.getScene().setRoot(loader.load());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
