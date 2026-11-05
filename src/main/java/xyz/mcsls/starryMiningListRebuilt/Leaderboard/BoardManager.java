package xyz.mcsls.starryMiningListRebuilt.Leaderboard;

import net.minecraft.network.packet.s2c.play.ScoreboardDisplayS2CPacket;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import xyz.mcsls.starryMiningListRebuilt.Config.SBConfig;
import xyz.mcsls.starryMiningListRebuilt.Global.Global;
import xyz.mcsls.starryMiningListRebuilt.StarryMiningListRebuilt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 榜单的管理类，负责计分板对象的创建、显示以及"挖掘榜/放置榜"之间的切换。
 */
public final class BoardManager {
    /**
     * 侧边栏对应的显示位置。
     */
    public static final ScoreboardDisplaySlot SLOT = ScoreboardDisplaySlot.SIDEBAR;

    /**
     * 玩家个人的显示偏好：
     * <ul>
     *     <li>没有记录：跟随全局开关</li>
     *     <li>true：强制显示</li>
     *     <li>false：强制隐藏</li>
     * </ul>
     */
    private static final Map<UUID, Boolean> playerPreferences = new HashMap<>();

    private static BoardType currentType = BoardType.MINING;
    private static boolean globalVisible = true;

    private BoardManager() {
    }

    /**
     * 获取指定榜单的计分板内部名字。
     */
    public static String getObjectiveName(SBConfig config, BoardType type) {
        if (config == null || type == null) {
            return null;
        }

        String key = type == BoardType.PLACING
                ? SBConfig.PlaceInternalNameConfigKey
                : SBConfig.InternalNameConfigKey;
        String objectiveName = config.getValue(key);
        return objectiveName == null ? null : objectiveName.trim();
    }

    /**
     * 从当前服务器计分板中获取指定榜单的计分板对象，不存在时返回 null。
     */
    public static ScoreboardObjective getObjective(SBConfig config, BoardType type) {
        return getObjective(Global.scoreboard, config, type);
    }

    public static ScoreboardObjective getObjective(Scoreboard scoreboard, SBConfig config, BoardType type) {
        if (scoreboard == null) {
            return null;
        }

        String objectiveName = getObjectiveName(config, type);
        if (objectiveName == null || objectiveName.isEmpty()) {
            return null;
        }

        return scoreboard.getNullableObjective(objectiveName);
    }

    /**
     * 服务器启动时调用，保证挖掘榜与放置榜的计分板对象都存在，并刷新它们的显示名。
     */
    public static void ensureObjectives(SBConfig config) {
        Scoreboard scoreboard = Global.scoreboard;
        if (scoreboard == null) {
            return;
        }

        String miningName = getObjectiveName(config, BoardType.MINING);
        String placingName = getObjectiveName(config, BoardType.PLACING);
        if (miningName != null && miningName.equals(placingName)) {
            StarryMiningListRebuilt.LOGGER.warn(
                    "ScoreboardName and PlaceScoreboardName are the same ({}), the two leaderboards will share one scoreboard.",
                    miningName);
        }

        for (BoardType type : BoardType.values()) {
            ensureObjective(scoreboard, config, type);
        }
    }

    private static void ensureObjective(Scoreboard scoreboard, SBConfig config, BoardType type) {
        String objectiveName = getObjectiveName(config, type);
        if (objectiveName == null || objectiveName.isEmpty()) {
            StarryMiningListRebuilt.LOGGER.warn("Skip leaderboard {}: its scoreboard name is empty.", type.getId());
            return;
        }

        String displayName = config.getValue(
                type == BoardType.PLACING ? SBConfig.PlaceDisplayNameConfigKey : SBConfig.DisplayNameConfigKey,
                objectiveName);

        ScoreboardObjective objective = scoreboard.getNullableObjective(objectiveName);
        if (objective == null) {
            objective = scoreboard.addObjective(objectiveName, ScoreboardCriterion.DUMMY,
                    Text.literal(displayName), ScoreboardCriterion.RenderType.INTEGER);
        }

        objective.setDisplayName(Text.literal(displayName));
    }

