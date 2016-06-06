package com.mogujie.tt.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mogujie.tt.R;
import com.mogujie.tt.app.IMApplication;
import com.mogujie.tt.imservice.manager.IMStackManager;
import com.mogujie.tt.mvp.view.CodeHongBaoFragment;
import com.mogujie.tt.mvp.view.LuckyHongBaoFragment;
import com.mogujie.tt.ui.adapter.HongbaoFragmentAdapter;
import com.mogujie.tt.ui.base.TTBaseActivity;
import com.mogujie.tt.ui.base.TTBaseFragmentActivity;

import java.util.ArrayList;
import java.util.List;

public class HongbaoActivity extends TTBaseFragmentActivity implements View.OnClickListener{

	private ViewPager mPageVp;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private HongbaoFragmentAdapter mFragmentAdapter;

	/**
	 * Tab显示内容TextView
	 */
	private TextView mTabChatTv, mTabContactsTv, mTabFriendTv;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	/**
	 * Fragment
	 */
	private LuckyHongBaoFragment luckyHongBaoFragment;
	private CodeHongBaoFragment codeHongBaoFragment;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;
	//滑动tag标签个数
	private int tabNum = 2;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.tt_activity_hongbao);
		findById();
		init();
		initTabLineWidth();

	}

	private void findById() {
		LayoutInflater.from(this).inflate(R.layout.tt_activity_hongbao, topContentView);
		//TOP_CONTENT_VIEW
		setLeftButton(R.drawable.tt_top_back);
		setLeftText(getResources().getString(R.string.top_left_back));
		setRightButton(R.drawable.tt_top_right_group_manager);
		topLeftBtn.setOnClickListener(this);
		letTitleTxt.setOnClickListener(this);
		topRightBtn.setOnClickListener(this);




		mTabContactsTv = (TextView) this.findViewById(R.id.id_contacts_tv);
		mTabChatTv = (TextView) this.findViewById(R.id.id_chat_tv);
		mTabFriendTv = (TextView) this.findViewById(R.id.id_friend_tv);

		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);

		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
	}

	private void init() {
		luckyHongBaoFragment = new LuckyHongBaoFragment();
		codeHongBaoFragment = new CodeHongBaoFragment();
		mFragmentList.add(luckyHongBaoFragment);
		mFragmentList.add(codeHongBaoFragment);

		mFragmentAdapter = new HongbaoFragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);

		mPageVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
									   int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();

				Log.e("offset:", offset + "");
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景：
				 * 记tabNum个页面,
				 * 从左到右分别为0,1,2
				 * 0->1; 1->2; 2->1; 1->0
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabNum) + currentIndex
							* (screenWidth / tabNum));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / tabNum) + currentIndex
							* (screenWidth / tabNum));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / tabNum) + currentIndex
							* (screenWidth / tabNum));
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / tabNum) + currentIndex
							* (screenWidth / tabNum));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
					case 0:
						mTabChatTv.setTextColor(Color.BLUE);
						break;
					case 1:
						mTabFriendTv.setTextColor(Color.BLUE);
						break;
					case 2:
						mTabContactsTv.setTextColor(Color.BLUE);
						break;
				}
				currentIndex = position;
			}
		});

	}

	/**
	 * 设置滑动条的宽度为屏幕的1/tabNum(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / tabNum;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		mTabChatTv.setTextColor(Color.BLACK);
		mTabFriendTv.setTextColor(Color.BLACK);
		mTabContactsTv.setTextColor(Color.BLACK);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.left_btn:
			case R.id.left_txt:
				HongbaoActivity.this.finish();
				break;
			case R.id.right_btn:
				break;
		}

	}

}
