package com.mogujie.tt.mvp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mogujie.tt.R;
import com.mogujie.tt.ui.fragment.MainFragment;

/**
 * 拼手气红包
 * Created by home on 2016/5/13.
 */
//public class CodeHongBaoFragment extends MainFragment {
    public class CodeHongBaoFragment extends Fragment {

    private EditText hongbaoNumEt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle bundle) {
        super.onCreateView(inflater, vg, bundle);
        View view = inflater.inflate(R.layout.tt_fragment_code_hongbao, vg,false);
        return view;
    }
/*
    @Override
    protected void initHandler() {

    }

    private void initData() {
        setTopTitle(getResources().getString(R.string.give_hongbao));
    }*/
}
