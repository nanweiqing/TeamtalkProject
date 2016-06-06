package com.mogujie.tt.ui.widget.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mogujie.tt.DB.entity.MessageEntity;
import com.mogujie.tt.DB.entity.UserEntity;
import com.mogujie.tt.R;
import com.mogujie.tt.imservice.entity.HongbaoAckMessage;
import com.mogujie.tt.imservice.entity.HongbaoMessage;
import com.mogujie.tt.ui.helper.Emoparser;

/**
 * 红包Ack的RenderView
 * @author : gy on 16-5-25.
 *这个样式只有一个，不需要根据mine 与 other区分成两个
 * 样式根据mine 与other不同可以分成两个
 */
public class HongbaoAckRenderView extends  BaseMsgRenderView {
    /** 文字消息体 */
    private TextView messageContent;

    public static HongbaoAckRenderView inflater(Context context, ViewGroup viewGroup, boolean isMine){
        int resource = isMine?R.layout.tt_mine_hongbao_ack_item:R.layout.tt_other_hongbao_ack_item;
        HongbaoAckRenderView hongbaoAckRenderView = (HongbaoAckRenderView) LayoutInflater.from(context).inflate(resource, viewGroup, false);
        hongbaoAckRenderView.setMine(isMine);
        hongbaoAckRenderView.setParentView(viewGroup);
        return hongbaoAckRenderView;
    }

    public HongbaoAckRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        messageContent = (TextView) findViewById(R.id.message_content);
    }


    /**
     * 控件赋值
     * @param messageEntity
     * @param userEntity
     */
    @Override
    public void render(MessageEntity messageEntity, UserEntity userEntity,Context context) {
        super.render(messageEntity, userEntity,context);
        HongbaoAckMessage hongbaoAckMessage = (HongbaoAckMessage) messageEntity;
        // 按钮的长按也是上层设定的
        // url 路径可以设定 跳转哦哦
        String content = hongbaoAckMessage.getContent();
        messageContent.setText(Emoparser.getInstance(getContext()).emoCharsequence(content)); // 所以上层还是处理好之后再给我 Emoparser 处理之后的

    }
    private static final String SCHEMA ="com.mogujie.tt://message_private_url";
    private static final String PARAM_UID ="uid";
    private String urlRegex = "((?:(http|https|Http|Https|rtsp|Rtsp):\\/\\/(?:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,64}(?:\\:(?:[a-zA-Z0-9\\$\\-\\_\\.\\+\\!\\*\\'\\(\\)\\,\\;\\?\\&\\=]|(?:\\%[a-fA-F0-9]{2})){1,25})?\\@)?)?((?:(?:[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.)+(?:(?:aero|arpa|asia|a[cdefgilmnoqrstuwxz])|(?:biz|b[abdefghijmnorstvwyz])|(?:cat|com|coop|c[acdfghiklmnoruvxyz])|d[ejkmoz]|(?:edu|e[cegrstu])|f[ijkmor]|(?:gov|g[abdefghilmnpqrstuwy])|h[kmnrtu]|(?:info|int|i[delmnoqrst])|(?:jobs|j[emop])|k[eghimnrwyz]|l[abcikrstuvy]|(?:mil|mobi|museum|m[acdghklmnopqrstuvwxyz])|(?:name|net|n[acefgilopruz])|(?:org|om)|(?:pro|p[aefghklmnrstwy])|qa|r[eouw]|s[abcdeghijklmnortuvyz]|(?:tel|travel|t[cdfghjklmnoprtvwz])|u[agkmsyz]|v[aceginu]|w[fs]|y[etu]|z[amw]))|(?:(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(?:25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[0-9])))(?:\\:\\d{1,5})?)(\\/(?:(?:[a-zA-Z0-9\\;\\/\\?\\:\\@\\&\\=\\#\\~\\-\\.\\+\\!\\*\\'\\(\\)\\,\\_])|(?:\\%[a-fA-F0-9]{2}))*)?(?:\\b|$)";


    @Override
    public void msgFailure(MessageEntity messageEntity) {
        super.msgFailure(messageEntity);
    }

    /**----------------set/get---------------------------------*/
    public TextView getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TextView messageContent) {
        this.messageContent = messageContent;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public ViewGroup getParentView() {
        return parentView;
    }

    public void setParentView(ViewGroup parentView) {
        this.parentView = parentView;
    }
}
