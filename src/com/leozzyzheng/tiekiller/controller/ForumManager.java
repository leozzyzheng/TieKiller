package com.leozzyzheng.tiekiller.controller;

import com.leozzyzheng.tiekiller.event.EventCenter;
import com.leozzyzheng.tiekiller.event.Msg;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.ForumPageListData.ForumPageData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.http.data.ManageForumListData;
import com.leozzyzheng.tiekiller.http.request.BaWuForumRequest;
import com.leozzyzheng.tiekiller.http.request.ForumPageRequest;
import com.leozzyzheng.tiekiller.http.request.ManageForumRequest;
import com.leozzyzheng.tiekiller.http.request.RecommendedForumRequest;

/**
 * Created by leozzyzheng on 2015/3/27.
 * 保存上次各种吧的数据的管理类
 */
public class ForumManager {
    private static ForumManager instance;

    public static ForumManager getInstance() {
        if (instance == null) {
            synchronized (ForumManager.class) {
                if (instance == null) {
                    instance = new ForumManager();
                }
            }
        }

        return instance;
    }

    private ForumManager() {

    }

    /**
     * 利用随便进入一个贴吧可以获取有管理权限贴吧名称的特点来获取有管理员权限的贴吧
     * 这个请求还是会调用{@link #requestManagerForum(String)} 来获取有管理权限贴吧的进一步信息
     */
    public void requestManagerForum() {
        ManageForumRequest request = new ManageForumRequest();
        request.send();
    }

    /**
     * 请求回调，请勿在UI层调用
     *
     * @param data 回调数据
     */
    public void __onReceivedManagerForumRequest(ManageForumListData data) {
        if (data != null) {
            requestManagerForum(data.justGetOne());
        } else {
            EventCenter.getInstance().trigger(Msg.MANAGE_FORUM_REQUEST, null);
        }
    }

    /**
     * 通过输入一个贴吧名来查找有管理权限的贴吧列表，实际上是请求了一个html网页进行正则匹配
     * 如果输入的确实是有权限的贴吧，那么会返回所有有权限的贴吧信息，否则返回空的信息或者失败
     *
     * @param name 有权限的贴吧名称
     */
    public void requestManagerForum(String name) {
        BaWuForumRequest request = new BaWuForumRequest(name);
        request.send();
    }

    /**
     * 请求回调，请勿在UI层调用
     *
     * @param baWuForumListData 回调数据
     */
    public void __onReceivedBaWuForumRequest(BaWuForumListData baWuForumListData) {
        if (baWuForumListData != null) {
            EventCenter.getInstance().trigger(Msg.MANAGE_FORUM_REQUEST, baWuForumListData);
        } else {
            EventCenter.getInstance().trigger(Msg.MANAGE_FORUM_REQUEST, null);
        }
    }

    /**
     * 获取喜欢贴吧的列表
     */
    public void requestLikeForum() {
        RecommendedForumRequest request = new RecommendedForumRequest();
        request.send();
    }

    /**
     * 请求回调，请勿在UI层使用
     *
     * @param data 回调数据
     */
    public void __onReceivedLikeFormRequest(LikeForumListData data) {
        EventCenter.getInstance().trigger(Msg.LIKE_FORUM_REQUEST, data);
    }

    /**
     * 请求指定贴吧的帖子页面
     *
     * @param name 贴吧名称
     * @param num  本次请求需要的帖子数量
     */
    public void requestForumPage(String name, int num) {
        ForumPageRequest request = new ForumPageRequest(name, num);
        request.send();
    }

    /**
     * 默认请求指定贴吧前50个帖子
     *
     * @param name 贴吧名称
     */
    public void requestForumPage(String name) {
        requestForumPage(name, 50);
    }

    public void __onReceivedForumPageRequest(ForumPageData data)
    {

    }
}
