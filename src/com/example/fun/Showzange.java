package com.example.fun;


import java.io.InputStream;

import com.example.gallery.ExitApplication;
import com.example.gallery.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Showzange extends Activity
{
	private int position;
	private String whattext;
	
	private Handler mHandler=new Handler()
    { 
		@SuppressWarnings("deprecation")
		@Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch(msg.what) 
            { 
            case 0:           	
            	whattext=readfile("wuyuanqingchun.txt");
                break; 
            case 1:
            	whattext=readfile("qingchun.txt");
            	break;
            case 2:
            	whattext=readfile("xiangxinweilai.txt");
            	break;
            case 3:
            	whattext=readfile("qingchundeshi.txt");
            	break;
            case 4:
            	whattext=readfile("qingchunzange.txt");
            	break;
            case 5:
            	whattext=readfile("zhiqingchun.txt");
            	break;
            case 6:
            	whattext=readfile("qingchunyanse.txt");
            	break;                
            } 
            TextView tv=(TextView)findViewById(R.id.textview);
			tv.setText(whattext);
        } 
    };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.showtext);
		Button btnback=(Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Tozange();
				
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
				readtext(0);
				title.setText("无怨的青春");
				break;
			case 1:
				readtext(1);				
				title.setText("青春");
				break;
			case 2:
				readtext(2);				
				title.setText("相信未来");
				break;
			case 3:
				readtext(3);				
				title.setText("青春的诗");
				break;
			case 4:
				readtext(4);				
				title.setText("青春是一首赞歌");
				break;
			case 5:
				readtext(5);				
				title.setText("致青春");
				break;
			case 6:
				readtext(6);			
				title.setText("青春的颜色");
				break;
			default:
				break;
			}
		} catch (Exception e)
		{
		}
	}
	
	private void Tozange(){
		Intent intent=new Intent();
		intent.setClass(this, Qingchunzange.class);
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
	
	private void readtext(final int position) 
    { 
         new Thread() 
         { 
                public void run() 
                {      
                	switch (position)
					{
					case 0:
						mHandler.sendEmptyMessage(0); 
						break;
					case 1:
						mHandler.sendEmptyMessage(1); 
						break;
					case 2:
						mHandler.sendEmptyMessage(2);
						break;
					case 3:
						mHandler.sendEmptyMessage(3); 
						break;
					case 4:
						mHandler.sendEmptyMessage(4); 
						break;
					case 5:
						mHandler.sendEmptyMessage(5); 
						break;
					case 6:
						mHandler.sendEmptyMessage(6); 
						break;
					default:
						break;
					}                           
                } 
         }.start(); 
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Tozange();
		}
		return true;
	}
	
}
