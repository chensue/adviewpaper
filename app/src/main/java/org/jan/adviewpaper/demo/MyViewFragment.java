package org.jan.adviewpaper.demo;
import java.util.ArrayList;

import org.jan.adverpaper.widget.JazzyViewPager;
import org.jan.adverpaper.widget.JazzyViewPager.TransitionEffect;
import org.jan.adverpaper.widget.OutlineContainer;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.achep.header2actionbar.HeaderFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
public class MyViewFragment extends HeaderFragment {

	private FrameLayout mContentOverlay;
	private static final String TAG = "MyViewFragment";
	private static int PAGER_START_PLAY = 0x123;
	// 切换间隔时间3秒
	private static final int PLAY_TIME = 3 * 1000;
	// 实现viewpager的控件
	private JazzyViewPager mViewPaper;
	// 圆形标签的父层
	private LinearLayout symbolContainer;
	private ImageView[] images;
	private ImageView[] circleSymbols;
	private ArrayList<String> imageUrlList;
	// 图片框架universalimageloader的图形帮助类
	private ImageLoader mImageLoader;
	private Handler mHandler;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		setHeaderBackgroundScrollMode(HEADER_BACKGROUND_SCROLL_PARALLAX);
		//注意！这个是很屌的方法，用来改变actionbar的透明度
		setOnHeaderScrollChangedListener(new OnHeaderScrollChangedListener() {
			@Override
			public void onHeaderScrollChanged(float progress, int height, int scroll) {
				height -= getActivity().getActionBar().getHeight();
				progress = (float) scroll / height;
				if (progress > 1f) progress = 1f;
				progress = (1 - (float) Math.cos(progress * Math.PI)) * 0.5f;
				((MainActivity) getActivity())
						.getFadingActionBarHelper()
						.setActionBarAlpha((int) (255 * progress));
			}
		});
		mImageLoader = ImageLoader.getInstance();
		initMockImages();

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public View onCreateHeaderView(LayoutInflater inflater, ViewGroup container) {
		return inflater.inflate(R.layout.fragment_header, container, false);
	}

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
		View view  =  inflater.inflate(R.layout.fragment_myview, container, false);
		setupMyHandler();
		initViews(view);
		return view;
	}

	@Override
	public View onCreateContentOverlayView(LayoutInflater inflater, ViewGroup container) {
		//        ProgressBar progressBar = new ProgressBar(getActivity());
		mContentOverlay = new FrameLayout(getActivity());
		//        mContentOverlay.addView(progressBar, new FrameLayout.LayoutParams(
		//                ViewGroup.LayoutParams.WRAP_CONTENT,
		//                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		//        if (mLoaded) mContentOverlay.setVisibility(View.GONE);
		return mContentOverlay;
	}

	private void setupMyHandler() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (PAGER_START_PLAY == msg.what) {
					Log.d(TAG, "----PAGER_START_PLAY-----");
					int current = mViewPaper.getCurrentItem();
					if (current == images.length - 1) {
						current = -1;
					}
					Log.d(TAG, "play item = " + current);
					mViewPaper.setCurrentItem(current + 1);
					mHandler.sendEmptyMessageDelayed(PAGER_START_PLAY,
							PLAY_TIME);
				}
			}
		};
	}

	private void initViews(View v) {
		symbolContainer = (LinearLayout) v.findViewById(R.id.symblo_container);
		circleSymbols = new ImageView[imageUrlList.size()];
		images = new ImageView[imageUrlList.size()];
		for (int i = 0; i < imageUrlList.size(); i++) {
			ImageView imageView = new ImageView(getActivity());
			ImageView circle = new ImageView(getActivity());
			imageView.setScaleType(ScaleType.CENTER_CROP);
			images[i] = imageView;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(3, 0, 3, 0);
			circle.setLayoutParams(lp);
			circle.setTag(i);
			circle.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.circle_normal));
			circleSymbols[i] = circle;
			symbolContainer.addView(circleSymbols[i]);
		}
		setViewPager(v,TransitionEffect.Standard);
	}

	private void setViewPager(View v,TransitionEffect effect) {
		mViewPaper = (JazzyViewPager) v.findViewById(R.id.adviewpaper);
		mViewPaper.setTransitionEffect(effect);
		mViewPaper.setAdapter(new MyPagerAdapter());
		mViewPaper.setOnPageChangeListener(new MyPageViewChangeListener());
		mViewPaper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (imageUrlList.size() == 0 || imageUrlList.size() == 1) {
					return true;
				} else {
					return false;
				}
			}
		});
		circleSymbols[0].setBackgroundDrawable(getResources().getDrawable(
				R.drawable.circle_selected));
		mViewPaper.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(PAGER_START_PLAY, PLAY_TIME);
	}

	/**
	 * 创建本地图片数据
	 */
	private void initMockImages() {
		imageUrlList = new ArrayList<String>();
		imageUrlList.add("drawable://" + R.mipmap.jd_ad_0);
		imageUrlList.add("drawable://" + R.mipmap.jd_ad_1);
		imageUrlList.add("drawable://" + R.mipmap.jd_ad_2);
		imageUrlList.add("drawable://" + R.mipmap.jd_ad_3);
		imageUrlList.add("drawable://" + R.mipmap.jd_ad_4);
	}

	/**
	 * 设置圆形标签的状态
	 *
	 * @param index
	 *            当前标签的位置
	 */
	private void setSymbolImages(int index) {
		for (ImageView image : circleSymbols) {
			Integer i = (Integer) image.getTag();
			if (i == index) {
				image.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_selected));
			} else {
				image.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.circle_normal));
			}
		}
	}
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageUrlList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(mViewPaper
					.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			mImageLoader.displayImage(imageUrlList.get(position),
					images[position]);
			container.addView(images[position], LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			final int index = position;
			images[position].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.e(TAG, "you clicked images position is" + index);
					Toast.makeText(getActivity(),
							"你点击了第" + (index + 1) + "张图", Toast.LENGTH_SHORT)
							.show();
				}
			});
			// 注意！不加这个方法要报IllegalStateException
			mViewPaper.setObjectForPosition(images[position], position);
			return images[position];
		}

	}

	private class MyPageViewChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int stateCode) {
			switch (stateCode) {
				case 0:
					// 你什么都没动
					break;
				case 1:
					// 正在滑动哦
					break;
				case 2:
					// 滑动完了
					break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			Log.d(TAG, "onPageSelected-->position:" + position);
			setSymbolImages(position);
		}
	}


}
