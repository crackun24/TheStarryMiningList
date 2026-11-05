package xyz.mcsls.starryMiningListRebuilt.Util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.Scoreboard;

public class ScoreboardUtil {

    /**
     * 给指定玩家在指定计分板上的分数 +1。
     *
     * @param scoreboard    服务器计分板，为空时直接忽略
     * @param objectiveName 计分板内部名字，为空或计分板不存在时直接忽略
     * @param player        玩家
     */
    public static void increment(Scoreboard scoreboard, String objectiveName, Player player) {
        if (scoreboard == null || objectiveName == null || objectiveName.isEmpty() || player == null) {
            return;
        }

        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            //计分板还没有被创建（例如服务器尚未启动完毕），忽略这次计数
            return;
        }

        ScoreAccess score = scoreboard.getOrCreatePlayerScore(player, objective);
        score.increment();
    }
}
