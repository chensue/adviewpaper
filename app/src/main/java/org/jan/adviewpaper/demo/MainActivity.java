package org.jan.adviewpaper.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.achep.header2actionbar.FadingActionBarHelper;

/**
 * demo 主界面
 * @author jan
 */
public class MainActivity extends Activity implements OnClickListener {

	private static final String TAG = "MainActivity";
	private ActionBar mActionBar;
	//这边的是ActionBar的辅助类，设置透明度在里面的干活！
	private FadingActionBarHelper mFadingActionBarHelper;
	private Button mSearchButton;
	//....

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActionBar = getActionBar();
		// 使用自定义的布局的ActionBar
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setCustomView(R.layout.my_actionbar);
		mSearchButton = (Button) mActionBar.getCustomView().findViewById(
				R.id.search_button);
		// 给搜索按钮添加点击事件
		mSearchButton.setOnClickListener(this);
		// 注意！定义Actionbar的背景色 ，这句不能忘记！
		mActionBar.getCustomView().setBackground(
				getResources().getDrawable(R.drawable.actionbar_bg));
		mFadingActionBarHelper = new FadingActionBarHelper(getActionBar(),
				getResources().getDrawable(R.drawable.actionbar_bg));
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container_view, new MyViewFragment()).commit();
		}
	}

	public FadingActionBarHelper getFadingActionBarHelper() {
		return mFadingActionBarHelper;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.search_button:
				Log.d(TAG, "你点击了搜索按钮！");
				Toast.makeText(this, "you clicked search", Toast.LENGTH_LONG).show();
				break;
		}
	}
}
