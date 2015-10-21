package com.txy.addressbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.txy.addressbook.SlideCutListView.RemoveDirection;
import com.txy.addressbook.SlideCutListView.RemoveListener;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		RemoveListener {
	private SlideCutListView slideCutListView;
	private ArrayAdapter<String> adapter;

	private MyAdapter myadapter;

	private List<String> dataSourceList = new ArrayList<String>();

	private ImageButton titlebarRButton;

	// 按两次返回键退出
	private static Boolean isQuit = false;
	Timer timer = new Timer();

	private SharedPreferences sp;

	public static Context c;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		c = this;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.usermsg);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);

		titlebarRButton = (ImageButton) findViewById(R.id.titlebarRbutton);
		titlebarRButton.setOnClickListener(this);

//		titlebarRButton.setText("send");
		init();

		Intent intent = getIntent();
		if (intent != null) {
			// intent.get
			Boolean sendResult = intent.getBooleanExtra("sendResult", false);
			Boolean from = intent.getBooleanExtra("from", false);
			if (from & sendResult) {
				Toast.makeText(MainActivity.this, "发送成功",
						Toast.LENGTH_SHORT).show();
			}
			if (from & !sendResult) {
				Toast.makeText(MainActivity.this, "发送失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void init() {
		slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
		slideCutListView.setRemoveListener(this);

		for (int i = 0; i < 20; i++) {
			dataSourceList.add("滑动删除" + i);
		}

		myadapter = new MyAdapter(this);

		adapter = new ArrayAdapter<String>(this, R.layout.list, R.id.list_nametv,
				dataSourceList);
		slideCutListView.setAdapter(myadapter);

		slideCutListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MyAdapter.ViewHolder vHollder = (MyAdapter.ViewHolder) view
						.getTag();

				String from = vHollder.fromname.getText().toString();
				String time = vHollder.time.getText().toString();
				String content = vHollder.content.getText().toString();

//				Intent intent = new Intent(MainActivity.this,
//						MsgDetailActivity.class);
//				// 可以把要传递的参数放到一个bundle里传递过去，bundle可以看做一个特殊的map.
//				Bundle bundle = new Bundle();
//				bundle.putString("from", from);
//				bundle.putString("time", time);
//				bundle.putString("content", content);
//				intent.putExtras(bundle);
//				// 也可以用这种方式传递.
//				// intent.putExtra("result", "第一个activity的内容");
//
//				startActivity(intent);

				Log.i("fuck", "点击了第" + position + "行");
				Toast.makeText(MainActivity.this,
						"点击了第" + position + "行", Toast.LENGTH_SHORT).show();
			}
		});

		slideCutListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// TODO Auto-generated method stub
						setShowCheckBox(true);
						Toast.makeText(MainActivity.this, "长按Item",
								Toast.LENGTH_SHORT).show();
						Log.i("TAG", "onlongClickItem");
						return false;
					}

				});
	}

	public void setShowCheckBox(boolean show) {
		// 设置显示 View.VISIBLE
		// 设置不显示也不占用空间 View.GONE
		// 设置不显示但占用空间 View.INVISIBLE
		// if (showCheckBox) {
		// cb.setVisibility(View.VISIBLE);
		// // 设置titlebar中的checkbox显示的时候，Item中的也要显示
		// this.myadapter.setShowCheckBox(showCheckBox);
		// } else {
		// cb.setVisibility(View.INVISIBLE);
		// }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		super.onOptionsItemSelected(item);
		return true;

	}

	// 滑动删除之后的回调方法
	@Override
	public void removeItem(RemoveDirection direction, int position) {
		myadapter.remove(position);
		// adapter.remove(adapter.getItem(position));
		switch (direction) {
		case RIGHT:
			Toast.makeText(this, "向右删除  " + position, Toast.LENGTH_SHORT)
					.show();
			break;
		case LEFT:
			Toast.makeText(this, "向左删除  " + position, Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == titlebarRButton) {
			Log.i("ab", "touch add button");
			startActivity(new Intent(MainActivity.this,DialogActivity.class));  
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

//	private void setMsgNotification(Intent i) {
//		NotificationManager notificationManager = (NotificationManager) this
//				.getSystemService(Context.NOTIFICATION_SERVICE);
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				"通知", System.currentTimeMillis());
//
//		// Notification中包含一个RemoteView控件，实际就是通知栏默认显示的View。通过设置RemoteVIew可以自定义布局
//		RemoteViews contentView = new RemoteViews(this.getPackageName(),
//				R.layout.notify_view);
//
//		contentView.setTextViewText(R.id.notify_from, "gagec");
//		contentView.setTextViewText(R.id.notify_content, "156468465154gdfgsfdga");
//		contentView.setTextViewText(R.id.notify_time, "2012-12-4");
//		notification.contentView = contentView;
//
//		notification.defaults = Notification.DEFAULT_SOUND;
//		//震动
//		long[] vibrate = {0,100,200,300};
//		notification.vibrate = vibrate;
//		//亮灯
//		notification.defaults |= Notification.DEFAULT_LIGHTS;
//		
//		Intent pIntent = new Intent(this, MsgDetailActivity.class);
//		// 可以把要传递的参数放到一个bundle里传递过去，bundle可以看做一个特殊的map.
//		Bundle bundle = new Bundle();
//		bundle.putString("from", "ec");
//		bundle.putString("time", "2012-12-4");
//		bundle.putString("content", "aghaegdfgsfdga");
//		pIntent.putExtras(bundle);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//				pIntent, 0);
//		notification.contentIntent = pendingIntent;
//		notificationManager.notify(123654, notification);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isQuit == false) {
				isQuit = true;
				Toast.makeText(getBaseContext(), "再按一次退出", Toast.LENGTH_SHORT)
						.show();
				TimerTask task = null;
				task = new TimerTask() {
					@Override
					public void run() {
						isQuit = false;
					}
				};
				timer.schedule(task, 2000);
			} else {
				// finish();
				// System.exit(0);
			}
		}
		return true;
	}
}
