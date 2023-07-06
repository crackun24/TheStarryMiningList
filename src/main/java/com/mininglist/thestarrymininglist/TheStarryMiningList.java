package com.mininglist.thestarrymininglist;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;


public class TheStarryMiningList implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    private Scoreboard mScoreboard;//计分板对象
    private ScoreboardObjective mScoreboardObj;//计分板的计分对象

    private void CreateScoreboard(final String name, final String display_name) {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            this.mScoreboard = server.getWorld(World.OVERWORLD).getScoreboard();//获取世界的计分板

            this.mScoreboardObj = mScoreboard.getObjective(name);//获取服务器的计分板对象

            if (mScoreboardObj == null) {//判断是否为空对象
                this.mScoreboardObj = mScoreboard.addObjective(name, ScoreboardCriterion.DUMMY, Text.literal(display_name),
                        ScoreboardCriterion.RenderType.INTEGER);
                this.mScoreboard.setObjectiveSlot(1, this.mScoreboardObj);//设置显示的位置
            }
        });
    }

    private void HookPlayerBreakBlockEvent() {//设置玩家破坏方块事件的回调
        PlayerBlockBreakEvents.AFTER.register(((world, player, pos, state, blockEntity) -> {
            ScoreboardPlayerScore score = this.mScoreboard.getPlayerScore(player.getName().getString(),
                    this.mScoreboardObj);//获取玩家计分对象

            int player_score = score.getScore();//获取玩家当前的计分数
            player_score++;//分数递增
            score.setScore(player_score);//更新玩家的分数
        }));
    }

    @Override
    public void onInitialize() {
        FabricLoader loader = FabricLoader.getInstance();//获取加载器的实例
        File config_file_path = loader.getConfigDir().toFile();//获取配置文件

        Config config = new Config(config_file_path.getPath());//读取配置文件

        String name = config.GetValue("ScoreboardName");//获取计分板的名字
        String disPlayName = config.GetValue("ScoreboardDisplayName");//获取计分板显示的名字
        CreateScoreboard(name, disPlayName);//创建计分板
        HookPlayerBreakBlockEvent();//设置玩家破坏方块事件的回调
    }
}
