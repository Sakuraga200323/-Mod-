package tsukineko.jp.technical_items.items.slash_blade;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
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

public class Syusui extends ItemTool {

    public Syusui(String name, ToolMaterial material) {
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
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        entity.hurtResistantTime = 0;
        if (entity instanceof EntityArrow){
            ((WorldServer) player.world).spawnParticle(
                    EnumParticleTypes.SWEEP_ATTACK,
                    true, entity.posX, entity.posY, entity.posZ,
                    3,
                    0.0D, 0.0D, 0.0D, 0.5D,
                    1
            );
        }
        return false;
    }



    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if (!worldIn.isRemote){

            double x = playerIn.posX - 2 * Math.sin(TMath.getRotationYawRad(playerIn));
            double y = playerIn.posY + playerIn.getEyeHeight() + 2 * Math.sin(-TMath.getRotationPitchRad(playerIn)) - 0.2d;
            double z = playerIn.posZ + 2 * Math.cos(TMath.getRotationYawRad(playerIn));
            System.out.println(x);
            System.out.println(y);
            System.out.println(z);
            EntityOrigin box;
            box = new EntityOrigin(
                    worldIn,playerIn,x,y,z,
                    playerIn.getRotationYawHead(),playerIn.rotationPitch,0.25f
            );
            box.particleA = null;
            box.particleB = EnumParticleTypes.WATER_BUBBLE;
            box.particleBspeed = 0.125f;
            box.slashNum = 8;
            worldIn.spawnEntity(box);
        }else{
            worldIn.playSound(playerIn,playerIn.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.MASTER,1.0f,12.0f);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
