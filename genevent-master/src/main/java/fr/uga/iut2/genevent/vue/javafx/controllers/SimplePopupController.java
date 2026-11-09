package fr.uga.iut2.genevent.vue.javafx.controllers;

import fr.uga.iut2.genevent.vue.javafx.JavaFXContext;
import javafx.fxml.FXML;
import javafx.scene.Scene;

import java.awt.*;

/**
 * @author theo
 * @project genevent
 * @date 16/06/2026 12:38
 */

/**
 * Used to manage the simple windows with back item
 */
public abstract class SimplePopupController {
    protected final JavaFXContext context;

    public SimplePopupController(JavaFXContext context) {
        this.context = context;
    }

    /**
     * Action defined by the parent
     */
    @FXML
    protected abstract void onBackClick();

    /**
     * Method call to verify all the field validity
     * @return boolean deffined by onVerifyFields
     */
    @FXML
    final boolean verifyField() {
        resetStyle();
        return onVerifyFields();
    }

    /**
     * Where all the checks of the fields are do
     * @return success or not
     */
    protected abstract boolean onVerifyFields();

    /**
     * Method to reset all the style of the boxes
     */
    protected abstract void resetStyle();

    /**
     * Method to provide the Scene to this class, used for disableWindow
     * @return the current scene
     */
    protected abstract Scene provideScene();

    /**
     * Permet de fermer la scène donné par provideScene
     */
    protected void disableWindow() {
        provideScene().getWindow().hide();
    }
}
