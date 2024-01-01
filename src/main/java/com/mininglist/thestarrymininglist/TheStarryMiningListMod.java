package com.mininglist.thestarrymininglist;

import com.mininglist.thestarrymininglist.commands.TheStarryMiningListCommand;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.server.MinecraftServer;

import static com.mininglist.thestarrymininglist.TheStarryMiningList.fancyName;

public class TheStarryMiningListMod {
    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(TheStarryMiningListMod::onServerStarted);
    }

    private static void onServerStarted(MinecraftServer server) {
        TheStarryMiningList.LOGGER.info(fancyName + " loaded!");
        TheStarryMiningListCommand.isScoreboardVisible = true;
        TheStarryMiningListCommand.isVisible();
    }
}
