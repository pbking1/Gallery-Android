package com.example.exist;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.example.gallery.ExitApplication;
import com.example.gallery.PersonDetal;
import com.example.gallery.R;
import com.example.util.ImageUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import static com.example.exist.HaveExist.*;
import static com.example.exist.ToSeeGallery.*;
import static com.example.gallery.PersonDetal.*;

public class DetalPerson extends Activity
{
	private static final String TAG = "gallery";
	private EditText editname;
	private EditText editqq;
	private EditText editphone;
	private EditText editemail;
	private ImageView imageview;
	private SQLiteDatabase mdb;
	private final static String ID="_id";
	private final static String NAME="name";
	private final static String QQ="qq";
	private final static String PHONE="phone";
	private final static String EMAIL="email";
	private final static String IMAGEDATA="imagedata"; //头像
	private final static String PHOTOFRAME="photoframe"; //相框
	private final static String POSITION_X="position_x"; //图片的位置x坐标
	private final static String POSITION_Y="position_y"; //图片的位置y坐标
	private final static String BACKGROUND="background";
	protected static final String PHOTOSTYLE ="photostyle";
	private String tablename;
	private Button buttonname,buttonphone,buttonqq,buttonemail;
	private String firstname,firstqq,firstphone,firstemail;
	private Button btndoneButton;
	private ImageView imgframe;
	static int bottonid; //声明为静态变量
	private Bitmap myBitmap;
	private boolean frompicorcamera=false;
	private Bitmap photo;
	private int mwidth;
	private int mheight;
	static String imgpath="";
	private int marknumber;
	private int photoframe;
	private int photonum;
	private TextView matetex;
	private String currentbtntex;
	private PopupWindow popupWindow;
	private String choosestring;
	int[] frame={R.drawable.photoframe1,R.drawable.photoframe2,R.drawable.photoframe3,R.drawable.photoframe4
			,R.drawable.photoframe5,R.drawable.photoframe6,R.drawable.photoframe7,R.drawable.photoframe8,R.drawable.photoframe9};
	int[] drawablephoto=
		{R.drawable.yuantu,R.drawable.fugu,R.drawable.qingrou,R.drawable.sumiao,R.drawable.diaosu,R.drawable.yangguang,R.drawable.andan,R.drawable.jiaopian,R.drawable.fanzhuan};
	int[] drawableframe=
		{R.drawable.sframe1,R.drawable.sframe2,R.drawable.sframe3,R.drawable.sframe4,R.drawable.sframe5,R.drawable.sframe6,R.drawable.sframe7,R.drawable.sframe8,R.drawable.sframe9};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
		setContentView(R.layout.test);
		matetex=(TextView)findViewById(R.id.matetex);
		editname=(EditText)findViewById(R.id.editname);
		editqq=(EditText)findViewById(R.id.editqq);
		editphone=(EditText)findViewById(R.id.editphone);
		editemail=(EditText)findViewById(R.id.editemail);
		imageview=(ImageView)findViewById(R.id.imageView);
		buttonname=(Button)findViewById(R.id.btnname);
		buttonphone=(Button)findViewById(R.id.btnphone);
		buttonqq=(Button)findViewById(R.id.btnqq);
		buttonemail=(Button)findViewById(R.id.btnemail);
		Button btnbackButton=(Button)findViewById(R.id.btnback);
		btndoneButton=(Button)findViewById(R.id.btndone);
		btnbackButton.setOnClickListener(listener);
		btndoneButton.setOnClickListener(listener);
		buttonphone.setOnClickListener(listener);
		buttonqq.setOnClickListener(listener);
		buttonemail.setOnClickListener(listener);
		imgframe=(ImageView)findViewById(R.id.imageframe);
		LayoutParams para;
        para = imgframe.getLayoutParams();
        para.height = intScreenX-50;
        para.width = intScreenX-50;
        imgframe.setLayoutParams(para);
        imgframe.setScaleType(ScaleType.CENTER);
        imgframe.setBackgroundResource(R.drawable.photoframe1);
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btnback:
				backto();
				
