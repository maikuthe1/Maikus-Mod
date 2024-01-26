package maiku.maikusmod.config;

import maiku.maikusmod.MaikusMod;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.GuiScreen;

import java.util.function.Function;

public class MaikusModModMenu implements ModMenuApi {

    @Override
    public String getModId() {
        return MaikusMod.MOD_ID;
    }

    @Override
    public Function<GuiScreen, ? extends GuiScreen> getConfigScreenFactory() {
        return screen -> new GuiMaikusModConfiguration(screen);
    }
}
