package com.mogujie.tt.imservice.event;

/**
 *
 * @author : gy on 16-5-19.
 */
public class HongbaoAckEvent {
    //发送消息确认时，发送内容和显示内容是不一样的，显示"我收到了张三的红包"，
    //但要发送时内容就变为了"李四抢了我发的红包"
    private String sendContent;
    private String showContent;
    //标识是否将消息发出还是说只存入本地数据库不对外发消息
    //0代表对外不发消息，1代表发消息，默认是1
    // (主要针对领取红包的消息，领取自己的红包不需要对外发消息，只存数据库即可)
    private int flag = 1;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public HongbaoAckEvent() {

    }

    public HongbaoAckEvent(String sendContent) {
        this.sendContent = sendContent;
    }

    public HongbaoAckEvent(String sendContent, String showContent) {
        this.sendContent = sendContent;
        this.showContent = showContent;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }
}
