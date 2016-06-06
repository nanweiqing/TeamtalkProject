package com.mogujie.tt.imservice.entity;

import android.text.TextUtils;
import android.util.Log;

import com.mogujie.tt.DB.entity.MessageEntity;
import com.mogujie.tt.config.DBConstant;
import com.mogujie.tt.config.MessageConstant;
import com.mogujie.tt.protobuf.helper.ProtoBuf2JavaBean;
import com.mogujie.tt.protobuf.IMBaseDefine;

import org.jboss.netty.util.internal.StringUtil;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 *
 * historical reasons,没有充分利用msgType字段
 * 多端的富文本的考虑
 */
public class MsgAnalyzeEngine {
    public static String analyzeMessageDisplay(String content){
        String finalRes = content;
        String originContent = content;
        while (!originContent.isEmpty()) {
            int nStart = originContent.indexOf(MessageConstant.IMAGE_MSG_START);
            if (nStart < 0) {// 没有头
                break;
            } else {
                String subContentString = originContent.substring(nStart);
                int nEnd = subContentString.indexOf(MessageConstant.IMAGE_MSG_END);
                if (nEnd < 0) {// 没有尾
                    String strSplitString = originContent;
                    break;
                } else {// 匹配到
                    String pre = originContent.substring(0, nStart);

                    originContent = subContentString.substring(nEnd
                            + MessageConstant.IMAGE_MSG_END.length());

                    if(!TextUtils.isEmpty(pre) || !TextUtils.isEmpty(originContent)){
                        finalRes = DBConstant.DISPLAY_FOR_MIX;
                    }else{
                        finalRes = DBConstant.DISPLAY_FOR_IMAGE;
                    }
                }
            }
        }
        return finalRes;
    }


    // 抽离放在同一的地方
    public static MessageEntity analyzeMessage(IMBaseDefine.MsgInfo msgInfo) {
       MessageEntity messageEntity = new MessageEntity();

       messageEntity.setCreated(msgInfo.getCreateTime());
       messageEntity.setUpdated(msgInfo.getCreateTime());
       messageEntity.setFromId(msgInfo.getFromSessionId());
       messageEntity.setMsgId(msgInfo.getMsgId());
       messageEntity.setMsgType(ProtoBuf2JavaBean.getJavaMsgType(msgInfo.getMsgType()));
       messageEntity.setStatus(MessageConstant.MSG_SUCCESS);
       messageEntity.setContent(msgInfo.getMsgData().toStringUtf8());
        /**
         * 解密文本信息
         */
       String desMessage = new String(com.mogujie.tt.Security.getInstance().DecryptMsg(msgInfo.getMsgData().toStringUtf8()));
       messageEntity.setContent(desMessage);
        Log.i("MoGuLogger","解密后的文本信息内容为:"+desMessage);

       // 文本信息不为空
       if(!TextUtils.isEmpty(desMessage)){
           List<MessageEntity> msgList =  textDecode(messageEntity);
           if(msgList.size()>1){
               // 混合消息
               MixMessage mixMessage = new MixMessage(msgList);
               return mixMessage;
           }else if(msgList.size() == 0){
              // 可能解析失败 默认返回文本消息
              return TextMessage.parseFromNet(messageEntity);
           }else{
               //简单消息，返回第一个
               return msgList.get(0);
           }
       }else{
           // 如果为空
           return TextMessage.parseFromNet(messageEntity);
       }
    }


