package com.leozzyzheng.tiekiller.http.data;

import java.util.List;

/**
 * Created by leozzyzheng on 2015/4/24.
 * 帖子数据
 */
public class ForumPageListData {

    private List<ForumPageData> forumPageDataList;

    public static class ForumPageData {

        private long id;
        private long tie;
        private long title;
        private int reply_num;
        private String last_time;
        private long last_time_int;
        private boolean is_top; //置顶
        private boolean is_good; //精品
    }

}
