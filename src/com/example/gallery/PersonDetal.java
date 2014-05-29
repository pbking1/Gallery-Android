package com.example.gallery;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import android.app.Activity;
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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import static com.example.gallery.StartNew.*;
import static com.example.gallery.Showdialog.*;
import com.example.util.ImageUtil;

public class PersonDetal extends Activity
{
	private SQLiteDatabase mydb;
	private final static String ID="_id";
	private final static String NAME="name";
	private final static String QQ="qq";
	private final static String PHONE="phone";
	private final static String EMAIL="email";
	private final static String IMAGEDATA="imagedata"; //ͷ��
	private final static String PHOTOFRAME="photoframe"; //���
	private final static String POSITION_X="position_x"; //ͼƬ��λ��x����
	private final static String POSITION_Y="position_y"; //ͼƬ��λ��y����
	private final static String BACKGROUND="background";
	private static final String PHOTOSTYLE ="photostyle";
	private int photoframe=0;//0��ʾĬ�����
	private static final String TAG = "gallery";
	public static int btnid;  //��ʾ���µİ���
	private int image_x; //��ʾ����ͼ��x����
	private int image_y; //��ʾ����ͼ��y����
	private EditText editname;
	private EditText editqq;
	private EditText editphone;
	private EditText editemail;
	private ImageView imgview;
	static String peoplename;
    static String btnname="";
	static Bitmap resizeBmp=null;
	static byte[] imagedata;
	static boolean Toaddnew=false;
	static int image_width;
	static int image_height;
	private boolean isfromaddnew=true;
	private boolean frompicorcamera=false; //������trueʱ��ζ�Ŵ�ͼ�����������պ󷵻�onresume
	public static int intScreenX; //�ֻ���Ļ���(����)
	public static int intScreenY; //�ֻ���Ļ�߶�(����)
	private ImageView imgframe;
	private Bitmap photo;	
	private int mwidth;
	private int mheight;
	private String imgpath="";//��Ƭ��·��
	private int countpg;//��¼��ǰ��ҳ��
	private PopupWindow popupWindow;
	private String choosestring;
	private int photonum;//��¼ѡ�����Ƭ��Ч��ͼ��0�������ԭͼ
	int[] frame={R.drawable.photoframe1,R.drawable.photoframe2,R.drawable.photoframe3,R.drawable.photoframe4
			,R.drawable.photoframe5,R.drawable.photoframe6,R.drawable.photoframe7,R.drawable.photoframe8,R.drawable.photoframe9};
	int[] drawablephoto=
		{R.drawable.yuantu,R.drawable.fugu,R.drawable.qingrou,R.drawable.sumiao,R.drawable.diaosu,R.drawable.yangguang,R.drawable.andan,R.drawable.jiaopian,R.drawable.fanzhuan};
	int[] drawableframe=
		{R.drawable.sframe1,R.drawable.sframe2,R.drawable.sframe3,R.drawable.sframe4,R.drawable.sframe5,R.drawable.sframe6,R.drawable.sframe7,R.drawable.sframe8,R.drawable.sframe9};
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //����ȫ����ʾ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��������ģʽ
        setContentView(R.layout.test);
		//setContentView(R.layout.person);
		Button btnback=(Button)findViewById(R.id.btnback);
		Button btndone=(Button)findViewById(R.id.btndone);
		btndone.setVisibility(View.VISIBLE);
		btndone.setText("����");
		
		Button btnname=(Button)findViewById(R.id.btnname);
		Button btnphone=(Button)findViewById(R.id.btnphone);
		Button btnqq=(Button)findViewById(R.id.btnqq);
		Button btnemail=(Button)findViewById(R.id.btnemail);
		btnname.setVisibility(View.INVISIBLE);
		btnqq.setVisibility(View.INVISIBLE);
		btnphone.setVisibility(View.INVISIBLE);
		btnemail.setVisibility(View.INVISIBLE);
		
		DisplayMetrics dm = new DisplayMetrics(); 
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		intScreenX = dm.widthPixels; 
		intScreenY = dm.heightPixels; 
		
