package com.csyd.yungewechat.po;

/**
 * Created by ChengShanyunduo
 * 2018/1/19
 * 微信接口文本消息
 */
public class TextMessage extends BaseMessage{

    private String Content;
    private String MsgId;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }
}
