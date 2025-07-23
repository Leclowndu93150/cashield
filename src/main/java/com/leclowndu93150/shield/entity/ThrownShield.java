package com.leclowndu93150.shield.entity;

import com.leclowndu93150.shield.Shield;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class ThrownShield extends AbstractArrow {

    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownShield.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(ThrownShield.class, EntityDataSerializers.BOOLEAN);

    private ItemStack shieldItem = new ItemStack(Shield.CAPTAIN_AMERICA_SHIELD.get());
    private boolean dealtDamage;
    private int ticksInAir;
    private static final int MAX_FLIGHT_TIME = 50; // 2.5 seconds

    public ThrownShield(EntityType<? extends ThrownShield> type, Level level) {
        super(type, level);
        this.pickup = Pickup.DISALLOWED;
        this.setNoGravity(true);
    }

    public ThrownShield(Level level, LivingEntity shooter, ItemStack stack) {
        super(Shield.THROWN_SHIELD.get(), shooter, level);
        this.shieldItem = stack.copy();
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.pickup = Pickup.DISALLOWED;
        this.setNoGravity(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
        this.entityData.define(RETURNING, false);
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();

        if (!this.level.isClientSide) {
            ticksInAir++;

            boolean shouldReturn = this.entityData.get(RETURNING);

            if (!shouldReturn && (ticksInAir >= MAX_FLIGHT_TIME || this.dealtDamage || this.inGround)) {
                this.entityData.set(RETURNING, true);
                shouldReturn = true;
            }

            if (shouldReturn && owner != null) {
                if (!isOwnerAlive()) {
                    if (this.pickup == Pickup.ALLOWED) {
                        this.spawnAtLocation(this.getPickupItem(), 0.1F);
                    }
                    this.discard();
                } else {
                    this.setNoPhysics(true);
                    Vec3 vec = owner.getEyePosition().subtract(this.position());
                    this.setPosRaw(this.getX(), this.getY() + vec.y * 0.015 * 2, this.getZ());

                    double speed = 0.05 * 3.0;
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec.normalize().scale(speed)));

                    if (this.distanceToSqr(owner) < 9.0) {
                        if (owner instanceof Player player) {
                            if (player.getAbilities().instabuild || player.getInventory().add(this.getPickupItem())) {
                                player.take(this, 1);
                                this.playSound(SoundEvents.ITEM_PICKUP, 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                                this.discard();
                            }
                        }
                    }
                }
            }
        }

        super.tick();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float damage = 12.0F;

        Entity owner = this.getOwner();
        DamageSource damageSource = DamageSource.thrown(this, owner == null ? this : owner);

        if (entity.hurt(damageSource, damage)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (owner instanceof LivingEntity) {
                    this.doPostHurtEffects(livingEntity);
                }

                double knockback = 1.5;
                Vec3 vec = this.getDeltaMovement().multiply(1.0, 0.0, 1.0).normalize().scale(knockback);
                if (vec.lengthSqr() > 0.0) {
                    livingEntity.push(vec.x, 0.1, vec.z);
                }
            }
        }

        this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        this.dealtDamage = true;
        this.entityData.set(RETURNING, true);

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.1, -0.1, -0.1));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
        this.entityData.set(RETURNING, true);
        this.inGround = false;

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.multiply(-0.5, -0.5, -0.5));
    }

    @Nullable
    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return this.dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected ItemStack getPickupItem() {
        return this.shieldItem.copy();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.SHIELD_BLOCK;
    }

    private boolean isOwnerAlive() {
        Entity owner = this.getOwner();
        if (owner != null && owner.isAlive()) {
            return !(owner instanceof ServerPlayer) || !owner.isSpectator();
        }
        return false;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Shield", 10)) {
            this.shieldItem = ItemStack.of(compound.getCompound("Shield"));
        }
        this.dealtDamage = compound.getBoolean("DealtDamage");
        this.ticksInAir = compound.getInt("TicksInAir");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("Shield", this.shieldItem.save(new CompoundTag()));
        compound.putBoolean("DealtDamage", this.dealtDamage);
        compound.putInt("TicksInAir", this.ticksInAir);
    }

    @Override
    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            if (!this.level.isClientSide && this.entityData.get(RETURNING) && this.pickup == Pickup.ALLOWED) {
                player.take(this, 1);
                this.discard();
            }
        }
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}