package com.leozzyzheng.tiekiller.http.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/27.
 * 用来存储喜欢的贴吧的数据结构
 */
public class LikeForumListData {

    public static class LikeForumData {
        private int forum_id;
        private String avatar;
        private int is_sign;
        private String forum_name;
        private String level_id;

        private LikeForumData(JSONObject jsonObject) throws JSONException {
            this.forum_id = jsonObject.getInt("forum_id");
            this.avatar = jsonObject.getString("avatar");
            this.is_sign = jsonObject.getInt("is_sign");
            this.forum_name = jsonObject.getString("forum_name");
            this.level_id = jsonObject.getString("level_id");
        }

        public int getForum_id() {
            return forum_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public int getIs_sign() {
            return is_sign;
        }

        public String getForum_name() {
            return forum_name;
        }

        public String getLevel_id() {
            return level_id;
        }
    }

    private LikeForumListData() {

    }

    private List<LikeForumData> mLikeForumDatas = new ArrayList<LikeForumData>();

    public static LikeForumListData parse(JSONObject jsonObject) throws JSONException {
        JSONArray likeArray = jsonObject.getJSONArray("like_forum");
        LikeForumListData likeForumListData = new LikeForumListData();

        for (int i = 0; i < likeArray.length(); ++i) {
            LikeForumData likeForumData = new LikeForumData(likeArray.getJSONObject(i));
            likeForumListData.mLikeForumDatas.add(likeForumData);
        }

        return likeForumListData;
    }

    public List<LikeForumData> getLikeForumDatas() {
        return mLikeForumDatas;
    }
}
