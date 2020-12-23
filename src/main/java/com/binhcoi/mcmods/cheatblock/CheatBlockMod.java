package com.binhcoi.mcmods.cheatblock;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CheatBlockMod implements ModInitializer {
    
    public static final String MOD_ID = "cheatblock";
    public static final Block CHEAT_BLOCK = new CheatBlock(net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));

    @Override
    public void onInitialize() {
        System.out.println("Hello world! CHEATING");
        Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"cheat_block"),CHEAT_BLOCK);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"cheat_block"),new BlockItem(CHEAT_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        PlayerBlockBreakEvents.BEFORE.register(new TreeFallHandler());
    }
}
