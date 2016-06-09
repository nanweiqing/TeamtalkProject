package com.mogujie.tt.net.response;

import java.util.List;

/**
 * Created by gy on 2016/6/9.
 */
public class GetFriendListRespBody {
    /*"userId": "IM0032016052415501119422",
            "countNum": "2",
            "startNum": "1",
            "endNum": "50",*/
    private String userId;
    private int countNum;
    private int startNum;
    private int endNum;
    private List<FriendDetailInfo> Datas;

    public GetFriendListRespBody(String userId, int countNum, int startNum, int endNum, List<FriendDetailInfo> datas) {
        this.userId = userId;
        this.countNum = countNum;
        this.startNum = startNum;
        this.endNum = endNum;
        Datas = datas;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getCountNum() {
        return countNum;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getEndNum() {
        return endNum;
    }

    public void setEndNum(int endNum) {
        this.endNum = endNum;
    }

    public List<FriendDetailInfo> getDatas() {
        return Datas;
    }

    public void setDatas(List<FriendDetailInfo> datas) {
        Datas = datas;
    }
}
