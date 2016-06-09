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
    private GetFriendListRespBody msgBody;

    public GetFriendListResp(MsgHeader msgHeader, GetFriendListRespBody getFriendListRespBody) {
        this.msgHeader = msgHeader;
        this.msgBody = getFriendListRespBody;
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public GetFriendListRespBody getGetFriendListRespBody() {
        return msgBody;
    }

    public void setGetFriendListRespBody(GetFriendListRespBody getFriendListRespBody) {
        this.msgBody = getFriendListRespBody;
    }
}
