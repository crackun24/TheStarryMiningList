package com.mininglist.thestarrymininglist;

import com.mininglist.thestarrymininglist.commands.SetScoreboardDisplayNameCommand;
import com.mininglist.thestarrymininglist.commands.TheStarryMiningListCommand;
import com.mininglist.thestarrymininglist.config.Config;
import com.mininglist.thestarrymininglist.event.HookPlayerBreakBlockEvent;
import com.mininglist.thestarrymininglist.function.CreateScoreboard;

import net.fabricmc.api.ModInitializer;
//#if MC < 11900
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
//#else
//$$ import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
//#endif

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TheStarryMiningList implements ModInitializer {
    private Feedback feedback;
    public static final Logger LOGGER = LogManager.getLogger();
    public static Scoreboard mScoreboard; //计分板对象
    public static ScoreboardObjective mScoreboardObj; //计分板的计分对象


    @Override
    public void onInitialize() {
        this.feedback = new Feedback("MiningList");
        feedback.start();
        FabricLoader loader = FabricLoader.getInstance();//获取加载器的实例
        File config_file_path = loader.getConfigDir().toFile();//获取配置文件
        Config config = new Config(config_file_path.getPath());//读取配置文件
        String name = config.GetValue("ScoreboardName");//获取计分板的名字
        String disPlayName = config.GetValue("ScoreboardDisplayName");//获取计分板显示的名字
        CreateScoreboard.create(name, disPlayName);//创建计分板
        HookPlayerBreakBlockEvent.hook();//设置玩家破坏方块事件的回调

        //初始化显示状态
        TheStarryMiningListMod.init();
        SetScoreboardDisplayNameCommand setScoreboardDisplayNameCommand = new SetScoreboardDisplayNameCommand(config);

        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            feedback.close();//关闭反馈服务
        });

        //#if MC<11900
        // 注册命令以切换计分板的可见/隐藏状态
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> TheStarryMiningListCommand.register(dispatcher));
        // 注册设置计分板显示名称的命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> setScoreboardDisplayNameCommand.register(dispatcher));
        // 注册设置玩家自己的计分板显示的命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> TheStarryMiningListCommand.registerSingleCommand(dispatcher));
        //#else
        //$$ CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TheStarryMiningListCommand.register(dispatcher));
        //$$ CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> setScoreboardDisplayNameCommand.register(dispatcher));
        //$$ CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TheStarryMiningListCommand.registerSingleCommand(dispatcher));
        //#endif
    }
}