package com.leozzyzheng.tiekiller.event;

import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;

/**
 * Created by leozzyzheng on 2015/4/20.
 * 消息定义
 */
public class MSG {
    public static class MSG_CODE {
        private static int index = 0;

        private int code;
        private Class<?> cls;

        private MSG_CODE() {
            this(null);
        }

        private MSG_CODE(Class<?> cls) {
            this.code = ++index;
            this.cls = cls;
        }

        public int code() {
            return code;
        }

        /**
         * 检查类型，如果类型正确，则直接返回原数据
         *
         * @param data 源数据
         * @return 类型正确的情况下的源数据
         * @throws IllegalArgumentException 源数据并不是之前制定的传输的类型的时候抛出这个异常
         *                                  最好不要catch这个异常从而在运行时能够捕捉到不正确的数据传递接收
         */
        public Object convert(Object data) {
            if (cls == null || data == null) {
                return data;
            }

            if (cls.isInstance(data)) {
                return data;
            } else {
                throw new IllegalArgumentException("Data is not the instance of class:" + cls.getName());
            }
        }
    }

    public static final MSG_CODE NONE = new MSG_CODE();
    public static final MSG_CODE MSG_PROCEEDING_STATE = new MSG_CODE(String.class);
    public static final MSG_CODE LIKE_FORUM_REQUEST_SUCCESS = new MSG_CODE(LikeForumListData.class);
    public static final MSG_CODE MANAGE_FORUM_REQUEST_SUCCESS = new MSG_CODE(BaWuForumListData.class);

    private MSG_CODE code;
    private Object data;

    public MSG(MSG_CODE code, Object data) {
        this.code = code;
        this.data = data;
    }

    public MSG_CODE getCode() {
        return code;
    }

    public Object getData() {
        return code.convert(data);
    }
}