				break;
			case R.id.btnedit:
				updateByMessage(1);
				break;
			case R.id.btndone:
				if(currentbtntex.equals("编辑")){
					btndoneButton.setText("保存");
					currentbtntex="保存";
					updateByMessage(1);
				}
				else{
					btndoneButton.setText("编辑");
					currentbtntex="编辑";
					updateByMessage(2);
				}
				break;
			case R.id.btnphone:
				Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + buttonphone.getText()));
                startActivity(intent);        		
				break;
			case R.id.btnqq:
				Intent intent1 = new Intent();
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity"));
                startActivity(intent1);        			
				break;
			case R.id.btnemail:
				Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName("com.android.email", "com.android.email.activity.Welcome"));
                startActivity(intent2);            			
				break;
			default:
				break;
			}
			
		}
	};
	
	private Bitmap getImageThumbnail(String imagePath, int width, int height) {  
        Bitmap bitmap = null;  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高，注意此处的bitmap为null   
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        options.inJustDecodeBounds = false; // 设为 false   
        // 计算缩放比   
        int h = options.outHeight;  
        int w = options.outWidth;
        
        int beWidth = w / width;  
        int beHeight = h / height;  
        int be = 1;  
        if (beWidth>1||beHeight>1) {  
            be = beWidth;  
        } 
        options.inSampleSize = be;
        
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false   
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }  
	
	private Bitmap getDiskBitmap(String imagePath,int width,int height) {
    	Bitmap bitmap = null;
        try  {  
            File file = new File(imagePath);  
            if(file.exists()) {
            	bitmap = getImageThumbnail(imagePath, width, height);
            } else {
            	BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.dianyidian);
        		bitmap = Bitmap.createScaledBitmap(bd.getBitmap(), width, height, true);
            }
        } catch (Exception e) {    
        }  
        int mywidth=bitmap.getWidth();
		int myheight=bitmap.getHeight();
		Matrix matrix=new Matrix(); 
		float a=(float)(intScreenX-50)/mywidth;
		matrix.postScale(a, a); 
		bitmap=Bitmap.createBitmap(bitmap, 0, 0, mywidth, myheight, matrix, true);	
        return bitmap;
    }
	
	private Handler mHandler=new Handler()
    { 

		@Override 
        public void handleMessage(Message msg) { 
            // TODO Auto-generated method stub 
            super.handleMessage(msg); 
            switch(msg.what) 
            { 
            case 0://在收到消息时，对界面进行更新 
            	Bundle bundle=getIntent().getExtras();
        		tablename=bundle.getString("TableName");
        		bottonid=bundle.getInt("ID"); //知道点击的是哪个imageview
        		marknumber=bundle.getInt("MARKNUM");
            	mdb=PersonDetal.createOrOpenDatabase();
            	Cursor mcur=mdb.query(tablename, new String[] {ID,NAME,QQ,PHONE,EMAIL,IMAGEDATA,PHOTOFRAME,POSITION_X,POSITION_Y,BACKGROUND,PHOTOSTYLE}, null, null, null, null, null);
        		Log.i(TAG, "zhoabudaoma");
        		int count=mcur.getCount();
        		if(mcur!=null && count>=0){
        			if(mcur.moveToFirst()){
        				do{
        					Log.i(TAG, "wokaka");
        					int temid=mcur.getInt(0);
        					if((marknumber*10+bottonid)==temid){
        						int arg2=mcur.getInt(10);
        						int arg3=mcur.getInt(6);
        						imgpath=mcur.getString(5);
        						getbitmap(arg2);
        						getframe(arg3);
        						firstname=mcur.getString(1);
        						firstqq=mcur.getString(2);
        						firstphone=mcur.getString(3);
        						firstemail=mcur.getString(4);   	
        						if((!mcur.getString(5).equals("")) && (!firstname.equals(""))){
        							btndoneButton.setText("编辑");
        							currentbtntex="编辑";
        							matetex.setText(mcur.getString(1));
        							buttonname.setText("姓名:"+mcur.getString(1));
            						buttonqq.setText("QQ:"+mcur.getString(2));
            						buttonphone.setText("电话:"+mcur.getString(3));
            						buttonemail.setText("邮箱:"+mcur.getString(4));         						
        						}
        						else{
        							currentbtntex="完成";
        							updateByMessage(1);
        						}
        					}
        				}while(mcur.moveToNext());
        			}
        		}
        		mcur.close(); 
             break; 
            case 1:
            	btndoneButton.setVisibility(View.VISIBLE);
            	buttonname.setVisibility(View.INVISIBLE);
        		buttonphone.setVisibility(View.INVISIBLE);
        		buttonqq.setVisibility(View.INVISIBLE);
        		buttonemail.setVisibility(View.INVISIBLE);
        		editemail.setVisibility(View.VISIBLE);
        		editqq.setVisibility(View.VISIBLE);
        		editphone.setVisibility(View.VISIBLE);
        		editname.setVisibility(View.VISIBLE);
        		editname.setText(firstname);
        		editqq.setText(firstqq);
        		editphone.setText(firstphone);
        		editemail.setText(firstemail);
        		imgframe.setOnClickListener(new OnClickListener()
				{			
					@Override
					public void onClick(View v)
					{
						if(imgpath.equals("")){
							choosestring="头像";
							popanother();
							popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);				
						}
						else{
							popwindow();
						}
					}
				});
            break;
            case 2:
            	String name=editname.getText().toString();
        		String qq=editqq.getText().toString();
        		String phone=editphone.getText().toString();
        		String email=editemail.getText().toString();
        		ContentValues cv=new ContentValues();
        		cv.put(NAME,name);
        		cv.put(QQ,qq);
        		cv.put(PHONE,phone);
        		cv.put(EMAIL,email);
        		cv.put(PHOTOFRAME,photoframe);
        		cv.put(PHOTOSTYLE,photonum);
        		cv.put(IMAGEDATA,imgpath);
        		String whereClause="_id=?";
        		String[] whereArgs={(marknumber*10+bottonid)+""}; 	
        		if(!(imgpath.equals(""))){
        			mdb.update(tablename,cv,whereClause,whereArgs);
            		backto();
        		}
        		else{
        			Toast.makeText(DetalPerson.this, "你还没有设置头像哦！！", Toast.LENGTH_LONG).show();
        		}
            break;
            case 3:
            	initPopuptWindow();
            	popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);
            	break;
            } 
        } 
    };
    
    private void getbitmap(int arg0){
    	Bitmap temBitmap;
		Bitmap firstBitmap=getDiskBitmap(imgpath, 400, 400);
		switch (arg0)
		{
		case 0:
			//原图
			imageview.setImageBitmap(getDiskBitmap(imgpath, 400, 400));
			photonum=0;
			break;
		case 1:
			//复古
			temBitmap = ImageUtil.oldRemeber(firstBitmap);
			imageview.setImageBitmap(temBitmap);
			photonum=1;
			break;
		case 2:
			//轻柔
			temBitmap = ImageUtil.blurImageAmeliorate(firstBitmap);
			imageview.setImageBitmap(temBitmap);	
			photonum=2;
			break;
		case 3:
			//素描
			temBitmap = ImageUtil.sketch(firstBitmap);
			imageview.setImageBitmap(temBitmap);
			photonum=3;
			break;
		case 4:
			//雕塑
			temBitmap = ImageUtil.emboss(firstBitmap);
			imageview.setImageBitmap(temBitmap);
			photonum=4;
			break;
		case 5:
			//阳光
			temBitmap = ImageUtil.sunshine(firstBitmap, firstBitmap.getWidth() / 2, firstBitmap.getHeight() / 2);
			imageview.setImageBitmap(temBitmap);
			photonum=5;
			break;
		case 6:
			//暗淡
			temBitmap = ImageUtil.sharpenImageAmeliorate(firstBitmap);
			imageview.setImageBitmap(temBitmap);
			photonum=6;
			break;
		case 7:
			//胶片
			temBitmap = ImageUtil.film(firstBitmap);
			imageview.setImageBitmap(temBitmap);
			photonum=7;
			break;
		case 8:
			//倒影
			temBitmap = ImageUtil.reverseBitmap(firstBitmap, 0);
			imageview.setImageBitmap(temBitmap);
			photonum=8;
			break;						
		default:
			imageview.setImageBitmap(getDiskBitmap(imgpath, 400, 400));
			photonum=0;
			break;
		}
    }
    
    protected void initPopuptWindow() {
		View popupWindow_view = getLayoutInflater().inflate(R.layout.choosewhat, null,
				false);
		popupWindow = new PopupWindow(popupWindow_view, intScreenX, intScreenY/3, true);
		popupWindow.setAnimationStyle(R.style.FromBottom);	
		popupWindow_view.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
					}				
				return false;
			}
		});		
		Button render = (Button) popupWindow_view.findViewById(R.id.btnrender);
		Button changephoto = (Button) popupWindow_view.findViewById(R.id.btnchangephoto);
		Button changeframe = (Button) popupWindow_view.findViewById(R.id.btnchangeframe);
		
		// 美化
		render.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				choosestring="美化";
				popanother();
				popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);									
			}
		});
		// 更改头像
		changephoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				choosestring="头像";
				popanother();
				popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);				
			}
		});
		// 更改相框
		changeframe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				choosestring="相框";
				popanother();
				popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);
			}
		});

	}
    
    public List<? extends Map<String, ?>> generateDataList(){
    	ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();;
    	int rowCounter=9;//得到表格的行数
    	if(choosestring.equals("美化")){
    		for(int i=0;i<rowCounter;i++){//循环生成每行的包含对应各个列数据的Map；col1、col2、col3为列名
	    		HashMap<String,Object> hmap=new HashMap<String,Object>();
	    		hmap.put("col1", drawablephoto[i]);   //第一列为图片 		
	    		list.add(hmap);
	    	}    	
    	}
    	else if(choosestring.equals("相框")){
    		for(int i=0;i<rowCounter;i++){//循环生成每行的包含对应各个列数据的Map；col1、col2、col3为列名
	    		HashMap<String,Object> hmap=new HashMap<String,Object>();
	    		hmap.put("col1", drawableframe[i]);   //第一列为图片 		
	    		list.add(hmap);
	    	}    	
    	}
    	return list;
	}

    
    private void popanother(){
		if(choosestring.equals("美化")){
			View popupWindow_view = getLayoutInflater().inflate(R.layout.photostyle, null,
					false);
			popupWindow=null;
			popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);			
			popupWindow.setAnimationStyle(R.style.FromBottom);
			
			//点击其他地方消失	
			
			popupWindow_view.setOnTouchListener(new OnTouchListener() {			
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
						}				
					return false;
				}
			});	
			GridView gv = (GridView)popupWindow_view.findViewById(R.id.photostyle);
			SimpleAdapter sca=new SimpleAdapter(
		        	this,
		        	generateDataList(), //数据List
		        	R.layout.photo_row, new String[]{"col1"}, //列名列表
		        	new int[]{R.id.photo01}//列对应控件id列表
		        );
			gv.setAdapter(sca);	
			gv.setOnItemClickListener( //设置选项被单击的监听器
		        	new OnItemClickListener(){
		        	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {//重写选项被单击事件的处理方法			        		 
		        		   getbitmap(arg2);
		        		   popupWindow.dismiss();
		        	   }        	   
		           	}
		        );        
		}	
		if(choosestring.equals("相框")){
			View popupWindow_view = getLayoutInflater().inflate(R.layout.framestyle, null,
					false);
			popupWindow=null;
			popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			// 设置动画效果
			
			popupWindow.setAnimationStyle(R.style.FromBottom);
			
			//点击其他地方消失	
			
			popupWindow_view.setOnTouchListener(new OnTouchListener() {			
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (popupWindow != null && popupWindow.isShowing()) {
						popupWindow.dismiss();
						popupWindow = null;
						}				
					return false;
				}
			});	
			GridView gv = (GridView)popupWindow_view.findViewById(R.id.framestyle);
			SimpleAdapter sca=new SimpleAdapter(
		        	this,
		        	generateDataList(), //数据List
		        	R.layout.frame_row, new String[]{"col1"}, //列名列表
		        	new int[]{R.id.frame01}//列对应控件id列表
		        );
			gv.setAdapter(sca);
			gv.setOnItemClickListener( //设置选项被单击的监听器
		        	new OnItemClickListener(){
		        	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {//重写选项被单击事件的处理方法			        		 
		        		   getframe(arg2);
		        		   popupWindow.dismiss();
		        	   }        	   
		           	}
		        );        
		}
		if(choosestring.equals("头像")){
			Log.i(TAG, "jinruletouxiangduan");
			View popupWindow_view = getLayoutInflater().inflate(R.layout.choosepicture, null,
					false);
			Log.i(TAG, "jinruletouxiangduan1");		
			popupWindow = new PopupWindow(popupWindow_view, intScreenX, intScreenY/4, true);
			popupWindow.setAnimationStyle(R.style.FromBottom);
			//点击其他地方消失		
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
			Button btnfromgallery = (Button) popupWindow_view.findViewById(R.id.btnfromgallery);
			Button btnfromcamera = (Button) popupWindow_view.findViewById(R.id.btnfromcamera);
			
			
			// 从相册选图
			btnfromgallery.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					popupWindow.dismiss();			
					Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
					getImage.addCategory(Intent.CATEGORY_OPENABLE);
					getImage.setDataAndType(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							"image/*");
					startActivityForResult(getImage, 0);
				}
			});
			// 更改头像
			btnfromcamera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub								
					popupWindow.dismiss();						
					Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
					getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
							.fromFile(new File(Environment
									.getExternalStorageDirectory(),
									"xiaoma.jpg")));
					startActivityForResult(getImageByCamera, 1);					
				}
			});
		}
	}
    
    private void getframe(int arg0){
		   switch (arg0)
			{
			case 0:
				//原相框
				imgframe.setBackgroundResource(frame[0]);
				photoframe=0;
				break;
			case 1:
				imgframe.setBackgroundResource(frame[1]);
				photoframe=1;
				break;
			case 2:
				imgframe.setBackgroundResource(frame[2]);
				photoframe=2;
				break;
			case 3:
				imgframe.setBackgroundResource(frame[3]);
				photoframe=3;
				break;
			case 4:
				imgframe.setBackgroundResource(frame[4]);
				photoframe=4;
				break;
			case 5:
				imgframe.setBackgroundResource(frame[5]);
				photoframe=5;
				break;
			case 6:
				imgframe.setBackgroundResource(frame[6]);
				photoframe=6;
				break;
			case 7:
				imgframe.setBackgroundResource(frame[7]);
				photoframe=7;
				break;
			case 8:
				imgframe.setBackgroundResource(frame[8]);
				photoframe=8;
				break;						
			default:
				imgframe.setBackgroundResource(frame[0]);
				photoframe=0;
				break;
			}
	}
	
    @ Override
	protected void onActivityResult ( int requestCode , int resultCode , Intent data )
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ContentResolver resolver = getContentResolver();
		
		if (requestCode == 0)
		{
			try
			{
				startphotozoom(data.getData());
			} catch ( Exception e )
			{
			}

		} else if (requestCode == 1)
		{
			try
			{
				super.onActivityResult(requestCode, resultCode, data);
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/xiaoma.jpg");
				startphotozoom(Uri.fromFile(temp));
			} catch ( Exception e )
			{
				e.printStackTrace();
			}
	    	
		}
		else if(requestCode==2){
			if(data!=null){
				Bundle extras = data.getExtras();
				if (extras != null) {
					photo = extras.getParcelable("data");
					String saveFilePath = "";				
					try {
						File sdCardDir = Environment.getExternalStorageDirectory();
						File filePath = new File(sdCardDir.getAbsolutePath() + "/mygallery" );
						if(!filePath.exists()){
							filePath.mkdirs();
							Log.i(TAG, "已经创建了路径");
						}				
						saveFilePath = filePath + "/" + filename() + ".png";
						FileOutputStream bos= new FileOutputStream(saveFilePath);
						photo.compress(Bitmap.CompressFormat.PNG, 0, bos);
						bos.flush();
						bos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					imgpath=saveFilePath;
					mwidth=photo.getWidth();
					mheight=photo.getHeight();
					Matrix matrix=new Matrix(); 
					float a=(float)(intScreenX-50)/mwidth;
					matrix.postScale(a, a); 
					myBitmap=Bitmap.createBitmap(photo, 0, 0, mwidth, mheight, matrix, true);
					imageview.setImageBitmap(myBitmap);			
				}
			}
		}
		frompicorcamera=true;    	
	}
    
    public String filename(){
		Random random = new Random(System.currentTimeMillis());
		java.util.Date dt = new java.util.Date(System.currentTimeMillis()); 
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String fileName = fmt.format(dt)+ Math.abs(random.nextInt()) % 100000; 		
		return fileName;
	}      
    
    private void startphotozoom(Uri uri){
		Intent intent = new Intent("com.android.camera.action.CROP");	
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true); 
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 2);
	}
    
    private void popwindow() 
	   { 
	        //匿名对象 
	       new Thread() 
	        { 
	             public void run() 
	             { 	               	                         			
	               mHandler.sendEmptyMessage(3);	                       
	                } 
	         }.start(); 
	    }
	
	private void updateByMessage( final int i) 
    { 
        //匿名对象 
         new Thread() 
         { 
                public void run() 
                { 
                	switch (i)
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

					default:
						break;
					}
                } 
         }.start(); 
    }
	
	
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if(frompicorcamera){
			
		}
		else{
			updateByMessage(0);
		}
	}



	private void backto(){
		totoseegallery=true;
		Intent intent=new Intent();
		intent.setClass(this, ToSeeGallery.class);
		Bundle bundle1=new Bundle();
		bundle1.putString("TABLENAME",tablename);
		bundle1.putInt("MARKNUM", marknum);
		intent.putExtras(bundle1);
		startActivity(intent);
		this.finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			backto();
		}
		return true;
	}
	
}
