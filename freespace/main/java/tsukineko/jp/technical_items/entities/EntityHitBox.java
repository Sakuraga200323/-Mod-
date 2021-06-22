package tsukineko.jp.technical_items.entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import tsukineko.jp.technical_items.TMath;

import javax.annotation.Nullable;
import java.util.*;

public class EntityHitBox extends Entity {
    private static final Predicate<Entity> HITBOX_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });



    public EntityPlayer shooter;
    public float speed;
    public float damage;
    public float explosionSize;
    public int livingTimeCount = 0;
    private int timeInGround;
    private int ticksInAir;
    public int liveTimeLimit = 300;
    public double renderDistanceWeight;
    private boolean inGround;
    public boolean updateFlag;
    public boolean guidedFlag;
    public boolean canAttackEntities;
    public Explosion explosionIn;
    public EnumParticleTypes particleA = EnumParticleTypes.END_ROD;
    public EnumParticleTypes particleB = EnumParticleTypes.EXPLOSION_NORMAL;
    public EnumParticleTypes particleC = EnumParticleTypes.EXPLOSION_NORMAL;
    public float particleAspeed = 0.5f;
    public float particleBspeed = 0.5f;
    public float particleCspeed = 0.5f;
    public int particleAnum = 1;
    public int particleBnum = 1;
    public int particleCnum = 1;
    
    public EntityHitBox(World worldIn) {
        super(worldIn);
    }

    public EntityHitBox(World worldIn, EntityPlayer shooter) {
        super(worldIn);
        this.shooter = shooter;
    }

    public EntityHitBox(World worldIn, EntityPlayer shooter, double x, double y, double z) {
        super(worldIn);
        this.shooter = shooter;
        this.setPosition(x, y, z);
    }

    public EntityHitBox(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
    }

    public EntityHitBox(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch, float speed, float damage) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
        setHitBoxStatus(speed,damage);
    }

    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public void setHitBoxStatus(float speed,float damage){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = 0.0f;
    }

    public void setHitBoxStatus(float speed,float damage,float sizeA, float sizeB){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = 0.0f;
        this.setSize(sizeA, sizeB);
    }

    public void setEnitySize(float a, float b){
        this.setSize(a, b);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {

        int hitEntityNum = 0;
        if(!(this.world.isRemote || (this.shooter == null || this.shooter.isDead)) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.onUpdate();
            this.livingTimeCount++;
            ++this.ticksInAir;
            if (this.livingTimeCount > this.liveTimeLimit) {
                this.setDead();
            }

            // Entityの場所からブロックを取得
            BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            // 空気以外なら≪inGround≫をtrueに
            if (iblockstate.getMaterial() != Material.AIR) {
                AxisAlignedBB axisalignedbb = iblockstate.getCollisionBoundingBox(this.world, blockpos);
                if (axisalignedbb != Block.NULL_AABB && axisalignedbb.offset(blockpos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
                    this.inGround = true;
                }
            }

            if(!this.inGround){
                
                Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
                
                float rotationYawRad = TMath.extendsToRadian(this.rotationYaw);
                float rotationPitchRad = TMath.extendsToRadian(this.rotationPitch);

                if(this.particleA != null) {
                    ((WorldServer) this.world).spawnParticle(
                            this.particleA, true,
                            this.posX, this.posY, this.posZ,
                            this.particleAnum,
                            0.0D, 0.0D, 0.0D,
                            this.particleAspeed, 2
                    );
                }
                
                this.posX -= this.speed * Math.sin(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                this.posZ += this.speed * Math.cos(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                this.posY += this.speed * Math.sin(-TMath.extendsToRadian(this.rotationPitch));
                
                Vec3d vec3d2 = new Vec3d(this.posX, this.posY, this.posZ);

                RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);

                if (raytraceresult != null) {
                    vec3d2 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
                }

                List<Entity> entities = this.findEntity(vec3d1, vec3d2);
                boolean hitted = false;
                if (entities != null) {
                    for (Entity entity : entities) {
                        if (entity != null) {
                            raytraceresult = new RayTraceResult(entity);
                        }

                        // 再代入したので、≪raytraceresult≫でEntityにぶつかるかどうかの判断
                        if (raytraceresult != null && raytraceresult.entityHit instanceof EntityLiving) {
                            // ちょっとよくわからん
                            if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                                //hitEntity(entity);
                                hitEntityNum += 1;
                                hitted = true;
                            }
                        }
                    }
                }
                // 本体を動かす処理
                this.setPositionAndRotation(
                        this.posX, this.posY, this.posZ,
                        TMath.extendsToRotation(rotationYawRad),
                        TMath.extendsToRotation(rotationPitchRad)
                );
            }
        }

        if( hitEntityNum > 0) {
            for (int i = 0; i <= hitEntityNum; ++i) {
                this.world.playSound(
                        this.shooter, this.shooter.getPosition(), SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1.0f, 12.0f
                );
            }
        }
    }


    public Explosion doExplosion(float size){
        return world.createExplosion((Entity) this.shooter, this.posX, this.posY, this.posZ, size, false);
    }

    protected void hitEntity(Entity e) {
        if (e != null) {
            EntityLivingBase entity = (EntityLivingBase) e;
            Entity attacker = (Entity) this.shooter;
            if (attacker == null)
                attacker = this;
            DamageSource damageSource;
            damageSource = DamageSource.causeIndirectDamage(this, (EntityPlayer) attacker);
            double x = entity.posX;
            double y = entity.posY;
            double z = entity.posZ;
            entity.attackEntityFrom(damageSource, this.damage/2);
            entity.attackEntityFrom(damageSource, this.damage/2);
            if(entity.isDead != true) {
            }
            if(this.particleB != null) {
                ((WorldServer) this.world).spawnParticle(
                        this.particleB,
                        true, this.posX, this.posY, this.posZ,
                        this.particleBnum,
                        0.0D, 0.0D, 0.0D, this.particleBspeed,
                        1
                );
            }
            this.world.playSound(this.shooter, this.shooter.getPosition(), SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS,1.0f,1.0f);
            entity.hurtResistantTime = 0;
        }
    }

    protected List<Entity> findEntity(Vec3d start, Vec3d end){
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(0.5D), HITBOX_TARGETS);
        List<Entity> resultList = new ArrayList<Entity>();
        double d0 = 0.0D;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity1 = list.get(i);

            if (entity1 != this.shooter || this.ticksInAir >= 5) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);

                if (raytraceresult != null) {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        resultList.add(entity1);
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
