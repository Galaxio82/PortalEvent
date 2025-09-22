package fr.galaxio.portalevent.server.events;

import fr.galaxio.portalevent.Config;
import fr.galaxio.portalevent.ModPortalEvent;
import fr.galaxio.portalevent.api.PortalAPI;
import fr.galaxio.portalevent.utils.MessageUtil;
import fr.galaxio.portalevent.utils.OpenReason;
import fr.galaxio.portalevent.utils.PortalType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityTravelToDimensionEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static fr.galaxio.portalevent.server.events.EventManager.randomOpen;
import static fr.galaxio.portalevent.utils.CheckTime.checkTime;

@EventBusSubscriber(modid = ModPortalEvent.MODID, value = Dist.DEDICATED_SERVER)
public class PortalEvent {

    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPortalStatusChanged(PortalStatusEvent event) {

        if (event.isOpen()) {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                MessageUtil.sendPortalMessage(player, event.getPortal().name(), true);
            }
        } else {
            for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                Level world = player.level();
                if ((world.dimension() == Level.NETHER && event.getPortal() == PortalType.NETHER) ||
                        (world.dimension() == Level.END && event.getPortal() == PortalType.END)) {
                    EventManager.teleportToOverworld(player);
                }
                MessageUtil.sendPortalMessage(player, event.getPortal().name(), false);
            }
        }

        // --- LOG ---
        System.out.println("[PortalEvent] " + event.getPortal().name() +
                " -> " + (event.isOpen() ? "OPEN" : "CLOSED"));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter < 1200) return;
        tickCounter = 0;

        MinecraftServer server = event.getServer();

        // ---- Nether ----
        if (Config.NETHER_ENABLED.get()) {
            if (Config.OPEN_PORTAL_TIME.get()) {
                boolean open = checkTime(
                        Config.NETHER_OPEN_DAY.get(),
                        Config.NETHER_OPEN_HOUR.get(),
                        Config.NETHER_OPEN_MINUTE.get(),
                        Config.NETHER_CLOSE_DAY.get(),
                        Config.NETHER_CLOSE_HOUR.get(),
                        Config.NETHER_CLOSE_MINUTE.get()
                );
                EventManager.setOpen(PortalType.NETHER, open, server, OpenReason.TIME);
            }

            if (Config.ENABLE_CUSTOM_EVENTS.get()) {
                boolean open = server.getPlayerCount() >= Config.NETHER_MIN_PLAYERS.get()
                        || randomOpen(Config.NETHER_RANDOM_CHANCE.get());

                if (open && (!PortalAPI.getPortalManager().isOpen(PortalType.NETHER)
                        || PortalAPI.getPortalManager().getReason(PortalType.NETHER) != OpenReason.CUSTOM_EVENT)) {
                    EventManager.setOpen(PortalType.NETHER, true, server, OpenReason.CUSTOM_EVENT);
                }
            }
        }

        // ---- End ----
        if (Config.END_ENABLED.get()) {
            if (Config.OPEN_PORTAL_TIME.get()) {
                boolean open = checkTime(
                        Config.END_OPEN_DAY.get(),
                        Config.END_OPEN_HOUR.get(),
                        Config.END_OPEN_MINUTE.get(),
                        Config.END_CLOSE_DAY.get(),
                        Config.END_CLOSE_HOUR.get(),
                        Config.END_CLOSE_MINUTE.get()
                );
                EventManager.setOpen(PortalType.END, open, server, OpenReason.TIME);
            }

            if (Config.ENABLE_CUSTOM_EVENTS.get()) {
                boolean open = server.getPlayerCount() >= Config.END_MIN_PLAYERS.get()
                        || randomOpen(Config.END_RANDOM_CHANCE.get());

                if (open && (!PortalAPI.getPortalManager().isOpen(PortalType.END)
                        || PortalAPI.getPortalManager().getReason(PortalType.END) != OpenReason.CUSTOM_EVENT)) {
                    EventManager.setOpen(PortalType.END, true, server, OpenReason.CUSTOM_EVENT);
                }
            }
        }

        EventManager.checkAutomaticClosures(server);
    }

    @SubscribeEvent
    public static void onPlayerTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ResourceKey<Level> target = event.getDimension();

        if (target.equals(Level.NETHER) && !PortalAPI.getPortalManager().isOpen(PortalType.NETHER)) {
            event.setCanceled(true);
            MessageUtil.sendActionBar(player, "message.portalevent.nether_closed");
        }

        if (target.equals(Level.END) && !PortalAPI.getPortalManager().isOpen(PortalType.END)) {
            event.setCanceled(true);
            MessageUtil.sendActionBar(player, "message.portalevent.end_closed");
        }
    }
}
