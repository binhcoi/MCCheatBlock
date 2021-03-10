package com.binhcoi.mcmods.cheatblock;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public class DupBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, SidedInventory, Tickable {

    private DefaultedList<ItemStack> inventory;
    private int dupTime;
    private final int dupTimeTotal=10;

    public static final int MOLD_SLOTS = 0;
    public static final int FUEL_SLOTS = 1;
    public static final int PROD_SLOTS = 2;

    public DupBlockEntity() {
        super(CheatBlockMod.DUP_BLOCK_ENTITY);
        inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        dupTime=0;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        Inventories.toTag(tag, inventory);
        tag.putShort("DupTime", (short)dupTime);
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, inventory);
        dupTime = tag.getShort("DupTime");
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (world.getBlockEntity(pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void tick() {
        boolean dirty = false;
        if (!world.isClient) {
            ItemStack moldItemStack = inventory.get(MOLD_SLOTS);
            ItemStack prodItemStack = inventory.get(PROD_SLOTS);
            ItemStack fuelItemStack = inventory.get(FUEL_SLOTS);
            if (!moldItemStack.isEmpty() && !fuelItemStack.isEmpty()) {
                if (dupTime < dupTimeTotal)
                    dupTime++;
                else {
                    dupTime = 0;
                    if (prodItemStack.isEmpty()) {
                        inventory.set(PROD_SLOTS, new ItemStack(moldItemStack.getItem()));
                        fuelItemStack.decrement(1);
                        dirty = true;
                    } else if (prodItemStack.getItem() == moldItemStack.getItem()
                            && prodItemStack.getCount() <= prodItemStack.getMaxCount()) {
                        fuelItemStack.decrement(1);
                        prodItemStack.increment(1);
                        dirty = true;
                    }
                }

            }

        }
        if (dirty)
            markDirty();
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN)
            return new int[] { PROD_SLOTS };
        return new int[] { FUEL_SLOTS };
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        return slot == FUEL_SLOTS;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == PROD_SLOTS;
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new DupBlockScreenHandler(syncId, inv, this);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
}
