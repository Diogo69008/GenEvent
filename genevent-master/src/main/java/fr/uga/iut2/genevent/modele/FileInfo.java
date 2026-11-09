package fr.uga.iut2.genevent.modele;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Structure enregistré pour garder les fichires (contrat) en mémoire
 */
public class FileInfo implements Serializable {
    private final String name;
    private final byte[] data;

    /**
     * Récupère les
     * @param file
     */
    public FileInfo(File file) {
        Objects.requireNonNull(file);

        this.name = file.getName();
        try {
            this.data = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }
}
