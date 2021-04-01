package jp.tsukineko.swordmod;

import jp.tsukineko.swordmod.entities.slash.EntitySlash;
import jp.tsukineko.swordmod.entities.slash.RenderEntitySlash;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.IOException;
import java.util.Arrays;

@Mod(
        modid = SwordMod.MOD_ID,
        name = SwordMod.MOD_NAME,
        version = SwordMod.VERSION
)

public class SwordMod {

    public static final String MOD_ID = "sword_mod";
    public static final String MOD_NAME = "SwordMod";
    public static final String VERSION = "1.0.0";
    public static final CreativeTabs MODS_CREATIVE_TAB = new ModTab("Sword Mod");

    @Mod.Instance(MOD_ID)
    public static SwordMod INSTANCE;

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        GameRegistry.addSmelting(Blocks.crystal_ore, new ItemStack(Items.crystal), 10.0F);
        if (event.getSide().isClient()) {
            RenderingRegistry.registerEntityRenderingHandler(EntitySlash.class, new IRenderFactory<EntitySlash>(){
                @Override
                public Render<? super EntitySlash> createRenderFor(RenderManager manager){
                    return new RenderEntitySlash(manager);
                }
            });

        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        GameRegistry.registerWorldGenerator(new SwordWorldGen(), 0);
        EntityRegistry.registerModEntity(new ResourceLocation("slash"), EntitySlash.class, "slash", 0, this, 50, 1, true, 1000, 22);
    }
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
    }

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
        public static final Block crystal_ore = new CreateBlockObject("crystal_ore", 1.5F, 100, 1,6.0F, Material.IRON, SwordMod.MODS_CREATIVE_TAB);
        public static final Block black_diamond_block = new CreateBlockObject("black_diamond_block", 2.0F, 150, 1,15.0F, Material.IRON, SwordMod.MODS_CREATIVE_TAB);
    }

    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
        /* Food */
        public static final Item food_chip = new CreateItemFoodObject("food_chip", 10, false);

        /* Sword Material */
        public static final Item sword_handle = new CreateItemObject("sword_handle", SwordMod.MODS_CREATIVE_TAB);
        public static final Item black_diamond = new CreateItemObject("black_diamond", SwordMod.MODS_CREATIVE_TAB);
        public static final Item crystal = new CreateItemObject("crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item wooden_crystal = new CreateItemObject("wooden_crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item stone_crystal = new CreateItemObject("stone_crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item iron_crystal = new CreateItemObject("iron_crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item golden_crystal = new CreateItemObject("golden_crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item diamond_crystal = new CreateItemObject("diamond_crystal", SwordMod.MODS_CREATIVE_TAB);
        public static final Item black_diamond_crystal = new CreateItemObject("black_diamond_crystal", SwordMod.MODS_CREATIVE_TAB);

        /* Plus Sword */
        public static final Item plus_wooden_sword = new CreateItemSwordObject("plus_wooden_sword", 118, 2, 200);
        public static final Item plus_stone_sword = new CreateItemSwordObject("plus_stone_sword", 262, 5, 200);
        public static final Item plus_iron_sword = new CreateItemSwordObject("plus_iron_sword", 500, 5, 100);
        public static final Item plus_golden_sword = new CreateItemSwordObject("plus_golden_sword", 64, 2, 200);
        public static final Item plus_diamond_sword = new CreateItemSwordObject("plus_diamond_sword", 3124, 7, 100);
        public static final Item plus_ice_sword = new CreateItemSwordObject("plus_ice_sword", 1000, 7, 100);
        public static final Item plus_obsidian_sword = new CreateItemSwordObject("plus_obsidian_sword", 2000, 8, 100);

        /* Other Sword */
        public static final Item ice_sword = new CreateItemSwordObject("ice_sword", 200, 6, 2);
        public static final Item ender_sword = new CreateItemSwordObject("ender_sword", 250, 4, 2);
        public static final Item obsidian_sword = new CreateItemSwordObject("obsidian_sword", 1000, 5, 1);
        public static final Item sword_of_eclipse = new CreateItemSwordObject("sword_of_eclipse", 3000, 8, 1);

        /* Test Item!! */
    }

    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
        }

        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
        }

        public static void setItemCustomModelResourceLocation(Item item){
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
        public static void setBlockCustomModelResourceLocation(Block block){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            /* Items */
            setItemCustomModelResourceLocation(Items.food_chip);
            setItemCustomModelResourceLocation(Items.sword_handle);
            setItemCustomModelResourceLocation(Items.black_diamond);
            setItemCustomModelResourceLocation(Items.crystal);
            setItemCustomModelResourceLocation(Items.wooden_crystal);
            setItemCustomModelResourceLocation(Items.stone_crystal);
            setItemCustomModelResourceLocation(Items.iron_crystal);
            setItemCustomModelResourceLocation(Items.golden_crystal);
            setItemCustomModelResourceLocation(Items.diamond_crystal);
            setItemCustomModelResourceLocation(Items.black_diamond_crystal);
            setItemCustomModelResourceLocation(Items.plus_wooden_sword);
            setItemCustomModelResourceLocation(Items.plus_stone_sword);
            setItemCustomModelResourceLocation(Items.plus_iron_sword);
            setItemCustomModelResourceLocation(Items.plus_golden_sword);
            setItemCustomModelResourceLocation(Items.plus_diamond_sword);
            setItemCustomModelResourceLocation(Items.plus_obsidian_sword);
            setItemCustomModelResourceLocation(Items.plus_ice_sword);
            setItemCustomModelResourceLocation(Items.ice_sword);
            setItemCustomModelResourceLocation(Items.ender_sword);
            setItemCustomModelResourceLocation(Items.obsidian_sword);
            setItemCustomModelResourceLocation(Items.sword_of_eclipse);
            /* Blocks */
            setBlockCustomModelResourceLocation(Blocks.crystal_ore);
            setBlockCustomModelResourceLocation(Blocks.black_diamond_block);
        }
    }

    @Mod.EventBusSubscriber
    public static class ClickRegistryHandler {
        @SubscribeEvent
        public static void onItemRightClick(PlayerInteractEvent.RightClickItem event){
            World world;
            world = event.getWorld();
            EntityPlayer player;
            player = event.getEntityPlayer();
            Item playerMainhandItem;
            playerMainhandItem = player.getHeldItemMainhand().getItem();
            Item swords[] = {Items.ender_sword, Items.ice_sword, Items.obsidian_sword};
            if(Arrays.asList(swords).contains(playerMainhandItem)){
                System.out.println(playerMainhandItem.getRegistryName());
                world.spawnEntity(new EntitySlash(world));
                world.spawnEntity(new EntityCow(world));
            }
        }
    }
}

