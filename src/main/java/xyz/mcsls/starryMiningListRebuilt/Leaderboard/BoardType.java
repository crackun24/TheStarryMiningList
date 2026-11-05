package xyz.mcsls.starryMiningListRebuilt.Leaderboard;

/**
 * 榜单类型。
 * <p>
 * 目前支持两种榜单：挖掘榜与放置榜，两者分别对应一个独立的计分板对象。
 */
public enum BoardType {
    MINING("mining", "msg.starryminglist.board.mining"),
    PLACING("placing", "msg.starryminglist.board.placing");

    private final String id;
    private final String translationKey;

    BoardType(String id, String translationKey) {
        this.id = id;
        this.translationKey = translationKey;
    }

    /**
     * 命令参数以及配置文件中使用的名字，例如 "mining"、"placing"。
     */
    public String getId() {
        return this.id;
    }

    /**
     * 榜单名字的翻译键。
     */
    public String getTranslationKey() {
        return this.translationKey;
    }

    /**
     * 根据名字查找榜单类型，找不到时返回 null。
     */
    public static BoardType fromId(String id) {
        if (id == null) {
            return null;
        }

        String trimmedId = id.trim();
        for (BoardType type : values()) {
            if (type.id.equalsIgnoreCase(trimmedId)) {
                return type;
            }
        }

        return null;
    }
}
