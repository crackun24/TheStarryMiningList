package xyz.mcsls.starryMiningListRebuilt.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Global.Global;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class ScoreboardCmd {
    //更新计分板的状态
    private static void updateState(SBConfig config) {
        if (isGlobalScoreboardVisible) {
            String internalName = config.getValue(SBConfig.InternalNameConfigKey);

            //获取记分对象
            ScoreboardObjective obj = Global.scoreboard.getObjective(internalName);

            Global.scoreboard.setObjectiveSlot(1, obj);
            System.out.println("show");
        } else {
            //隐藏计分板

            System.out.println("hidden");
            Global.scoreboard.setObjectiveSlot(1, null);
        }
    }

    //全局的计分板目前是否可见
    public static boolean isGlobalScoreboardVisible = true;

    // 注册命令以切换计分板的全局可见/隐藏状态
    public static void registerAdmin(CommandDispatcher<ServerCommandSource> dispatcher, SBConfig config) {
        dispatcher.register(literal("miningboardg").then(argument("mode", BoolArgumentType.bool()).executes(context -> {
            Text retMsg;
            if (context.getSource().hasPermissionLevel(1)) {
                isGlobalScoreboardVisible = BoolArgumentType.getBool(context, "mode");
                updateState(config);

                Text stateMsg = isGlobalScoreboardVisible ? new TranslatableText("msg.starryminglist.show") : new TranslatableText("msg.starryminglist.hide");

                retMsg = new TranslatableText("msg.starryminglist.switch_global").append(stateMsg).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            } else {
                retMsg = new TranslatableText("msg.starryminglist.permission_denied").setStyle(Style.EMPTY.withColor(Formatting.RED));
            }

            context.getSource().getPlayer().sendMessage(retMsg, false);

            return 1;
        })));
    }

    // 注册命令以切换计分板玩家的可见/隐藏状态
    public static void registerPlayer(CommandDispatcher<ServerCommandSource> dispatcher, SBConfig config) {
        dispatcher.register(literal("miningboard").then(argument("display", BoolArgumentType.bool()).executes(context -> {
            ServerPlayerEntity player = context.getSource().getPlayer();
            if (player == null) {//判断获取的玩家对象是否为空
                return 1;
            }

            Text stateMsg;

            if (BoolArgumentType.getBool(context, "display")) {//切换为显示状态
                ScoreboardObjective obj = Global.scoreboard.getObjective(config.getValue(SBConfig.InternalNameConfigKey));
                player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(1, obj));

                stateMsg = new TranslatableText("msg.starryminglist.show");

            } else {
                //切换为关闭状态
                player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(1, null));

                stateMsg = new TranslatableText("msg.starryminglist.hide");
            }

            Text retMsg = new TranslatableText("msg.starryminglist.switch_self").append(stateMsg).setStyle(Style.EMPTY.withColor(Formatting.GREEN));

            player.sendMessage(retMsg, false);
            return 1;
        })));
    }
}
