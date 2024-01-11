package com.mininglist.thestarrymininglist.commands;

import com.mininglist.thestarrymininglist.config.Config;
import com.mininglist.thestarrymininglist.function.SetScoreboardDisplayName;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.IOException;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SetScoreboardDisplayNameCommand {
    Config mConfig;

    //构造函数
    public SetScoreboardDisplayNameCommand(Config conf) {
        this.mConfig = conf;
    }

    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("setScoreboardDisplayName")
                .then(argument("scoreboardName", StringArgumentType.word())
                        .then(argument("displayName", StringArgumentType.greedyString())
                                .executes(context -> {
                                    if (context.getSource().hasPermissionLevel(1)) {//判断是否有op权限
                                        String scoreboardName = StringArgumentType.getString(context, "scoreboardName");
                                        String displayName = StringArgumentType.getString(context, "displayName");
                                        SetScoreboardDisplayName.set(scoreboardName, displayName, context.getSource());
                                        try {
                                            this.mConfig.UpdateValue(Config.key_display_name,displayName);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            context.getSource().getPlayer().sendMessage(Text.of("文件写入错误"),true);
                                        }

                                    } else {
                                        context.getSource().getPlayer().sendMessage(Text.of("权限不足"), true);
                                    }
                                    return 1;
                                })))
        );
    }
}
