package com.example.gallery;

import com.example.more.HelpActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

public class LogoActivity extends Activity
{
	private String SHAREDPREFERENCES_NAME="myname";
	private boolean isFirstIn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //����ȫ����ʾ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��������ģʽ
		setContentView(R.layout.logo);
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, MODE_PRIVATE);
				 isFirstIn = preferences.getBoolean("isFirstIn", true);  //��û��ֵ��Ĭ��Ϊtrue
				 if (!isFirstIn) {
		             Intent intent = new Intent(LogoActivity.this, MainActivity.class);
		             startActivity(intent);
		             LogoActivity.this.finish();
				 } else {
		             Intent intent = new Intent(LogoActivity.this, HelpActivity.class);
		             startActivity(intent);
		             LogoActivity.this.finish();
				 }			
			}
		}, 2500);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//����BACK��
		if(keyCode==KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}
	
}
