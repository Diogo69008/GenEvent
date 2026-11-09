package fr.uga.iut2.genevent.vue.javafx;

import fr.uga.iut2.genevent.vue.javafx.controllers.*;

import java.util.function.Function;

/**
 * @author theo
 * @project genevent
 * @date 14/06/2026 16:46
 *
 * Liste des pages avec leurs fxml et le controller
 */
public enum DashboardPage {
    HOME("dashboard_home.fxml", HomeController::new),
    AGENDA( "dashboard_agenda.fxml", AgendaController::new),
    LIEU( "dashboard_lieu.fxml", LieuxController::new),
    ARTISTE( "dashboard_artiste.fxml", ArtisteController::new),
    PRESTATAIRE( "dashboard_prestataire.fxml", PrestataireController::new),
    ;

    private final String pageId, file;
    private final Function<JavaFXContext, Object> controllerFactory;

    DashboardPage(String file, Function<JavaFXContext, Object> controllerFactory) {
        this.pageId = "dashboard_" + name().toLowerCase();
        this.file = file;
        this.controllerFactory = controllerFactory;
    }

    public String getPageId() {
        return pageId;
    }

    public String getFile() {
        return file;
    }

    public Function<JavaFXContext, Object> getControllerFactory() {
        return controllerFactory;
    }
}
