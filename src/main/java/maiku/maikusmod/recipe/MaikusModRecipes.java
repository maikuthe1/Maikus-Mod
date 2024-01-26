package maiku.maikusmod.recipe;

import maiku.maikusmod.block.MaikusModBlocks;
import net.minecraft.core.block.Block;
import turniplabs.halplibe.helper.RecipeHelper;

public class MaikusModRecipes {

    private void craftingRecipesBlocks() {
        RecipeHelper.Crafting.createRecipe(MaikusModBlocks.soundDampener, 1, new Object[]{
            "11 ", "11 ", "11 ",
            '1', Block.wool
        });
    }

    public void Initialize()
    {
        craftingRecipesBlocks();
    }
}
