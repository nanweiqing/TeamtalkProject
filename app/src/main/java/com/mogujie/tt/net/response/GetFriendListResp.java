package com.mogujie.tt.net.response;

import com.mogujie.tt.net.request.MsgHeader;

import java.util.List;

/**
 *
 *
 * Created by gy on 2016/6/9.
 */
public class GetFriendListResp {
    private MsgHeader msgHeader;
//    private GetFriendListRespBody msgBody;
    private String msgBody;
    public GetFriendListResp(MsgHeader msgHeader, String getFriendListRespBody) {
        this.msgHeader = msgHeader;
        this.msgBody = getFriendListRespBody;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public String getGetFriendListRespBody() {
        return msgBody;
    }

    public void setGetFriendListRespBody(String getFriendListRespBody) {
        this.msgBody = getFriendListRespBody;
    }
}
