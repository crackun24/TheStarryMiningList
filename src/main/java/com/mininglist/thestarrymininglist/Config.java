package com.mininglist.thestarrymininglist;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Properties;
import java.io.*;

//配置类
public class Config {
    static final String CONFIG_FILE_NAME = "miningList.properties";//配置文件的名称
    private final Properties mProp;

    private void CreateDefaultConfigFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))//创建新的文件写入对象
        {
            String DEFAULT_CONFIG_DATA =
                    "ScoreboardDisplayName = starryBoard\n" +
                    "ScoreboardName = starryBoard";
            writer.write(DEFAULT_CONFIG_DATA);//写入默认的配置文件信息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//创建默认的配置文件

    public Config(final String filePath) {//构造函数,传入配置文件的地址
        File file = new File(filePath + "\\" + CONFIG_FILE_NAME);//拼接成正确的配置文件路径

        if (!file.exists())//判断配置文件是否存在
        {
            try {
                CreateDefaultConfigFile(file);//如果配置文件不存在,则直接创建默认的配置文件
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.mProp = new Properties();//创建新的properties文件读取对象

        try (Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()),
                StandardCharsets.UTF_8)) {//创建输入流,使用utf-8的字符集
            this.mProp.load(reader);//加载配置文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String GetValue(final String key)//获取配置文件对应的值
    {
        return this.mProp.getProperty(key);//返回值
    }
}
