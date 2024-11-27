package com.Paf_WiSe_24_25_GrpD.GlobalCityQuest.config;

/**
 * Status für den Authentifizierungsprozess.
 */
public enum AuthStatus {
    SUCCESS,                 // Authentifizierung erfolgreich
    INVALID_CREDENTIALS,     // Ungültige Zugangsdaten
    USER_NOT_FOUND,           // Benutzername existiert nicht
    UNAUTHORIZED
}
