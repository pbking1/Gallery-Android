package com.example.fun;

import com.example.gallery.ExitApplication;
import com.example.gallery.FunActivity;
import com.example.gallery.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Qingchunyishi extends Activity
{
	int[] msgids={R.string.tshilianduanxin,R.string.tchikuangshaonian,
			R.string.tforeveryoung,R.string.tzaolian,R.string.tfatfather,
			R.string.talie,R.string.tjustonedollar};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.qingchunyishi);
		Button btnbackButton=(Button)findViewById(R.id.btnback);
		btnbackButton.setOnClickListener(new OnClickListener()
		{		
			@Override
			public void onClick(View v)
			{
				ToFun();			
			}
		});
		ListView lv=(ListView)findViewById(R.id.ListView01);	
		BaseAdapter ba=new BaseAdapter()
		{		
			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				RelativeLayout rl=new RelativeLayout(Qingchunyishi.this);
				rl.setPadding(5, 5, 5, 5);
				TextView tv=new TextView(Qingchunyishi.this);
				tv.setText(getResources().getText(msgids[position]));
				tv.setTextSize(24);			  
				tv.setTextColor(Color.parseColor("#FFFFFF"));
				tv.setPadding(5, 5, 5, 5);
				tv.setGravity(Gravity.LEFT);
				rl.addView(tv);
				return rl;
			}
			
			@Override
			public long getItemId(int position)
			{			
				return 0;
			}
			
			@Override
			public Object getItem(int arg0)
			{			
				return null;
			}
			
			@Override
			public int getCount()
			{
				return 7;
			}
		};
		lv.setAdapter(ba);
		lv.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				switch (arg2)
				{
				case 0:
					Totext(0);
					break;
				case 1:
					Totext(1);
					break;
				case 2:
					Totext(2);
					break;
				case 3:
					Totext(3);
					break;
				case 4:
					Totext(4);
					break;
				case 5:
					Totext(5);
					break;
				case 6:
					Totext(6);
					break;																				
				default:
					break;
				}
				
			}
		});
	}
	
	private void Totext(int position){
		Intent intent=new Intent();
		intent.setClass(Qingchunyishi.this, Showtext.class);
		Bundle bundle=new Bundle();
		bundle.putInt("POSITION", position);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private void ToFun(){
		Intent intent1=new Intent();
		intent1.setClass(this, FunActivity.class);
		startActivity(intent1);
		this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			ToFun();
		}
		return true;
	}
	
	
}
