package tsukineko.jp.technical_items.items.trigger_items;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tsukineko.jp.technical_items.TechnicalItemsMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tsukineko.jp.technical_items.TMath;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;

import javax.annotation.Nullable;
import java.util.List;
import java.lang.String;

public class ShooterTrigger extends Item {

    String name = "shooter_trigger";
    int cooltimeToChangeMode;
    int cooltimeToChangeName;

    // SoundEvent soundShoot = SoundEvent.REGISTRY.getObject(new ResourceLocation("sword_mod:entity_guided_bullet_shoot"));
    SoundEvent soundShoot = SoundEvents.ENTITY_SHULKER_SHOOT;
    SoundEvent soundChangeMode = SoundEvents.ENTITY_BLAZE_HURT;
    SoundEvent soundHealDamage = SoundEvents.E_PARROT_IM_BLAZE;

    String[] bulletsStatus1 = {
            "アステロイド:通常弾",
            "ギムレット:徹甲弾",
            "バイパー:誘導弾",
            "コブラ:強化誘導弾",
            "メテオラ:炸裂弾",
            "トマホーク:誘導炸裂弾",
    };

    float[][] bulletsStatus2 = {
            // {speed, damage, guided, explosion, penetration}
            {1.00f, 3.00f, 0.00f, 0.01f, 1.00f},
            {1.50f, 4.00f, 0.00f, 0.01f, 3.00f},
            {1.00f, 2.00f, 1.00f, 0.01f, 1.00f},
            {1.50f, 2.50f, 2.00f, 0.01f, 2.00f},
            {0.75f, 2.00f, 0.00f, 1.50f, 3.00f},
            {1.00f, 2.00f, 1.00f, 1.50f, 4.00f},
    };

    boolean[] bulletsStatus3 = {
            false,true,false,false,false,false
    };

