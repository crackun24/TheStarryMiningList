package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardManager;

import java.util.Objects;

import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

public class ServerStartedEvent {

    public static void onServerStarted(MinecraftServer server, SBConfig config) {
        scoreboard = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getScoreboard();

        //创建挖掘榜与放置榜的计分板对象（如果不存在的话）
        BoardManager.ensureObjectives(config);

        //读取上次保存的榜单类型，并把它显示到侧边栏
        BoardManager.setCurrentType(BoardManager.loadFromConfig(config));
        BoardManager.applyGlobal(server, config);
    }
}
