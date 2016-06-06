
package com.mogujie.tt.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.R;
import com.mogujie.tt.imservice.entity.HongbaoStealRecordEntity;
import com.mogujie.tt.utils.CommonUtil;
import com.mogujie.tt.utils.Logger;

import java.util.List;

/**
 * @Description 红包领取记录
 * @author gy
 * @date 2016-5-24
 */
public class HongbaoRecordAdapter extends BaseAdapter {
    private Context context = null;
    private List<HongbaoStealRecordEntity> hbStealRecordList = null;
    private static Logger logger = Logger.getLogger(HongbaoRecordAdapter.class);

    public HongbaoRecordAdapter(Context cxt,List<HongbaoStealRecordEntity> ids) {
        this.context = cxt;
        this.hbStealRecordList = ids;
    }
    /*public HongbaoRecordAdapter(Context cxt) {
        this.context = cxt;
    }*/

    public void setData(List<HongbaoStealRecordEntity> ids){
        this.hbStealRecordList = ids;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hbStealRecordList.size();
    }

    @Override
    public Object getItem(int position) {
        return hbStealRecordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.tt_hongbao_steal_record_list_item, null);
            viewHolder.portraitIv = (ImageView)convertView.findViewById(R.id.hongbao_stealer_iv);
            viewHolder.hongbao_stealer_tv = (TextView) convertView.findViewById(R.id.hongbao_stealer_tv);
            viewHolder.hongbao_num_tv = (TextView) convertView.findViewById(R.id.hongbao_num_tv);
            viewHolder.hongbao_most_man = (TextView) convertView.findViewById(R.id.hongbao_most_man);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.hongbao_stealer_tv.setText(hbStealRecordList.get(position).getHongbaoStealer());
        viewHolder.hongbao_num_tv.setText(hbStealRecordList.get(position).getHongbaoNum()+"元");
        viewHolder.hongbao_most_man.setVisibility(View.VISIBLE);
        return convertView;
    }

    public class ViewHolder{
        private ImageView portraitIv;
        private TextView hongbao_stealer_tv;
        private TextView hongbao_num_tv;
        private TextView hongbao_most_man;

    }
}