    public ShooterTrigger() {
        super();
        this.setRegistryName(TechnicalItemsMod.MOD_ID, this.name);
        this.setCreativeTab(TechnicalItemsMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(20);
        this.cooltimeToChangeMode = 0;
        ForgeRegistries.ITEMS.register(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        Integer mode;
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt != null) {
            mode = nbt.getInteger("mode");
            tooltip.add("§r§1§lCost: §r" + String.valueOf(bulletsStatus2[mode-1][4]));
            tooltip.add("§r§1§lSpeed: §r" + String.valueOf(bulletsStatus2[mode-1][0]));
            tooltip.add("§r§1§lDamage: §r" + String.valueOf(bulletsStatus2[mode-1][1]));
            tooltip.add("§r§1§lGuided: §r" + String.valueOf(bulletsStatus2[mode-1][2]));
            tooltip.add("§r§1§lExplosion: §r" + String.valueOf(bulletsStatus2[mode-1][3]));
            tooltip.add("§r§1§lPenetration: §r" + String.valueOf(bulletsStatus3[mode-1]));
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        Item item, itemOffHand;
        ItemStack itemStack, itemStackOffHand;
        double x,y,z,x2,y2,z2;
        float rotationYaw,rotationPitch,temp;
        EntityGuidedBullet bullet;
        NBTTagCompound mainNBT = null;
        int bulletMode = 0, bulletCost = 0, bulletPos = 0;

        itemStack = playerIn.getHeldItem(handIn);
        itemStackOffHand = playerIn.getHeldItemOffhand();

        item = itemStack.getItem();
        itemOffHand = itemStackOffHand.getItem();
        mainNBT = itemStack.getTagCompound();
        if (mainNBT == null) {
            mainNBT = new NBTTagCompound();
            itemStack.setTagCompound(mainNBT);
        }
        if (mainNBT.getInteger("mode") == 0) {
            mainNBT.setInteger("mode", 1);
        }
        bulletMode = mainNBT.getInteger("mode");
        if (mainNBT.getInteger("cost") == 0) {
            mainNBT.setInteger("cost", 1);
        }
        bulletCost = mainNBT.getInteger("cost");
        if (mainNBT.getInteger("pos") == 0) {
            mainNBT.setInteger("pos", 1);
        }

        if (playerIn.isSneaking() == true) {
            // メインハンド処理
            if (item instanceof ShooterTrigger) {

                boolean couldShootBullet = false;
                bulletPos = mainNBT.getInteger("pos");
                if (itemStack.getMaxDamage() > (itemStack.getItemDamage() + bulletCost)) {
                    couldShootBullet = true;
                    if (bulletPos == 1) {
                        temp = 90;
                        mainNBT.setInteger("pos", 2);
                    } else {
                        temp = -90;
                        mainNBT.setInteger("pos", 1);
                    }
                    x = playerIn.posX - 0.4 * Math.sin(TMath.addRotationYawNumRad(playerIn, (float) temp));
                    y = playerIn.posY + 1.2d + Math.sin(-TMath.getRotationPitchRad(playerIn));
                    z = playerIn.posZ + 0.4 * Math.cos(TMath.addRotationYawNumRad(playerIn, (float) temp));

                    bullet = new EntityGuidedBullet(worldIn, playerIn, x, y, z, playerIn.getRotationYawHead(), playerIn.rotationPitch);
                    bullet.particleGuiding = EnumParticleTypes.END_ROD;
                    bullet.particleNotGuiding = EnumParticleTypes.END_ROD;
                    bullet.particleDead = EnumParticleTypes.LAVA;
                    bullet = setBulletStatus(bullet, bulletMode);
                    worldIn.spawnEntity((Entity) bullet);

                    itemStack.damageItem(bulletCost, playerIn);
                }
                if (!couldShootBullet){
                    playerIn.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2.0f, 1.0f);
                }else {
                    playerIn.playSound(soundShoot, 1.0f, 1.0f);
                }
            }

            // オフハンド処理

        }else{
            if (this.cooltimeToChangeMode == 0){
                int f1, cost;
                String displayName;
                f1 = itemStack.getMaxDamage() - itemStack.getItemDamage();
                bulletMode += 1;
                if(bulletMode > 6)
                    bulletMode = 1;
                if (mainNBT != null)
                    mainNBT.setInteger("mode",bulletMode);
                if(!worldIn.isRemote) {
                    displayName = bulletsStatus1[bulletMode-1];
                    cost = (int) bulletsStatus2[bulletMode-1][4];
                    System.out.print(displayName);
                    System.out.print(cost);
                    mainNBT.setString("name",displayName);
                    mainNBT.setInteger("cost",cost);
                    itemStack.setStackDisplayName("§r" + displayName + " §1<" + String.valueOf(f1) + ">");
                }else{
                    ITooltipFlag tip = ITooltipFlag.TooltipFlags.NORMAL;
                    List<String> list = Lists.<String>newArrayList();
                    this.addInformation(itemStack, worldIn, list, tip);
                }
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
    }

    private EntityGuidedBullet setBulletStatus(EntityGuidedBullet bullet, int bulletMode) {
        bullet.setBulletStatus(
                bulletsStatus2[bulletMode-1][0],
                bulletsStatus2[bulletMode-1][1],
                bulletsStatus2[bulletMode-1][2],
                bulletsStatus2[bulletMode-1][3],
                bulletsStatus3[bulletMode-1]
        );
        return bullet;
    }

    int tickToHealItem;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!worldIn.isRemote) {
            if (this.cooltimeToChangeMode > 0) {
                this.cooltimeToChangeMode -= 1;
            }
        }

        EntityPlayer entity = (EntityPlayer) entityIn;

        if (isSelected || stack == entity.getHeldItemOffhand()) {
            this.tickToHealItem += 1;
            this.cooltimeToChangeName += 1;
            if (this.tickToHealItem >= 40 && stack.getItemDamage() >= 1) {
                if (!worldIn.isRemote) {
                    stack.setItemDamage(getDamage(stack) - 1);
                    this.tickToHealItem = 0;
                    entityIn.playSound(soundHealDamage, 1.0f, 0.5f);
                    worldIn.playSound((EntityPlayer) entityIn, entityIn.getPosition(),soundHealDamage,SoundCategory.PLAYERS,0.5f,1.0f);
                }
            }


            NBTTagCompound nbt = stack.getTagCompound();
            if(nbt != null){
                if(nbt.getString("name") != null) {
                    String bulletName = "§r" + nbt.getString("name") + "§1<" + String.valueOf(stack.getMaxDamage()-stack.getItemDamage()) + ">";
                    if(stack.getDisplayName() != bulletName) {
                        stack.setStackDisplayName(bulletName);
                    }
                }
            }
        }
    }
}
