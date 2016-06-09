package com.mogujie.tt.net.request;

/**
 * Created by alluser on 2016/6/8.
 */
public class GetFriendListMsg {

    private MsgHeader msgHeader;
    private GetFriendListReqBody getFriendListBody;
//    private String bodyStr;

    public GetFriendListMsg(MsgHeader msgHeader, GetFriendListReqBody getFriendListBody) {
        this.msgHeader = msgHeader;
        this.getFriendListBody = getFriendListBody;
    }
   /* public GetFriendListMsg(MsgHeader msgHeader, String bodyStr) {
        this.msgHeader = msgHeader;
        this.bodyStr = bodyStr;
    }

    public String getBodyStr() {
        return bodyStr;
    }

    public void setBodyStr(String bodyStr) {
        this.bodyStr = bodyStr;
    }*/

    public GetFriendListMsg() {
    }

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public GetFriendListReqBody getGetFriendListBody() {
        return getFriendListBody;
    }

    public void setGetFriendListBody(GetFriendListReqBody getFriendListBody) {
        this.getFriendListBody = getFriendListBody;
    }
}
