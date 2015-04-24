package com.leozzyzheng.tiekiller.controller;

import com.leozzyzheng.tiekiller.event.EventCenter;
import com.leozzyzheng.tiekiller.event.MSG;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;
import com.leozzyzheng.tiekiller.http.data.ManageForumListData;
import com.leozzyzheng.tiekiller.http.request.BaWuForumRequest;
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
            EventCenter.getInstance().trigger(MSG.MANAGE_FORUM_REQUEST_SUCCESS, null);
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
            EventCenter.getInstance().trigger(MSG.MANAGE_FORUM_REQUEST_SUCCESS, baWuForumListData);
        } else {
            EventCenter.getInstance().trigger(MSG.MANAGE_FORUM_REQUEST_SUCCESS, null);
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
        EventCenter.getInstance().trigger(MSG.LIKE_FORUM_REQUEST_SUCCESS, data);
    }
}
