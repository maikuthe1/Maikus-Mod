package maiku.maikusmod.item;

import maiku.maikusmod.MaikusMod;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemHelper;

public class MaikusModItems {
    private final String MOD_ID = MaikusMod.MOD_ID;
    private int itemId = 17500;

    // Items
    public static Item gelatin;

    public void Initialize()
    {
         gelatin = ItemHelper.createItem(MOD_ID,
                new Item(itemId++),
                "ingredient.gelatin",
                "temp.png");
    }
}
