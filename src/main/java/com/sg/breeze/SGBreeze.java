package com.sg.breeze;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGBreeze implements ModInitializer {
	public static final String MOD_ID = "sg-breeze";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Random r = new Random();

	public static final int spawnRatio = 50;

	@Override
	public void onInitialize() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
			if (!entity.getWorld().getDifficulty().equals(Difficulty.HARD)) {
				return;
			}
			if (!entity.getType().equals(EntityType.BLAZE)) {
				return;
			}
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			BlazeEntity blaze = (BlazeEntity) entity;
			BreezeEntity breeze = EntityType.BREEZE.create(serverLevel, SpawnReason.NATURAL);
			breeze.setPos(blaze.getPos().getX(), blaze.getPos().getY(), blaze.getPos().getZ());
			serverLevel.spawnNewEntityAndPassengers(breeze);
		});
	}
}