package com.mininglist.thestarrymininglist;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseMgr {
    private Logger LOGGER = LogManager.getLogger();//获取记录器

    private Connection mConn;//数据库的连接对象
    public DataBaseMgr(){
       LOGGER.info("Connecting to dataBase.");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//加载数据库的类
        } catch (Exception e) {
            e.printStackTrace();//抛出异常
        }

    }
}
