package com.mininglist.thestarrymininglist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

//配置类
public class Config {
    static final String CONFIG_FILE_NAME = "miningList.properties";//配置文件的名称
    private Properties mProp;
    private final Logger LOGGER = LogManager.getLogger();//日志记录器

    public Config(final String url) {//构造函数,传入配置文件的地址
        this.mProp = new Properties();//创建新的properties文件读取对象

        LOGGER.info(new String("Reading config %s.").formatted(CONFIG_FILE_NAME));

        try (InputStream input = new FileInputStream(url)) {//创建输入流

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
