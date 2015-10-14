package com.txy.addressbook;  
  
  
import com.example.sortlistview.MainActivity;

import android.app.Activity;  
import android.content.Intent;
import android.os.Bundle;  
import android.util.Log;
import android.view.MotionEvent;  
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.LinearLayout;  
  
/** 
 * @author yangyu 
 *  功能描述：弹出Activity界面 
 */  
public class DialogActivity extends Activity implements OnClickListener{  
    private LinearLayout layout01,layout02,layout03,layout04;  
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_dialog);  
  
        initView();  
    }  
  
    /** 
     * 初始化组件 
     */  
    private void initView(){  
        //得到布局组件对象并设置监听事件  
        layout01 = (LinearLayout)findViewById(R.id.llayout01); 
        layout02 = (LinearLayout)findViewById(R.id.llayout02); 
        layout01.setOnClickListener(this);
        layout02.setOnClickListener(this);
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }  
      
    @Override  
    public void onClick(View v) {  
          if (v == layout01) {
        	  Log.i("mylog", "touch add");
        	  finish();
        	  startActivity(new Intent(DialogActivity.this, AddDetailActivity.class));
          }
          if (v == layout02) {
        	  Log.i("mylog", "touch multi");
          }
    }  
}  