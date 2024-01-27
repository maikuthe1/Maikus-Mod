package maiku.maikusmod.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.world.LevelListener;
import net.minecraft.core.world.World;
import maiku.maikusmod.util.*;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import maiku.maikusmod.MaikusMod;
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

    public int checkBlocksAroundPoint(World world, double centerX, double centerY, double centerZ, double radius, Block targetBlock) {
        int num = 0;
        for (double x = centerX - radius; x <= centerX + radius; x++) {
            for (double y = centerY - radius; y <= centerY + radius; y++) {
                for (double z = centerZ - radius; z <= centerZ + radius; z++) {
                    Block block = world.getBlock((int)x, (int)y, (int)z);
                    if(block == null)
                        continue;
                    if (block == targetBlock) {
                        num++;
                        //return true;
                    }
                }
            }
        }
        return num;
    }

    double lastx = 0, lasty = 0, lastz = 0;
    boolean validEntity = false;
    /**
     * @author Maiku
     * @reason Maikus Mod: Sound muffler
     */
    @Inject(method = "playSoundAtEntity(Lnet/minecraft/core/entity/Entity;Ljava/lang/String;FF)V", at = @At(value ="HEAD"), cancellable = true) // Don't think we need the arguments in the method here not sure
    private void onEntityPlaySound(Entity entity, String soundPath, float volume, float pitch, CallbackInfo ci){
        if(!(entity instanceof EntityPlayer))
        {
            validEntity = true;
            lastx = entity.x;
            lasty = entity.y;
            lastz = entity.z;
            return;
        }
        validEntity = false;
    }

    /**
     * @author Maiku
     * @reason Maikus Mod: Sound muffler
     */
    @ModifyVariable(method = "playSoundAtEntity(Lnet/minecraft/core/entity/Entity;Ljava/lang/String;FF)V", at = @At("HEAD"), ordinal = 0)
        private float injected(float volume) {
            if(!validEntity)
                return volume;
            //TODO: Optimize this, checking every block in a radius every time a sound is played is not a good solution
            int numblocks = checkBlocksAroundPoint(world, lastx, lasty, lastz, 5, MaikusModBlocks.soundDampener);
            float newVolume = volume;
            for (int i = 0; i < numblocks; i++) {
                newVolume -= (volume * 0.25);
            }

            //String isValid = (validEntity) ? "true" : "false";
            //MaikusMod.LOGGER.info("Valid: " + isValid + " Volume: " + Float.toString(volume) + " New volume: " + Float.toString(newVolume) + " Num blocks: " + Integer.toString(numblocks));
            
            return Math.max(0, newVolume);
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
