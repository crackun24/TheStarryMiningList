package com.mininglist.thestarrymininglist;

import com.mininglist.thestarrymininglist.commands.TheStarryMiningListCommand;

import com.mininglist.thestarrymininglist.dataType.Msg;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.server.MinecraftServer;

public class TheStarryMiningListMod {
    public static void init() {
        ServerLifecycleEvents.SERVER_STARTED.register(TheStarryMiningListMod::onServerStarted);
    }

    private static void onServerStarted(MinecraftServer server) {
        TheStarryMiningList.LOGGER.info(Msg.ANSI_GREEN + "TheStarryMiningList加载成功!" + Msg.ANSI_RESET);
        TheStarryMiningListCommand.isScoreboardVisible = true;
        TheStarryMiningListCommand.isVisible();
    }
}
