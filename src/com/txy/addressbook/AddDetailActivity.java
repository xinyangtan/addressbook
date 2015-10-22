package com.txy.addressbook;

import java.util.ArrayList;
import java.util.List;

import com.example.sortlistview.CharacterParser;
import com.txy.tools.DBManager;
import com.txy.tools.SortModel;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddDetailActivity extends Activity implements OnClickListener{

	private List<PhoneItemHolder> phoneItem;
	private LayoutInflater iInflater;
	private Button titlebarRButton;
	private Button titlebarLButton;
	private Button delButton;
	private ImageView headImageView;
	private EditText nameTV;
	private LinearLayout phoneLinearLayout;
	private EditText emailTV;
	private EditText qqTV;
	private EditText wechatTV;
	private SortModel person;
	private Context context;
	
	public static final int DETAIL_MODE = 1;
	public static final int EDIT_MODE = 2;
	public static final int ADD_MODE = 3;

	private int mode = 1; // 模式 1.查看 2.编辑
	private CharacterParser characterParser;
	private DBManager dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_add_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar_add_detail);
		this.context = this;
		phoneItem = new ArrayList<AddDetailActivity.PhoneItemHolder>();
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		// 初始化DBManager
        dbManager = new DBManager(this);
		Intent intent = getIntent();
		if (intent != null) {
			person = (SortModel) intent.getSerializableExtra("person");
		}
		if (person == null) {
			person = new SortModel();
		}
		titlebarRButton = (Button)findViewById(R.id.titlebar_add_detail_rbutton);
		titlebarRButton.setOnClickListener(this);
		titlebarLButton = (Button)findViewById(R.id.titlebar_add_detail_lbutton);
		titlebarLButton.setOnClickListener(this);
		titlebarLButton.setText("<所有联系人");
		delButton = (Button) findViewById(R.id.add_detail_del_button);
		delButton.setOnClickListener(this);
		
		headImageView = (ImageView) findViewById(R.id.add_detail_headimage);
		nameTV = (EditText) findViewById(R.id.add_detail_name);
		emailTV = (EditText) findViewById(R.id.add_detail_email);
		qqTV = (EditText) findViewById(R.id.add_detail_qq);
		wechatTV = (EditText) findViewById(R.id.add_detail_wechat);
		phoneLinearLayout = (LinearLayout) findViewById(R.id.add_detail_phonenum_linearlayout);
		iInflater = LayoutInflater.from(this);
		
		initView();
		int mode = DETAIL_MODE;
		if (intent != null) {
			mode = intent.getIntExtra("mode", DETAIL_MODE);
		}
		setMode(mode);
	}

	private void initView() {
		this.nameTV.setText(this.person.getName());
		this.emailTV.setText(this.person.getEmail());
		this.qqTV.setText(this.person.getQq());
		this.wechatTV.setText(this.person.getWechat());
		if (this.person.getPhoneList() == null) {
			this.addPhoneItem("", this.mode);// 手机
		}
		else {
			for (String phone: this.person.getPhoneList()) {
				this.addPhoneItem(phone, this.mode);// 手机
			}
		}
		this.setMode(this.mode);
	}
	
	@SuppressLint("NewApi")
	private void setMode(int mode) {
//		if (this.mode == mode) {
//			return;
//		}
		this.mode = mode;
		if (this.mode == DETAIL_MODE) {
			this.nameTV.setEnabled(false); // 头像姓名
			this.nameTV.setBackground(null);
			this.nameTV.setTextColor(Color.BLACK);
			
			this.emailTV.setEnabled(false); // 
			this.emailTV.setBackground(null);
			this.emailTV.setTextColor(Color.BLACK);
			
			this.qqTV.setEnabled(false); // 
			this.qqTV.setBackground(null);
			this.qqTV.setTextColor(Color.BLACK);
			
			this.wechatTV.setEnabled(false); // 
			this.wechatTV.setBackground(null);
			this.wechatTV.setTextColor(Color.BLACK);
			
			this.titlebarRButton.setText("编辑");// titlebar 按钮
			this.delButton.setVisibility(View.GONE);//删除按钮
		} else if (this.mode == EDIT_MODE) {
			this.nameTV.setEnabled(true);
			this.nameTV.setBackgroundResource(R.drawable.edittext_bg);
			
			this.emailTV.setEnabled(true);
			this.emailTV.setBackgroundResource(R.drawable.edittext_bg);
			
			this.qqTV.setEnabled(true);
			this.qqTV.setBackgroundResource(R.drawable.edittext_bg);

			this.wechatTV.setEnabled(true);
			this.wechatTV.setBackgroundResource(R.drawable.edittext_bg);
			
			this.titlebarRButton.setText("完成");// titlebar 按钮
			this.delButton.setVisibility(View.VISIBLE);//删除按钮
		} else if (this.mode == ADD_MODE) {
			this.nameTV.setEnabled(true);
			this.nameTV.setBackgroundResource(R.drawable.edittext_bg);

			this.emailTV.setEnabled(true);
			this.emailTV.setBackgroundResource(R.drawable.edittext_bg);
			
			this.qqTV.setEnabled(true);
			this.qqTV.setBackgroundResource(R.drawable.edittext_bg);

			this.wechatTV.setEnabled(true);
			this.wechatTV.setBackgroundResource(R.drawable.edittext_bg);
			
			this.titlebarRButton.setText("保存");// titlebar 按钮
			this.delButton.setVisibility(View.GONE);//删除按钮
			
		}
		
		for (PhoneItemHolder p:this.phoneItem){
			setPhoneItemMode(p);
		}
	}
	
	private boolean delData() {
		if (this.person.get_id() == -1) {
			Toast.makeText(this, "未保存的数据", Toast.LENGTH_SHORT).show();
			return false;
		}
		dbManager.deletePerson(person);
		return true;
	}
	
	private void saveData() {
		if (this.fillData()) {
			dbManager.add(this.person);
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void updateData() {
		if (this.fillData()) {
			dbManager.updatePerson(this.person);
			Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean fillData() {
		String name = this.nameTV.getText().toString();
		// 汉字转换成拼音
		String pinyin = characterParser.getSelling(name);
		if (pinyin.equals("")) {
			return false;
		}
		String sortLetters = pinyin.substring(0, 1).toUpperCase();

		this.person.setName(name, sortLetters, pinyin);
		String phoneString = "";
		for (PhoneItemHolder p: this.phoneItem) {
			String phonenum = p.phoneTV.getText().toString();
			if (!phonenum.equals("")) {
				phoneString += p.phoneTV.getText().toString();
				phoneString += "|";
			}
		}
		this.person.setPhonenum(phoneString);
		this.person.setEmail(this.emailTV.getText().toString());
		this.person.setQq(this.qqTV.getText().toString());
		this.person.setWechat(this.wechatTV.getText().toString());
		return true;
	}
	
	private void call(String num) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+num));
		startActivity(intent);
	}

	private void sendMsg(String num, String content) {
		Uri uri = Uri.parse("smsto:" + num);          
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);          
//		intent.putExtra("sms_body", "102");      
		startActivity(intent);
	}
	
	@SuppressLint("NewApi")
	private void setPhoneItemMode(PhoneItemHolder p) {
		EditText phoneTV = p.phoneTV;
		ImageButton call = p.call;
		ImageButton msg = p.msg;
		ImageButton add = p.add;
		ImageButton del = p.del;
		
		if (this.mode == DETAIL_MODE) {
			phoneTV.setEnabled(false);
			phoneTV.setBackground(null);
			phoneTV.setTextColor(Color.BLACK);
			add.setVisibility(View.GONE);
			del.setVisibility(View.GONE);
			call.setVisibility(View.VISIBLE);
			msg.setVisibility(View.VISIBLE);
		} else if (this.mode == EDIT_MODE || this.mode == ADD_MODE) {
			phoneTV.setEnabled(true);
			phoneTV.setBackgroundResource(R.drawable.edittext_bg);
			add.setVisibility(View.VISIBLE);
			del.setVisibility(View.VISIBLE);
			call.setVisibility(View.GONE);
			msg.setVisibility(View.GONE);
		}
	}
	
	@SuppressLint("NewApi")
	private void addPhoneItem(String phone, int mode) {
		final View v = iInflater.inflate(R.layout.phonenum_item, null);
		// TODO Auto-generated method stub
		final EditText phoneTV = (EditText) v.findViewById(R.id.phonenum_item_phoneTV);
		final ImageButton call = (ImageButton) v.findViewById(R.id.phonenum_item_callButton);
		final ImageButton msg = (ImageButton) v.findViewById(R.id.phonenum_item_msgButton);
		final ImageButton add = (ImageButton) v.findViewById(R.id.phonenum_item_addButton);
		final ImageButton del = (ImageButton) v.findViewById(R.id.phonenum_item_delButton);
		
		final PhoneItemHolder p = new PhoneItemHolder(phoneTV, call, msg, add, del);
		setPhoneItemMode(p);
		this.phoneItem.add(p);
		
		phoneTV.setText(phone);
		phoneLinearLayout.addView(v);
		
		String phonenum = phoneTV.getText().toString();

		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phonenum = phoneTV.getText().toString();
				call(phonenum);
			}
		});
				
		msg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String phonenum = phoneTV.getText().toString();
				sendMsg(phonenum, null);
			}
		});
		
		del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (phoneItem.size() > 1) {
					phoneLinearLayout.removeView(v);
					phoneItem.remove(p);
				}
			}
		});
		
		add.setOnClickListener((OnClickListener) context);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.add_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == titlebarRButton) {
//			finish();
			if (this.mode == DETAIL_MODE) {
				this.setMode(EDIT_MODE);
			} 
			else if (this.mode == EDIT_MODE){
				updateData();
				this.setMode(DETAIL_MODE);
			}
			else if (this.mode == ADD_MODE){
				saveData();
				this.finish();
			}
		}
		else if (v == titlebarLButton) {
			if (this.mode == DETAIL_MODE) {
				this.finish();
			} 
			else if (this.mode == EDIT_MODE){
				this.finish();
			}
			else if (this.mode == ADD_MODE){
				this.finish();
			}
		}
		
		if (v == delButton) {
			if(delData()){
				finish();
			}
		}
		
		for(PhoneItemHolder h:this.phoneItem) {
			if(v == h.add){
				this.addPhoneItem(null, this.mode);
				break;
			}
		}
	}

	class PhoneItemHolder {
		public EditText phoneTV;
		public ImageButton call;
		public ImageButton msg;
		public ImageButton add;
		public ImageButton del;
		
		public PhoneItemHolder(EditText phoneTV,
				ImageButton call, ImageButton msg, ImageButton add,
				ImageButton del) {
			this.phoneTV = phoneTV;
			this.call = call;
			this.msg = msg;
			this.add = add;
			this.del = del;
		}
	}
}
