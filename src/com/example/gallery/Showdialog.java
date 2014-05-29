package com.example.gallery;


import java.io.File;


import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Showdialog extends Activity
{
	private static final String TAG = "gallery";
	private final static String ID="_id";
	private final static String NAME="albumname";
	SQLiteDatabase db,mdb;
	static String tablename;
	public static String dbPath;
	private ImageButton btnback,btnok;
	private EditText editname;
	private EditText edittime;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler=new Handler()
    { 
		@Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch(msg.what) {
            case 0:
            	showdata();
            	break;
            }
		}
    };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //����ȫ����ʾ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��������ģʽ
		setContentView(R.layout.showdialog);
		Button btnButton=(Button)findViewById(R.id.btnback);
		btnButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				toMain();		
			}
		});
		String string="create table if not exists"+" "+"albums"+
		   		"(_id integer primary key autoincrement,albumname verchar(20))";
		createTable(string);//����һ�����洢��������
		editname=(EditText)findViewById(R.id.galleryname);
		edittime=(EditText)findViewById(R.id.gallerytime);
		btnback=(ImageButton)findViewById(R.id.cancel);
		btnok=(ImageButton)findViewById(R.id.ok);
		Calendar c=Calendar.getInstance();//��ȡ��ǰ����
   		edittime.setText(c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH));
   		edittime.setFocusable(false);
		btnback.setOnClickListener(listener);
		btnok.setOnClickListener(listener);
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.cancel:
				toMain();		//���ص���ҳ��
				break;
			case R.id.ok:
				updatedata();
				break;
			default:
				break;
			}
		}
	};
	
	private void showdata(){
		tablename=editname.getText().toString();		    	   
   		if(tablename.equals("")){
   			Toast.makeText(Showdialog.this, "��������Ʋ���Ϊ�գ�", Toast.LENGTH_LONG).show();
   		}
   		else{
   			mdb=createOrOpenDatabase();
   			Cursor cursor=mdb.query("albums", new String[] {ID,NAME}, null, null, null, null, null);
   			int count=cursor.getCount();
   			int num=0;
   			if(cursor!=null && count>0){ //������album
   				if(cursor.moveToFirst()){
   					do{
   						String tempname=cursor.getString(1);
   						if(tablename.equals(tempname)){
   							Toast.makeText(Showdialog.this, tablename+"������Ѵ��ڣ��������������ƣ�", Toast.LENGTH_LONG).show();
   							break;
   						}
   						else{
   							num++;
   						}
   					}while(cursor.moveToNext());
   				}
   				if(num==count){ //���µ���������ݿ���
   					addtosql();	
   				}
   			}
   			else{ //û���κ�albumʱ���Ͻ���������ݿ���
   				addtosql();	
   			}
   		}	
	}
			   
	
	private void addtosql(){
		ContentValues cv=new ContentValues();
		cv.put(NAME, tablename);
		mdb.insert("albums", null, cv);
   		String string="create table if not exists"+" "+tablename+
   		"(_id integer primary key autoincrement,name verchar(20),qq verchar(20),phone verchar(20),email verchar(20),imagedata verchar(100),photoframe integer,position_x long,position_y long,background verchar(100),photostyle integer)";
   		createTable(string);		    	   		
   		startnew(); //��ת��StartNew�Ľ���
	}
	
	
	private void updatedata(){
		new Thread() 
        { 
               public void run() 
               {      
                   mHandler.sendEmptyMessage(0);               
               } 
        }.start(); 
	}
	
		
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	private void toMain(){
		Intent intent1=new Intent();
		intent1.setClass(this, MainActivity.class);
		startActivity(intent1);
		this.finish();
	}
	
	private void startnew(){
		Log.i(TAG, "tablename="+tablename);
		Intent intent=new Intent();
		intent.setClass(this, StartNew.class);
		Bundle bundle=new Bundle();
		intent.putExtras(bundle);
		startActivity(intent);
		this.finish();
	}
	
	private SQLiteDatabase creatdatabase(){
		if(android.os.Environment.MEDIA_MOUNTED.equals(
				android.os.Environment.getExternalStorageState())){
			Log.i(TAG, "����sd��");
		}
		//��SD���ϴ������ݿⷽ������:
		dbPath=android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath()+"/database";
		Log.i(TAG, dbPath);
		File dbp=new File(dbPath);
		File dbf=new File(dbPath+"/"+"gallery.db");
		//Ŀ¼�������򴴽�һ��Ŀ¼
		if(!dbp.exists()){
            dbp.mkdir(); 
      }
		 boolean isFileCreateSuccess=false;
		 //�ж����ݿ��ļ��Ƿ񴴽��ɹ�
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
	

	//�������
	private void createTable(String sql){
		SQLiteDatabase sld=creatdatabase();
		try{
			sld.execSQL(sql);
			sld.close();
		}catch(Exception e){		
		}		
	}
	
	/**�����ݿ�**/
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
			toMain();
		}
		return true;
	}
	
}
