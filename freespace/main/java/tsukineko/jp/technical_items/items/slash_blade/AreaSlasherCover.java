package tsukineko.jp.technical_items.items.slash_blade;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
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


public class AreaSlasherCover extends ItemTool{

    public AreaSlasherCover(String name, ToolMaterial material) {
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

    public int coolTime = 0;
    public int slashAreaRotation = 360;
    public int slashNum = 8;
    public boolean flag = false;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn){
        if(worldIn.isRemote) {
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1.0f, 12.0f);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    int tempCount1 = 0;
    int tempCount2 = 0;
    int tempCount3 = 0;
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
    }
}
