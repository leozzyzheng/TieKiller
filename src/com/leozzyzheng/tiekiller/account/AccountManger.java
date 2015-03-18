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

    private String name; //名称
    private String bdusstoken; //登陆凭据

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
}
