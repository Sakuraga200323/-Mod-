package tsukineko.jp.technical_items.items.mami_items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tsukineko.jp.technical_items.TechnicalItemsMod;

public class YellowRibbon extends Item {

    public YellowRibbon(String name) {
        super();
        this.setCreativeTab(TechnicalItemsMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        this.setRegistryName(TechnicalItemsMod.MOD_ID, name);
        this.setMaxStackSize(1);
        this.setMaxDamage(40);
        ForgeRegistries.ITEMS.register(this);
    }


    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if (!worldIn.isRemote){
            ItemStack stack = new ItemStack(TechnicalItemsMod.Items.MAMI_RIFLE);
            playerIn.addItemStackToInventory(stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
