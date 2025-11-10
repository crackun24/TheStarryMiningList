package xyz.mcsls.starryMiningListRebuilt.Server;


import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;
import xyz.mcsls.starryMiningListRebuilt.Command.ScoreboardCmd;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Event.PlayerBreakBlockEvent;
import xyz.mcsls.starryMiningListRebuilt.Event.ServerStartedEvent;
import xyz.mcsls.starryMiningListRebuilt.StarryMiningListRebuilt;
import xyz.mcsls.starryMiningListRebuilt.Entity.Base.MsgColorPrefix;

public class ServerSideInit {

    Logger LOGGER = StarryMiningListRebuilt.LOGGER;
    public SBConfig config;


    //执行模组的初始化事件
    public void onInit() {
        LOGGER.info(MsgColorPrefix.getColorStr(MsgColorPrefix.ANSI_GREEN, "Loading the starry mining list(rebuilt ver)"));
        LOGGER.info(MsgColorPrefix.ANSI_GREEN, "Loading configuration...");

        try {
            config = new SBConfig(FabricLoader.getInstance().getConfigDir().toFile().getPath());
        } catch (Exception e) {
            LOGGER.error(MsgColorPrefix.ANSI_RED, "Could not load config file,mod did not load,err: ", e.getMessage());
            return;
        }

        LOGGER.info(MsgColorPrefix.ANSI_GREEN + "Configuration loaded.");

        //注册服务器启动完毕的事件
        ServerLifecycleEvents.SERVER_STARTED.register(s -> ServerStartedEvent.onServerStarted(s, config));

        //注册玩家破坏方块的事件
        PlayerBlockBreakEvents.AFTER.register(((world, playerEntity, blockPos, blockState, blockEntity) -> PlayerBreakBlockEvent.onBreak(playerEntity, config)));

        //注册切换全局显示的指令的事件
        CommandRegistrationCallback.EVENT.register((d, r, e) -> ScoreboardCmd.registerAdmin(d, config));
        CommandRegistrationCallback.EVENT.register((d, r, e) -> ScoreboardCmd.registerPlayer(d, config));
    }
}
