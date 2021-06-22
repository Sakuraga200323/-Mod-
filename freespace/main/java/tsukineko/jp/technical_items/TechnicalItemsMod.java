package tsukineko.jp.technical_items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tsukineko.jp.technical_items.entities.*;
import tsukineko.jp.technical_items.entity_renderes.RenderBullet;
import tsukineko.jp.technical_items.entity_renderes.RenderGuidedBullet;
import tsukineko.jp.technical_items.entity_renderes.RenderHitBox;
import tsukineko.jp.technical_items.items.Sraw;
import tsukineko.jp.technical_items.items.mami_items.MamiRifle;
import tsukineko.jp.technical_items.items.mami_items.YellowRibbon;
import tsukineko.jp.technical_items.items.slash_blade.*;
import tsukineko.jp.technical_items.items.trigger_items.ShooterBlackTrigger;
import tsukineko.jp.technical_items.items.trigger_items.ShooterTrigger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.fml.client.registry.RenderingRegistry.*;

@Mod(
        modid = TechnicalItemsMod.MOD_ID,
        name = TechnicalItemsMod.MOD_NAME,
        version = TechnicalItemsMod.VERSION
)
public class TechnicalItemsMod {

    public static final String MOD_ID = "tti";
    public static final String MOD_NAME = "TechnicalItemsMod";
    public static final String VERSION = "1.0.0";
    public static final CreativeTabs MODS_CREATIVE_TAB = new TechnicalItemsModTab("TechnicalItems");

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static TechnicalItemsMod INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        if(event.getSide().isClient()){
            registerEntityRenderingHandler(
                    EntityGuidedBullet.class, manager -> new RenderGuidedBullet<EntityGuidedBullet >(manager, 1.0f)
            );
            registerEntityRenderingHandler(
                    EntityBullet.class, manager -> new RenderBullet<EntityBullet>(manager)
            );
            registerEntityRenderingHandler(
                    EntityHitBox.class, manager -> new RenderHitBox<EntityHitBox>(manager,1.0f)
            );
        }
        EntityRegistry.registerModEntity(new ResourceLocation("bullet"), EntityBullet.class, "bullet", 2003230, this, 50, 1, true, 1000, 22);
        EntityRegistry.registerModEntity(new ResourceLocation("guided_bullet"), EntityGuidedBullet.class, "guided_bullet", 2003231, this, 50, 1, true, 1000, 22);
        EntityRegistry.registerModEntity(new ResourceLocation("hit_box"), EntityHitBox.class, "hit_box", 2003232, this, 50, 1, true, 1000, 22);
        EntityRegistry.registerModEntity(new ResourceLocation("sraw"), EntitySrawBullet.class, "sraw", 2003233, this, 50, 1, true, 1000, 22);
        EntityRegistry.registerModEntity(new ResourceLocation("origin"), EntityOrigin.class, "origin", 200324, this, 50, 1, true, 1000, 22);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
        public static final Item SHOOTER_TRIGGER = new ShooterTrigger();
        public static final Item SHOOTER_BLACKTRIGGER = new ShooterBlackTrigger();
        public static final Item MAMI_RIFLE = new MamiRifle("mami_rifle");
        public static final Item YELLOW_RIBBON = new YellowRibbon("yellow_ribbon");
        public static final Item SYUSUI = new Syusui("syusui", EnumHelper.addToolMaterial(
                "syusui",1,3000,1.0f,7.0f,150
        ));
        public static final Item IZAYOI = new Izayoi("izayoi", EnumHelper.addToolMaterial(
                "izayoi",1,3000,1.0f,2.0f,150
        ));
        public static final Item AREA_SRASHER_COVER = new AreaSlasherCover("area_slasher0", EnumHelper.addToolMaterial(
                "area_slasher0",1,3000,1.0f,1.0f,150
        ));
        public static final Item AREA_SRASHER = new AreaSlasher("area_slasher1", EnumHelper.addToolMaterial(
                "area_slasher1",1,3000,1.0f,2.0f,150
        ));
        public static final Item AREA_SRASHER_COMPLETERY = new AreaSlasherCompletery("area_slasher2", EnumHelper.addToolMaterial(
                "area_slasher2",1,3000,1.0f,1.0f,150
        ));
        public static final Item SRAW = new Sraw("sraw");
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
            event.getRegistry().register(Items.SHOOTER_TRIGGER);
            event.getRegistry().register(Items.SHOOTER_BLACKTRIGGER);
            event.getRegistry().register(Items.MAMI_RIFLE);
            event.getRegistry().register(Items.YELLOW_RIBBON);
            event.getRegistry().register(Items.SYUSUI);
            event.getRegistry().register(Items.AREA_SRASHER);
            event.getRegistry().register(Items.AREA_SRASHER_COVER);
            event.getRegistry().register(Items.AREA_SRASHER_COMPLETERY);
            event.getRegistry().register(Items.SRAW);
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }


        public static void setCustomModelResourceLocation(Item item, int meta){
            ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
        public static void setCustomModelResourceLocation(Block block, int meta){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), meta, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            setCustomModelResourceLocation(Items.SHOOTER_TRIGGER, 0);
            setCustomModelResourceLocation(Items.SHOOTER_BLACKTRIGGER, 0);
            setCustomModelResourceLocation(Items.MAMI_RIFLE, 0);
            setCustomModelResourceLocation(Items.YELLOW_RIBBON, 0);
            setCustomModelResourceLocation(Items.SYUSUI, 0);
            setCustomModelResourceLocation(Items.IZAYOI, 0);
            setCustomModelResourceLocation(Items.AREA_SRASHER,0);
            setCustomModelResourceLocation(Items.AREA_SRASHER_COVER,0);
            setCustomModelResourceLocation(Items.AREA_SRASHER_COMPLETERY,0);
            setCustomModelResourceLocation(Items.SRAW,0);
        }

    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */


}
