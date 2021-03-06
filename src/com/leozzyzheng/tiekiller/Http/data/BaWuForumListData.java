package com.leozzyzheng.tiekiller.http.data;

import com.leozzyzheng.tiekiller.http.exception.CannotParseDataException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leozzyzheng on 2015/3/26.
 * 有吧务权限的吧列表
 */
public class BaWuForumListData {

    public static class BaWuForumData {
        private String role;
        private String forum_name;
        private int forum_id;

        private BaWuForumData(JSONObject jsonObject) throws JSONException {
            this.forum_id = jsonObject.getInt("forum_id");
            this.forum_name = jsonObject.getString("forum_name");
            this.role = jsonObject.getString("role");
        }

        public String getRole() {
            return role;
        }

        public String getForum_name() {
            return forum_name;
        }

        public int getForum_id() {
            return forum_id;
        }
    }

    private List<BaWuForumData> mForumList = new ArrayList<BaWuForumData>();


    private BaWuForumListData() {
    }

    public static BaWuForumListData parse(String html) throws CannotParseDataException {
        BaWuForumListData self = new BaWuForumListData();
        try {
            Pattern pattern = Pattern.compile("\"manage_forums\":([\\w\\W]*?\\}\\])");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                String manage = matcher.group();
                manage = manage.replace("\"manage_forums\":", "");
                JSONArray jsonArray = new JSONArray(manage);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    BaWuForumData data = new BaWuForumData(jsonObject);
                    self.mForumList.add(data);
                }

                return self;
            } else {
                throw new CannotParseDataException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CannotParseDataException();
        }
    }

    public List<BaWuForumData> getForumList() {
        return mForumList;
    }

    @Override
    public String toString() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (BaWuForumData data : mForumList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("forum_id", data.forum_id);
                jsonObject.put("role", data.role);
                jsonObject.put("forum_name", data.forum_name);
                jsonArray.put(jsonObject);
            }

            return jsonArray.toString();
        } catch (JSONException e) {
            return "";
        }
    }
}
