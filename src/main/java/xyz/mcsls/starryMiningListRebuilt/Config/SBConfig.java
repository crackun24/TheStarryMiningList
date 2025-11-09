package xyz.mcsls.starryMiningListRebuilt.Config;

import org.apache.logging.log4j.Logger;
import xyz.mcsls.starryMiningListRebuilt.StarryMiningListRebuilt;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;

public class SBConfig {

    File file;
    Logger LOGGER = StarryMiningListRebuilt.LOGGER;

    public static final String ConfigFileName = "miningList.properties";//配置文件的名称
    public Properties prop;

    //配置文件中的键的信息
    public static String DisplayNameConfigKey = "ScoreboardDisplayName";
    public static String InternalNameConfigKey = "ScoreboardName";

    private void createDefaultConfigFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))//创建新的文件写入对象
        {
            String DEFAULT_CONFIG_DATA =
                    "ScoreboardDisplayName = MiningList\n" +
                    "ScoreboardName = MiningList";
            writer.write(DEFAULT_CONFIG_DATA);//写入默认的配置文件信息
        } catch (Exception e) {
            LOGGER.warn("SBConfig file write error.");
        }
    }//创建默认的配置文件

    public SBConfig(final String filePath) throws IOException {//构造函数,传入配置文件的地址
        file = new File(filePath + "\\" + ConfigFileName);//拼接成正确的配置文件路径
        if (!file.exists())//判断配置文件是否存在
        {
            createDefaultConfigFile(file);//如果配置文件不存在,则直接创建默认的配置文件
        }

        this.prop = new Properties();//创建新的properties文件读取对象

        try (Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()),
                StandardCharsets.UTF_8)) {//创建输入流,使用utf-8的字符集
            this.prop.load(reader);//加载配置文件
        } catch (Exception e) {
            throw e;
        }
    }

    public String getValue(final String key) { //获取配置文件对应的值
        return this.prop.getProperty(key); //返回值
    }

    //更新配置文件的值
    public void updateValue(final String key, final String val) throws IOException {
        this.prop.setProperty(key, val);
        this.prop.store(Files.newOutputStream(this.file.toPath()), null);
    }
}
