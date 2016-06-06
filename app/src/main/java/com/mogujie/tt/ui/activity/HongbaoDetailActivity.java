/**
 * Created by alluser on 2016/5/24.
 */
package com.mogujie.tt.ui.activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.entity.HongbaoStealRecordEntity;
import com.mogujie.tt.imservice.event.HongbaoAckEvent;
import com.mogujie.tt.imservice.event.HongbaoEvent;
import com.mogujie.tt.ui.adapter.HongbaoRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class HongbaoDetailActivity extends Activity  implements View.OnClickListener{

    private ImageView hongbaoCloseIv;
    private TextView hongbaoSenderTv;
    private TextView hongbaoContenntTv;
    private TextView hongbaoMoneyTv;
    private TextView hongbaoStealOutTv;
    private ListView hongbaoDetailLv;
    private TextView hongbaoMyRecordTv;
    private HongbaoRecordAdapter hongbaoRecordAdapter;
    private String fromName;
    private String toName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_activity_hongbao_detail);

        fromName = getIntent().getStringExtra("fromName");
        toName = getIntent().getStringExtra("toName");
        //如果是我领取了我自己的红包，则不向群里发额外消息，如果我领取的是别人的红包，则向群里通知
        if(fromName.equals("me")){
            HongbaoAckEvent hongbaoAckEvent = new HongbaoAckEvent();
            hongbaoAckEvent.setShowContent("你领取了自己发的红包");
            hongbaoAckEvent.setFlag(0);
            EventBus.getDefault().post(hongbaoAckEvent);
        }else{
            HongbaoAckEvent hongbaoAckEvent = new HongbaoAckEvent(toName+"领取了你发的红包","你领取了"+fromName+"发的红包");
            hongbaoAckEvent.setFlag(1);
            EventBus.getDefault().post(hongbaoAckEvent);
        }

        hongbaoCloseIv = (ImageView) findViewById(R.id.hongbao_close_iv);
        hongbaoSenderTv = (TextView) findViewById(R.id.hongbao_sender_tv);
        hongbaoContenntTv = (TextView) findViewById(R.id.hongbao_contennt_tv);
        hongbaoMoneyTv = (TextView) findViewById(R.id.hongbao_money_tv);
        hongbaoStealOutTv = (TextView) findViewById(R.id.hongbao_steal_out_tv);
        hongbaoDetailLv = (ListView) findViewById(R.id.hongbao_detail_lv);
        hongbaoMyRecordTv = (TextView) findViewById(R.id.hongbao_my_record_tv);
        hongbaoCloseIv.setOnClickListener(this);
        hongbaoMyRecordTv.setOnClickListener(this);

        List<HongbaoStealRecordEntity> hbrecordList = new ArrayList<>();
        HongbaoStealRecordEntity hb = new HongbaoStealRecordEntity("","mountain",100);
        HongbaoStealRecordEntity hb1 = new HongbaoStealRecordEntity("","kermit",200);
        HongbaoStealRecordEntity hb2 = new HongbaoStealRecordEntity("","glen",300);
        HongbaoStealRecordEntity hb3 = new HongbaoStealRecordEntity("","alvin",400);
        hbrecordList.add(hb);
        hbrecordList.add(hb1);
        hbrecordList.add(hb2);
        hbrecordList.add(hb3);

        hongbaoRecordAdapter = new HongbaoRecordAdapter(this,hbrecordList);
        hongbaoDetailLv.setAdapter(hongbaoRecordAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hongbao_close_iv:
                finish();
                break;
            case R.id.hongbao_my_record_tv:
                Intent intent = new Intent(HongbaoDetailActivity.this,HongbaoMyRecordActivity.class);
                startActivity(intent);
                break;
        }
    }
}

