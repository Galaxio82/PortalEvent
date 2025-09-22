package fr.galaxio.portalevent.api;

import fr.galaxio.portalevent.server.events.EventManager;

public class PortalAPI {
    private static final EventManager INSTANCE = new EventManager();

    public static IPortalManager getPortalManager() {
        return INSTANCE;
    }
}

