package maiku.maikusmod.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import maiku.maikusmod.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import maiku.maikusmod.block.MaikusModBlocks;

@Mixin(value = World.class, remap = false)
public class WorldMixin {

    public World world = (World)(Object)this;

    /**
     * @author AtomicStryker
     * @reason Maikus Mod: Dyanmic Lights
     */
    @Overwrite
    public float getBrightness(int i, int j, int k, int l)
    {
        float lc = DynamicLightCache.cache.getLightValue(i, j, k);
        if(lc > l)
        {
            return lc;
        }

        int lightValue = world.getBlockLightValue(i, j, k);
        float torchLight = DynamicLightDispatcher.getLightBrightness(world, i, j, k);
        if(lightValue < torchLight)
        {
            int floorValue = (int)java.lang.Math.floor(torchLight);
            if(floorValue==15)
            {
                return world.getWorldType().getBrightnessRamp()[15];
            }
            else
            {
                int ceilValue = (int)java.lang.Math.ceil(torchLight);
                float lerpValue = torchLight-floorValue;
                return (1.0f-lerpValue)*world.getWorldType().getBrightnessRamp()[floorValue]+lerpValue*world.getWorldType().getBrightnessRamp()[ceilValue];
            }
        }

        lc = world.getWorldType().getBrightnessRamp()[lightValue];
        DynamicLightCache.cache.setLightValue(i, j, k, lc);
        return lc;
    }

    public boolean checkBlocksAroundPoint(World world, double centerX, double centerY, double centerZ, double radius, Block targetBlock) {
        for (double x = centerX - radius; x <= centerX + radius; x++) {
            for (double y = centerY - radius; y <= centerY + radius; y++) {
                for (double z = centerZ - radius; z <= centerZ + radius; z++) {
                    Block block = world.getBlock((int)x, (int)y, (int)z);
                    if(block == null)
                        continue;
                    if (block == targetBlock) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @author Maiku
     * @reason Maikus Mod: Sound muffler
     */
    @Inject(method = "playSoundAtEntity(Lnet/minecraft/core/entity/Entity;Ljava/lang/String;FF)V", at = @At(value ="HEAD"), cancellable = true) // Don't think we need the arguments in the method here not sure
    private void onEntityPlaySound(Entity entity, String soundPath, float volume, float pitch, CallbackInfo ci){
        if(!(entity instanceof EntityPlayer))
        {
            if(checkBlocksAroundPoint(world, entity.x, entity.y, entity.z, 5, MaikusModBlocks.soundDampener))
            {
                ci.cancel();
                return;
            }
        }
    }


    /**
     * @author AtomicStryker
     * @reason Maikus Mod: Dyanmic Lights
     */
    @Overwrite
    public float getLightBrightness(int i, int j, int k)
    {
        float lc = DynamicLightCache.cache.getLightValue(i, j, k);
        if(lc >= 0)
        {
            return lc;
        }

        int lightValue = world.getBlockLightValue(i, j, k);
        float torchLight = DynamicLightDispatcher.getLightBrightness(world, i, j, k);
        if(lightValue < torchLight)
        {
            int floorValue = (int)java.lang.Math.floor(torchLight);
            if(floorValue==15)
            {
                return world.getWorldType().getBrightnessRamp()[15];
            }
            else
            {
                int ceilValue = (int)java.lang.Math.ceil(torchLight);
                float lerpValue = torchLight-floorValue;
                return (1.0f-lerpValue)*world.getWorldType().getBrightnessRamp()[floorValue]+lerpValue*world.getWorldType().getBrightnessRamp()[ceilValue];
            }
        }

        lc = world.getWorldType().getBrightnessRamp()[lightValue];
        DynamicLightCache.cache.setLightValue(i, j, k, lc);
        return lc;
    }


    @Inject(at = @At(value = "TAIL"), method = "notifyBlockOfNeighborChange")
    private void injected(int i, int j, int k, int blockID, CallbackInfo ci) {
        world.markBlockNeedsUpdate(i, j, k);
    }

}
