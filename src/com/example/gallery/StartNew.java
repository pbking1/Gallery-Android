package com.example.gallery;

import static com.example.gallery.PersonDetal.Toaddnew;
import static com.example.gallery.PersonDetal.btnid;
import static com.example.gallery.PersonDetal.imagedata;
import static com.example.gallery.Showdialog.dbPath;
import static com.example.gallery.Showdialog.tablename;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

public class StartNew extends Activity
{
	private SQLiteDatabase mydb;
	private PopupWindow popupWindow;
	protected static final String TAG = "ABC";
	private final static String NAME="name"; //姓名
	private final static String QQ="qq";  //QQ
	private final static String PHONE="phone"; //电话
	private final static String EMAIL="email"; //电子邮箱
	private final static String IMAGEDATA="imagedata"; //头像
	private final static String PHOTOFRAME="photoframe"; //相框
	private final static String POSITION_X="position_x"; //图片的位置x坐标
	private final static String POSITION_Y="position_y"; //图片的位置y坐标
	private final static String BACKGROUND="background"; //背景图
	private static final String PHOTOSTYLE ="photostyle";//效果图
	private static Bitmap bmpout;
	private Button addorcancel;
	private Button done;
	private ImageButton morepaper;
	
	@SuppressWarnings("deprecation")
	private AbsoluteLayout listview;
	private View layout;
	LayoutInflater mInflater;
	static int countpage=1; //记录相册的页数,至少一页
	private ImageView img1,img2,img3,img4,img5,img6,img7,img8,img9,img10;
	private boolean longtouch=false; //默认为false，为true时触发拖拽事件
	private int position_x[]=new int[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000}; //记录照片的x坐标
	private int position_y[]=new int[]{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000}; //记录照片的y坐标
	int[] bgIds={R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,
			R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10};
	int[] drawableIds=
		{R.drawable.sbg1,R.drawable.sbg2,R.drawable.sbg3,R.drawable.sbg4,R.drawable.sbg5,R.drawable.sbg6,R.drawable.sbg7,R.drawable.sbg8,R.drawable.sbg9,R.drawable.sbg10,R.drawable.camera,R.drawable.album};
	public static int intScreenX;
	public static int intScreenY;
	private int intWidth;
	private int intHeight;
	private String wallbg="0";//默认背景

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置全屏显示
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
        
		setContentView(R.layout.list_item);
		DisplayMetrics dm = new DisplayMetrics(); 
	    getWindowManager().getDefaultDisplay().getMetrics(dm); 
	    intScreenX = dm.widthPixels; 
	    intScreenY = dm.heightPixels; 
	    
		addorcancel=(Button)findViewById(R.id.addorcancel);
		done=(Button)findViewById(R.id.done);
		morepaper=(ImageButton)findViewById(R.id.morewallpaper);						
		morepaper.setOnClickListener(listener);
		addorcancel.setOnClickListener(listener);
		done.setOnClickListener(listener);
		
