package com.example.sortlistview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.txy.addressbook.AddDetailActivity;
import com.txy.addressbook.DialogActivity;
import com.txy.addressbook.R;
import com.txy.addressbook.SlideCutListView.RemoveDirection;
import com.txy.addressbook.SlideCutListView.RemoveListener;
import com.txy.tools.DBManager;
import com.txy.tools.SortModel;

public class MainActivity extends Activity implements OnClickListener,
		RemoveListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private TextView noUserTipsTV;

	private DBManager dbManager;
	
	private ImageButton titlebarRButton;

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.sortlistview_activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar);

		titlebarRButton = (ImageButton) findViewById(R.id.titlebarRbutton);
		titlebarRButton.setOnClickListener(this);

		// 初始化DBManager
        dbManager = new DBManager(this);
//        ArrayList<SortModel> persons = new ArrayList<SortModel>();
//        SortModel p1 = new SortModel();
//        p1.setName("谭新阳");
//        p1.setPhonenum("13533803185");
//        persons.add(p1);
//        SortModel p2 = new SortModel();
//        p2.setName("谭新阳2");
//        p2.setPhonenum("13533803185");
//        persons.add(p2);
//        dbManager.add(persons);
		initViews();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		startActivity(new Intent(MainActivity.this, DialogActivity.class));
		return true;
	}
	
	private void updateUI() {
		if(adapter.getCount() == 0) {
			sortListView.setVisibility(View.GONE);
			noUserTipsTV.setVisibility(View.VISIBLE);
		} else {
			sortListView.setVisibility(View.VISIBLE);
			noUserTipsTV.setVisibility(View.GONE);
		}
	}
	
	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		noUserTipsTV = (TextView) findViewById(R.id.no_user_tips_tv);
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,
						AddDetailActivity.class);
				// 可以把要传递的参数放到一个bundle里传递过去，bundle可以看做一个特殊的map.
				Bundle bundle = new Bundle();
				bundle.putString("name", ((SortModel) adapter.getItem(position)).getName());
				bundle.putString("phonenum", ((SortModel) adapter.getItem(position)).getPhonenum());
				bundle.putSerializable("person", (SortModel)adapter.getItem(position));
				intent.putExtras(bundle);
				// 也可以用这种方式传递.
				// intent.putExtra("result", "第一个activity的内容");

				startActivity(intent);

			}
		});

//		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		SourceDateList = filledData();

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		updateUI();

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData() {
		List<SortModel> mSortList = dbManager.query();

//		for (int i = 0; i < date.length; i++) {
//			SortModel sortModel = new SortModel();
//			sortModel.setName(date[i]);
//			// 汉字转换成拼音
//			String pinyin = characterParser.getSelling(date[i]);
//			String sortString = pinyin.substring(0, 1).toUpperCase();
//
//			// 正则表达式，判断首字母是否是英文字母
//			if (sortString.matches("[A-Z]")) {
//				sortModel.setSortLetters(sortString.toUpperCase());
//			} else {
//				sortModel.setSortLetters("#");
//			}
//
//			mSortList.add(sortModel);
//		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		updateUI();
	}

	@Override
	public void removeItem(RemoveDirection direction, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == titlebarRButton) {
			Log.i("ab", "touch add button");
			startActivity(new Intent(MainActivity.this, DialogActivity.class));
		}

	}

}
