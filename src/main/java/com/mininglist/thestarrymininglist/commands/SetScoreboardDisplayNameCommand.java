package com.mininglist.thestarrymininglist.commands;

import com.mininglist.thestarrymininglist.function.SetScoreboardDisplayName;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetScoreboardDisplayNameCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("setScoreboardDisplayName")
            .then(argument("scoreboardName", StringArgumentType.word())
            .then(argument("displayName", StringArgumentType.greedyString())
                .executes(context -> {
                String scoreboardName = StringArgumentType.getString(context,"scoreboardName");
                String displayName = StringArgumentType.getString(context, "displayName");
                SetScoreboardDisplayName.set(scoreboardName, displayName, context.getSource());
                return 1;
            })))
        );
    }
}
