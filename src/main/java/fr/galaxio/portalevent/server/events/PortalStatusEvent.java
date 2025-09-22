package fr.galaxio.portalevent.server.events;

import fr.galaxio.portalevent.utils.PortalType;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.Event;

public class PortalStatusEvent extends Event {
    private final PortalType portal;
    private final boolean open;
    private final MinecraftServer server;

    public PortalStatusEvent(PortalType portal, boolean open, MinecraftServer server) {
        this.portal = portal;
        this.open = open;
        this.server = server;
    }

    public PortalType getPortal() {
        return portal;
    }

    public boolean isOpen() {
        return open;
    }

    public MinecraftServer getServer() {
        return server;
    }
}

