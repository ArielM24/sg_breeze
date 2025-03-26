package com.sg.breeze;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.BreezeEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Difficulty;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SGBreeze implements ModInitializer {
	public static final String MOD_ID = "sg-breeze";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Random r = new Random();

	public static final int spawnRatio = 85;

	@Override
	public void onInitialize() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
			if (!entity.getWorld().getDifficulty().equals(Difficulty.HARD)) {
				return;
			}
			if (!entity.getType().equals(EntityType.BLAZE)) {
				return;
			}
			BlazeEntity blaze = (BlazeEntity) entity;
			NbtElement check = ((IEntityDataSaver)blaze).getSGBreezePersistentData().get("breeze_spawn");
			if(check != null){
				return;
			}
			((IEntityDataSaver)blaze).getSGBreezePersistentData().putString("breeze_spawn", "checked");
			boolean willSpawn = r.nextInt(100) <= spawnRatio;
			if (!willSpawn) {
				return;
			}
			int xOffset = 1 + r.nextInt(2);
			int zOffset = 1 + r.nextInt(2);
			xOffset = r.nextBoolean() ? xOffset : -xOffset;
			zOffset = r.nextBoolean() ? zOffset : -zOffset;
			BlockPos pos = blaze.getBlockPos();
			BlockPos spawnPos = new BlockPos(pos.getX() + xOffset, pos.getY(), pos.getZ() + zOffset);
			BlockPos floor = spawnPos.offset(Direction.DOWN);
			BlockPos air = spawnPos.offset(Direction.UP, 1);
			if(!blaze.getWorld().getBlockState(floor).isOpaqueFullCube()){
				return;
			};
			if(!blaze.getWorld().getBlockState(spawnPos).isAir()){
				return;
			};
			if(!blaze.getWorld().getBlockState(air).isAir()){
				return;
			};
			BreezeEntity breeze = EntityType.BREEZE.create(serverLevel, SpawnReason.NATURAL);
			breeze.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
			serverLevel.spawnNewEntityAndPassengers(breeze);
		});
	}
}