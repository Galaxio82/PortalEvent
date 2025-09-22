package fr.galaxio.portalevent.server.events;

import fr.galaxio.portalevent.Config;
import fr.galaxio.portalevent.api.IPortalManager;
import fr.galaxio.portalevent.utils.MessageUtil;
import fr.galaxio.portalevent.utils.OpenReason;
import fr.galaxio.portalevent.utils.PortalType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;

import java.util.*;

import static fr.galaxio.portalevent.utils.CheckTime.*;

public class EventManager implements IPortalManager {

    private static final Map<PortalType, Long> portalCloseTime = new HashMap<>();
    private static final Map<PortalType, Boolean> portalStatus = new HashMap<>();
    private static final Map<PortalType, OpenReason> portalOpenReason = new HashMap<>();
    private static final Map<PortalType, Integer> lastAlertMinutes = new HashMap<>();

    private static final Random RANDOM = new Random();

    public static void setOpen(PortalType portal, boolean open, MinecraftServer server, OpenReason reason) {
        boolean oldStatus = portalStatus.getOrDefault(portal, false);

        if (open) {
            portalStatus.put(portal, true);
            portalOpenReason.put(portal, reason);

            if (reason == OpenReason.CUSTOM_EVENT && !oldStatus) {
                portalCloseTime.put(portal, System.currentTimeMillis() + 30 * 60 * 1000L);
            } else if (reason != OpenReason.CUSTOM_EVENT) {
                portalCloseTime.remove(portal);
            }
        } else {
            portalStatus.remove(portal);
            portalOpenReason.remove(portal);
            portalCloseTime.remove(portal);
        }

        if (oldStatus != open) {
            PortalStatusEvent event = new PortalStatusEvent(portal, open, server);
            NeoForge.EVENT_BUS.post(event);
        }

        // --- LOG ---
        System.out.println("[PortalManager] " + portal.name() +
                " -> " + (open ? "OPEN" : "CLOSED") +
                " | REASON: " + reason.name() +
                (reason == OpenReason.CUSTOM_EVENT && open
                        ? " | Scheduled closure at: " + portalCloseTime.get(portal)
                        : ""));
    }

    @Override
    public boolean isOpen(PortalType portal) {
        return portalStatus.getOrDefault(portal, false);
    }

    @Override
    public OpenReason getReason(PortalType portal) {
        return portalOpenReason.getOrDefault(portal, OpenReason.TIME);
    }

    @Override
    public long getTimeRemaining(PortalType portal) {
        long now = System.currentTimeMillis();

        boolean open = isOpen(portal);
        OpenReason reason = getReason(portal);

        if (open && reason == OpenReason.CUSTOM_EVENT) {
            return portalCloseTime.getOrDefault(portal, now) - now;
        }
        else if (reason == OpenReason.TIME) {
            if (!Config.OPEN_PORTAL_TIME.get()) return 0;

            if (open) return getNextCloseTimestamp(portal) - now;
            else return getNextOpenTimestamp(portal) - now;
        }
        return 0;
    }

    public static long getTimeRemainingStatic(PortalType portal) {
        long now = System.currentTimeMillis();

        boolean open = portalStatus.getOrDefault(portal, false);
        OpenReason reason = portalOpenReason.getOrDefault(portal, OpenReason.TIME);

        if (open && reason == OpenReason.CUSTOM_EVENT) {
            return portalCloseTime.getOrDefault(portal, now) - now;
        }
        else if (reason == OpenReason.TIME) {
            if (!Config.OPEN_PORTAL_TIME.get()) return 0;

            if (open) return getNextCloseTimestamp(portal) - now;
            else return getNextOpenTimestamp(portal) - now;
        }
        return 0;
    }

    public static boolean randomOpen(double chance) {
        return RANDOM.nextDouble() < chance;
    }

    public static void teleportToOverworld(ServerPlayer player) {
        ServerLevel overworld = Objects.requireNonNull(player.getServer()).overworld();

        Vec3 targetPos = new Vec3(
                Config.OVERWORLD_TP_X.get(),
                Config.OVERWORLD_TP_Y.get(),
                Config.OVERWORLD_TP_Z.get()
        );

        TeleportTransition transition = new TeleportTransition(
                overworld,
                targetPos,
                Vec3.ZERO,
                player.getYRot(),
                player.getXRot(),
                TeleportTransition.DO_NOTHING
        );

        player.teleport(transition);
        MessageUtil.sendActionBar(player, "message.portalevent.dimension_closed");
    }

    public static void checkAutomaticClosures(MinecraftServer server) {
        long now = System.currentTimeMillis();

        for (PortalType portal : PortalType.values()) {
            boolean isOpen = portalStatus.getOrDefault(portal, false);
            OpenReason reason = portalOpenReason.getOrDefault(portal, OpenReason.TIME);

            long remainingMs = getTimeRemainingStatic(portal);

            if (reason == OpenReason.TIME && !Config.OPEN_PORTAL_TIME.get()) {
                remainingMs = 0;
            }

            String action = isOpen ? "closure" : "opening";
            System.out.println("[PortalManager] " + portal.name() +
                    " - Time remaining before " + action + ": " + formatRemaining(remainingMs));

            if (remainingMs > 0) {
                long targetTime = now + remainingMs;
                sendCountdownMessages(server, portal, targetTime, isOpen);
            }

            if (isOpen && reason == OpenReason.CUSTOM_EVENT && remainingMs <= 0) {
                setOpen(portal, false, server, OpenReason.CUSTOM_EVENT);
            }
        }
    }

    public static void sendCountdownMessages(MinecraftServer server, PortalType portal, long targetTime, boolean isClosing) {
        long now = System.currentTimeMillis();
        int minutesLeft = (int) Math.ceil((targetTime - now) / 60000.0);
        Integer last = lastAlertMinutes.getOrDefault(portal, -1);

        if ((minutesLeft <= 5 && minutesLeft > 4 && last != 5) ||
                (minutesLeft <= 1 && minutesLeft > 0 && last != 1)) {

            int sendMinutes = minutesLeft <= 5 && minutesLeft > 4 ? 5 : 1;

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                MessageUtil.sendCountdown(player, portal.name(), sendMinutes, isClosing);
            }
            lastAlertMinutes.put(portal, sendMinutes);
        }

        if (minutesLeft <= 0) {
            lastAlertMinutes.remove(portal);
        }
    }


    public Optional<Long> getCloseTime(PortalType portal) {
        return Optional.ofNullable(portalCloseTime.get(portal));
    }
}
