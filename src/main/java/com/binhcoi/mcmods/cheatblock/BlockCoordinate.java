package com.binhcoi.mcmods.cheatblock;

import net.minecraft.util.math.BlockPos;

public class BlockCoordinate {
    public int x;
    public int y;
    public int z;
    public BlockPos pos;
    public int layer;

    public BlockCoordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        pos = new BlockPos(x, y, z);
        layer = 0;
    }

    public BlockCoordinate(BlockPos pos) {
        this.pos = pos;
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        layer = 0;
    }

    public BlockCoordinate(BlockPos pos, int layer) {
        this.pos = pos;
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        this.layer = layer;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlockCoordinate other = (BlockCoordinate) obj;
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    @Override
    public int hashCode() {
        return (y + z * 31) * 31 + x;
    }
}