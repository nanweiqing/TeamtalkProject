package com.mogujie.tt.ui.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.entity.HongbaoStealRecordEntity;
import com.mogujie.tt.ui.adapter.HongbaoRecordAdapter;

import java.util.ArrayList;
import java.util.List;

public class HongbaoMyRecordActivity extends Activity implements View.OnClickListener{

    private ImageView hongbaoCloseIv;
    private TextView hongbaoMyTv;
    private TextView hongbaoMoneyTv;
    private ListView hongbaoDetailLv;
    private HongbaoRecordAdapter hongbaoRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_activity_hongbao_my_record);

        hongbaoCloseIv = (ImageView) findViewById(R.id.hongbao_close_iv);
        hongbaoMyTv = (TextView) findViewById(R.id.hongbao_my_tv);
        hongbaoMoneyTv = (TextView) findViewById(R.id.hongbao_money_tv);
        hongbaoDetailLv = (ListView) findViewById(R.id.hongbao_detail_lv);
        hongbaoCloseIv.setOnClickListener(this);

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
        switch (view.getId()){
            case R.id.hongbao_close_iv:
                finish();
                break;
        }
    }
}
