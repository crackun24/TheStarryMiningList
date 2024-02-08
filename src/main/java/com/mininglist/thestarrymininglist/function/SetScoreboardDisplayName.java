package com.mininglist.thestarrymininglist.function;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
//#if MC>12001
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
//#endif

import net.minecraft.server.command.ServerCommandSource;
//#if MC >= 11900
import net.minecraft.text.Text;
//#else
//$$ import net.minecraft.text.LiteralText;
//#endif

import net.minecraft.text.Text;

public class SetScoreboardDisplayName {
    public static void set(String scoreboardName, String displayName, ServerCommandSource source) throws CommandSyntaxException {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        ScoreboardObjective objective = scoreboard.getNullableObjective(scoreboardName);
        if (objective != null) {
            //#if MC<11900
            //$$ objective.setDisplayName(new LiteralText(displayName));
            //#else
            objective.setDisplayName(Text.literal(displayName));
            //#endif
        }
        //#if MC<12002
        //$$ scoreboard.setObjectiveSlot(1, objective);
        //#else
        scoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, objective);
        //#endif

        source.getPlayer().sendMessage(Text.of("计分板 [ " + scoreboardName + " ] 显示名称已更改为: [ " + displayName + " ]"), true);
    }
}
