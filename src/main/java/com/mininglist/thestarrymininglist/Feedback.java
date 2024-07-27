package com.mininglist.thestarrymininglist;

import com.google.gson.Gson;
import com.mininglist.thestarrymininglist.dataType.FeedBackJson;
import com.mininglist.thestarrymininglist.dataType.Msg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class Feedback extends Thread {//发送反馈
    private final String FEED_BACK_URL = "https://feedback.mcsls.xyz/send_feedback";
    private String modType;//模组的类型
    private boolean mCloseState;//主线程的关闭状态
    private String server_id;//服务器的标识
    public static final Logger LOGGER = LogManager.getLogger();

    private synchronized boolean getCloseState() {
        return this.mCloseState;
    }

    private void genServerId()//获取网卡的 mac 码
    {
        try {
            // 获取所有网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 获取该网络接口的MAC地址
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    // 将MAC地址字节数组转换为可读格式
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    this.server_id = sb.toString();
                    break;//不需要再循环
                }
            }
            if (this.server_id.isEmpty()) {
                this.server_id = "unknown";//设置为未知的mac码
            }
        } catch (Exception e) {
            LOGGER.info(Msg.ANSI_RED + "发送遥测数据失败" + Msg.ANSI_RESET);
        }
    }

    private void onSendFeedback()//发送反馈
    {
        try {
            Gson gson = new Gson();
            FeedBackJson json_obj = new FeedBackJson(this.server_id, System.currentTimeMillis() / 1000, modType);
            String req_body = gson.toJson(json_obj);//构建json 字符串

            URL url = new URL(this.FEED_BACK_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = req_body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            LOGGER.info(Msg.ANSI_RED + "发送遥测数据失败" + Msg.ANSI_RESET);
        }
    }

    @Override
    public void run() {//线程的主方法
        genServerId();//获取服务器的 mac 地址
        while (!getCloseState()) {
            try {
                onSendFeedback();
                sleep(43200000);//每隔12个小时发送一次反馈数据
            } catch (InterruptedException e) {
                break;//打断的时候退出
            }
        }
    }

    //关闭
    public synchronized void close() {
        try {
            this.mCloseState = true;//设置关闭的信号成立
            this.interrupt();
        } catch (Exception e)//不用处理打断的异常
        {

        }
    }

    public Feedback(String mod_type) {
        this.modType = mod_type;
    }
}