    /**
     * 从配置文件中读取上次保存的榜单类型。
     */
    public static BoardType loadFromConfig(SBConfig config) {
        String saved = config.getValue(SBConfig.BoardModeConfigKey);
        BoardType type = BoardType.fromId(saved);
        if (type == null) {
            if (saved != null && !saved.trim().isEmpty()) {
                StarryMiningListRebuilt.LOGGER.warn(
                        "Unknown leaderboard type '{}' in {}, falling back to 'mining'.",
                        saved, SBConfig.ConfigFileName);
            }
            type = BoardType.MINING;
        }

        StarryMiningListRebuilt.LOGGER.info("Displaying the '{}' leaderboard.", type.getId());
        return type;
    }

    public static void setCurrentType(BoardType type) {
        if (type != null) {
            currentType = type;
        }
    }

    /**
     * 将当前榜单应用到服务器的侧边栏，并同步给所有在线玩家。
     */
    public static void applyGlobal(MinecraftServer server, SBConfig config) {
        Scoreboard scoreboard = Global.scoreboard;
        if (scoreboard == null) {
            return;
        }

        ScoreboardObjective objective = getObjective(scoreboard, config, currentType);
        scoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, globalVisible ? objective : null);

        if (server == null) {
            return;
        }

        //全局隐藏时无视个人偏好，保证所有玩家都会被隐藏
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ScoreboardObjective target = globalVisible && shouldShow(player, objective) ? objective : null;
            player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(ScoreboardDisplaySlot.SIDEBAR, target));
        }
    }

    /**
     * 修改全局显示状态。
     */
    public static void setGlobalVisible(boolean visible, MinecraftServer server, SBConfig config) {
        globalVisible = visible;
        applyGlobal(server, config);
    }

    /**
     * 在挖掘榜与放置榜之间切换，并把结果保存到配置文件。
     */
    public static void switchTo(BoardType type, MinecraftServer server, SBConfig config) {
        if (type == null) {
            return;
        }

        currentType = type;

        try {
            config.updateValue(SBConfig.BoardModeConfigKey, type.getId());
        } catch (IOException e) {
            StarryMiningListRebuilt.LOGGER.error("Could not save leaderboard mode to {}", SBConfig.ConfigFileName, e);
        }

        Scoreboard scoreboard = Global.scoreboard;
        if (scoreboard == null) {
            return;
        }

        ScoreboardObjective objective = getObjective(scoreboard, config, currentType);
        if (globalVisible) {
            scoreboard.setObjectiveSlot(ScoreboardDisplaySlot.SIDEBAR, objective);
        }
        broadcast(server, objective);

        StarryMiningListRebuilt.LOGGER.info("Leaderboard switched to '{}'.", type.getId());
    }

    /**
     * 显示/隐藏单个玩家看到的当前榜单。
     */
    public static void showToPlayer(ServerPlayerEntity player, boolean visible, SBConfig config) {
        if (player == null) {
            return;
        }

        playerPreferences.put(player.getUuid(), visible);

        ScoreboardObjective objective = getObjective(config, currentType);
        player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(
                ScoreboardDisplaySlot.SIDEBAR, visible && objective != null ? objective : null));
    }

    private static boolean shouldShow(ServerPlayerEntity player, ScoreboardObjective objective) {
        if (objective == null) {
            return false;
        }

        Boolean preference = playerPreferences.get(player.getUuid());
        return preference != null ? preference : globalVisible;
    }

    private static void broadcast(MinecraftServer server, ScoreboardObjective objective) {
        if (server == null) {
            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.sendPacket(new ScoreboardDisplayS2CPacket(
                    ScoreboardDisplaySlot.SIDEBAR, shouldShow(player, objective) ? objective : null));
        }
    }
}
