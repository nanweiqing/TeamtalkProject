package com.mogujie.tt.net.response;

/**
 * 获取到的好友详情信息
 * Created by gy on 2016/6/9.
 */
public class FriendDetailInfo {
    private String userId;
    /*"refId": " 000000111",
            "fUserId": " 122222",
            "fUserName": " 1",
            "fNickName": "成功",
            "fSignature": " 0000001",
            "fUserIcon": " 1",
            "fIntro": "123成功",
            "fCreateTime": "2016-05-24 10:33:45"*/
    private String refId;
    private String fUserId;
    private String fUserName;
    private String fNickName;
    private String fSignature;
    private String fUserIcon;
    private String fIntro;
    private String fCreateTime;

    public FriendDetailInfo(String userId, String refId, String fUserId, String fUserName, String fNickName, String fSignature, String fUserIcon, String fIntro, String fCreateTime) {
        this.userId = userId;
        this.refId = refId;
        this.fUserId = fUserId;
        this.fUserName = fUserName;
        this.fNickName = fNickName;
        this.fSignature = fSignature;
        this.fUserIcon = fUserIcon;
        this.fIntro = fIntro;
        this.fCreateTime = fCreateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getfUserId() {
        return fUserId;
    }

    public void setfUserId(String fUserId) {
        this.fUserId = fUserId;
    }

    public String getfUserName() {
        return fUserName;
    }

    public void setfUserName(String fUserName) {
        this.fUserName = fUserName;
    }

    public String getfNickName() {
        return fNickName;
    }

    public void setfNickName(String fNickName) {
        this.fNickName = fNickName;
    }

    public String getfSignature() {
        return fSignature;
    }

    public void setfSignature(String fSignature) {
        this.fSignature = fSignature;
    }

    public String getfUserIcon() {
        return fUserIcon;
    }

    public void setfUserIcon(String fUserIcon) {
        this.fUserIcon = fUserIcon;
    }

    public String getfIntro() {
        return fIntro;
    }

    public void setfIntro(String fIntro) {
        this.fIntro = fIntro;
    }

    public String getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(String fCreateTime) {
        this.fCreateTime = fCreateTime;
    }
}
