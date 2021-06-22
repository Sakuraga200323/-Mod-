package tsukineko.jp.technical_items.items.slash_blade;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import tsukineko.jp.technical_items.TMath;
import tsukineko.jp.technical_items.TechnicalItemsMod;
import tsukineko.jp.technical_items.TechnicalItemsModTab;
import tsukineko.jp.technical_items.entities.EntityGuidedBullet;
import tsukineko.jp.technical_items.entities.EntityHitBox;
import tsukineko.jp.technical_items.entities.EntityOrigin;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class AreaSlasherCompletery extends ItemTool{

    public AreaSlasherCompletery(String name, ToolMaterial material) {
        super(0.0f, 2.0f, material,
                new Set<Block>() {
                    @Override
                    public int size() {
                        return 0;
                    }
                    @Override
                    public boolean isEmpty() {
                        return false;
                    }
                    @Override
                    public boolean contains(Object o) {
                        return false;
                    }
                    @Override
                    public Iterator<Block> iterator() {
                        return null;
                    }
                    @Override
                    public Object[] toArray() {
                        return new Object[0];
                    }
                    @Override
                    public <T> T[] toArray(T[] a) {
                        return null;
                    }
                    @Override
                    public boolean add(Block block) {
                        return false;
                    }
                    @Override
                    public boolean remove(Object o) {
                        return false;
                    }
                    @Override
                    public boolean containsAll(Collection<?> c) {
                        return false;
                    }
                    @Override
                    public boolean addAll(Collection<? extends Block> c) {
                        return false;
                    }
                    @Override
                    public boolean retainAll(Collection<?> c) {
                        return false;
                    }
                    @Override
                    public boolean removeAll(Collection<?> c) {
                        return false;
                    }
                    @Override
                    public void clear() {

                    }
                }
        );
        this.setCreativeTab(TechnicalItemsMod.MODS_CREATIVE_TAB);
        this.setTranslationKey(name);
        this.setRegistryName(TechnicalItemsMod.MOD_ID, name);
        ForgeRegistries.ITEMS.register(this);
    }


    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer playerIn, Entity entity) {
        entity.hurtResistantTime = 0;
        double x = playerIn.posX - 0.4 * Math.sin(TMath.getRotationYawRad(playerIn))*Math.sin(-TMath.getRotationPitchRad(playerIn));
        double y = playerIn.posY + 0.8d + Math.sin(-TMath.getRotationPitchRad(playerIn));
        double z = playerIn.posZ + 0.4 * Math.cos(TMath.getRotationYawRad(playerIn))*Math.sin(-TMath.getRotationPitchRad(playerIn));

        playerIn.world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK,x,y,z,0,0,0,0);
        return false;
    }


    public int coolTime = 0;
    public int slashAreaRotation = 360;
    public int slashNum = 8;
    public boolean flag = false;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        ItemStack mainStack = playerIn.getHeldItemMainhand();
        ItemStack offStack = playerIn.getHeldItemOffhand();
        if(offStack.getItem() == this){
            int damageNum = offStack.getItemDamage();
            playerIn.inventory.deleteStack(offStack);
            ItemStack newMainStack = new ItemStack(TechnicalItemsMod.Items.AREA_SRASHER);
            ItemStack newOffStack = new ItemStack(TechnicalItemsMod.Items.AREA_SRASHER_COVER);
            newMainStack.damageItem(damageNum,playerIn);
            newOffStack.damageItem(damageNum,playerIn);
            playerIn.addItemStackToInventory(newMainStack);
            playerIn.addItemStackToInventory(newOffStack);
            playerIn.inventory.setInventorySlotContents(5,offStack);
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.MASTER, 1.0f, 12.0f);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    int tempCount = 0;
    int tempCount2 = 0;
    int tempCount3 = 0;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
    }
}
