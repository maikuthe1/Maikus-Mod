package maiku.maikusmod.recipe;

import maiku.maikusmod.block.MaikusModBlocks;
import maiku.maikusmod.item.MaikusModItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import turniplabs.halplibe.helper.RecipeHelper;

public class MaikusModRecipes {

    private void craftingRecipesBlocks() {
        RecipeHelper.Crafting.createRecipe(MaikusModBlocks.soundDampener, 1, new Object[]{
            "11 ", "11 ", "11 ",
            '1', Block.wool
        });
    }

    private void furnaceRecipes() {
         RecipeHelper.blastingManager.addSmelting(Item.slimeball.id, new ItemStack(MaikusModItems.gelatin));
    }

    public void Initialize()
    {
        craftingRecipesBlocks();
        furnaceRecipes();
    }
}
