package com.example.exist;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.gallery.ExitApplication;
import com.example.gallery.MainActivity;
import com.example.gallery.R;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import static com.example.gallery.Showdialog.*;
import static com.example.exist.ToSeeGallery.*;

public class HaveExist extends Activity
{
	private GridView mygridview;
	private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
	HashMap<String, Object> item ;
	HashMap<String, Object> item2 ;
	String []data;
	private SQLiteDatabase mydb;
	private final static String ID="_id";
	private final static String NAME="name";
	private final static String ALBUMNAME="albumname";
	private final static String QQ="qq";
	private final static String PHONE="phone";
	private final static String EMAIL = "email";
	private final static String IMAGEDATA ="imagedata";
	private final static String PHOTOFRAME="photoframe"; //相框
	private final static String POSITION_X="position_x"; //图片的位置x坐标
	private final static String POSITION_Y="position_y"; //图片的位置y坐标
	private final static String BACKGROUND="background";
	private static final String PHOTOSTYLE ="photostyle";
	private String TableName[]=new String[100];
	private String tabname;
	static boolean totoseegallery=false;
	private ImageView img_de;
	private ImageView img_xiangce;
	private boolean enterlong=false;
	private SQLiteDatabase mdb;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.list_gallery);
		Button btomainButton=(Button)findViewById(R.id.btnback);
		btomainButton.setOnClickListener(new OnClickListener()
		{		
			@Override
			public void onClick(View v)
			{
				toMainActivity();
				
			}
		});
		mydb=createOrOpenDatabase();
		String string="create table if not exists"+" "+"albums"+
		   		"(_id integer primary key autoincrement,albumname verchar(20))";
		createTable(string);//创建一个表格存储相册的名字
		mdb=createOrOpenDatabase();
		Cursor cursor=mdb.query("albums", new String[] {ID,ALBUMNAME}, null, null, null, null, null);
		int count=cursor.getCount();
		int numcount=0;
		if(cursor!=null && count>0){
			if(cursor.moveToFirst()){
				do{
					TableName[numcount]=cursor.getString(1);
					numcount++;
				}while(cursor.moveToNext());
			}
			//实例化一个适配器
	        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.onegallery, new String[]{"imageItem", "delItem","textItem"}, new int[]{R.id.xiangce, R.id.ic_del,R.id.text_item});
	        //获得GridView实例
	         mygridview = (GridView) findViewById(R.id.mygridview);      
	         item2= new HashMap<String, Object>();
	         mygridview.setAdapter(adapter);
			for(int i=0;i<count;i++){
 				 item= new HashMap<String, Object>();
 				 ListAdapter la = mygridview.getAdapter();
 	 			 item.put("imageItem", R.drawable.add_mate_icon);	 
 	 			 item.put("textItem", TableName[i]);
 	 			 item.put("delItem", null);		 
 	 			 items.add(item);		 
 	 			 ((SimpleAdapter)la).notifyDataSetChanged();
 	 			 item=null;
			}
			mygridview.setOnItemClickListener(new ItemClickListener());
	        mygridview.setOnItemLongClickListener(new ItemLongClickListener());
		}
		else{
			Toast.makeText(HaveExist.this, "你还没有创建任何纪念册哦！！！", Toast.LENGTH_LONG).show();
		}
	}

	
	class ItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
		{
			final AdapterView<?> parent1=parent;
			final int position1=position;
			RelativeLayout ll=(RelativeLayout)view;//获取当前选中选项对应的LinearLayout
			img_de=(ImageView)ll.getChildAt(1);
			img_xiangce=(ImageView)ll.getChildAt(0);		
				img_de.setVisibility(View.VISIBLE);
				enterlong=true;
				Animation shake = AnimationUtils.loadAnimation(
						HaveExist.this, R.anim.shakeanim);
				shake.reset();
				shake.setFillAfter(true);
				img_de.startAnimation(shake);
				img_xiangce.startAnimation(shake);
				img_de.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						if(position1==parent1.getCount()-1){ //最后一个
							deletesql(position1,true); //最后一个
							ListAdapter la = mygridview.getAdapter();
							Object removeObject=parent1.getItemAtPosition(position1);
							Log.i(TAG, "reomve"+position1);
							items.remove(removeObject);
							((SimpleAdapter)la).notifyDataSetChanged();
						}
						else{
							deletesql(position1, false);
							ListAdapter la = mygridview.getAdapter();
							Object removeObject=parent1.getItemAtPosition(position1);
							items.remove(removeObject);
							Log.i(TAG, "reomve"+position1);
							((SimpleAdapter)la).notifyDataSetChanged();
							for(int i=position1;i<(parent1.getCount()-1);i++){							  
					            Object object = parent1.getItemAtPosition(position1+1);  				            
					            items.set(position1, (Map<String, Object>)object);				            
							}
						}
						img_de.setVisibility(View.INVISIBLE);
						img_de.clearAnimation();
						img_xiangce.clearAnimation();
						enterlong=false;
					}
				});
			return true;
		}
		
	}
	
	private void deletesql(int position,boolean lastone){
		String string="DROP TABLE IF EXISTS"+" "+TableName[position];
		mydb.execSQL(string);  
		int albumcount=0;
		String whereClause="albumname=?";
		String[] whereArgs={TableName[position]};
		mdb.delete("albums", whereClause, whereArgs);
		if(lastone){
			TableName[position]=null; //直接将最后一个赋值为null
		}
		else{
			for(int j=0;j<TableName.length;j++){
				if(TableName[j]!=null){
					albumcount++; //记录实际的相册的数量
				}
			}
			for(int i=position;i<(albumcount-1);i++){
				TableName[position]=TableName[position+1];
			}
			TableName[albumcount-1]=null;
		}
	}
	
	class ItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {			
			parent.getItemAtPosition(position);
			System.out.println(parent.getItemAtPosition(position));
			if(!enterlong){ //进入了长按后便不会执行短按的方法
 				Cursor cursor=mydb.query(TableName[position], new String[] {ID,NAME,QQ,PHONE,EMAIL,IMAGEDATA,PHOTOFRAME,POSITION_X,POSITION_Y,BACKGROUND,PHOTOSTYLE}, null, null, null, null, null);
 				int count=cursor.getCount();
 				if(cursor!=null && count>=0){				
 						tabname=TableName[position];
 						toseegallery(); //跳转到TOSeeGallery界面						
 					}
 				}
		  }
	  }
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		img_de.clearAnimation();
		img_xiangce.clearAnimation();
		img_de.setVisibility(View.INVISIBLE);
		enterlong=false;
		return super.onTouchEvent(event);
	}

	
	private void toMainActivity(){
		Intent intent1=new Intent();
		intent1.setClass(this, MainActivity.class);
		startActivity(intent1);
		HaveExist.this.finish();
	}
	
	private void toseegallery(){
		 totoseegallery=false;
		 Intent intent=new Intent();
		 intent.setClass(this, ToSeeGallery.class);
		 Bundle bundle=new Bundle();
		 bundle.putString("TABLENAME", tabname);
		 intent.putExtras(bundle);
		 startActivity(intent);
		 HaveExist.this.finish();
	}
	
	private SQLiteDatabase creatdatabase(){
		if(android.os.Environment.MEDIA_MOUNTED.equals(
				android.os.Environment.getExternalStorageState())){
			Log.i(TAG, "存在sd卡");
		}
		//在SD卡上创建数据库方法如下:
		dbPath=android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/database";
		Log.i(TAG, dbPath);
		File dbp=new File(dbPath);
		File dbf=new File(dbPath+"/"+"gallery.db");
		//目录不存在则创建一个目录
		if(!dbp.exists()){
            dbp.mkdir(); 
      }
		 boolean isFileCreateSuccess=false;
		 //判断数据库文件是否创建成功
		 if(!dbf.exists()){
			 try
			{
				 isFileCreateSuccess=dbf.createNewFile();
			} catch (Exception e)
			{			
			}
		 }
		else {
			 isFileCreateSuccess=true;
		 }
		if(isFileCreateSuccess){
             db = SQLiteDatabase.openOrCreateDatabase(dbf, null);
		}	
		return db;
	}
	
	//创建表格
	private void createTable(String sql){
		SQLiteDatabase sld=creatdatabase();
		try{
			sld.execSQL(sql);
			sld.close();
		}catch(Exception e){		
		}		
	}
	
	public static SQLiteDatabase createOrOpenDatabase()
	{		
		SQLiteDatabase sld=null;
		try{
			sld=SQLiteDatabase.openOrCreateDatabase(new File(dbPath+"/"+"gallery.db"), null);				
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return sld;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			toMainActivity();
		}
		return true;
	}
	
}
