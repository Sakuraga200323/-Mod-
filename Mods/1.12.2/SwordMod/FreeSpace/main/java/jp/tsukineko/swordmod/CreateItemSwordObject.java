package jp.tsukineko.swordmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CreateItemSwordObject extends ItemSword {
    public CreateItemSwordObject(String  name, Integer durability, Integer damage, Integer enchantability){
        super(EnumHelper.addToolMaterial(name, 1, durability, 1, damage, enchantability));
        this.setRegistryName(SwordMod.MOD_ID, name);
        this.setCreativeTab(SwordMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        ForgeRegistries.ITEMS.register(this);
    }
}