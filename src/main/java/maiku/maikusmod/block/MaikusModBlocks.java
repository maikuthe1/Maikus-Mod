package maiku.maikusmod.block;

import maiku.maikusmod.MaikusMod;
import net.minecraft.client.sound.block.BlockSound;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import turniplabs.halplibe.helper.BlockBuilder;

public class MaikusModBlocks {
    private final String MOD_ID = MaikusMod.MOD_ID;
    private int blockId = 1300;

    // Blocks
    public static Block soundDampener;

    public void Initialize()
    {
        soundDampener = new BlockBuilder(MOD_ID)
            .setBlockSound(new BlockSound("step.cloth", "step.cloth", 1.0f, 1.0f))
            .setHardness(1.0f)
            .setResistance(1.0f)
            .setTextures("crate.png")
            .setFlammability(2, 2)
            .setTags()
            .build(new BlockSoundDampener("sounddampener", blockId++, Material.cloth));
    }
}