		imgview=(ImageView)findViewById(R.id.imageView);
		imgframe=(ImageView)findViewById(R.id.imageframe);
			
		LayoutParams para;
        para = imgframe.getLayoutParams();
        para.height = intScreenX-50;
        para.width = intScreenX-50;
        imgframe.setLayoutParams(para);
        imgframe.setScaleType(ScaleType.CENTER);
		imgframe.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{			
				if(imgpath.equals("")){
					choosestring="ͷ��";
					popanother();
					popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);				
				}
				else{
					popwindow();
				}
				
				
			}
		});
		editname=(EditText)findViewById(R.id.editname);
		editqq=(EditText)findViewById(R.id.editqq);
		editphone=(EditText)findViewById(R.id.editphone);
		editemail=(EditText)findViewById(R.id.editemail);
		editname.setVisibility(View.VISIBLE);
		editqq.setVisibility(View.VISIBLE);
		editphone.setVisibility(View.VISIBLE);
		editemail.setVisibility(View.VISIBLE);
		btnback.setOnClickListener(listener);
		btndone.setOnClickListener(listener);
		
	}
	
	private OnClickListener listener =new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.btnback:
				finishthis();
				break;
			case R.id.btndone:
				creatsql();
				break;
			default:
				break;
			}
			
		}
	};
	
	

	public void finishthis(){
		this.finish();
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
				// TODO Auto-generated catch block
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
					float b=(float)400/mheight;
					matrix.postScale(a, a); 
					resizeBmp=Bitmap.createBitmap(photo, 0, 0, mwidth, mheight, matrix, true);
					imgview.setImageBitmap(resizeBmp);
					imagedata=img(resizeBmp);
			}
		}
		}
		isfromaddnew=false;
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
		
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, "/mydata");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		intent.putExtra("scale", true); 
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, 2);
	}
	
	/* ��URIתΪ��ͨ�ļ�·�� */
    public String getFilePathFromUri(Uri uri) {
    	String[] proj = { MediaStore.Images.Media.DATA };
    	Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
    	int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    	actualimagecursor.moveToFirst();
    	String path = actualimagecursor.getString(actual_image_column_index);
    	return path;
     }
	

	public static Bitmap decodeSampledBitmap(byte[] data,int reqWidth, 
			int reqHeight) {
	        // ��һ�ν�����inJustDecodeBounds����Ϊtrue������ȡͼƬ��С
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    //BitmapFactory.decodeResource(res, resId, options);
	    BitmapFactory.decodeByteArray(data, 0, data.length, options);
	    // �������涨��ķ�������inSampleSizeֵ
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    // ʹ�û�ȡ����inSampleSizeֵ�ٴν���ͼƬ
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
    // ԴͼƬ�ĸ߶ȺͿ��
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = (int)(intScreenX-50)/width;
    	return inSampleSize;

	}


	
	public static byte[] img(Bitmap bitmap)
    {
    
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	//Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
    	bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    	return baos.toByteArray();
    
   }
	
	
	public static Bitmap getPicFromBytes ( byte[] bytes , BitmapFactory.Options opts )
	{
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}
	
	public static byte[] readStream ( InputStream inStream ) throws Exception
	{
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1)
		{
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}
	
	public synchronized Drawable byteToDrawable(byte[] img) {  
        Bitmap bitmap;  
        if (img != null) {               
            bitmap = BitmapFactory.decodeByteArray(img,0, img.length);  
            Drawable drawable = new BitmapDrawable(bitmap);                
            return drawable;  
        }  
        else 
        	return null;    
    } 
	
	
	private void creatsql(){
		//�õ�������˵ľ�����Ϣ
		String name=editname.getText().toString();
		String qq=editqq.getText().toString();
		String phone=editphone.getText().toString();
		String email=editemail.getText().toString();
		peoplename=name;
		ContentValues cv=new ContentValues();
		cv.put(NAME, name);
		cv.put(QQ, qq);
		cv.put(PHONE, phone);
		cv.put(EMAIL, email);
		cv.put(IMAGEDATA, imgpath);
		cv.put(PHOTOFRAME, photoframe);
		cv.put(POSITION_X, image_x);
		cv.put(POSITION_Y, image_y);
		cv.put(PHOTOSTYLE,photonum);
		String whereClause="_id=?";
		int tempbtnid=(countpage-1)*10+btnid; //����ҳ���Ĳ�ͬ������id
		String[] whereArgs={tempbtnid+""};	
		mydb.update(tablename, cv, whereClause, whereArgs);
		Toaddnew=true;
		
		this.finish();
	}
	
	 private Handler mHandler=new Handler()
	    { 

			

			@Override 
	        public void handleMessage(Message msg) { 
	            // TODO Auto-generated method stub 
	            super.handleMessage(msg); 
	            switch(msg.what) 
	            { 
	            case 0://���յ���Ϣʱ���Խ�����и��� 
	            	if(isfromaddnew){
	            		//��AddNew��PersonDetalʱӦִ�д˲���
	        			Bundle bundle1=getIntent().getExtras();
	        			btnid=bundle1.getInt("ID"); //֪����������ĸ�imageview
	        			countpg=bundle1.getInt("CountPage");
	        			image_x=bundle1.getInt("POSITION_X");
	        			image_y=bundle1.getInt("POSITION_Y"); 		        			
	        		}
	        		mydb=createOrOpenDatabase();
	        		Cursor cur=mydb.query(tablename, new String[] {ID,NAME,QQ,PHONE,EMAIL,IMAGEDATA,PHOTOFRAME,POSITION_X,POSITION_Y,BACKGROUND,PHOTOSTYLE}, null, null, null, null, null);	        		
	        		int count=cur.getCount();	        		
	        		if(cur!=null && count>=0){
	        			if(cur.moveToFirst()){
	        				do{	        					
	        					int temid=cur.getInt(0);       
	        					if(((countpg-1)*10+btnid)==temid){	        							        			        						
	        						imgpath=cur.getString(5);
	        						getframe(cur.getInt(6));
	        						getbitmap(cur.getInt(10));	        
	        						editname.setText(cur.getString(1));
	        						editqq.setText(cur.getString(2));
	        						editphone.setText(cur.getString(3));
	        						editemail.setText(cur.getString(4));
	        					}
	        				}while(cur.moveToNext());
	        			}
	        		}
	        		cur.close(); 
	                break; 
	            case 1:
	            	initPopuptWindow();
	            	popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);
	            	break;
	            } 
	        } 
	    };
	
	
		protected void initPopuptWindow() {
			View popupWindow_view = getLayoutInflater().inflate(R.layout.choosewhat, null,
					false);
			// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
			popupWindow = new PopupWindow(popupWindow_view, intScreenX, intScreenY/3, true);
			//popupWindow=new PopupWindow(popupWindow_view);
			// ���ö���Ч��
			popupWindow.setAnimationStyle(R.style.FromBottom);
			//��������ط���ʧ		
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
			// pop.xml��ͼ����Ŀؼ�
			Button render = (Button) popupWindow_view.findViewById(R.id.btnrender);
			Button changephoto = (Button) popupWindow_view.findViewById(R.id.btnchangephoto);
			Button changeframe = (Button) popupWindow_view.findViewById(R.id.btnchangeframe);
			
			// ����
			render.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// �������ִ����ز���
					Log.i(TAG, "this is ����");
					// �Ի�����ʧ
					popupWindow.dismiss();
					choosestring="����";
					popanother();
					popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);									}
			});
			// ����ͷ��
			changephoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// �������ִ����ز���
					Log.i(TAG, "this is ����ͷ��");
					popupWindow.dismiss();
					choosestring="ͷ��";
					popanother();
					popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);				
				}
			});
			// �������
			changeframe.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// �������ִ����ز���
					Log.i(TAG, "this is �������");
					popupWindow.dismiss();
					choosestring="���";
					popanother();
					popupWindow.showAtLocation(findViewById(R.id.btndone),Gravity.BOTTOM, 0, 0);
				}
			});

		}
		
		private void getbitmap(int arg0){
		   Bitmap temBitmap;
 		   Bitmap firstBitmap=getDiskBitmap(imgpath, 400, 400);
 		   switch (arg0)
			{
			case 0:
				//ԭͼ
				imgview.setImageBitmap(getDiskBitmap(imgpath, 400, 400));
				photonum=0;
				break;
			case 1:
				//����
				temBitmap = ImageUtil.oldRemeber(firstBitmap);
				imgview.setImageBitmap(temBitmap);
				photonum=1;
				break;
			case 2:
				//����
				temBitmap = ImageUtil.blurImageAmeliorate(firstBitmap);
				imgview.setImageBitmap(temBitmap);	
				photonum=2;
				break;
			case 3:
				//����
				temBitmap = ImageUtil.sketch(firstBitmap);
				imgview.setImageBitmap(temBitmap);
				photonum=3;
				break;
			case 4:
				//����
				temBitmap = ImageUtil.emboss(firstBitmap);
				imgview.setImageBitmap(temBitmap);
				photonum=4;
				break;
			case 5:
				//����
				temBitmap = ImageUtil.sunshine(firstBitmap, firstBitmap.getWidth() / 2, firstBitmap.getHeight() / 2);
				imgview.setImageBitmap(temBitmap);
				photonum=5;
				break;
			case 6:
				//����
				temBitmap = ImageUtil.sharpenImageAmeliorate(firstBitmap);
				imgview.setImageBitmap(temBitmap);
				photonum=6;
				break;
			case 7:
				//��Ƭ
				temBitmap = ImageUtil.film(firstBitmap);
				imgview.setImageBitmap(temBitmap);
				photonum=7;
				break;
			case 8:
				//��ת
				temBitmap = ImageUtil.reverseBitmap(firstBitmap,0);
				imgview.setImageBitmap(temBitmap);
				photonum=8;
				break;						
			default:
				imgview.setImageBitmap(getDiskBitmap(imgpath, 400, 400));
				photonum=0;
				break;
			}
		}
		
		private void getframe(int arg0){
	 		   switch (arg0)
				{
				case 0:
					//ԭ���
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
		
		private void popanother(){
			if(choosestring.equals("����")){
				View popupWindow_view = getLayoutInflater().inflate(R.layout.photostyle, null,
						false);
				popupWindow=null;
				popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
				// ���ö���Ч��
				
				popupWindow.setAnimationStyle(R.style.FromBottom);
				
				//��������ط���ʧ	
				
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
			        	generateDataList(), //����List
			        	R.layout.photo_row, new String[]{"col1"}, //�����б�
			        	new int[]{R.id.photo01}//�ж�Ӧ�ؼ�id�б�
			        );
				gv.setAdapter(sca);	
				gv.setOnItemClickListener( //����ѡ������ļ�����
			        	new OnItemClickListener(){
			        	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {//��дѡ������¼��Ĵ�����			        		 
			        		   getbitmap(arg2);
			        		   popupWindow.dismiss();
			        	   }        	   
			           	}
			        );        
			}	
			if(choosestring.equals("���")){
				View popupWindow_view = getLayoutInflater().inflate(R.layout.framestyle, null,
						false);
				popupWindow=null;
				popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
				// ���ö���Ч��
				
				popupWindow.setAnimationStyle(R.style.FromBottom);
				
				//��������ط���ʧ	
				
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
			        	generateDataList(), //����List
			        	R.layout.frame_row, new String[]{"col1"}, //�����б�
			        	new int[]{R.id.frame01}//�ж�Ӧ�ؼ�id�б�
			        );
				gv.setAdapter(sca);
				gv.setOnItemClickListener( //����ѡ������ļ�����
			        	new OnItemClickListener(){
			        	   public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
								long arg3) {//��дѡ������¼��Ĵ�����			        		 
			        		   getframe(arg2);
			        		   popupWindow.dismiss();
			        	   }        	   
			           	}
			        );        
			}
			if(choosestring.equals("ͷ��")){
				Log.i(TAG, "jinruletouxiangduan");
				View popupWindow_view = getLayoutInflater().inflate(R.layout.choosepicture, null,
						false);
				Log.i(TAG, "jinruletouxiangduan1");
				// ����PopupWindowʵ��,200,150�ֱ��ǿ�Ⱥ͸߶�
				popupWindow = new PopupWindow(popupWindow_view, intScreenX, intScreenY/4, true);
				//popupWindow=new PopupWindow(popupWindow_view);
				// ���ö���Ч��
				popupWindow.setAnimationStyle(R.style.FromBottom);
				//��������ط���ʧ		
				Log.i(TAG, "jinruletouxiangduan2");
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
				Log.i(TAG, "jinruletouxiangduan3");
				// pop.xml��ͼ����Ŀؼ�
				Button btnfromgallery = (Button) popupWindow_view.findViewById(R.id.btnfromgallery);
				Button btnfromcamera = (Button) popupWindow_view.findViewById(R.id.btnfromcamera);
				
				
				// �����ѡͼ
				btnfromgallery.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// �������ִ����ز���
						popupWindow.dismiss();			
						Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
						getImage.addCategory(Intent.CATEGORY_OPENABLE);
						getImage.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(getImage, 0);
					}
				});
				// ����ͷ��
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
		
		public List<? extends Map<String, ?>> generateDataList(){
	    	ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();;
	    	int rowCounter=9;//�õ���������
	    	if(choosestring.equals("����")){
	    		for(int i=0;i<rowCounter;i++){//ѭ������ÿ�еİ�����Ӧ���������ݵ�Map��col1��col2��col3Ϊ����
		    		HashMap<String,Object> hmap=new HashMap<String,Object>();
		    		hmap.put("col1", drawablephoto[i]);   //��һ��ΪͼƬ 		
		    		list.add(hmap);
		    	}    	
	    	}
	    	else if(choosestring.equals("���")){
	    		for(int i=0;i<rowCounter;i++){//ѭ������ÿ�еİ�����Ӧ���������ݵ�Map��col1��col2��col3Ϊ����
		    		HashMap<String,Object> hmap=new HashMap<String,Object>();
		    		hmap.put("col1", drawableframe[i]);   //��һ��ΪͼƬ 		
		    		list.add(hmap);
		    	}    	
	    	}
	    	return list;
		}

	    private Bitmap getImageThumbnail(String imagePath, int width, int height) {  
	        Bitmap bitmap = null;  
	        BitmapFactory.Options options = new BitmapFactory.Options();  
	        options.inJustDecodeBounds = true;  
	        // ��ȡ���ͼƬ�Ŀ�͸ߣ�ע��˴���bitmapΪnull   
	        bitmap = BitmapFactory.decodeFile(imagePath, options);  
	        options.inJustDecodeBounds = false; // ��Ϊ false   
	        // �������ű�   
	        int h = options.outHeight;  
	        int w = options.outWidth;
	        
	        int beWidth = w / width;  
	        int beHeight = h / height;  
	        int be = 1;  
	        if (beWidth>1||beHeight>1) {  
	            be = beWidth;  
	        } 
	        options.inSampleSize = be;
	        
	        // ���¶���ͼƬ����ȡ���ź��bitmap��ע�����Ҫ��options.inJustDecodeBounds ��Ϊ false   
	        bitmap = BitmapFactory.decodeFile(imagePath, options);
	        return Bitmap.createScaledBitmap(bitmap, width, height, true);
	    }  
	    
	    /* ������ͼƬתΪBitmap */
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
	    
	  private void popwindow() 
	   { 
	        //�������� 
	       new Thread() 
	        { 
	             public void run() 
	             { 	               	                         			
	               mHandler.sendEmptyMessage(1);	                       
	                } 
	         }.start(); 
	    }
	    
	    
	private void updateByMessage() 
    { 
        //�������� 
         new Thread() 
         { 
                public void run() 
                { 
                	long v=Thread.currentThread().getId();           		          			
           			mHandler.sendEmptyMessage(0);          	              
                } 
         }.start(); 
    }
	

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		//����Ǵ�ͼ�����������أ���frompicorcamera=true����ִ������ķ���
		Log.i(TAG, "frompicorcamera="+frompicorcamera);
		if(!frompicorcamera){
			long i=Thread.currentThread().getId();
			Log.i(TAG, "bthread="+i);
			updateByMessage();
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
			finishthis();
		}
		return true;
	}
	
}
