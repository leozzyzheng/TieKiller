package com.leozzyzheng.tiekiller.ui.base;

import android.app.Application;
import com.leozzyzheng.tiekiller.controller.AccountManger;
import com.leozzyzheng.tiekiller.http.HttpEngineProvider;

/**
 * Created by leozzyzheng on 2015/4/20.
 * 自定义的Application类
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //创建http引擎
        HttpEngineProvider.getInstance().createVollyEngine(this);

        //初始化各类单例
        AccountManger.getInstance().loadFromFile(this);
    }
}
