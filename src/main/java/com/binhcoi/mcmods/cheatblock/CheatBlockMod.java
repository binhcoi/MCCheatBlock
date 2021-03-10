package com.binhcoi.mcmods.cheatblock;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CheatBlockMod implements ModInitializer {
    
    public static final String MOD_ID = "cheatblock";

    public static final Block CHEAT_BLOCK = new CheatBlock(net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy(Blocks.CRAFTING_TABLE));
    public static final Block DUP_BLOCK = new DupBlock(net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings.copy(Blocks.DISPENSER));

    public static BlockEntityType<DupBlockEntity> DUP_BLOCK_ENTITY;
    public static ScreenHandlerType<DupBlockScreenHandler> DUP_BLOCK_SCREEN_HANDLER;

    @Override
    public void onInitialize() {
        System.out.println("Hello world! CHEATING");

        Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"cheat_block"),CHEAT_BLOCK);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"cheat_block"),new BlockItem(CHEAT_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
        Registry.register(Registry.BLOCK,new Identifier(MOD_ID,"dup_block"),DUP_BLOCK);
        Registry.register(Registry.ITEM,new Identifier(MOD_ID,"dup_block"),new BlockItem(DUP_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));

        DUP_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, MOD_ID, BlockEntityType.Builder.create(DupBlockEntity::new, DUP_BLOCK).build(null));
        DUP_BLOCK_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(new Identifier(MOD_ID,"dup_block"), DupBlockScreenHandler::new);
        PlayerBlockBreakEvents.BEFORE.register(new TreeFallHandler());
    }
}
