package fr.galaxio.portalevent.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageUtil {

    // --- Méthode générique ---
    public static void sendActionBar(ServerPlayer player, String key, Object... args) {
        player.displayClientMessage(Component.translatable(key, args), true);
    }

    public static void sendChat(ServerPlayer player, String key, Object... args) {
        player.displayClientMessage(Component.translatable(key, args), false);
    }

    public static void sendColoredActionBar(ServerPlayer player, String key, ChatFormatting... styles) {
        player.displayClientMessage(
                Component.translatable(key).withStyle(styles),
                true
        );
    }

    public static void sendColoredActionBar(ServerPlayer player, String key, Object[] args, ChatFormatting... styles) {
        player.displayClientMessage(
                Component.translatable(key, args).withStyle(styles),
                true
        );
    }

    // --- Méthodes spécialisées Portail ---
    public static void sendPortalMessage(ServerPlayer player, String portalName, boolean opening) {
        String key = opening ? "message.portalevent.portal_open" : "message.portalevent.portal_close";

        sendColoredActionBar(
                player,
                key,
                new Object[]{portalName},
                opening ? ChatFormatting.GREEN : ChatFormatting.RED,
                ChatFormatting.BOLD
        );
    }

    public static void sendCountdown(ServerPlayer player, String portalName, int minutes, boolean closing) {
        String key = closing
                ? "message.portalevent.countdown_close"
                : "message.portalevent.countdown_open";

        LocalDateTime targetTime = LocalDateTime.now().plusMinutes(minutes);
        String timeStr = targetTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        sendColoredActionBar(
                player,
                key,
                new Object[]{portalName, minutes, timeStr},
                ChatFormatting.GOLD
        );
    }
}