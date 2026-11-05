package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.server.network.ServerPlayerEntity;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Global.Global;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardManager;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardType;
import xyz.mcsls.starryMiningListRebuilt.Util.ScoreboardUtil;

public class PlayerPlaceBlockEvent {

    /**
     * 玩家成功放置方块后调用，给放置榜 +1。
     */
    public static void onPlace(ServerPlayerEntity player) {
        SBConfig config = Global.config;
        if (player == null || config == null) {
            return;
        }

        //获取放置榜的内部名字
        String internalName = BoardManager.getObjectiveName(config, BoardType.PLACING);

        //更新放置榜的分数
        ScoreboardUtil.increment(Global.scoreboard, internalName, player.getName().getString());
    }
}
