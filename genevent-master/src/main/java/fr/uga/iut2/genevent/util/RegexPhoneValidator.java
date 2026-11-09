package fr.uga.iut2.genevent.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author theo
 * @project genevent
 * @date 16/06/2026 20:53
 */
public class RegexPhoneValidator {
    private static final String PHONE_REGEX = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}$";
    private static final Pattern PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * Utilise le regex pour vérifier le numéro
     * Exemple de numéro vélide
     * - 0612345678
     * - +33 7 12 34 56 78
     * - 01-23-45-67-89
     *
     * @param telephone numéro à tester
     * @return true si le numéro est valide
     */
    public static boolean validerNumero(String telephone) {
        if (telephone == null) return false;
        Matcher matcher = PATTERN.matcher(telephone);
        return matcher.matches();
    }
}
