package com.mininglist.thestarrymininglist.function;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

//#if MC>=12002
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
//#endif

import net.minecraft.scoreboard.ScoreboardCriterion;
//#if MC<11904
//$$ import net.minecraft.text.LiteralText;
//#elseif MC>12003
import net.minecraft.scoreboard.number.NumberFormat;
import net.minecraft.scoreboard.number.NumberFormatTypes;
//#else
import net.minecraft.text.Text;
//#endif

import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Objects;

import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboard;
import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboardObj;

public class CreateScoreboard {
    //创建计分板
    public static void create(final String name, final String display_name) {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            mScoreboard = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getScoreboard();//获取世界的计分板
            mScoreboardObj = mScoreboard.getNullableObjective(name);//获取服务器的计分板对象
            if (mScoreboardObj == null) {//判断是否为空对象
                //#if MC<11900
                //$$ mScoreboardObj = mScoreboard.addObjective(name, ScoreboardCriterion.DUMMY, new LiteralText(display_name), ScoreboardCriterion.RenderType.INTEGER);
                //#elseif MC>=12003
                mScoreboardObj = mScoreboard.addObjective(name, ScoreboardCriterion.DUMMY, Text.literal(display_name), ScoreboardCriterion.RenderType.INTEGER, true, null);
                //#endif

                //#if MC<12002
                //$$ mScoreboard.setObjectiveSlot(1, null);
                //#else
                mScoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, null);
                //#endif
            }
        });
    }
}
