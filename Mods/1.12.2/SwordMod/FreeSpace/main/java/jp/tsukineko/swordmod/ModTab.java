package jp.tsukineko.swordmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class ModTab extends CreativeTabs {
    public ModTab(String label) {
        super(getNextID(), label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(SwordMod.Items.obsidian_sword);
    }

}
