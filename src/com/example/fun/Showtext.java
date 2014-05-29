package com.example.fun;

import java.io.InputStream;

import com.example.gallery.ExitApplication;
import com.example.gallery.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Showtext extends Activity
{
	private int position;
	private String whattext;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //����ȫ����ʾ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��������ģʽ
		setContentView(R.layout.showtext);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toyishi();
				
			}
		});
		TextView title=(TextView)findViewById(R.id.titleview);
		Bundle bundle=getIntent().getExtras();
		position=bundle.getInt("POSITION");
		try
		{
			switch (position)
			{
			case 0:
				whattext=readfile("shilianduanxin.txt");
				title.setText("ʧ������");
				break;
			case 1:
				whattext=readfile("chikuangshaonian.txt");
				title.setText("���տ�������");
				break;
			case 2:
				whattext=readfile("foreveryoung.txt");
				title.setText("����Զ�������ഺ");
				break;
			case 3:
				whattext=readfile("zaolian.txt");
				title.setText("���ŵ�����");
				break;
			case 4:
				whattext=readfile("fatfather.txt");
				title.setText("���صĸ���");
				break;
			case 5:
				whattext=readfile("alie.txt");
				title.setText("Բ��һ����");
				break;
			case 6:
				whattext=readfile("needone.txt");
				title.setText("����Ҫһ��Ԫ");
				break;
			default:
				break;
			}
			
			TextView tv=(TextView)findViewById(R.id.textview);
			tv.setText(whattext);
		} catch (Exception e)
		{
			
		}
	}
	
	private void Toyishi(){
		Intent intent=new Intent();
		intent.setClass(this, Qingchunyishi.class);
		startActivity(intent);
		this.finish();
	}
	
	private String readfile(String filename){
		String text="";
		try
		{
			InputStream is=getAssets().open(filename);
			int size=is.available();		
			byte[] buffer=new byte[size];
			is.read(buffer);
			is.close();		
			text=new String(buffer,"GB2312");
		} catch (Exception e)
		{
			return null;
		}
		return text;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Toyishi();
		}
		return true;
	}
	
}
