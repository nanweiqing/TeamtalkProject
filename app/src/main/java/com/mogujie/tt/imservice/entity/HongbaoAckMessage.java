package com.mogujie.tt.imservice.entity;

import android.util.Log;

import com.mogujie.tt.DB.entity.MessageEntity;
import com.mogujie.tt.DB.entity.PeerEntity;
import com.mogujie.tt.DB.entity.UserEntity;
import com.mogujie.tt.config.DBConstant;
import com.mogujie.tt.config.MessageConstant;
import com.mogujie.tt.imservice.support.SequenceNumberMaker;

import org.jboss.netty.logging.CommonsLoggerFactory;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 红包消息
 * @author : gy on 16-5-16.
 */
public class HongbaoAckMessage extends MessageEntity implements Serializable {

    //红包响应消息发送的消息和其显示的消息内容是不一样的，所以用showContent和content进行区分
    //譬如我点开红包后应该显示我领取了张三发的红包，但此时我实际上发送给张三的消息是李四领取了我发的红包
    private String showContent;

    public String getShowContent() {
        return showContent;
    }

    public void setShowContent(String showContent) {
        this.showContent = showContent;
    }

    public HongbaoAckMessage(){
        msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
        Log.i("MoGuLogger","红包确认消息生成的序列号为:"+msgId);
    }

     private HongbaoAckMessage(MessageEntity entity){
         /**父类的id*/
         id =  entity.getId();
         msgId  = entity.getMsgId();
         fromId = entity.getFromId();
         toId   = entity.getToId();
         sessionKey = entity.getSessionKey();
         content=entity.getContent();
         msgType=entity.getMsgType();
         displayType=entity.getDisplayType();
         status = entity.getStatus();
         created = entity.getCreated();
         updated = entity.getUpdated();
     }

     public static HongbaoAckMessage parseFromNet(MessageEntity entity){
         String strContent = entity.getContent();
         // 判断开头与结尾
         if (strContent.startsWith(MessageConstant.HONGBAO_MSG_ACK_START)
                 && strContent.endsWith(MessageConstant.HONGBAO_MSG_ACK_END)){
             String hongbaoContent = strContent.substring(MessageConstant.HONGBAO_MSG_ACK_START.length());
             hongbaoContent = hongbaoContent.substring(0,hongbaoContent.indexOf(MessageConstant.HONGBAO_MSG_ACK_END));
             HongbaoAckMessage hongbaoAckMessage = new HongbaoAckMessage(entity);
             hongbaoAckMessage.setStatus(MessageConstant.MSG_SUCCESS);
             hongbaoAckMessage.setDisplayType(DBConstant.SHOW_HONGBAO_ACK_TYPE);
             hongbaoAckMessage.setContent(hongbaoContent);
             return hongbaoAckMessage;
         }else{
             throw new RuntimeException("no hongbao type,cause by [start,end] is wrong!");
         }


     }

    public static HongbaoAckMessage parseFromDB(MessageEntity entity){
        if(entity.getDisplayType()!=DBConstant.SHOW_HONGBAO_ACK_TYPE){
            throw new RuntimeException("#HongbaoMessage# parseFromDB,not SHOW_HONGBAO_ACK_TYPE");
        }
        HongbaoAckMessage hongbaoAckMessage = new HongbaoAckMessage(entity);
        return hongbaoAckMessage;
    }

    public static HongbaoAckMessage buildForSend(String content, UserEntity fromUser, PeerEntity peerEntity){
        HongbaoAckMessage hongbaoAckMessage = new HongbaoAckMessage();
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        hongbaoAckMessage.setFromId(fromUser.getPeerId());
        hongbaoAckMessage.setToId(peerEntity.getPeerId());
        hongbaoAckMessage.setUpdated(nowTime);
        hongbaoAckMessage.setCreated(nowTime);
        hongbaoAckMessage.setDisplayType(DBConstant.SHOW_HONGBAO_ACK_TYPE);
        hongbaoAckMessage.setGIfEmo(true);
        int peerType = peerEntity.getType();
        int msgType = peerType == DBConstant.SESSION_TYPE_GROUP ? DBConstant.MSG_TYPE_GROUP_TEXT
                : DBConstant.MSG_TYPE_SINGLE_TEXT;
        hongbaoAckMessage.setMsgType(msgType);
        hongbaoAckMessage.setStatus(MessageConstant.MSG_SENDING);
        // 内容的设定
        hongbaoAckMessage.setContent(content);
        hongbaoAckMessage.buildSessionKey(true);
        return hongbaoAckMessage;
    }


    /**
     * Not-null value.
     * DB的时候需要
     */
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public byte[] getSendContent() {

        // 发送的时候非常关键
        String encryptContent = MessageConstant.HONGBAO_MSG_ACK_START
                + content + MessageConstant.HONGBAO_MSG_ACK_END;
        try {
            /** 加密*/
            String sendContent =new String(com.mogujie.tt.Security.getInstance().EncryptMsg(encryptContent));
            return sendContent.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
