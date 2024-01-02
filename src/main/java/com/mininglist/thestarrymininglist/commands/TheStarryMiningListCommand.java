package com.mininglist.thestarrymininglist.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>12001
//$$ import net.minecraft.scoreboard.ScoreboardDisplaySlot;
//#endif
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboard;
import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboardObj;

public class TheStarryMiningListCommand {
    public static boolean isScoreboardVisible = true;
    // 注册命令以切换计分板的可见/隐藏状态
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("theStarryMiningList")
        .then(argument("mode", BoolArgumentType.bool())
            .executes(context -> {
                isScoreboardVisible = BoolArgumentType.getBool(context, "mode");
                isVisible();
                sendMessage(context.getSource().getPlayer());
                return 1;
            }))
        );
    }

    public static void isVisible() {
        if (isScoreboardVisible) {
            mScoreboard.setObjectiveSlot(1, mScoreboardObj); // 显示计分板
        } else {
            mScoreboard.setObjectiveSlot(1, null); // 隐藏计分板
        }
    }

    private static void sendMessage(PlayerEntity player) {
        Text mode = Text.of(isScoreboardVisible ? "[ 已启用计分板 ]" : "[ 已关闭计分板 ]");
        player.sendMessage(mode, true);
    }
}
