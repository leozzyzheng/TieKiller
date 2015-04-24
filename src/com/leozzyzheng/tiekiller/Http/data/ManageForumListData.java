package com.leozzyzheng.tiekiller.http.data;

import com.leozzyzheng.tiekiller.http.exception.CannotParseDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leozzyzheng on 2015/4/24.
 * 通过进入某个吧后获取的有管理权限贴吧的名称数据
 */
public class ManageForumListData {
    private List<String> manager;
    private List<String> assist;

    private ManageForumListData() {

    }

    public static ManageForumListData parse(HashMap phpObject) throws CannotParseDataException {
        ManageForumListData listData = new ManageForumListData();
        try {
            HashMap manager_info = (HashMap) phpObject.get("manager_info");

            if (manager_info != null) {
                HashMap manager = (HashMap) manager_info.get("manager");
                if (manager != null) {
                    HashMap map = (HashMap) manager.get("forum_list");
                    listData.manager = new ArrayList<String>();

                    for (int i = 0; i < map.size(); ++i) {
                        String name = (String) map.get(i);
                        listData.manager.add(name);
                    }

                } else {
                    throw new CannotParseDataException();
                }

                HashMap assist = (HashMap) manager_info.get("assist");
                if (assist != null) {
                    HashMap map = (HashMap) assist.get("forum_list");
                    listData.assist = new ArrayList<String>();

                    for (int i = 0; i < map.size(); ++i) {
                        String name = (String) map.get(i);
                        listData.assist.add(name);
                    }

                } else {
                    throw new CannotParseDataException();
                }
            } else {
                throw new CannotParseDataException();
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new CannotParseDataException();
        }

        return listData;
    }

    /**
     * 获取一个有管理权限的贴吧名称，不管是什么职位都可以
     *
     * @return 如果可以拿到则返回名称，否则返回bull
     */
    public String justGetOne() {
        if (manager.size() != 0) {
            return manager.get(0);
        }

        if (assist.size() != 0) {
            return assist.get(0);
        }

        return null;
    }
}
