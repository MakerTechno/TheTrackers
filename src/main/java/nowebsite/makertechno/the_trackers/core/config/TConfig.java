package nowebsite.makertechno.the_trackers.core.config;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.loading.LoadingModList;
import nowebsite.makertechno.the_trackers.TheTrackers;
import nowebsite.makertechno.the_trackers.client.gui.cursors.TRenderCursor;
import nowebsite.makertechno.the_trackers.core.event.TModClient;
import nowebsite.makertechno.the_trackers.core.track.EntityTracker;
import nowebsite.makertechno.the_trackers.core.track.algorithm.ProjectAlgorithmLib;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = TheTrackers.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TConfig {
    public static boolean isModLoaded(String modid) {
        return LoadingModList.get().getModFileById(modid) != null;
    }
    public static final boolean CF_LOADED = isModLoaded("confluence");
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    /* Basic settings. */
    private static final ForgeConfigSpec.BooleanValue AVAILABLE = BUILDER
            .comment("Enable tracking pointer")
            .translation("the_trackers.configuration.available")
            .define("Available", true);
    private static final ForgeConfigSpec.DoubleValue GUI_SCALE = BUILDER
            .comment("Scale of cursor")
            .translation("the_trackers.configuration.gui_scale")
            .defineInRange("Scale",0.6, 0.01, 4);
    private static final ForgeConfigSpec.IntValue MAX_TRACK_QUANTITY = BUILDER
            .comment("Max tracking quantity")
            .translation("the_trackers.configuration.quantity")
            .defineInRange("Max quantity",10, 1, 400);
    private static final ForgeConfigSpec.IntValue REFRESH_POS_INTERVAL = BUILDER
            .comment("Interval between world position refresh")
            .translation("the_trackers.configuration.interval")
            .defineInRange("Interval", 1, 1, 160);

    /* HUD mode control. */
    private static final ForgeConfigSpec.BooleanValue CENTER_RELATIVE_AVAILABLE = BUILDER
            .comment("Enable center relative icon display")
            .translation("the_trackers.configuration.center_relative_available")
            .define("Center relative available", true);
    // Specific setting for center relative mode.
    public static final ForgeConfigSpec.EnumValue<ProjectAlgorithmLib.Type> PROJECT_ALGORITHM = BUILDER
            .comment("The algorithm of center rela projection")
            .translation("the_trackers.configuration.project_algorithm")
            .defineEnum("Project algorithm", ProjectAlgorithmLib.Type.AITOFF);

    private static final ForgeConfigSpec.BooleanValue TRACK_FULL_AVAILABLE = BUILDER
            .comment("Enable full track icon display")
            .translation("the_trackers.configuration.track_full_available")
            .define("Track full available", false);
    private static final ForgeConfigSpec.BooleanValue HEAD_FLAT_AVAILABLE = BUILDER
            .comment("Enable longitude icon display")
            .translation("the_trackers.configuration.head_flat_available")
            .define("Head flat available", false);


    /* Switch for those unwelcome functions */
    public static final ForgeConfigSpec.BooleanValue TRACK_SECONDARY_ENEMIES = BUILDER
        .comment("Enable pointing secondary marked enemies")
        .translation("the_trackers.configuration.track_secondary_enemies")
        .define("Secondary track available", false);

    /* Tracking list */
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CENTER_RELATIVE_BIND = BUILDER
        .comment("List of entity types with cursors for center relative display, separated with \"|\". e.g. EntityType|PointerIconType|EntityIconType(|OptionalValue)")
        .translation("the_trackers.configuration.center_relative_tracking")
        .defineList(
            "CenterRelativeTracking",
            CF_LOADED ?
            List.of(
                "minecraft:ender_dragon|normal|ender_dragon_head",
                "minecraft:wither|normal_white|wither_head",
                "confluence:king_slime|normal|king_slime",
                "confluence:eye_of_cthulhu|normal|eye_of_cthulhu",
                "confluence:brain_of_cthulhu|normal|brain_of_cthulhu",
                "confluence:eater_of_worlds|normal|eater_of_worlds",
                "confluence:queen_bee|normal|queen_bee",
                "confluence:deerclops|normal|deerclops",
                "confluence:skeletron|normal|skeletron",
                "confluence:hill_of_flesh|normal|wall_of_flesh",
                "confluence:wall_of_flesh|normal|wall_of_flesh"
            ) : List.of(
                    "minecraft:ender_dragon|normal|ender_dragon_head",
                    "minecraft:wither|normal_white|wither_head"
            ),
            ConfigProcessor::isValidEntityBindCRCursor
        );

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> CENTER_RELATIVE_BIND_SECONDARY = BUILDER
        .comment("Secondary list of entity types with cursors for center relative display, separated with \"|\". e.g. EntityType|PointerIconType|EntityIconType(|OptionalValue)")
        .translation("the_trackers.configuration.center_relative_tracking_secondary")
        .defineList(
            "CenterRelativeTrackingSecondary",
            CF_LOADED ? List.of(
                "confluence:demon_eye|normal|none",
                "confluence:flying_fish|normal|none",
                "confluence:crimera|normal|none",
                "confluence:eater_of_souls|normal|none",
                "confluence:giant_worm|normal|none",
                "confluence:tomb_crawler|normal|none",
                "confluence:devourer|normal|none",
                "confluence:cave_bat|normal|none",
                "confluence:jungle_bat|normal|none",
                "confluence:snatcher|normal|none",
                "confluence:man_eater|normal|none",
                "confluence:hornet|normal|none",
                "confluence:hell_bat|normal|none",
                "confluence:ice_bat|normal|none",
                "confluence:spore_bat|normal|none",
                "confluence:harpy|normal|none",
                "confluence:cursed_skull|normal|none",
                "confluence:dark_caster|normal|none",
                "confluence:antlion_swarmer|normal|none",
                "confluence:giant_antlion_swarmer|normal|none",
                "confluence:wyvern|normal|none",
                "confluence:granite_elemental|normal|none",
                "confluence:ghost|normal|none",
                "confluence:fire_imp|normal|none",
                "confluence:demon|normal|none",
                "confluence:voodoo_demon|normal|none",
                "confluence:bone_serpent|normal|none",
                "confluence:wither_bone_serpent|normal|none",
                "confluence:meteor_head|normal|none"
            ) : List.of(),
            ConfigProcessor::isValidEntityBindCRCursor
        );

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> TRACK_FULL_BIND = BUILDER
            .comment("List of entity types with cursors for full track display, separated with \"|\"")
            .translation("the_trackers.configuration.track_full_tracking")
            .defineList(
                    "entityType|type:icon(|optionalPattern)",
                    List.of(
                        "minecraft:wither|normal:point_x"
                    ),
                ConfigProcessor::isValidCREntityBindDTCursor
            );

    public static final ForgeConfigSpec SPEC = BUILDER.build();
    public static boolean available;
    public static double scale;
    public static int maxTrackingQuantity;
    public static int interval;
    public static boolean centerRelativeAvailable;
    public static boolean secondaryAvailable;
    public static boolean trackFullAvailable;
    public static boolean headFlatAvailable;
    public static ProjectAlgorithmLib.Type projectAlgorithm;
    public static Set<Pair<EntityType<?>, Supplier<? extends TRenderCursor>>> CRCursorWithEntities;
    public static Set<Pair<EntityType<?>, Supplier<? extends TRenderCursor>>> CRCursorWithSecondaryEntities;
    public static Set<Pair<EntityType<?>, Supplier<? extends TRenderCursor>>> DTCursorWithEntities;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        available = AVAILABLE.get();
        scale = GUI_SCALE.get() * 0.6;
        interval = REFRESH_POS_INTERVAL.get();
        maxTrackingQuantity = MAX_TRACK_QUANTITY.get();
        centerRelativeAvailable = CENTER_RELATIVE_AVAILABLE.get();
        secondaryAvailable = TRACK_SECONDARY_ENEMIES.get();
        trackFullAvailable = TRACK_FULL_AVAILABLE.get();
        headFlatAvailable = HEAD_FLAT_AVAILABLE.get();
        projectAlgorithm = PROJECT_ALGORITHM.get();
        if(TModClient.isLoaded) {
            CRCursorWithEntities = ConfigProcessor.collectCREntityBindCursor(CENTER_RELATIVE_BIND.get());
            CRCursorWithSecondaryEntities = ConfigProcessor.collectCREntityBindCursor(CENTER_RELATIVE_BIND_SECONDARY.get());
            DTCursorWithEntities = ConfigProcessor.collectDTEntityBindCursor(TRACK_FULL_BIND.get());
            EntityTracker.reCalcAllEntityGroups();
        }
        EntityTracker.getRENDERING().forEach((uuid, trackerEntityState) -> trackerEntityState.getComponent().flush());
    }

    @SubscribeEvent
    static void onFileChanged(final ModConfigEvent.Reloading event) {
        onLoad(event);
    }
}
