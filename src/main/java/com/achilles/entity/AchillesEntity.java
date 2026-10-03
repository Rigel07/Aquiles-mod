package com.achilles.entity;

import com.achilles.registry.ModEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AchillesEntity extends Mob {
    private long nextSound;

    public AchillesEntity(EntityType<? extends AchillesEntity> type, Level level) {
        super(type, level);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide && tickCount > nextSound && random.nextInt(4) == 0) {
            nextSound = tickCount + 180 + random.nextInt(360);
            playSound(ModSoundEvents.AQUILLES_CUTE.get(), 0.75F, 0.85F + random.nextFloat() * 0.25F);
        }
        if (level().isClientSide && tickCount % 8 == 0) {
            // Pequeña torpeza: hace un saltito ocasional.
            if (random.nextInt(40) == 0) setDeltaMovement(getDeltaMovement().add(0, 0.18, 0));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() { return ModSoundEvents.AQUILLES_CUTE.get(); }
    @Override
    protected SoundEvent getHurtSound(net.minecraft.world.damagesource.DamageSource source) { return ModSoundEvents.AQUILLES_HURT.get(); }
    @Override
    protected SoundEvent getDeathSound() { return ModSoundEvents.AQUILLES_HURT.get(); }
}
