package xyz.mcsls.starryMiningListRebuilt.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardManager;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardType;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class ScoreboardCmd {

    //把提示信息发送给执行者（玩家或控制台）
    private static void reply(ServerCommandSource source, Text text) {
        Entity entity = source.getEntity();
        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).sendMessage(text, false);
        } else {
            source.sendFeedback(text, false);
        }
    }

    // 注册命令以切换计分板的全局可见/隐藏状态，以及切换展示的榜单
    public static void registerAdmin(CommandDispatcher<ServerCommandSource> dispatcher, SBConfig config) {
        dispatcher.register(literal("miningboardg")
                // /miningboardg <true|false> 全局显示/隐藏
                .then(argument("visible", BoolArgumentType.bool())
                        .executes(context -> setGlobalVisible(context, config)))
                // /miningboardg mode <mining|placing> 在挖掘榜和放置榜之间切换
                .then(literal("mode")
                        .then(argument("board", StringArgumentType.word())
                                .suggests(ScoreboardCmd::suggestBoards)
                                .executes(context -> switchBoard(context, config)))));
    }

    // 注册命令以切换计分板玩家的可见/隐藏状态
    public static void registerPlayer(CommandDispatcher<ServerCommandSource> dispatcher, SBConfig config) {
        dispatcher.register(literal("miningboard").then(argument("display", BoolArgumentType.bool()).executes(context -> {
            ServerCommandSource source = context.getSource();
            Entity entity = source.getEntity();
            if (!(entity instanceof ServerPlayerEntity)) {
                reply(source, Text.translatable("msg.starryminglist.player_only").setStyle(Style.EMPTY.withColor(Formatting.RED)));
                return 0;
            }

            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            boolean display = BoolArgumentType.getBool(context, "display");
            BoardManager.showToPlayer(player, display, config);

            Text stateMsg = display ? Text.translatable("msg.starryminglist.show") : Text.translatable("msg.starryminglist.hide");
            Text retMsg = Text.translatable("msg.starryminglist.switch_self").append(stateMsg).setStyle(Style.EMPTY.withColor(Formatting.GREEN));

            reply(source, retMsg);
            return 1;
        })));
    }

    // /miningboardg <true|false>
    private static int setGlobalVisible(CommandContext<ServerCommandSource> context, SBConfig config) {
        ServerCommandSource source = context.getSource();
        if (!source.hasPermissionLevel(1)) {
            reply(source, Text.translatable("msg.starryminglist.permission_denied").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            return 0;
        }

        boolean visible = BoolArgumentType.getBool(context, "visible");
        BoardManager.setGlobalVisible(visible, source.getServer(), config);

        Text stateMsg = visible ? Text.translatable("msg.starryminglist.show") : Text.translatable("msg.starryminglist.hide");
        Text retMsg = Text.translatable("msg.starryminglist.switch_global").append(stateMsg).setStyle(Style.EMPTY.withColor(Formatting.GREEN));

        reply(source, retMsg);
        return 1;
    }

    // /miningboardg mode <mining|placing>
    private static int switchBoard(CommandContext<ServerCommandSource> context, SBConfig config) {
        ServerCommandSource source = context.getSource();
        if (!source.hasPermissionLevel(1)) {
            reply(source, Text.translatable("msg.starryminglist.permission_denied").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            return 0;
        }

        String boardId = StringArgumentType.getString(context, "board");
        BoardType type = BoardType.fromId(boardId);

        if (type == null) {
            reply(source, Text.translatable("msg.starryminglist.board_unknown", boardId).setStyle(Style.EMPTY.withColor(Formatting.RED)));
            return 0;
        }

        BoardManager.switchTo(type, source.getServer(), config);

        Text retMsg = Text.translatable("msg.starryminglist.switch_board")
                .append(Text.translatable(type.getTranslationKey()))
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN));

        reply(source, retMsg);
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestBoards(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        for (BoardType type : BoardType.values()) {
            builder.suggest(type.getId());
        }
        return builder.buildFuture();
    }
}
