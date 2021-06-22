package tsukineko.jp.technical_items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class TechnicalItemsModTab extends CreativeTabs {
    public TechnicalItemsModTab(String label) {
        super(getNextID(), label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(TechnicalItemsMod.Items.SHOOTER_TRIGGER);
    }

}
