package com.binhcoi.mcmods.cheatblock;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class CheatBlockModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(CheatBlockMod.DUP_BLOCK_SCREEN_HANDLER, DupBlockScreen::new);
    }
    
}
