package xyz.mcsls.starryMiningListRebuilt.Event;

import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;


import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;

public class PlayerBreakBlockEvent {

    public static void onBreak(Player player, SBConfig config) {
        String internalName =
                config.getValue(SBConfig.InternalNameConfigKey);

        // 获取计分目标
        Objective obj = scoreboard.getObjective(internalName);

        if (obj == null) {
            return;
        }

        // 获取或创建该玩家在此计分目标下的分数
        ScoreAccess score =
                scoreboard.getOrCreatePlayerScore(player, obj);

        // 分数 +1
        score.increment();
    }
}
