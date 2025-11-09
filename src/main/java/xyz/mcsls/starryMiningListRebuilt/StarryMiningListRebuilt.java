package xyz.mcsls.starryMiningListRebuilt;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.mcsls.starryMiningListRebuilt.Server.ServerSideInit;

public class StarryMiningListRebuilt implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final ServerSideInit init = new ServerSideInit();

    @Override
    public void onInitialize() {
        init.onInit();//执行模组的初始化操作
    }
}
