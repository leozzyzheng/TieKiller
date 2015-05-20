package com.leozzyzheng.tiekiller.event;

import com.leozzyzheng.tiekiller.http.data.BaWuForumListData;
import com.leozzyzheng.tiekiller.http.data.LikeForumListData;

/**
 * Created by leozzyzheng on 2015/4/20.
 * 消息定义
 */
public class Msg {

    /**
     * *************************消息枚举,加消息写这里，其余地方不用动**********************
     */

    public static final MsgCode NONE = new MsgCode();
    public static final MsgCode LIKE_FORUM_REQUEST = new MsgCode(LikeForumListData.class);
    public static final MsgCode MANAGE_FORUM_REQUEST = new MsgCode(BaWuForumListData.class);

    /**
     * ***********************消息枚举结束*********************************************
     */

    private MsgCode code;
    private Object data;

    public Msg(MsgCode code, Object data) {
        this.code = code;
        this.data = data;
    }

    public MsgCode getCode() {
        return code;
    }

    public Object getData() {
        return code.convert(data);
    }

    public static class MsgCode {
        private static int index = 0;

        private int code;
        private Class<?> cls;

        private MsgCode() {
            this(null);
        }

        private MsgCode(Class<?> cls) {
            this.code = ++index;
            this.cls = cls;
        }

        public int code() {
            return code;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof MsgCode && code == ((MsgCode) o).code();
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
}