    /**
     * todo 优化字符串分析
     * 增加发红包的消息格式
     * @param msg
     * @return
     */
    private static List<MessageEntity> textDecode(MessageEntity msg){
        List<MessageEntity> msgList = new ArrayList<>();

        String originContent = msg.getContent();
        Log.i("MoGuLogger","红包确认信息内容为:"+originContent);
        while (!TextUtils.isEmpty(originContent)) {

            int mmStart = originContent.indexOf(MessageConstant.HONGBAO_MSG_ACK_START);
            Log.i("MoGuLogger","红包确认信息前缀有无:"+mmStart);
            if(mmStart >= 0){
                int mEnd = originContent.indexOf(MessageConstant.HONGBAO_MSG_ACK_END);
                if(mEnd > 0){
                    MessageEntity entity = addMessage(msg,originContent);
                    if(entity!=null){
                        msgList.add(entity);
                    }
                    originContent = "";
                }
            }


            int mStart = originContent.indexOf(MessageConstant.HONGBAO_MSG_START);
            Log.i("MoGuLogger","红包前缀有无:"+mStart);
            if(mStart >= 0){
                Log.i("MoGuLogger","收到红包时，匹配到的红包前缀为:"+mStart);
                int mEnd = originContent.indexOf(MessageConstant.HONGBAO_MSG_END);
                if(mEnd > 0){
                    /*String subContentString = originContent.substring(mStart);
                    Log.i("MoGuLogger","收到红包时，匹配到的红包前缀2为:"+subContentString);
                    String matchString = subContentString.substring(0, mEnd
                            + MessageConstant.HONGBAO_MSG_END.length());
                    Log.i("MoGuLogger","收到红包时，匹配到的红包前缀3为:"+matchString);*/
                    MessageEntity entity = addMessage(msg,originContent);
                    if(entity!=null){
                        msgList.add(entity);
                    }
                    originContent = "";
                }
            }

            int nStart = originContent.indexOf(MessageConstant.IMAGE_MSG_START);
            Log.i("MoGuLogger","收到图片时，匹配到的图片前缀为:"+nStart);
            if (nStart < 0) {// 没有头
                String strSplitString = originContent;

                MessageEntity entity = addMessage(msg, strSplitString);
                if(entity!=null){
                    msgList.add(entity);
                }

                originContent = "";
            } else {
                String subContentString = originContent.substring(nStart);
                Log.i("MoGuLogger","收到图片时，匹配到的图片前缀1为:"+subContentString);
                int nEnd = subContentString.indexOf(MessageConstant.IMAGE_MSG_END);
                Log.i("MoGuLogger","收到图片时，匹配到的图片前缀2为:"+nEnd);
                if (nEnd < 0) {// 没有尾
                    String strSplitString = originContent;


                    MessageEntity entity = addMessage(msg,strSplitString);
                    if(entity!=null){
                        msgList.add(entity);
                    }

                    originContent = "";
                } else {// 匹配到
                    String pre = originContent.substring(0, nStart);
                    Log.i("MoGuLogger","匹配到的图片前缀4为:"+pre);
                    MessageEntity entity1 = addMessage(msg,pre);
                    if(entity1!=null){
                        msgList.add(entity1);
                    }

                    String matchString = subContentString.substring(0, nEnd
                            + MessageConstant.IMAGE_MSG_END.length());
                    Log.i("MoGuLogger","匹配到的图片前缀3为:"+matchString);
                    MessageEntity entity2 = addMessage(msg,matchString);
                    if(entity2!=null){
                        msgList.add(entity2);
                    }

                    originContent = subContentString.substring(nEnd
                            + MessageConstant.IMAGE_MSG_END.length());
                }
            }
        }

        return msgList;
    }


    public static MessageEntity addMessage(MessageEntity msg,String strContent) {
        if (TextUtils.isEmpty(strContent.trim())){
            return null;
        }
        msg.setContent(strContent);

        if (strContent.startsWith(MessageConstant.IMAGE_MSG_START)
                && strContent.endsWith(MessageConstant.IMAGE_MSG_END)) {
            try {
                ImageMessage imageMessage =  ImageMessage.parseFromNet(msg);
                return imageMessage;
            } catch (JSONException e) {
                // e.printStackTrace();
                return null;
            }
        } else if(strContent.startsWith(MessageConstant.HONGBAO_MSG_START)
                && strContent.endsWith(MessageConstant.HONGBAO_MSG_END)){
            Log.i("MoGuLogger","匹配到的红包消息为:"+strContent);
                HongbaoMessage hongbaoMessage =  HongbaoMessage.parseFromNet(msg);
                return hongbaoMessage;
        }else if(strContent.startsWith(MessageConstant.HONGBAO_MSG_ACK_START)
                && strContent.endsWith(MessageConstant.HONGBAO_MSG_ACK_END)){
            Log.i("MoGuLogger","匹配到的红包确认消息为:"+strContent);
            HongbaoAckMessage hongbaoAckMessage =  HongbaoAckMessage.parseFromNet(msg);
            return hongbaoAckMessage;
        }else{
           return TextMessage.parseFromNet(msg);
        }
    }

}
