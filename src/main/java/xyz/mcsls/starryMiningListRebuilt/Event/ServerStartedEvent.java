package xyz.mcsls.starryMiningListRebuilt.Event;

import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.world.World;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;

import java.util.Objects;

import static xyz.mcsls.starryMiningListRebuilt.Global.Global.scoreboard;

public class ServerStartedEvent {
    public static void onServerStarted(MinecraftServer server, SBConfig config) {
        String internalName = config.getValue(SBConfig.InternalNameConfigKey);//获取内部的名字
        String displayName = config.getValue(SBConfig.DisplayNameConfigKey);

        scoreboard = Objects.requireNonNull(server.getWorld(World.OVERWORLD).getScoreboard());
        ScoreboardObjective scoreboardObjective = scoreboard.getNullableObjective(internalName);

        //判断获取到的计分板对象是否为空
        if (scoreboardObjective != null) {

            scoreboardObjective.setDisplayName(new LiteralText(displayName));
            return;
        }

        scoreboard.addObjective(internalName, ScoreboardCriterion.DUMMY, new LiteralText(displayName), ScoreboardCriterion.RenderType.INTEGER);
        scoreboard.setObjectiveSlot(1, null);
        scoreboardObjective.setDisplayName(new LiteralText(displayName));

    }
}
