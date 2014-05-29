package com.example.gallery;

import com.example.fun.Qingchunyishi;
import com.example.fun.Qingchunzange;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FunActivity extends Activity
{
	private MyImageView btnyishi,btnzange;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.fun);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				ToMain();
				
			}
		});
		btnyishi=(MyImageView)findViewById(R.id.yishi);
		btnzange=(MyImageView)findViewById(R.id.zange);
		btnyishi.setOnClickListener(listener);
		btnzange.setOnClickListener(listener);
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.yishi:
				Toyishi();
				break;
			case R.id.zange:
				Tozange();
				break;
			default:
				break;
			}
			
		}
	};
	
	private void Tozange(){
		Intent intent=new Intent();
		intent.setClass(FunActivity.this, Qingchunzange.class);
		startActivity(intent);
		this.finish();
	}
	
	private void Toyishi(){
		Intent intent=new Intent();
		intent.setClass(FunActivity.this, Qingchunyishi.class);
		startActivity(intent);
		this.finish();
	}
	
	private void ToMain(){
		Intent intent=new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			ToMain();
		}
		return true;
	}
}
