package com.mogujie.tt.net.request;

/**
 *根据userId获取好友列表详细信息
 * Created by gy on 2016/6/8.
 */
public class GetFriendListReqBody {

    private String userId;
    private String startNum;
    private String endNum;

    public GetFriendListReqBody(String userId, String startNum, String endNum) {
        this.userId = userId;
        this.startNum = startNum;
        this.endNum = endNum;
    }

    public GetFriendListReqBody() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }
}
