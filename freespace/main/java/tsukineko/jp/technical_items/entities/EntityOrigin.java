package tsukineko.jp.technical_items.entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import tsukineko.jp.technical_items.TMath;

import javax.annotation.Nullable;
import java.util.List;

public class EntityOrigin extends Entity {
    private static final Predicate<Entity> HITBOX_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });
    public EntityPlayer shooter;
    public float speed;
    public int livingTimeCount = 0;
    private int timeInGround;
    private int ticksInAir;
    public int slashNum = 4;
    public double renderDistanceWeight;
    private boolean inGround;
    public boolean updateFlag;
    public boolean guidedFlag;
    public EnumParticleTypes particleA = null;
    public EnumParticleTypes particleB = EnumParticleTypes.END_ROD;
    public float particleBspeed = 0;
    public EnumParticleTypes particleC = EnumParticleTypes.END_ROD;
    public double particleAspeed = 0.0f;


    public EntityOrigin(World worldIn) {
        super(worldIn);
    }

    public EntityOrigin(World worldIn, EntityPlayer shooter) {
        super(worldIn);
        this.shooter = shooter;
    }

    public EntityOrigin(World worldIn, EntityPlayer shooter, double x, double y, double z) {
        super(worldIn);
        this.shooter = shooter;
        this.setPosition(x, y, z);
    }

    public EntityOrigin(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
    }

    public EntityOrigin(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch, float speed) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
        this.speed = speed;
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        if(!(this.world.isRemote || (this.shooter == null || this.shooter.isDead)) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.onUpdate();
            this.livingTimeCount++;
            ++this.ticksInAir;
            if (this.livingTimeCount > 20) {
                hit();
            }

            if (this.particleA != null){

                ((WorldServer) this.world).spawnParticle(
                        this.particleA, true,
                        this.posX, this.posY, this.posZ,
                        1,
                        0.0D, 0.0D, 0.0D,
                        this.particleAspeed,2
                );

            }

            boolean hitted = false;


            // Entityの場所からブロックを取得
            BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            // 空気以外なら≪inGround≫をtrueに
            if (iblockstate.getMaterial() != Material.AIR) {
                AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);
                if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                    this.inGround = true;
                    hitted = true;
                }
            }

            if(!this.inGround){

                Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);

                float rotationYawRad = TMath.extendsToRadian(this.rotationYaw);
                float rotationPitchRad = Math.min(TMath.extendsToRadian(90),TMath.extendsToRadian(this.rotationPitch+1.0f));

                this.posX -= this.speed * Math.sin(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                this.posZ += this.speed * Math.cos(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                this.posY += this.speed * Math.sin(-TMath.extendsToRadian(this.rotationPitch));

                Vec3d vec3d2 = new Vec3d(this.posX, this.posY, this.posZ);

                RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);

                if (raytraceresult != null) {
                    vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
                }
                // ぶつかった場所と、そもそも動く前のベクトル間でEntityを取得
                Entity entity = this.findEntityOnPath(vec3d1, vec3d2);
                // Entityいた
                if (entity != null) {
                    // ぶつかるかどうか判別の≪raytraceresult≫にEntityとぶつかるかどうかの判別をしてくれるやつを再代入
                    // 相変わらずこれはちょっとわからん
                    raytraceresult = new RayTraceResult(entity);
                }

                // 再代入したので、≪raytraceresult≫でEntityにぶつかるかどうかの判断
                if (raytraceresult != null && raytraceresult.entityHit instanceof EntityLiving) {
                    // ちょっとよくわからん
                    if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                        if (raytraceresult.entityHit != this.shooter) {
                            hitted = true;
                        }
                    }
                }

                if(hitted == false){
                    // 本体を動かす処理
                    this.setPositionAndRotation(
                            this.posX, this.posY, this.posZ,
                            TMath.extendsToRotation(rotationYawRad),
                            TMath.extendsToRotation(rotationPitchRad)
                    );
                }
            }

            if (hitted == true || this.inGround == true) {
                hit();
            }
        }
    }

    protected void hit() {
        ((WorldServer) this.world).spawnParticle(
                this.particleB,
                true, this.posX, this.posY, this.posZ,
                30,
                0.0D, 0.0D, 0.0D, this.particleBspeed,
                0
        );
        double x = - Math.sin(TMath.extendsToRadian(this.rotationYaw));
        double y = Math.sin(-TMath.extendsToRadian(this.rotationPitch));
        double z = Math.cos(TMath.extendsToRadian(this.rotationYaw));
        World worldIn = this.world;
        EntityPlayer playerIn = this.shooter;
        float rotation = 360 / this.slashNum;
        for (int num = 0; num <= this.slashNum; ++num){
            EntityHitBox box, box2;
            box = new EntityHitBox(
                    worldIn,playerIn,this.posX - x*1.2,this.posY - y*1.2,this.posZ - z*1.2,
                    TMath.fixRotationYaw(this.rotationYaw+(num*rotation)),
                    TMath.fixRotationPitch(this.rotationPitch)
            );
            box2 = new EntityHitBox(
                    worldIn,playerIn,this.posX - x*1.2,this.posY - y*1.2,this.posZ - z*1.2,
                    TMath.fixRotationYaw(this.rotationYaw),
                    TMath.fixRotationPitch(this.rotationPitch+(num*rotation))
            );
            box.setHitBoxStatus(0.5f,4.0f,1.0f,1.0f);
            box2.setHitBoxStatus(0.5f,4.0f,1.0f,1.0f);
            box.setEnitySize(6.0f,6.0f);
            box2.setEnitySize(6.0f,6.0f);
            box.particleA = null;
            box2.particleA = null;
            box.particleB = EnumParticleTypes.SWEEP_ATTACK;
            box2.particleB = EnumParticleTypes.SWEEP_ATTACK;
            box.particleBspeed = 0.2f;
            box2.particleBspeed = 0.2f;
            box.particleBnum = 1;
            box2.particleBnum = 1;
            box.liveTimeLimit = 10;
            box2.liveTimeLimit = 10;
            worldIn.spawnEntity(box);
            worldIn.spawnEntity(box2);
        }
        this.setDead();
    }


    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end){
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(-this.motionX, -this.motionY, -this.motionZ).grow(10.0D),HITBOX_TARGETS);
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = list.get(i);

            if (entity1 != this.shooter || this.ticksInAir >= 5) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        return entity;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
