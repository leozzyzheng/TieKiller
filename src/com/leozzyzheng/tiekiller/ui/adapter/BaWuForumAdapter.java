package com.leozzyzheng.tiekiller.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.leozzyzheng.tiekiller.R;
import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;

import java.util.List;

/**
 * Created by leozzyzheng on 2015/3/27.
 * 有权限吧的adapter
 */
public class BaWuForumAdapter extends BaseAdapter {

    private List<BaWuForumListData.BaWuForumData> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public BaWuForumAdapter(Context context, List<BaWuForumListData.BaWuForumData> data) {
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

    /**
     * 设置数据，会让整个列表都刷新
     *
     * @param data 新的数据
     */
    public void setData(List<BaWuForumListData.BaWuForumData> data) {
        mData = data;
        notifyDataSetInvalidated();
    }

    private static class ViewHolder {
        public TextView forumName;
        public TextView forumRole;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_bawu_forum, parent, false);
            viewHolder.forumName = (TextView) convertView.findViewById(R.id.forum_name);
            viewHolder.forumRole = (TextView) convertView.findViewById(R.id.forum_role);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mData != null && position < mData.size() && position >= 0) {
            BaWuForumListData.BaWuForumData data = mData.get(position);
            viewHolder.forumName.setText(data.getForum_name());
            viewHolder.forumRole.setText(data.getRole().equalsIgnoreCase("assist") ? "小吧主" : "大吧主");
        }

        return convertView;
    }
}
