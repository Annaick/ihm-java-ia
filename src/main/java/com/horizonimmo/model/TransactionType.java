package com.horizonimmo.model;

public enum TransactionType {
    VENTE,
    LOCATION;

    /**
     * Parsing tolerant pour les valeurs venues de l'agent vocal (qui peut
     * envoyer une casse ou un mot different de l'enum strict, ex. "location",
     * "louer", "achat"). Renvoie null si rien ne correspond, plutot que de
     * faire echouer toute la requete avec une 400.
     */
    public static TransactionType parseLoose(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return switch (normalized) {
            case "VENTE", "ACHAT", "ACHETER", "VENDRE", "SALE", "BUY" -> VENTE;
            case "LOCATION", "LOUER", "LOCATIF", "RENT", "RENTAL" -> LOCATION;
            default -> {
                try {
                    yield TransactionType.valueOf(normalized);
                } catch (IllegalArgumentException e) {
                    yield null;
                }
            }
        };
    }
}
