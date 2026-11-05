package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.entity.player.PlayerEntity;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardManager;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardType;
import xyz.mcsls.starryMiningListRebuilt.Util.ScoreboardUtil;

import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

public class PlayerBreakBlockEvent {
    public static void onBreak(PlayerEntity player, SBConfig config) {
        //获取内部的名字
        String internalName = BoardManager.getObjectiveName(config, BoardType.MINING);

        //更新挖掘榜的分数
        ScoreboardUtil.increment(scoreboard, internalName, player);
    }
}
