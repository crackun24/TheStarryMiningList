package com.mininglist.thestarrymininglist.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>12001
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
//#endif
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboard;
import static com.mininglist.thestarrymininglist.TheStarryMiningList.mScoreboardObj;

public class TheStarryMiningListCommand {
    public static boolean isScoreboardVisible = true;

    // 注册命令以切换计分板的全局可见/隐藏状态
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("theStarryMiningList")
                .then(argument("mode", BoolArgumentType.bool())
                        .executes(context -> {
                            String ret_msg;
                            if (context.getSource().hasPermissionLevel(1)) {
                                isScoreboardVisible = BoolArgumentType.getBool(context, "mode");
                                isVisible();
                                ret_msg = "已设置挖掘榜显示状态为: " + (isScoreboardVisible ? "开启" : "关闭");
                            } else {
                                ret_msg = "权限不足";
                            }
                            context.getSource().getPlayer().sendMessage(Text.of(ret_msg), true);
                            return 1;
                        }))
        );
    }

    //注册控制单个玩家设置计分板是否显示的指令
    public static void registerSingleCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("theStarryMiningDisplay")
                .then(argument("mode", BoolArgumentType.bool())
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();//获取玩家对象
                            if (player == null)//判断执行命令的对象是否为玩家
                            {
                                return 1;
                            }

                            boolean is_display = BoolArgumentType.getBool(context, "mode");

                            setSinglePlayerDisplayState(context.getSource().getPlayer(), is_display);

                            String ret_msg = "你的计分板显示状态已设置为: " + (is_display ? "开启" : "关闭");
                            context.getSource().getPlayer().sendMessage(Text.of(ret_msg), true);
                            return 1;
                        }))
        );
    }


    public static void isVisible() {
        if (isScoreboardVisible) {
            //#if MC<12002
            //$$ mScoreboard.setObjectiveSlot(1, mScoreboardObj); // 显示计分板
            //#else
            mScoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, mScoreboardObj);
            //#endif

        } else {
            //#if MC<12002
            //$$ mScoreboard.setObjectiveSlot(1, null); // 隐藏计分板
            //#else
            mScoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, null);
            //#endif
        }
    }

    //设置单个玩家的计分板显示状态
    public static void setSinglePlayerDisplayState(ServerPlayerEntity player, boolean state) {
        if (state) {
            //#if MC<12002
            //$$ player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(1, mScoreboardObj));//发送关闭玩家的计分板的数据包
            //#else
            player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, mScoreboardObj));//发送关闭玩家的计分板的数据包
            //#endif
        } else {
            //#if MC<12002
            //$$ player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(1, null));//发送关闭玩家的计分板的数据包
            //#else
            player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, null));//发送关闭玩家的计分板的数据包
            //#endif
        }
    }
}
