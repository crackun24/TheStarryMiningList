package xyz.mcsls.starryMiningListRebuilt.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import org.jspecify.annotations.Nullable;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Global.Global;

import java.util.Objects;


public class ScoreboardCmd {
    //更新计分板的状态
    private static void updateState(SBConfig config) {
        if (isGlobalScoreboardVisible) {
            String internalName = config.getValue(SBConfig.InternalNameConfigKey);

            //获取记分对象
            @Nullable Objective obj = Global.scoreboard.getObjective(internalName);

            Global.scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, obj);
        } else {
            //隐藏计分板

            Global.scoreboard.setDisplayObjective(DisplaySlot.SIDEBAR, null);
        }
    }

    //全局的计分板目前是否可见
    public static boolean isGlobalScoreboardVisible = true;

    // 注册命令以切换计分板的全局可见/隐藏状态
    public static void registerAdmin(
            CommandDispatcher<CommandSourceStack> dispatcher,
            SBConfig config
    ) {
        dispatcher.register(
                Commands.literal("miningboardg")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                        .then(
                                Commands.argument("mode", BoolArgumentType.bool())
                                        .executes(context -> {
                                            isGlobalScoreboardVisible =
                                                    BoolArgumentType.getBool(context, "mode");

                                            updateState(config);

                                            Component stateMsg = isGlobalScoreboardVisible
                                                    ? Component.translatable("msg.starryminglist.show")
                                                    : Component.translatable("msg.starryminglist.hide");

                                            Component retMsg =
                                                    Component.translatable("msg.starryminglist.switch_global")
                                                            .append(stateMsg)
                                                            .withStyle(ChatFormatting.GREEN);

                                            context.getSource().sendSystemMessage(retMsg);

                                            return 1;
                                        })
                        )
        );
    }

    // 注册命令以切换计分板玩家的可见/隐藏状态
    public static void registerPlayer(
            CommandDispatcher<CommandSourceStack> dispatcher,
            SBConfig config
    ) {
        dispatcher.register(
                Commands.literal("miningboard")
                        .then(
                                Commands.argument("display", BoolArgumentType.bool())
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayer();

                                            if (player == null) {
                                                return 1;
                                            }

                                            Component stateMsg;

                                            if (BoolArgumentType.getBool(context, "display")) {
                                                // 切换为显示状态
                                                Objective obj = Global.scoreboard.getObjective(
                                                        config.getValue(SBConfig.InternalNameConfigKey)
                                                );

                                                player.connection.send(
                                                        new ClientboundSetDisplayObjectivePacket(
                                                                DisplaySlot.SIDEBAR,
                                                                obj
                                                        )
                                                );

                                                stateMsg = Component.translatable(
                                                        "msg.starryminglist.show"
                                                );
                                            } else {
                                                // 切换为隐藏状态
                                                player.connection.send(
                                                        new ClientboundSetDisplayObjectivePacket(
                                                                DisplaySlot.SIDEBAR,
                                                                null
                                                        )
                                                );

                                                stateMsg = Component.translatable(
                                                        "msg.starryminglist.hide"
                                                );
                                            }

                                            Component retMsg =
                                                    Component.translatable(
                                                                    "msg.starryminglist.switch_self"
                                                            )
                                                            .append(stateMsg)
                                                            .withStyle(ChatFormatting.GREEN);

                                            player.sendSystemMessage(retMsg);

                                            return 1;
                                        })
                        )
        );
    }
}
