package maiku.maikusmod.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.Item;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Entity.class, remap = false)
public class EntityMixin {
    public Entity me = (Entity)(Object)this;

    /**
     * @author AtomicStryker
     * @reason Maikus Mod: Dyanmic Footsteps based on boots
     */
    @Redirect(method = "move(DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockSoundEffect(DDDLnet/minecraft/core/block/Block;Lnet/minecraft/core/enums/EnumBlockSoundEffectType;)V", ordinal = 0))
    private void customFootsteps(World world, double x, double y, double z, Block block, EnumBlockSoundEffectType soundType){
        if (me instanceof EntityPlayer) {
            EntityPlayer player = ((EntityPlayer)me);
            boolean leatherBoots = player.inventory.armorInventory[0] != null && player.inventory.armorInventory[0].itemID == Item.armorBootsLeather.id;
            boolean barefoot = player.inventory.armorInventory[0] == null;
            int numMetalArmor = 0;
            int[] armorArray = {Item.armorBootsIron.id, Item.armorChestplateIron.id, Item.armorLeggingsIron.id, Item.armorHelmetIron.id};
            for(int i = 0; i < 4; i++)
            {
                if(player.inventory.armorInventory[i] != null)
                    for (int checkNum : armorArray) {
                        if (checkNum == player.inventory.armorInventory[i].itemID) {
                            numMetalArmor++;
                        }
                    }
            }
            if(barefoot || leatherBoots){
                    world.playSoundAtEntity(me, "step.cloth", (barefoot) ? 0.08F : 0.1F, (barefoot) ? 3F : 2F);

                if(block.blockMaterial == Material.wood)
                {
                    world.playSoundAtEntity(me, "step.wood", (barefoot) ? 0.04F : 0.25F, 1.2F);
                }
                if(block.blockMaterial == Material.grass)
                {
                    world.playSoundAtEntity(me, "step.grass", (barefoot) ? 0.02F : 0.1F, 0.8F);
                }
                if(block.blockMaterial == Material.dirt)
                {
                    world.playSoundAtEntity(me, "step.gravel", (barefoot) ? 0.02F : 0.15F, 0.8F);
                }
                if(block.blockMaterial == Material.sand)
                {
                    world.playSoundAtEntity(me, "step.sand", (barefoot) ? 0.08F : 0.15F, 0.8F);
                }
                if(block.blockMaterial == Material.stone)
                {
                    world.playSoundAtEntity(me, "step.stone", (barefoot) ? 0.04F : 0.15F, 0.8F);
                }
            }
            else if(numMetalArmor > 1)
            {
                world.playBlockSoundEffect(x, y, z, block, soundType);
                world.playSoundAtEntity(me, "maikusmod.metalarmor", 0.3F, 0.9F);
            }
            else
            {
                world.playBlockSoundEffect(x, y, z, block, soundType);
            }
        }
        else {
            world.playBlockSoundEffect(x, y, z, block, soundType);
        }
    }
}
