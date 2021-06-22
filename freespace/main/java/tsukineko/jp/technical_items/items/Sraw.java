package tsukineko.jp.technical_items.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tsukineko.jp.technical_items.TMath;
import tsukineko.jp.technical_items.TechnicalItemsMod;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;
import tsukineko.jp.technical_items.entities.EntitySrawBullet;

public class Sraw extends Item {
    SoundEvent soundShoot = SoundEvents.ENTITY_SHULKER_SHOOT;
    SoundEvent soundChangeMode = SoundEvents.ENTITY_BLAZE_HURT;
    SoundEvent soundHealDamage = SoundEvents.E_PARROT_IM_BLAZE;

    public Sraw(String name){
        super();
        this.setCreativeTab(TechnicalItemsMod.MODS_CREATIVE_TAB);
        this.setRegistryName(TechnicalItemsMod.MOD_ID, name);
        this.setTranslationKey(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(40);
        ForgeRegistries.ITEMS.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack mainItemStack = playerIn.getHeldItem(handIn);
        Item mainItem = mainItemStack.getItem();
        double x,y,z;
        boolean shooted = false;
        playerIn.getCooldownTracker().setCooldown(this, 140);
        shooted = true;
        x = playerIn.posX - 1 * Math.sin(TMath.getRotationYawRad(playerIn));
        y = playerIn.posY + playerIn.getEyeHeight() + 0.5 * Math.sin(-TMath.getRotationPitchRad(playerIn));
        z = playerIn.posZ + 1 * Math.cos(TMath.getRotationYawRad(playerIn));
        EntitySrawBullet bullet = new EntitySrawBullet(
                worldIn, playerIn, x, y, z, playerIn.getRotationYawHead(), playerIn.rotationPitch
        );
        bullet.setBulletStatus(0.75f, 20.0f, 1.0f);
        worldIn.spawnEntity(bullet);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.MASTER, 1.0f, 2.0f);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, SoundCategory.MASTER, 1.0f, 2.0f);
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_SKELETON_HURT, SoundCategory.MASTER, 1.0f, 2.0f);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, mainItemStack);
    }
}
