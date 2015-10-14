package com.txy.addressbook;

import com.txy.tools.SortModel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AddDetailActivity extends Activity implements OnClickListener{

	private Button titlebarRButton;
	private TextView nameTV;
	private TextView phonenumTV;
	private SortModel person;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_add_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_bar_add_detail);
		
		titlebarRButton = (Button)findViewById(R.id.titlebar_add_detail_rbutton);
		titlebarRButton.setOnClickListener(this);
		
		nameTV = (TextView)findViewById(R.id.add_detail_nametv);
		phonenumTV = (TextView)findViewById(R.id.add_detail_phonenumtv);
		
		Intent intent = getIntent();
		if (intent != null) {
			String name = intent.getStringExtra("name");
			String phonenum = intent.getStringExtra("phonenum");
			nameTV.setText(name);
			phonenumTV.setText(phonenum);
			person = (SortModel) intent.getSerializableExtra("person");
		}
		else {
			person = new SortModel();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == titlebarRButton) {
			finish();
		}
	}

}
