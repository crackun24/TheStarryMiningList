package xyz.mcsls.starryMiningListRebuilt.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardManager;
import xyz.mcsls.starryMiningListRebuilt.Leaderboard.BoardType;

import java.util.concurrent.CompletableFuture;


public class ScoreboardCmd {

    //把提示信息发送给命令执行者（玩家或控制台）
    private static void reply(CommandSourceStack source, Component text) {
        source.sendSystemMessage(text);
    }

    // 注册命令以切换计分板的全局可见/隐藏状态，以及切换展示的榜单
    public static void registerAdmin(
            CommandDispatcher<CommandSourceStack> dispatcher,
            SBConfig config
    ) {
        dispatcher.register(
                Commands.literal("miningboardg")
                        .requires(Commands.hasPermission(Commands.LEVEL_ADMINS))
                        // /miningboardg <true|false> 全局显示/隐藏
                        .then(
                                Commands.argument("visible", BoolArgumentType.bool())
                                        .executes(context -> setGlobalVisible(context, config))
                        )
                        // /miningboardg mode <mining|placing> 在挖掘榜和放置榜之间切换
                        .then(
                                Commands.literal("mode")
                                        .then(
                                                Commands.argument("board", StringArgumentType.word())
                                                        .suggests(ScoreboardCmd::suggestBoards)
                                                        .executes(context -> switchBoard(context, config))
                                        )
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
                                            CommandSourceStack source = context.getSource();
                                            ServerPlayer player = source.getPlayer();

                                            if (player == null) {
                                                reply(source, Component.translatable(
                                                                "msg.starryminglist.player_only")
                                                        .withStyle(ChatFormatting.RED));
                                                return 0;
                                            }

                                            boolean display = BoolArgumentType.getBool(context, "display");
                                            BoardManager.showToPlayer(player, display, config);

                                            Component stateMsg = display
                                                    ? Component.translatable("msg.starryminglist.show")
                                                    : Component.translatable("msg.starryminglist.hide");
                                            Component retMsg = Component.translatable(
                                                            "msg.starryminglist.switch_self")
                                                    .append(stateMsg)
                                                    .withStyle(ChatFormatting.GREEN);

                                            reply(source, retMsg);
                                            return 1;
                                        })
                        )
        );
    }

    // /miningboardg <true|false>
    private static int setGlobalVisible(CommandContext<CommandSourceStack> context, SBConfig config) {
        CommandSourceStack source = context.getSource();
        boolean visible = BoolArgumentType.getBool(context, "visible");

        BoardManager.setGlobalVisible(visible, source.getServer(), config);

        Component stateMsg = visible
                ? Component.translatable("msg.starryminglist.show")
                : Component.translatable("msg.starryminglist.hide");
        Component retMsg = Component.translatable("msg.starryminglist.switch_global")
                .append(stateMsg)
                .withStyle(ChatFormatting.GREEN);

        reply(source, retMsg);
        return 1;
    }

    // /miningboardg mode <mining|placing>
    private static int switchBoard(CommandContext<CommandSourceStack> context, SBConfig config) {
        CommandSourceStack source = context.getSource();
        String boardId = StringArgumentType.getString(context, "board");
        BoardType type = BoardType.fromId(boardId);

        if (type == null) {
            reply(source, Component.translatable("msg.starryminglist.board_unknown", boardId)
                    .withStyle(ChatFormatting.RED));
            return 0;
        }

        BoardManager.switchTo(type, source.getServer(), config);

        Component retMsg = Component.translatable("msg.starryminglist.switch_board")
                .append(Component.translatable(type.getTranslationKey()))
                .withStyle(ChatFormatting.GREEN);

        reply(source, retMsg);
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestBoards(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        for (BoardType type : BoardType.values()) {
            builder.suggest(type.getId());
        }
        return builder.buildFuture();
    }
}
