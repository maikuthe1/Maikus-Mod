package maiku.maikusmod.mixin;

import maiku.maikusmod.config.MaikusModConfig;
import maiku.maikusmod.util.DynamicLightRenderer;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.Minecraft;

@Mixin(value = WorldRenderer.class, remap = false)
public class WorldRendererMixin {
	@Shadow
	Minecraft mc;
	
	@Inject(method = "updateCameraAndRender", at = @At("TAIL"))
	public void onRender(float partialTicks, CallbackInfo ci) {
		if(mc.theWorld != null && !MaikusModConfig.resetLight) {
			DynamicLightRenderer.instance.OnTickInGame(mc);
		}
	}

}
