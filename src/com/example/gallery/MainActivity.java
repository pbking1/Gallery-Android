package com.example.gallery;


import com.example.exist.HaveExist;
import com.example.more.Aboutus;
import com.example.more.Feedback;
import com.example.more.HelpActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private MyImageView btnnew,btnexist,btnfun,btnmore;
	SQLiteDatabase db;
	static String tablename;
	public static String dbPath;
	private String clickbtn;
	private PopupWindow popupWindow;
	private int ScreenX;
	private int ScreenY;
	private long mExitTime;
	private String SHAREDPREFERENCES_NAME="myname";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.main);
		
		SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean("isFirstIn", false);
        editor.commit();   
        
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		ScreenX = dm.widthPixels; 
		ScreenY = dm.heightPixels; 
		
		btnnew=(MyImageView)findViewById(R.id.newbtn);
		btnexist=(MyImageView)findViewById(R.id.existbtn);
		btnfun=(MyImageView)findViewById(R.id.funbtn);
		btnmore=(MyImageView)findViewById(R.id.morebtn);
		btnnew.setOnClickListener(listener);
		btnexist.setOnClickListener(listener);
		btnfun.setOnClickListener(listener);
		btnmore.setOnClickListener(listener);
	}
	
	private OnClickListener listener=new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.newbtn:
				clickbtn="newbtn";
				load_start_Animation();			
				break;
			case R.id.existbtn:
				clickbtn="existbtn";
				load_start_Animation();
				break;
			case R.id.funbtn:
				clickbtn="funbtn";
				load_start_Animation();
				break;
			case R.id.morebtn:
				initPopuptWindow();
				popupWindow.showAtLocation(findViewById(R.id.newbtn),Gravity.LEFT, 0, 0);
				break;
			default:
				break;
			}		
		}
	};
	

	private void load_start_Animation() {		
		int the_Animation_ID = R.anim.big_to_small;
		if (the_Animation_ID != 0) {
			Animation animation = AnimationUtils.loadAnimation(this, the_Animation_ID);
			animation.setFillAfter(true);
			animation.setFillEnabled(true);
			if(clickbtn.equals("newbtn")){
				btnnew.startAnimation(animation);
			}
			else if(clickbtn.equals("existbtn")){
				btnexist.startAnimation(animation);
			}
			else if(clickbtn.equals("funbtn")){
				btnfun.startAnimation(animation);
			}
			else if(clickbtn.equals("morebtn")){
				btnmore.startAnimation(animation);
			}
			animation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {				
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
						Intent intent = new Intent();
						if(clickbtn.equals("newbtn")){
							intent.setClass(MainActivity.this, Showdialog.class);				
						}
						else if(clickbtn.equals("existbtn")){
							intent.setClass(MainActivity.this, HaveExist.class);
						}
						else if(clickbtn.equals("funbtn")){
							intent.setClass(MainActivity.this, FunActivity.class);
						}
						startActivity(intent);
						MainActivity.this.finish();
				}	
			});
		}
	}
	
	protected void initPopuptWindow() {
		View popupWindow_view = getLayoutInflater().inflate(R.layout.morelayout, null,
				false);
		popupWindow = new PopupWindow(popupWindow_view, (int)(ScreenX/2.5), ScreenY, true);
		popupWindow.setAnimationStyle(R.style.AnimationFade);	
		popupWindow_view.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
					}				
				return false;
			}
		});		
		ImageView aboutus = (ImageView) popupWindow_view.findViewById(R.id.aboutus);
		ImageView upgrade = (ImageView) popupWindow_view.findViewById(R.id.upgrade);
		ImageView feedback= (ImageView) popupWindow_view.findViewById(R.id.feedback);
		ImageView help= (ImageView) popupWindow_view.findViewById(R.id.help);
		
		
		aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToAboutus(); //跳转到Aboutus的界面
			}
		});
		
		upgrade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				showtoast();
			}
		});
		
		feedback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToFeedback();
			}
		});
		
		help.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToHelp();
			}
		});

	 }
	
	private void showtoast(){
		Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_LONG).show();
	}
	
	private void ToAboutus(){
		Intent intent1=new Intent();
		intent1.setClass(this, Aboutus.class);
		startActivity(intent1);
		this.finish();
	}
	
	private void ToFeedback(){
		Intent intent2=new Intent();
		intent2.setClass(this, Feedback.class);
		startActivity(intent2);
		this.finish();
	}
	
	private void ToHelp(){
		Intent intent3=new Intent();
		intent3.setClass(this, HelpActivity.class);
		startActivity(intent3);
		this.finish();
	}
	
	 public boolean onKeyDown(int keyCode,KeyEvent event)
		{
	    	//应该弹出是否确定退出对话框
			if (keyCode==KeyEvent.KEYCODE_BACK)
			{
				if ((System.currentTimeMillis() - mExitTime) > 2000) {// 如果两次按键时间间隔大于2000毫秒，则不退出
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();// 更新mExitTime
				} else {
					this.onDestroy();
					ExitApplication.getInstance().exit();//通知所有Activity退出
				}			
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	 
    protected void onDestroy()
    {
    	super.onDestroy();
    }
	
}
