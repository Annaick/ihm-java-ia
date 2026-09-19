package com.horizonimmo.util;

/**
 * Parsing tolerant pour les nombres venus de l'agent vocal, qui envoie
 * parfois un entier sous forme decimale (ex. "70000.0" au lieu de
 * "70000"). Le binding strict de Spring sur un champ Integer rejette
 * "70000.0" avec une 400 ; on parse ici en double puis on arrondit, plutot
 * que de faire echouer toute la requete.
 */
public final class NumberUtils {

    private NumberUtils() {
    }

    public static Integer parseLooseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return (int) Math.round(Double.parseDouble(value.trim()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
