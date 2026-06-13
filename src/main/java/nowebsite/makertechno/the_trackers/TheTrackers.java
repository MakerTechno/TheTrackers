package nowebsite.makertechno.the_trackers;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import nowebsite.makertechno.the_trackers.core.config.TConfig;
import nowebsite.makertechno.the_trackers.data.datagen.ModDataGenerators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(value = TheTrackers.MOD_ID)
public class TheTrackers {
    public static final String MOD_ID = "the_trackers";
    public static final Logger LOGGER = LoggerFactory.getLogger("TheTrackers");

    public TheTrackers(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        context.registerConfig(ModConfig.Type.CLIENT, TConfig.SPEC);
        bus.addListener(ModDataGenerators::gatherData);
    }

}
