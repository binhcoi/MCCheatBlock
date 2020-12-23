package com.binhcoi.mcmods.cheatblock;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.Before;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TreeFallHandler implements Before {

    @Override
    public boolean beforeBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state,
            BlockEntity blockEntity) {
        if (!world.isClient()) {
            Block originalBlock = world.getBlockState(pos).getBlock();
            if (originalBlock.isIn(BlockTags.LOGS)) {
                ArrayList<BlockPos> treePos = scanTreeBlocks(pos, world);
                for (BlockPos blockPos : treePos) {
                    world.breakBlock(blockPos, true);
                }
            }

        }
        return true;
    }

    private ArrayList<BlockPos> scanTreeBlocks(BlockPos originalPos, World world) {
        ArrayList<BlockPos> treePos = new ArrayList<BlockPos>();
        treePos.add(originalPos);

        HashSet<BlockCoordinate> processed = new HashSet<BlockCoordinate>();
        processed.add(toBlockCoordinate(originalPos));

        Queue<BlockPos> potentialPos = new LinkedList<BlockPos>();

        getSurrounding(originalPos, processed, potentialPos);

        while (potentialPos.size() > 0) {
            BlockPos currentPos = potentialPos.remove();
            processed.add(toBlockCoordinate(currentPos));
            if (isTreePart(world.getBlockState(currentPos))) {
                treePos.add(currentPos);
                getSurrounding(currentPos, processed, potentialPos);
            }
        }
        return treePos;
    }

    private static final EnumSet<Direction> ALL_DIRECTIONS = EnumSet.allOf(Direction.class);

    private void getSurrounding(BlockPos pos, HashSet<BlockCoordinate> processed, Queue<BlockPos> potentialPos) {
        ALL_DIRECTIONS.forEach(direction -> {
            BlockPos temp = pos.offset(direction);
            BlockCoordinate coord = toBlockCoordinate(temp);
            if (!processed.contains(coord))
                potentialPos.add(temp);
        });

    }

    private BlockCoordinate toBlockCoordinate(BlockPos pos) {
        return new BlockCoordinate(pos.getX(), pos.getY(), pos.getZ());
    }

    private boolean isTreePart(BlockState state) {
        return state.isIn(BlockTags.LOGS) || state.isIn(BlockTags.LEAVES);
    }
}
