package com.mogujie.tt.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogujie.tt.R;

/**
 * 点开红包界面
 * Created by gy on 2016/5/21.
 */
public class HongbaoOpenActivity extends Activity implements View.OnClickListener{

    private ImageView hongbaoCloseTv,openHongbaoIv;
    private TextView hongbaoReceivedDetailTv;
    //红包是否我自己发的
    private String fromName;
    private String toName;
    //红包是否还有余额可以抢
    private boolean hongbaoStealedOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tt_activity_hongbao_open);

        fromName = getIntent().getStringExtra("fromName");
        toName = getIntent().getStringExtra("toName");
        hongbaoStealedOut = getIntent().getBooleanExtra("hongbaoStealedOut", false);

        hongbaoCloseTv = (ImageView) findViewById(R.id.hongbao_close_iv);
        openHongbaoIv = (ImageView) findViewById(R.id.hongbao_open_iv);
        hongbaoReceivedDetailTv = (TextView) findViewById(R.id.hongbao_receive_detail_tv);

        hongbaoCloseTv.setOnClickListener(this);
        openHongbaoIv.setOnClickListener(this);
        hongbaoReceivedDetailTv.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.hongbao_close_iv:
                finish();
                break;
            case R.id.hongbao_open_iv:
                Intent intent = new Intent(this,HongbaoDetailActivity.class);
                intent.putExtra("fromName", fromName);
                intent.putExtra("toName", toName);
                startActivity(intent);
                finish();
                break;
            case R.id.hongbao_receive_detail_tv:
                break;
        }
    }
}
