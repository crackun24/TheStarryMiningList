package xyz.mcsls.starryMiningListRebuilt.Config;

import org.apache.logging.log4j.Logger;
import xyz.mcsls.starryMiningListRebuilt.StarryMiningListRebuilt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

public class SBConfig {

    private final File file;
    private final Logger LOGGER = StarryMiningListRebuilt.LOGGER;

    public static final String ConfigFileName = "miningList.properties";//配置文件的名称
    public Properties prop;

    //配置文件中的键的信息
    public static final String DisplayNameConfigKey = "ScoreboardDisplayName";
    public static final String InternalNameConfigKey = "ScoreboardName";
    public static final String PlaceDisplayNameConfigKey = "PlaceScoreboardDisplayName";
    public static final String PlaceInternalNameConfigKey = "PlaceScoreboardName";
    public static final String BoardModeConfigKey = "ScoreboardMode";

    //配置文件中各项的默认值
    public static final String DefaultDisplayName = "MiningList";
    public static final String DefaultInternalName = "MiningList";
    public static final String DefaultPlaceDisplayName = "PlaceList";
    public static final String DefaultPlaceInternalName = "PlaceList";
    public static final String DefaultBoardMode = "mining";

    private static final String[] DEFAULT_CONFIG_LINES = new String[]{
            DisplayNameConfigKey + " = " + DefaultDisplayName,
            InternalNameConfigKey + " = " + DefaultInternalName,
            PlaceDisplayNameConfigKey + " = " + DefaultPlaceDisplayName,
            PlaceInternalNameConfigKey + " = " + DefaultPlaceInternalName,
            BoardModeConfigKey + " = " + DefaultBoardMode
    };

    private void createDefaultConfigFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(file.toPath()), StandardCharsets.UTF_8))) {//创建新的文件写入对象
            for (String line : DEFAULT_CONFIG_LINES) {
                writer.write(line);//写入默认的配置文件信息
                writer.newLine();
            }
        } catch (Exception e) {
            LOGGER.warn("SBConfig file write error.", e);
        }
    }//创建默认的配置文件

    public SBConfig(final String filePath) throws IOException {//构造函数,传入配置文件的地址
        file = new File(filePath, ConfigFileName);//拼接成正确的配置文件路径
        if (!file.exists())//判断配置文件是否存在
        {
            createDefaultConfigFile(file);//如果配置文件不存在,则直接创建默认的配置文件
        }

        this.prop = new Properties();//创建新的properties文件读取对象

        try (Reader reader = new BufferedReader(new InputStreamReader(
                Files.newInputStream(file.toPath()), StandardCharsets.UTF_8))) {//创建输入流,使用utf-8的字符集
            this.prop.load(reader);//加载配置文件
        }

        //老版本的配置文件里没有放置榜相关的键，这里补齐并保存
        if (ensureDefaults()) {
            save();
        }
    }

    //为缺失的键补上默认值，返回是否有键被补充
    private boolean ensureDefaults() {
        boolean changed = false;
        changed |= putIfAbsent(DisplayNameConfigKey, DefaultDisplayName);
        changed |= putIfAbsent(InternalNameConfigKey, DefaultInternalName);
        changed |= putIfAbsent(PlaceDisplayNameConfigKey, DefaultPlaceDisplayName);
        changed |= putIfAbsent(PlaceInternalNameConfigKey, DefaultPlaceInternalName);
        changed |= putIfAbsent(BoardModeConfigKey, DefaultBoardMode);
        return changed;
    }

    private boolean putIfAbsent(final String key, final String value) {
        if (this.prop.getProperty(key) != null) {
            return false;
        }

        this.prop.setProperty(key, value);
        return true;
    }

    public String getValue(final String key) { //获取配置文件对应的值
        return this.prop.getProperty(key); //返回值
    }

    //获取配置文件对应的值，没有配置时返回默认值
    public String getValue(final String key, final String defaultValue) {
        String value = this.prop.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        return value.trim();
    }

    //更新配置文件的值
    public void updateValue(final String key, final String val) throws IOException {
        this.prop.setProperty(key, val);
        save();
    }

    private void save() throws IOException {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(this.file.toPath()), StandardCharsets.UTF_8))) {
            this.prop.store(writer, null);
        }
    }
}
