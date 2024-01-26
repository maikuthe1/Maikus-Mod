package maiku.maikusmod;

import net.fabricmc.api.ModInitializer;
import turniplabs.halplibe.helper.SoundHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import maiku.maikusmod.block.MaikusModBlocks;
import maiku.maikusmod.recipe.MaikusModRecipes;
import maiku.maikusmod.config.MaikusModConfig;

public class MaikusMod implements ModInitializer {
    public static final String MOD_ID = "maikusmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        
        MaikusModConfig.initializeSettingsFile(false);
        SoundHelper.addSound(MOD_ID, "barefoot.ogg");
        SoundHelper.addSound(MOD_ID, "metalarmor1.ogg");

        new MaikusModBlocks().Initialize();
        new MaikusModRecipes().Initialize();

        LOGGER.info("MaikusMod initialized.");
    }
}
