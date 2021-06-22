package tsukineko.jp.technical_items.entities;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tsukineko.jp.technical_items.TMath;


import javax.annotation.Nullable;
import java.util.List;

public class EntityGuidedBullet extends Entity implements IProjectile {

    private static final Predicate<Entity> AMMO_TARGETS = Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_.canBeCollidedWith();
        }
    });

    public EntityPlayer shooter;
    public float speed;
    public float damage;
    public float explosionSize;
    public float rotationMagnification;
    public int livingTimeCount = 0;
    public int livingTimeLimit = 300;
    public int particleDeadNum = 5;
    public float particleDeadSpeed = 0.1f;
    private int timeInGround;
    private int ticksInAir;
    public double renderDistanceWeight;
    private boolean inGround;
    public boolean updateFlag;
    public boolean guidedFlag;
    public boolean canAttackEntities;
    public Explosion explosionIn;
    public EnumParticleTypes particleGuiding = EnumParticleTypes.END_ROD;
    public EnumParticleTypes particleNotGuiding = EnumParticleTypes.EXPLOSION_NORMAL;
    public EnumParticleTypes particleDead = EnumParticleTypes.EXPLOSION_NORMAL;

    public EntityGuidedBullet(World worldIn) {
        super(worldIn);
    }

    public EntityGuidedBullet(World worldIn, EntityPlayer shooter) {
        super(worldIn);
        this.shooter = shooter;
    }

    public EntityGuidedBullet(World worldIn, EntityPlayer shooter, double x, double y, double z) {
        super(worldIn);
        this.shooter = shooter;
        this.setPosition(x, y, z);
    }

    public EntityGuidedBullet(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
    }

    public EntityGuidedBullet(World worldIn, EntityPlayer shooter, double x, double y, double z, float rotationYaw, float rotationPitch, float speed, float damage) {
        super(worldIn);
        this.shooter = shooter;
        this.setPositionAndRotation(x, y, z, rotationYaw, rotationPitch);
        setBulletStatus(speed,damage);
    }
    
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    public void setBulletStatus(float speed,float damage){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = 0.0f;
        this.rotationMagnification = 0.0f;
    }

    public void setBulletStatus(float speed,float damage, float rotationMagnification){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = 0.0f;
        this.rotationMagnification = rotationMagnification;
    }

    public void setBulletStatus(float speed, float damage, float rotationMagnification, float explosionSize){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = explosionSize;
        this.rotationMagnification = rotationMagnification;
    }

    public void setBulletStatus(float speed, float damage, float rotationMagnification, float explosionSize, boolean canAttackEntities){
        this.speed = speed;
        this.damage = damage;
        this.explosionSize = explosionSize;
        this.rotationMagnification = rotationMagnification;
        this.canAttackEntities = canAttackEntities;
    }

    public void setEnitySize(float a, float b){
        this.setSize(a, b);
    }

    @Override
    protected void entityInit() {
        this.explosionIn = null;
        this.setSize(0.25f, 0.25f);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {

    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    private float beRotationYaw, beRotationPitch;
    @Override
    public void onUpdate(){
        if(!(this.world.isRemote || (this.shooter == null || this.shooter.isDead)) && this.world.isBlockLoaded(new BlockPos(this))) {
            super.onUpdate();
            // 活動しているtick数の処理 300以上になったらパーティクルとともに消滅(explosionSizeが0以上にされていたら爆散)
            this.livingTimeCount++;
            ++this.ticksInAir;
            if (this.livingTimeCount > this.livingTimeLimit) {
                hitSomethingOrNaturalDead(this);
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

            // ↑で空気以外に触れているかの判別をしたので、条件分岐
            if (!this.inGround) { //触れていない場合
                // Entityのposからベクトルを作成
                Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);

                float rotationYawRad = TMath.extendsToRadian(this.rotationYaw);
                float rotationPitchRad = TMath.extendsToRadian(this.rotationPitch);

                // 発射者がスニークしているかどうか
                // 基本誘導弾はスニーク中のみ誘導可能な設定
                if (this.shooter.isSneaking() == true && this.rotationMagnification > 0.0f) { // スニーク中だった場合
                    // 誘導中なのを表すためにパーティクル≪END_ROD≫を生成
                    ((WorldServer) this.world).spawnParticle(
                            this.particleGuiding, true,
                            this.posX, this.posY, this.posZ,
                            1,
                            0.0D, 0.0D, 0.0D,
                            0.0D, 1
                    );
                    // 発射者の頭の向きをそのまま適応
                    // 向きの各sin、cosに≪speed≫を掛けて速さを変えられるようにしている
                    // 大体は1.0fとかが一番制御しやすい
                    rotationYawRad = (
                            TMath.extendsToRadian(this.rotationYaw)
                            + TMath.fixRotationYaw(this.rotationMagnification * TMath.addRotationYawNumRad(this.shooter, -this.rotationYaw))
                    );
                    rotationPitchRad = (
                            TMath.extendsToRadian(this.rotationPitch)
                            + TMath.fixRotationPitch(this.rotationMagnification * TMath.addRotationPitchNumRad(this.shooter, -this.rotationPitch))
                    );
                    this.posX -= this.speed * Math.sin(rotationYawRad) * MathHelper.cos(rotationPitchRad);
                    this.posY += this.speed * Math.sin(-rotationPitchRad);
                    this.posZ += this.speed * Math.cos(rotationYawRad) * MathHelper.cos(rotationPitchRad);
                } else { // スニークしてなかった場合
                    ((WorldServer) this.world).spawnParticle(
                            this.particleNotGuiding, true,
                            this.posX, this.posY, this.posZ,
                            1,
                            0.0D, 0.0D, 0.0D,
                            0.0D,2
                    );
                    // 誘導しない　つまりまっすぐ進むので、Entityの向きを使ってそのまま計算、posに加算
                    this.posX -= this.speed * Math.sin(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                    this.posZ += this.speed * Math.cos(TMath.extendsToRadian(this.rotationYaw)) * MathHelper.cos(TMath.extendsToRadian(this.rotationPitch));
                    this.posY += this.speed * Math.sin(-TMath.extendsToRadian(this.rotationPitch));
                }

                // ポジションに加算が行われたので、ベクトル再生成
                // 一応代入する変数は≪vec3d2≫を別に作った
                Vec3d vec3d2 = new Vec3d(this.posX, this.posY, this.posZ);

                // よくわからんけど、今向いてる方向に何かしらがあってそれとぶつかるかの判定をしてくれるらしい
                RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d1, vec3d2, false, true, false);

                // ぶつかったばあい
                if (raytraceresult != null) {
                    // ≪vec3d2≫に、ぶつかる座標で作ったベクトルを再代入
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
                        // 自前の「raytraceresultでぶつかったEntityに処理を行う関数」を実行
                        this.hitEntity(raytraceresult.entityHit);
                        // ぶつかった場合このEntity自体どうなるかの処理、音とともに消える
                        if (!this.canAttackEntities)
                            hitSomethingOrNaturalDead(this);
                    }
                }
                // 本体を動かす処理
                this.setPositionAndRotation(
                        this.posX, this.posY, this.posZ,
                        TMath.extendsToRotation(rotationYawRad),
                        TMath.extendsToRotation(rotationPitchRad)
                );
            }else{
                // ぶつかった場合このEntity自体どうなるかの処理、音とともに消える
                hitSomethingOrNaturalDead(this);
            }
        }else{
            this.setDead();
        }
    }

    public Explosion doExplosion(float size){
        return world.createExplosion((Entity) this.shooter, this.posX, this.posY, this.posZ, size, false);
    }

    protected void hitEntity(Entity entity) {
        if (entity != null) {
            Entity attacker = (Entity) this.shooter;
            if (attacker == null)
                attacker = this;
            DamageSource damageSource;
            if(this.explosionSize > 0.0f) {
                explosionIn = doExplosion(this.explosionSize);
                damageSource = DamageSource.causeExplosionDamage(explosionIn);
            }else{
                damageSource = DamageSource.causePlayerDamage((EntityPlayer) attacker);
            }
            entity.attackEntityFrom(damageSource, this.damage);
            ((WorldServer) this.world).spawnParticle(
                    this.particleDead,
                    true, this.posX, this.posY, this.posZ,
                    5,
                    0.0D, 0.0D, 0.0D, 0.5D,
                    1
            );
            this.world.playSound(this.shooter, this.shooter.getPosition(),SoundEvents.BLOCK_ANVIL_USE,SoundCategory.PLAYERS,1.0f,1.0f);
            entity.hurtResistantTime = 0;
            ((EntityLiving) entity).knockBack(
                    this.shooter, 0.5f,
                    Math.sin(this.rotationYaw / 180.0F * (float) Math.PI),
                    -Math.cos(this.rotationYaw / 180.0F * (float) Math.PI)
            );
        }
    }

    protected void hitSomethingOrNaturalDead(EntityGuidedBullet selfEntity){
        double x=0,y=0,z=0;
        float rotationYawRad = TMath.extendsToRadian(this.rotationYaw);
        float rotationPitchRad = TMath.extendsToRotation(this.rotationPitch);
        x += 1.5*Math.sin(rotationYawRad) * MathHelper.cos(rotationPitchRad);
        y += -1.5*Math.sin(-rotationPitchRad);
        z += -1.5*Math.cos(rotationYawRad) * MathHelper.cos(rotationPitchRad);
        // ≪particleDead≫パーティクル
        ((WorldServer) this.world).spawnParticle(this.particleDead, true, this.posX, this.posY, this.posZ, this.particleDeadNum, 0.0D, 0.0D, 0.0D, this.particleDeadSpeed,2 );
        // 爆発サイズがあれば爆発するやつ
        if (selfEntity.explosionSize > 0.0f && explosionIn == null)
            explosionIn = selfEntity.doExplosion(selfEntity.explosionSize);
        // ぶつかったsound≪E_PARROT_IM_BLAZE≫を作成、再生
        SoundEvent soundIn = SoundEvents.E_PARROT_IM_BLAZE;
        selfEntity.shooter.playSound(soundIn, 1.0f, 1.0f);
        // 消滅
        selfEntity.setDead();
        this.explosionIn = null;
    }


    @Nullable
    protected Entity findEntityOnPath(Vec3d start, Vec3d end){
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(2*(double) this.speed), AMMO_TARGETS);
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
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
            d0 = 1.0D;

        d0 = d0 * 64.0D * this.renderDistanceWeight;
        return distance < d0 * d0;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        return 15728880;
    }
}
