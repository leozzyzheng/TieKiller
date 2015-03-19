package com.leozzyzheng.tiekiller.account;

/**
 * Created by leozzyzheng on 2015/3/18.
 * 账户管理类，用来获取和保存各类用户登陆信息的类
 */
public class AccountManger {
    private static AccountManger mInstance;

    public static AccountManger getInstance() {
        synchronized (AccountManger.class) {
            if (mInstance == null) {
                mInstance = new AccountManger();
            }
            return mInstance;
        }
    }

    private AccountManger() {

    }

    private String name = ""; //名称
    private String bdusstoken = ""; //登陆凭据
    private String id = ""; //用户id
    private String portrait = "";//我也不知道是什么

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBdusstoken() {
        return bdusstoken;
    }

    public void setBdusstoken(String bdusstoken) {
        this.bdusstoken = bdusstoken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setPortraitMd5(String md5) {
        this.portrait = "http://himg.bdimg.com/sys/portrait/item/" + md5 + ".jpg";
    }
}