		mInflater=LayoutInflater.from(this);
		listview=(AbsoluteLayout)findViewById(R.id.lilayout);
		listview.setBackgroundResource(bgIds[0]);
		creatsql();//第一次进来时首先创建了10行列表
		countpage=1;
		listview.addView(getview());
	}
	
	@SuppressWarnings("deprecation")
	private View getview(){
		layout=null;
		layout = (AbsoluteLayout) mInflater.inflate(R.layout.tenphotos2, null);
		img1=(ImageView)layout.findViewById(R.id.img1);
		img2=(ImageView)layout.findViewById(R.id.img2);
		img3=(ImageView)layout.findViewById(R.id.img3);
		img4=(ImageView)layout.findViewById(R.id.img4);
		img5=(ImageView)layout.findViewById(R.id.img5);
		img6=(ImageView)layout.findViewById(R.id.img6);
		img7=(ImageView)layout.findViewById(R.id.img7);
		img8=(ImageView)layout.findViewById(R.id.img8);
		img9=(ImageView)layout.findViewById(R.id.img9);
		img10=(ImageView)layout.findViewById(R.id.img10);

		intWidth=100;
		intHeight=100;
		img1.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,20) ); 
		img2.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,20) ); 
		img3.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,40+intHeight) ); 
		img4.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,40+intHeight) ); 
		img5.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,60+2*intHeight) ); 
		img6.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,60+2*intHeight) ); 
		img7.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,80+3*intHeight) ); 
		img8.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,80+3*intHeight) ); 
		img9.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,100+4*intHeight) );
		img10.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,100+4*intHeight) ); 
		
		img1.setOnLongClickListener(l);
		img2.setOnLongClickListener(l);
		img3.setOnLongClickListener(l);
		img4.setOnLongClickListener(l);
		img5.setOnLongClickListener(l);
		img6.setOnLongClickListener(l);
		img7.setOnLongClickListener(l);
		img8.setOnLongClickListener(l);
		img9.setOnLongClickListener(l);
		img10.setOnLongClickListener(l);
		
		img1.setOnTouchListener(listener2);
		img2.setOnTouchListener(listener2);
		img3.setOnTouchListener(listener2);
		img4.setOnTouchListener(listener2);
		img5.setOnTouchListener(listener2);
		img6.setOnTouchListener(listener2);
		img7.setOnTouchListener(listener2);
		img8.setOnTouchListener(listener2);
		img9.setOnTouchListener(listener2);
		img10.setOnTouchListener(listener2);
		
		img1.setOnClickListener(listener);
		img2.setOnClickListener(listener);
		img3.setOnClickListener(listener);
		img4.setOnClickListener(listener);
		img5.setOnClickListener(listener);
		img6.setOnClickListener(listener);
		img7.setOnClickListener(listener);
		img8.setOnClickListener(listener);
		img9.setOnClickListener(listener);
		img10.setOnClickListener(listener);
		
		return layout;
		
	}
	
	private OnTouchListener listener2=new OnTouchListener()
	{
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			switch (v.getId())
			{
			case R.id.img1:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[0] = (int)(event.getRawX());  
	                position_y[0] = (int)(event.getRawY() - 50); 	            
	                v.layout(position_x[0] - img1.getWidth()/2, position_y[0] - img1.getHeight()/2, position_x[0] + img1.getWidth()/2, position_y[0] + img1.getHeight()/2); 	               
				}
				break;
			case R.id.img2:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[1] = (int)(event.getRawX());  
	                position_y[1] = (int)(event.getRawY() - 50); 	                	               	            
	                v.layout(position_x[1] - img2.getWidth()/2, position_y[1] - img2.getHeight()/2, position_x[1] + img2.getWidth()/2, position_y[1] + img2.getHeight()/2);  
				}
				break;
			case R.id.img3:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[2] = (int)(event.getRawX());  
	                position_y[2] = (int)(event.getRawY() - 50); 	             
	                v.layout(position_x[2] - img3.getWidth()/2, position_y[2] - img3.getHeight()/2, position_x[2] + img3.getWidth()/2, position_y[2] + img3.getHeight()/2);  
				}
				break;
			case R.id.img4:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[3] = (int)(event.getRawX());  
	                position_y[3] = (int)(event.getRawY() - 50); 	               
	                v.layout(position_x[3] - img4.getWidth()/2, position_y[3] - img4.getHeight()/2, position_x[3] + img4.getWidth()/2, position_y[3] + img4.getHeight()/2);  
				}
				break;
			case R.id.img5:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[4] = (int)(event.getRawX());  
	                position_y[4] = (int)(event.getRawY() - 50); 	                
	                v.layout(position_x[4] - img5.getWidth()/2, position_y[4] - img5.getHeight()/2, position_x[4] + img5.getWidth()/2, position_y[4] + img5.getHeight()/2);  
				}
				break;
			case R.id.img6:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[5] = (int)(event.getRawX());  
	                position_y[5] = (int)(event.getRawY() - 50);                
	                v.layout(position_x[5] - img6.getWidth()/2, position_y[5] - img6.getHeight()/2, position_x[5] + img6.getWidth()/2, position_y[5] + img6.getHeight()/2);  
				}
				break;
			case R.id.img7:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[6] = (int)(event.getRawX());  
	                position_y[6] = (int)(event.getRawY() - 50); 	               
	                v.layout(position_x[6] - img7.getWidth()/2, position_y[6] - img7.getHeight()/2, position_x[6] + img7.getWidth()/2, position_y[6] + img7.getHeight()/2);  
				}
				break;
			case R.id.img8:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[7] = (int)(event.getRawX());  
	                position_y[7] = (int)(event.getRawY() - 50);                
	                v.layout(position_x[7] - img8.getWidth()/2, position_y[7] - img8.getHeight()/2, position_x[7] + img8.getWidth()/2, position_y[7] + img8.getHeight()/2);  
				}
				break;
			case R.id.img9:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[8] = (int)(event.getRawX());  
	                position_y[8] = (int)(event.getRawY() - 50); 	               
	                v.layout(position_x[8] - img9.getWidth()/2, position_y[8] - img9.getHeight()/2, position_x[8] + img9.getWidth()/2, position_y[8] + img9.getHeight()/2);  
				}
				break;
			case R.id.img10:
				if(event.getAction()==MotionEvent.ACTION_MOVE){
					position_x[9] = (int)(event.getRawX());  
	                position_y[9] = (int)(event.getRawY() - 50); 	                
	                v.layout(position_x[9] - img10.getWidth()/2, position_y[9] - img10.getHeight()/2, position_x[9] + img10.getWidth()/2, position_y[9] + img10.getHeight()/2);  
				}
				break;
			default:
				break;
			}
			longtouch=false; //长按结束后使longtouch=false
			return false;  //只有return false才会相应后面的onclick事件，而return true则不会响应onclick事件
		}
	};
	
	//要触屏移动时，必须要先长按，这样就把onclick和ontouch分清楚了
	private OnLongClickListener l=new OnLongClickListener()
	{

		@Override
		public boolean onLongClick(View v)
		{
			longtouch=true;
			return true;
		}
	};
	
	/**单击事件**/
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			//当longtouch=false时才生效
			if(!longtouch){
				switch (v.getId())
				{
				//添加新的一页的按钮
				case R.id.addorcancel:
					updatesql();	//首先要保存上一页的数据
					initdata();		//初始化10个图片的位置和背景图
					countpage++;	//新建时使countpage自加1
					creatsql();		//再次插入10行数据
					listview.removeAllViews();	//除去当前的view
					listview.addView(getview()); //重新加入一个新的界面
					break;
					//完成添加的按钮
				case R.id.done:
					updatesql();//完成时要保存该页的数据
					toMainActivity();
					break;
					//更多墙纸的按钮
				case R.id.morewallpaper:
					initPopuptWindow();
					popupWindow.showAtLocation(findViewById(R.id.addorcancel), Gravity.BOTTOM, 0,0);
					break;
					//图片一
				case R.id.img1:
					ToPersonDetal(1,position_x[0] , position_y[0]);
					break;
					//图片二
				case R.id.img2:
					ToPersonDetal(2,position_x[1] , position_y[1]);
					break;
					//图片三
				case R.id.img3:
					ToPersonDetal(3,position_x[2] , position_y[2]);
					break;
					//图片四
				case R.id.img4:
					ToPersonDetal(4,position_x[3] , position_y[3]);
					break;
					//图片五
				case R.id.img5:
					ToPersonDetal(5,position_x[4] , position_y[4]);
					break;
					//图片六
				case R.id.img6:
					ToPersonDetal(6,position_x[5] , position_y[5]);
					break;
					//图片七
				case R.id.img7:
					ToPersonDetal(7,position_x[6] , position_y[6]);
					break;
					//图片八
				case R.id.img8:
					ToPersonDetal(8,position_x[7] , position_y[7]);
					break;
					//图片九
				case R.id.img9:
					ToPersonDetal(9,position_x[8] , position_y[8]);
					break;
					//图片十
				case R.id.img10:
					ToPersonDetal(10,position_x[9] , position_y[9]);
					break;
				default:
					break;
				}
			}
			
		}
	};
	
	/**新建后初始化新的界面数据**/
	private void initdata(){
		listview.setBackgroundResource(bgIds[0]);
		position_x[0]=position_x[1]=position_x[2]=1000;
		position_x[3]=position_x[4]=position_x[5]=1000;
		position_x[6]=position_x[7]=position_x[8]=1000;
		position_x[9]=position_y[0]=position_y[1]=1000;
		position_y[2]=position_y[3]=position_y[4]=1000;
		position_y[5]=position_y[6]=position_y[7]=1000;
		position_y[8]=position_y[9]=1000;
	}
	
	/**跳转到PersonDetal**/
	private void ToPersonDetal(int id,int x,int y){
		Intent intent=new Intent();
		intent.setClass(StartNew.this, PersonDetal.class);
		Bundle bundle=new Bundle();
		bundle.putInt("ID", id);		
		bundle.putInt("POSITION_X", x);
		bundle.putInt("POSITION_Y", y);
		bundle.putInt("CountPage", countpage);
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	
	/**每创建一页都要添加十行初始化数据到数据库**/
	private void creatsql(){
		//初始化十组数据
		mydb=createOrOpenDatabase();
		for(int i=0;i<10;i++){
			ContentValues cv=new ContentValues();
			cv.put(NAME, "");
			cv.put(QQ, "");
			cv.put(PHONE, "");
			cv.put(EMAIL, "");
			cv.put(IMAGEDATA, "");
			cv.put(PHOTOFRAME, 0);
			cv.put(POSITION_X, 1000);  //默认x坐标的位置是1000
			cv.put(POSITION_Y, 1000);  //默认y坐标的位置是1000
			cv.put(BACKGROUND, "0");
			cv.put(PHOTOSTYLE, 0);
			mydb.insert(tablename, null, cv);
			cv=null;
		}				
		
	}
	
	/**新建新的一页或者点击完成后都要更新数据库**/
	private void updatesql(){
		for(int i=0;i<10;i++){
			ContentValues cValues=new ContentValues();
			cValues.put(POSITION_X, position_x[i]);
			cValues.put(POSITION_Y, position_y[i]);
			cValues.put(BACKGROUND, wallbg);
			String whereClause="_id=?";
			String[] whereArgs={(countpage-1)*10+i+1+""};
			mydb.update(tablename, cValues, whereClause, whereArgs);
			cValues=null;
		}
	}
	
	/**打开数据库**/
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
	
	/**初始化更多壁纸的弹出窗口**/
	public void initPopuptWindow() {
		View popupWindow_view = getLayoutInflater().inflate(R.layout.choosewallpaper, null,
				false);
		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setAnimationStyle(R.style.FromBottom);	// 设置动画效果	
		
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
		
		GridView gv = (GridView)popupWindow_view.findViewById(R.id.wallppgv);
		SimpleAdapter sca=new SimpleAdapter(
	        	this,
	        	generateDataList(), //数据List
	        	R.layout.grid_row, new String[]{"col1"}, //列名列表
	        	new int[]{R.id.ImageView01}//列对应控件id列表
	        );
		gv.setAdapter(sca);	
		gv.setOnItemClickListener( //设置选项被单击的监听器
	        	new OnItemClickListener(){	        
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {//重写选项被单击事件的处理方法
					popupWindow.dismiss();
	        		 switch (arg2)
					{
	        		 //选择默认的壁纸
					case 0:
						listview.setBackgroundResource(bgIds[0]);
						wallbg="0";
						break;
					//选择第一张壁纸
					case 1:
						listview.setBackgroundResource(bgIds[1]);
						wallbg="1";
						break;
					//选择第二张壁纸
					case 2:
						listview.setBackgroundResource(bgIds[2]);
						wallbg="2";
						break;
					//选择第三张壁纸
					case 3:
						listview.setBackgroundResource(bgIds[3]);
						wallbg="3";
						break;
					//选择第四张壁纸
					case 4:
						listview.setBackgroundResource(bgIds[4]);
						wallbg="4";
						break;
					//选择第五张壁纸
					case 5:
						listview.setBackgroundResource(bgIds[5]);
						wallbg="5";
						break;
					//选择第六张壁纸
					case 6:
						listview.setBackgroundResource(bgIds[6]);
						wallbg="6";
						break;
					//选择第七张壁纸
					case 7:
						listview.setBackgroundResource(bgIds[7]);
						wallbg="7";
						break;
					//选择第八张壁纸
					case 8:
						listview.setBackgroundResource(bgIds[8]);
						wallbg="8";
						break;
					//选择第九张壁纸
					case 9:
						listview.setBackgroundResource(bgIds[9]);
						wallbg="10";
						break;
					//拍照选择壁纸
					case 10:
						Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					    startActivityForResult(intentCam, 1);	
						break;
					//从相册选择壁纸
					case 11:
						Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
						getImage.addCategory(Intent.CATEGORY_OPENABLE);
						getImage.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(getImage, 0);
						break;
					default:
						break;
					} 
	        	   }        	   
	           	}
	        );        
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0)
		{
			try
			{
				Uri uri = data.getData();
				wallbg=getFilePathFromUri(uri); //将路径存到wallbg
			} catch ( Exception e )
			{
			}

		} else if ( resultCode == Activity.RESULT_OK && requestCode == 1 && data!=null)
		{
				Bundle extras = data.getExtras();
				if(extras!=null){
					//Bitmap new_bitmap = extras.getParcelable("data");
					Bitmap new_bitmap = (Bitmap) extras.get("data");
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
						new_bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
						bos.flush();
						bos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					wallbg=saveFilePath;
				}
		}
		Bitmap bp=getDiskBitmap(wallbg, intScreenX,intScreenY);
		Drawable drawable = new BitmapDrawable(bp); 
		listview.setBackground(drawable);
	}
	
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
    	//int width, height;
    	//w;//
        try  {  
            File file = new File(imagePath);  
            if(file.exists()) {
            	bitmap = getImageThumbnail(imagePath, width, height);
            } else {
            	BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_launcher);
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
	
	public String filename(){
		Random random = new Random(System.currentTimeMillis());
		java.util.Date dt = new java.util.Date(System.currentTimeMillis()); 
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String fileName = fmt.format(dt)+ Math.abs(random.nextInt()) % 100000; 		
		return fileName;
	}      
	
	/* 将URI转为普通文件路径 */
    public String getFilePathFromUri(Uri uri) {
    	String[] proj = { MediaStore.Images.Media.DATA };
    	Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
    	int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	actualimagecursor.moveToFirst();
    	String path = actualimagecursor.getString(actual_image_column_index);
    	return path;
     }


	public List<? extends Map<String, ?>> generateDataList(){
    	ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();;
    	int rowCounter=12;//得到表格的行数
    	for(int i=0;i<rowCounter;i++){//循环生成每行的包含对应各个列数据的Map；col1、col2、col3为列名
    		HashMap<String,Object> hmap=new HashMap<String,Object>();
    		hmap.put("col1", drawableIds[i]);   //第一列为图片 		
    		list.add(hmap);
    	}    	
    	return list;
	}

	
	private void toMainActivity(){
		if(bmpout!=null && !bmpout.isRecycled()){
			bmpout.recycle();
			bmpout=null;
		}
		System.gc();
		Intent intent2=new Intent();
		intent2.setClass(this, MainActivity.class);
		startActivity(intent2);
		this.finish();
	}
	
	public static enum ScalingLogic {
        CROP, FIT,SCALE_CROP
    }


	public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {  
		Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);  
		Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);  
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Config.ARGB_8888);  
		Canvas canvas = new Canvas(scaledBitmap);  
		canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));return scaledBitmap;  
	} 
	
	public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {  
		   if (scalingLogic == ScalingLogic.CROP) {  
		     final float srcAspect = (float)srcWidth / (float)srcHeight;  
		     final float dstAspect = (float)dstWidth / (float)dstHeight;  
		     if (srcAspect > dstAspect) {  
		      final int srcRectWidth = (int)(srcHeight * dstAspect);  
		      final int srcRectLeft = (srcWidth - srcRectWidth) / 2;  
		      return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);  
		     } else {  
		       final int srcRectHeight = (int)(srcWidth / dstAspect);  
		       final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;  
		       return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);  
		     }  
		   } else {  
		     return new Rect(0, 0, srcWidth, srcHeight);  
		   }  
		}  

	 public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {  
	   if (scalingLogic == ScalingLogic.FIT) {  
	   final float srcAspect = (float)srcWidth / (float)srcHeight;  
	   final float dstAspect = (float)dstWidth / (float)dstHeight;  
	   if (srcAspect > dstAspect) {  
       return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));  
	   } else {  
	       return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);  
	   }  
	  } else {  
	     return new Rect(0, 0, dstWidth, dstHeight);  
	  }  
	} 
	

	public static Bitmap getBmp() 
	{	 			
		bmpout = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
		bmpout=createScaledBitmap(bmpout, (int)(1.5263*60), (int)(1.5263*60), ScalingLogic.SCALE_CROP);		
		return bmpout;
	 }
	
	public static Bitmap getBmp(byte[] imgdata){
		if(imgdata!=null){
			Bitmap bitmap=BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
			bitmap=createScaledBitmap(bitmap, (int)(1.5263*60), (int)(1.5263*60), ScalingLogic.SCALE_CROP);
			return bitmap;
		}
		else{
			return null;
		}
	}
	
	
	private Handler mHandler=new Handler()
    { 
		@SuppressWarnings("deprecation")
		@Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            switch(msg.what) 
            { 
            case 0://在收到消息时，对界面进行更新            	
            	if(Toaddnew){           		
            		if(!(position_x[0]==1000 && position_y[0]==1000)){
            			img1.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[0]-50,position_y[0]-50) );           			
            		}
            		if(!(position_x[1]==1000 && position_y[1]==1000)){           	
            			img2.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[1]-50,position_y[1]-50) ); 
            		}
            		if(!(position_x[2]==1000 && position_y[2]==1000)){
            			img3.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[2]-50,position_y[2]-50) ); 
            		}
            		if(!(position_x[3]==1000 && position_y[3]==1000)){
            			
            			img4.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[3]-50,position_y[3]-50) ); 
            		}
            		if(!(position_x[4]==1000 && position_y[4]==1000)){
            			
            			img5.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[4]-50,position_y[4]-50) ); 
            		}
            		if(!(position_x[5]==1000 && position_y[5]==1000)){
            			
            			img6.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[5]-50,position_y[5]-50) ); 
            		}
            		if(!(position_x[6]==1000 && position_y[6]==1000)){
            			
            			img7.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[6]-50,position_y[6]-50) ); 
            		}
            		if(!(position_x[7]==1000 && position_y[7]==1000)){
            			
            			img8.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[7]-50,position_y[7]-50) ); 
            		}
            		if(!(position_x[8]==1000 && position_y[8]==1000)){
            			
            			img9.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[8]-50,position_y[8]-50) ); 
            		}
            		if(!(position_x[9]==1000 && position_y[9]==1000)){
            			
            			img10.setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[9]-50,position_y[9]-50) ); 
            		}
        			Toaddnew=false;
        			if(btnid==1){       		
        				img1.setImageBitmap(getBmp());   
        			}
        			else if(btnid==2){       	
        				img2.setImageBitmap(getBmp());
        			}
        			else if(btnid==3){       	
        				img3.setImageBitmap(getBmp());
        			}
        			else if(btnid==4){       
        				img4.setImageBitmap(getBmp());
        			}
        			else if(btnid==5){       			
        				img5.setImageBitmap(getBmp());
        			}
        			else if(btnid==6){      
        				img6.setImageBitmap(getBmp());
        			}
        			else if(btnid==7){    	
        				img7.setImageBitmap(getBmp());
        			}
        			else if(btnid==8){       	
        				img8.setImageBitmap(getBmp());
        			}
        			else if(btnid==9){       			
        				img9.setImageBitmap(getBmp());
        			}
        			else if(btnid==10){        
        				img10.setImageBitmap(getBmp());
        			}
        		}
                break; 
            } 
        } 
    };
	
	private void updateByMessage() 
    { 
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
		if(Toaddnew){
			updateByMessage();
		}
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
