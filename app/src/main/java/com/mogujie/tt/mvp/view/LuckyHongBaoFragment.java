package com.mogujie.tt.mvp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.event.AudioEvent;
import com.mogujie.tt.imservice.event.HongbaoEvent;
import com.mogujie.tt.ui.fragment.MainFragment;

import de.greenrobot.event.EventBus;

/**
 * 拼手气红包
 * Created by home on 2016/5/13.
 */
public class LuckyHongBaoFragment extends Fragment {

    private EditText hongbaoSumEt;
    private EditText messageEt;
    private Button putMoneyBtn;
    private Spinner spinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle bundle) {
        super.onCreateView(inflater, vg, bundle);
        View view = inflater.inflate(R.layout.tt_fragment_lucky_hongbao, vg,false);
        hongbaoSumEt = (EditText) view.findViewById(R.id.hongbao_sum_et);
        messageEt = (EditText) view.findViewById(R.id.messageEt);
        spinner = (Spinner) view.findViewById(R.id.groupSpinner);
        putMoneyBtn = (Button) view.findViewById(R.id.putMoneyBtn);
        putMoneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(!TextUtils.isEmpty(hongbaoSumEt.getText().toString()) &&
                        (Integer.valueOf(hongbaoSumEt.getText().toString()) > 0)){
                    putMoneyBtn.setEnabled(true);
                }else{
                    putMoneyBtn.setEnabled(false);
                    return;
                }*/
                Log.d("MoGuLogger","hongbao#发红包");
                HongbaoEvent hongbaoEvent;
                if(TextUtils.isEmpty(messageEt.getText().toString())){
                    hongbaoEvent = new HongbaoEvent("恭喜发财");
                }else{
                    String content = messageEt.getText().toString();
                    Log.d("MoGuLogger","hongbao#发红包时的内容是"+content);
                    hongbaoEvent = new HongbaoEvent(content);
                }
                EventBus.getDefault().post(hongbaoEvent);
                getActivity().finish();
            }
        });
        initSpinnerData();
        return view;
    }

    public void initSpinnerData(){
        // 建立数据源
        final String[] mItems = new String[]{"无","部门A","部门B","部门C"};
// 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }

}
