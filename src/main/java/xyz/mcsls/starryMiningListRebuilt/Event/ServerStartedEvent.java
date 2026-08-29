package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;

import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

public class ServerStartedEvent {

    public static void onServerStarted(MinecraftServer server, SBConfig config) {
        String internalName =
                config.getValue(SBConfig.InternalNameConfigKey);

        String displayName =
                config.getValue(SBConfig.DisplayNameConfigKey);

        // 直接获取服务器计分板
        scoreboard = server.getScoreboard();

        // 获取已有计分目标
        Objective scoreboardObjective =
                scoreboard.getObjective(internalName);

        // 已存在，只更新显示名称
        if (scoreboardObjective != null) {
            scoreboardObjective.setDisplayName(
                    Component.literal(displayName)
            );
            return;
        }

        // 不存在则创建
        scoreboardObjective = scoreboard.addObjective(
                internalName,
                ObjectiveCriteria.DUMMY,
                Component.literal(displayName),
                ObjectiveCriteria.RenderType.INTEGER,
                true,
                null
        );

        // 显示到右侧栏
        scoreboard.setDisplayObjective(
                DisplaySlot.SIDEBAR,
                scoreboardObjective
        );
    }
}