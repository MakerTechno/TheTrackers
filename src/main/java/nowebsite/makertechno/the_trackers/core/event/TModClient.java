package nowebsite.makertechno.the_trackers.core.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import nowebsite.makertechno.the_trackers.TheTrackers;
import nowebsite.makertechno.the_trackers.client.gui.TGui;
import nowebsite.makertechno.the_trackers.core.config.ConfigProcessor;
import nowebsite.makertechno.the_trackers.core.config.TConfig;
import nowebsite.makertechno.the_trackers.core.track.EntityTracker;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = TheTrackers.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class TModClient {
    public static boolean isLoaded = false;
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        isLoaded = true;
        TConfig.CRCursorWithEntities = ConfigProcessor.collectCREntityBindCursor(TConfig.CENTER_RELATIVE_BIND.get());
        TConfig.CRCursorWithSecondaryEntities = ConfigProcessor.collectCREntityBindCursor(TConfig.CENTER_RELATIVE_BIND_SECONDARY.get());
        TConfig.DTCursorWithEntities = ConfigProcessor.collectDTEntityBindCursor(TConfig.TRACK_FULL_BIND.get());
        EntityTracker.reCalcAllEntityGroups();
    }
    @SubscribeEvent
    public static void regRenderer(@NotNull RegisterGuiOverlaysEvent event){
        event.registerBelow(VanillaGuiOverlay.CROSSHAIR.id(), "pointer", new TGui(Minecraft.getInstance()));
    }
}
