package fr.galaxio.portalevent.api;

import fr.galaxio.portalevent.utils.OpenReason;
import fr.galaxio.portalevent.utils.PortalType;

import java.util.Optional;

public interface IPortalManager {

    /**
     * Vérifie si un portail est ouvert
     */
    boolean isOpen(PortalType portal);

    /**
     * Raison actuelle de l'ouverture/fermeture
     */
    OpenReason getReason(PortalType portal);

    /**
     * Temps prévu de fermeture pour les CUSTOM_EVENT
     */
    Optional<Long> getCloseTime(PortalType portal);

    /**
     * Temps restant avant ouverture/fermeture (ms)
     */
    long getTimeRemaining(PortalType portal);
}

