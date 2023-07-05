package com.mininglist.thestarrymininglist;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TheStarryMiningList implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void onInitialize() {
        FabricLoader loader = FabricLoader.getInstance();//获取加载器的实例

        String configPath = loader.getConfigDirectory().getPath();//获取配置文件的路径
        Config config = new Config(configPath);//读取配置文件
    }
}
