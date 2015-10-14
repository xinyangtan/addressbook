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
	 * ����ת����ƴ������
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * ����ƴ��������ListView�����������
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

		// ��ʼ��DBManager
        dbManager = new DBManager(this);
//        ArrayList<SortModel> persons = new ArrayList<SortModel>();
//        SortModel p1 = new SortModel();
//        p1.setName("̷����");
//        p1.setPhonenum("13533803185");
//        persons.add(p1);
//        SortModel p2 = new SortModel();
//        p2.setName("̷����2");
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
		// ʵ��������תƴ����
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		noUserTipsTV = (TextView) findViewById(R.id.no_user_tips_tv);
		// �����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// ����ĸ�״γ��ֵ�λ��
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
				// ����Ҫ����adapter.getItem(position)����ȡ��ǰposition����Ӧ�Ķ���
				Toast.makeText(getApplication(),
						((SortModel) adapter.getItem(position)).getName(),
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,
						AddDetailActivity.class);
				// ���԰�Ҫ���ݵĲ����ŵ�һ��bundle�ﴫ�ݹ�ȥ��bundle���Կ���һ�������map.
				Bundle bundle = new Bundle();
				bundle.putString("name", ((SortModel) adapter.getItem(position)).getName());
				bundle.putString("phonenum", ((SortModel) adapter.getItem(position)).getPhonenum());
				bundle.putSerializable("person", (SortModel)adapter.getItem(position));
				intent.putExtras(bundle);
				// Ҳ���������ַ�ʽ����.
				// intent.putExtra("result", "��һ��activity������");

				startActivity(intent);

			}
		});

//		SourceDateList = filledData(getResources().getStringArray(R.array.date));
		SourceDateList = filledData();

		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		updateUI();

		mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// �������������ֵ�ĸı�����������
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// ������������ֵΪ�գ�����Ϊԭ�����б�����Ϊ���������б�
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
	 * ΪListView�������
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData() {
		List<SortModel> mSortList = dbManager.query();

//		for (int i = 0; i < date.length; i++) {
//			SortModel sortModel = new SortModel();
//			sortModel.setName(date[i]);
//			// ����ת����ƴ��
//			String pinyin = characterParser.getSelling(date[i]);
//			String sortString = pinyin.substring(0, 1).toUpperCase();
//
//			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
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
	 * ����������е�ֵ���������ݲ�����ListView
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

		// ����a-z��������
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
