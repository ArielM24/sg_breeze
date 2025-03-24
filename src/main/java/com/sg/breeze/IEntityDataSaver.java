package com.sg.breeze;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver {
    NbtCompound getPersistentData();
}
