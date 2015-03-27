package com.leozzyzheng.tiekiller.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData.LikeForumData;
import com.leozzyzheng.tiekiller.utils.UILHelper;

import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/27.
 * 喜欢贴吧的Adapter
 */
public class LikeForumListAdapter extends BaseAdapter {

    private List<LikeForumData> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public LikeForumListAdapter(Context context, List<LikeForumData> data) {
        mData = data;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (mData != null && mData.get(position) != null) {
            return mData.get(position).getForum_id();
        } else {
            return 0;
        }
    }

    private static class ViewHolder {
        public ImageView forumAvatar;
        public TextView forumName;
        public TextView forumLevel;
        public TextView forumIsSign;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_like_forum, parent, false);
            viewHolder.forumAvatar = (ImageView) convertView.findViewById(R.id.forum_avatar);
            viewHolder.forumName = (TextView) convertView.findViewById(R.id.forum_name);
            viewHolder.forumLevel = (TextView) convertView.findViewById(R.id.forum_level);
            viewHolder.forumIsSign = (TextView) convertView.findViewById(R.id.forum_is_sign);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mData != null && position < mData.size() && position >= 0) {
            LikeForumData data = mData.get(position);
            UILHelper.getImageLoader(mContext).displayImage(data.getAvatar(), viewHolder.forumAvatar);
            viewHolder.forumName.setText(data.getForum_name());
            viewHolder.forumLevel.setText(data.getLevel_id());
            viewHolder.forumIsSign.setText(data.getIs_sign() == 1 ? "已签到" : "未签到");
        }

        return convertView;
    }
}
