package jp.tsukineko.swordmod;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemFood;

public class CreateItemObject extends Item{
    public CreateItemObject(String name, CreativeTabs creative_tab){
        this.setRegistryName(SwordMod.MOD_ID, name);
        this.setCreativeTab(creative_tab);
        this.setTranslationKey(name);
        ForgeRegistries.ITEMS.register(this);
    }
}
