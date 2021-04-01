package jp.tsukineko.swordmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CreateItemFoodObject extends ItemFood {
    public CreateItemFoodObject(String name, Integer amount, Boolean isWolfFood){
        super(amount, isWolfFood);
        this.setRegistryName(SwordMod.MOD_ID, name);
        this.setCreativeTab(SwordMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        ForgeRegistries.ITEMS.register(this);
    }
}