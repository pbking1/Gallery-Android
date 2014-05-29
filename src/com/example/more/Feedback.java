package com.example.more;

import com.example.gallery.ExitApplication;
import com.example.gallery.MainActivity;
import com.example.gallery.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Feedback extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.feedback);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				ToMain();			
			}
		});
	}
	
	private void ToMain(){
		Intent intent1=new Intent();
		intent1.setClass(this, MainActivity.class);
		startActivity(intent1);
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
