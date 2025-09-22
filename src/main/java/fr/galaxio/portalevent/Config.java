package fr.galaxio.portalevent;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue NETHER_ENABLED = BUILDER
            .comment("Nether ouvert automatiquement ?")
            .define("netherEnabled", true);

    public static final ModConfigSpec.BooleanValue END_ENABLED = BUILDER
            .comment("End ouvert automatiquement ?")
            .define("endEnabled", true);

    public static final ModConfigSpec.BooleanValue OPEN_PORTAL_TIME = BUILDER
            .comment("Ouvrir les portails selon l'heure défini")
            .define("OpenPortalTime", true);

    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_CUSTOM_EVENTS = BUILDER
            .comment("Activer les événements personnalisés (Player / Random chance)")
            .define("enableCustomEvents", false);

    public static final ModConfigSpec.IntValue NETHER_OPEN_DAY = BUILDER
            .comment("Jour d'ouverture du Nether (1=Lundi, 7=Dimanche)")
            .defineInRange("netherOpenDay", 1, 1, 7);

    public static final ModConfigSpec.IntValue NETHER_OPEN_HOUR = BUILDER
            .comment("Heure d'ouverture du Nether (0-23)")
            .defineInRange("netherOpenHour", 18, 0, 23);

    public static final ModConfigSpec.IntValue NETHER_OPEN_MINUTE = BUILDER
            .comment("Minute d'ouverture du Nether (0-59)")
            .defineInRange("netherOpenMinute", 0, 0, 59);

    public static final ModConfigSpec.IntValue NETHER_CLOSE_DAY = BUILDER
            .comment("Jour de fermeture du Nether (1=Lundi, 7=Dimanche)")
            .defineInRange("netherCloseDay", 1, 1, 7);

    public static final ModConfigSpec.IntValue NETHER_CLOSE_HOUR = BUILDER
            .comment("Heure de fermeture du Nether (0-23)")
            .defineInRange("netherCloseHour", 20, 0, 23);

    public static final ModConfigSpec.IntValue NETHER_CLOSE_MINUTE = BUILDER
            .comment("Minute de fermeture du Nether (0-59)")
            .defineInRange("netherCloseMinute", 0, 0, 59);

    public static final ModConfigSpec.IntValue NETHER_MIN_PLAYERS = BUILDER
            .comment("Nombre minimum de joueurs pour ouvrir le Nether")
            .defineInRange("netherMinPlayers", 1, 1, 100);

    public static final ModConfigSpec.DoubleValue NETHER_RANDOM_CHANCE = BUILDER
            .comment("Chance d'ouverture aléatoire du Nether (0.0-1.0)")
            .defineInRange("netherRandomChance", 0.2, 0.0, 1.0);

    public static final ModConfigSpec.IntValue END_OPEN_DAY = BUILDER
            .comment("Jour d'ouverture de l'End (1=Lundi, 7=Dimanche)")
            .defineInRange("endOpenDay", 7, 1, 7);

    public static final ModConfigSpec.IntValue END_OPEN_HOUR = BUILDER
            .comment("Heure d'ouverture de l'End")
            .defineInRange("endOpenHour", 14, 0, 23);

    public static final ModConfigSpec.IntValue END_OPEN_MINUTE = BUILDER
            .comment("Minute d'ouverture de l'End")
            .defineInRange("endOpenMinute", 0, 0, 59);

    public static final ModConfigSpec.IntValue END_CLOSE_DAY = BUILDER
            .comment("Jour de fermeture de l'End (1=Lundi, 7=Dimanche)")
            .defineInRange("endCloseDay", 7, 1, 7);

    public static final ModConfigSpec.IntValue END_CLOSE_HOUR = BUILDER
            .comment("Heure de fermeture de l'End")
            .defineInRange("endCloseHour", 16, 0, 23);

    public static final ModConfigSpec.IntValue END_CLOSE_MINUTE = BUILDER
            .comment("Minute de fermeture de l'End")
            .defineInRange("endCloseMinute", 0, 0, 59);

    public static final ModConfigSpec.IntValue END_MIN_PLAYERS = BUILDER
            .comment("Nombre minimum de joueurs pour ouvrir l'End")
            .defineInRange("endMinPlayers", 1, 1, 100);

    public static final ModConfigSpec.DoubleValue END_RANDOM_CHANCE = BUILDER
            .comment("Chance d'ouverture aléatoire de l'End (0.0-1.0)")
            .defineInRange("endRandomChance", 0.2, 0.0, 1.0);

    // Overworld TP coordinates (si Nether/End fermés)
    public static final ModConfigSpec.DoubleValue OVERWORLD_TP_X = BUILDER
            .comment("Coordonnée X pour téléporter les joueurs dans l'Overworld")
            .defineInRange("overworldTpX", 0.0, -30000000.0, 30000000.0);

    public static final ModConfigSpec.DoubleValue OVERWORLD_TP_Y = BUILDER
            .comment("Coordonnée Y pour téléporter les joueurs dans l'Overworld")
            .defineInRange("overworldTpY", 80.0, 0.0, 320.0);

    public static final ModConfigSpec.DoubleValue OVERWORLD_TP_Z = BUILDER
            .comment("Coordonnée Z pour téléporter les joueurs dans l'Overworld")
            .defineInRange("overworldTpZ", 0.0, -30000000.0, 30000000.0);


    public static final ModConfigSpec SPEC = BUILDER.build();
}
