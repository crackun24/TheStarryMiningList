package com.mininglist.thestarrymininglist.dataType;

public class FeedBackJson {
    private String uuid;//唯一标识码
    private long time;//启动的时间
    private String modType;//模组的类型

    public FeedBackJson(String uuid,long time,String mod_type)
    {
        this.uuid = uuid;
        this.time = time;
        this.modType= mod_type;
    }
}
