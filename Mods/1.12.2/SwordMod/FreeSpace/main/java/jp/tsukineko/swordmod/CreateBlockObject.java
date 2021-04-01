package jp.tsukineko.swordmod;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CreateBlockObject extends Block {
    public CreateBlockObject(
            String name,
            Float hardness,int lightLevel, int lightOpacity, Float resistance,
            Material material,
            CreativeTabs creativeTabs
    ) {
        super(material);
        this.setCreativeTab(creativeTabs);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setHardness(hardness);
        this.setLightLevel(lightLevel);
        this.setLightOpacity(lightOpacity);
        this.setResistance(resistance);
        this.setDefaultState(this.blockState.getBaseState());
        ForgeRegistries.BLOCKS.register(this);
        ForgeRegistries.ITEMS.register(new ItemBlock(this).setRegistryName("sword_mod", name));
    }
}
