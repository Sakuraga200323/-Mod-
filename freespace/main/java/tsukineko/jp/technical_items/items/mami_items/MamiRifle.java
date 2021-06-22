package tsukineko.jp.technical_items.items.mami_items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tsukineko.jp.technical_items.TechnicalItemsMod;
import tsukineko.jp.technical_items.TMath;
import tsukineko.jp.technical_items.entities.EntityBullet;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;

import java.util.List;

public class MamiRifle extends Item {

    public MamiRifle(String name) {
        super();
        this.setCreativeTab(TechnicalItemsMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        this.setRegistryName(TechnicalItemsMod.MOD_ID, name);
        this.setMaxStackSize(1);
        this.setMaxDamage(10);
        ForgeRegistries.ITEMS.register(this);
    }


    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack mainItemStack = playerIn.getHeldItem(handIn);
        Item mainItem = mainItemStack.getItem();
        double x,y,z;
        boolean shooted = false;
        if ( mainItemStack.getItemDamage() <= 0 ){
            if (!worldIn.isRemote) {
                shooted = true;

                x = playerIn.posX - 1 * Math.sin(TMath.getRotationYawRad(playerIn));
                y = playerIn.posY + playerIn.getEyeHeight() + 0.5 * Math.sin(-TMath.getRotationPitchRad(playerIn));
                z = playerIn.posZ + 1 * Math.cos(TMath.getRotationYawRad(playerIn));
                EntityGuidedBullet bullet = new EntityGuidedBullet(
                        worldIn, playerIn, x, y, z, playerIn.getRotationYawHead(), playerIn.rotationPitch
                );
                bullet.setBulletStatus(10.0f, 5.0f);
                bullet.particleNotGuiding = EnumParticleTypes.FLAME;
                bullet.particleDead = EnumParticleTypes.WATER_BUBBLE;
                worldIn.spawnEntity((Entity) bullet);
                ((WorldServer) worldIn).spawnParticle(
                        EnumParticleTypes.END_ROD,
                        true, x, y, z,
                        5,
                        0.0D, 0.0D, 0.0D, 0.1D,
                        1
                );
                mainItemStack.damageItem(9, playerIn);
                Potion pot = MobEffects.NIGHT_VISION;
                playerIn.addPotionEffect(new PotionEffect(pot,1,10));
            }else{
                worldIn.playSound(playerIn,playerIn.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.MASTER,0.1f,3.0f);
                worldIn.playSound(playerIn,playerIn.getPosition(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.MASTER,0.1f,3.0f);
            }
        }else{
            worldIn.playSound(playerIn,playerIn.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER,1.0f,3.0f);
            playerIn.inventory.deleteStack(mainItemStack);
            final EntityItem entityItem = playerIn.dropItem(mainItemStack, true, false);
            entityItem.lifespan = 60;
            /*
            List<ItemStack> stackList = null;
            Integer[] intList = {0,1,2,3,4,5,6,7,8};
            ItemStack itemStack = null,temp;
            Integer slotNum = null;
            for (Integer i: intList) {
                temp = playerIn.inventory.getStackInSlot(i);
                if ((temp.getItem() instanceof MamiRifle)){
                    itemStack = temp;
                    slotNum = i;
                    break;
                }
            }
            if (slotNum != null || itemStack != null){
                playerIn.setItemStackToSlot(,itemStack);
            }
             */

        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, mainItemStack);
    }
}
