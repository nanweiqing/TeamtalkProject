package com.mogujie.tt.imservice.entity;

import com.mogujie.tt.DB.entity.MessageEntity;
import com.mogujie.tt.DB.entity.PeerEntity;
import com.mogujie.tt.DB.entity.UserEntity;
import com.mogujie.tt.config.DBConstant;
import com.mogujie.tt.config.MessageConstant;
import com.mogujie.tt.imservice.support.SequenceNumberMaker;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 红包消息
 * @author : gy on 16-5-16.
 */
public class HongbaoMessage extends MessageEntity implements Serializable {

    //标识红包是否被我抢过,0代表未被抢过，1代表被抢过
    private int isHbStealedByMe = 0;

    public int getIsHbStealedByMe() {
        return isHbStealedByMe;
    }

    public void setIsHbStealedByMe(int isHbStealedByMe) {
        this.isHbStealedByMe = isHbStealedByMe;
    }

    public HongbaoMessage(){
         msgId = SequenceNumberMaker.getInstance().makelocalUniqueMsgId();
     }

     private HongbaoMessage(MessageEntity entity){
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

     public static HongbaoMessage parseFromNet(MessageEntity entity){
         String strContent = entity.getContent();
         // 判断开头与结尾
         if (strContent.startsWith(MessageConstant.HONGBAO_MSG_START)
                 && strContent.endsWith(MessageConstant.HONGBAO_MSG_END)){
             String hongbaoContent = strContent.substring(MessageConstant.HONGBAO_MSG_START.length());
             hongbaoContent = hongbaoContent.substring(0,hongbaoContent.indexOf(MessageConstant.HONGBAO_MSG_END));
             HongbaoMessage hongbaoMessage = new HongbaoMessage(entity);
             hongbaoMessage.setStatus(MessageConstant.MSG_SUCCESS);
             hongbaoMessage.setDisplayType(DBConstant.SHOW_HONGBAO_TYPE);
             hongbaoMessage.setContent(hongbaoContent);
             return hongbaoMessage;
         }else{
             throw new RuntimeException("no hongbao type,cause by [start,end] is wrong!");
         }


     }

    public static HongbaoMessage parseFromDB(MessageEntity entity){
        if(entity.getDisplayType()!=DBConstant.SHOW_HONGBAO_TYPE){
            throw new RuntimeException("#HongbaoMessage# parseFromDB,not SHOW_HONGBAO_TYPE");
        }
        HongbaoMessage hongbaoMessage = new HongbaoMessage(entity);
        return hongbaoMessage;
    }

    public static HongbaoMessage buildForSend(String content, UserEntity fromUser, PeerEntity peerEntity){
        HongbaoMessage hongbaoMessage = new HongbaoMessage();
        int nowTime = (int) (System.currentTimeMillis() / 1000);
        hongbaoMessage.setFromId(fromUser.getPeerId());
        hongbaoMessage.setToId(peerEntity.getPeerId());
        hongbaoMessage.setUpdated(nowTime);
        hongbaoMessage.setCreated(nowTime);
        hongbaoMessage.setDisplayType(DBConstant.SHOW_HONGBAO_TYPE);
        hongbaoMessage.setGIfEmo(true);
        int peerType = peerEntity.getType();
        int msgType = peerType == DBConstant.SESSION_TYPE_GROUP ? DBConstant.MSG_TYPE_GROUP_TEXT
                : DBConstant.MSG_TYPE_SINGLE_TEXT;
        hongbaoMessage.setMsgType(msgType);
        hongbaoMessage.setStatus(MessageConstant.MSG_SENDING);
        // 内容的设定
        hongbaoMessage.setContent(content);
        hongbaoMessage.buildSessionKey(true);
        return hongbaoMessage;
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
        String encryptContent = MessageConstant.HONGBAO_MSG_START
                + content + MessageConstant.HONGBAO_MSG_END;
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
