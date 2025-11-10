package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;


import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

public class PlayerBreakBlockEvent {
    public static void onBreak(PlayerEntity player, SBConfig config) {

        String internalName = config.getValue(SBConfig.InternalNameConfigKey);//获取内部的名字

        //获取记分对象
        ScoreboardObjective obj = scoreboard.getNullableObjective(internalName);

        ScoreboardPlayerScore score = scoreboard.getPlayerScore(player.getName().getString(), obj);

        //获取玩家的分数
        int playerScore = score.getScore();
        //更新玩家的分数
        score.setScore(++playerScore);

    }

}
