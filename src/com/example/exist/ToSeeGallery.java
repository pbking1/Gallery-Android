package com.example.exist;

import static com.example.exist.DetalPerson.bottonid;
import static com.example.exist.DetalPerson.imgpath;
import static com.example.gallery.StartNew.intScreenX;
import static com.example.gallery.StartNew.intScreenY;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.example.gallery.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.gallery.ExitApplication;
import com.example.gallery.PersonDetal;
import static com.example.exist.HaveExist.*;

@SuppressWarnings("deprecation")
public class ToSeeGallery extends Activity implements OnPageChangeListener
{

	private Button btnback;
	private ImageView[][] img=new ImageView[30][30];
	private int intWidth; //����ͼ�Ŀ��
	private int intHeight; //����ͼ�ĸ߶�
	private LayoutInflater mInflater;
	private AbsoluteLayout listview;
	private View layout;
	private SQLiteDatabase mdb;
	private String tablename;
	private ArrayList<View> views;
	private ViewPager mViewPager;
	private final static String ID="_id";
	private final static String NAME="name";
	private final static String QQ="qq";
	private final static String PHONE="phone";
	private final static String EMAIL = "email";
	private final static String IMAGEDATA ="imagedata";
	private final static String PHOTOFRAME="photoframe"; //���
	private final static String POSITION_X="position_x"; //ͼƬ��λ��x����
	private final static String POSITION_Y="position_y"; //ͼƬ��λ��y����
	private final static String BACKGROUND="background";
	private static final String PHOTOSTYLE ="photostyle";
	private int position_x[][]=new int[10][10]; //��¼��Ƭ��x����
	private int position_y[][]=new int[10][10]; //��¼��Ƭ��y����
	public static final String TAG = "woai";
	int countlayout=0;
	static int[] lastposition=new int[10];	
	static int[] firstposition=new int[10];
	private Bitmap bmpout;
	private byte[][] imgda=new byte[1024][100];
	static String[] layoutshunxu=new String[10];
	static int marknum=0; //Ĭ�ϴ��ڵ�һҳ
	static int pagenum;//һ���ж���ҳ
	private int pagenumber;//һ���ж���ҳ
	private String[] curpicture=new String[30]; //��¼ҳ��ı���ͼ
	private boolean longtouch=false;
	private PopupWindow popupWindow;
	private String[] imagepath=new String[100];
	int[] bgIds={R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,R.drawable.bg4,R.drawable.bg5,
			R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,R.drawable.bg9,R.drawable.bg10};
	int[] drawableIds=
		{R.drawable.sbg1,R.drawable.sbg2,R.drawable.sbg3,R.drawable.sbg4,R.drawable.sbg5,R.drawable.sbg6,R.drawable.sbg7,R.drawable.sbg8,R.drawable.sbg9,R.drawable.sbg10,R.drawable.camera,R.drawable.album};
	private Button btnedit;
	private boolean editpage=false;//editpage��ʼ��Ϊfalse�����������ƶ�ͼƬ��
	private int editclick=0;//��¼���edit��ť�Ĵ���
	protected int qiannum;//ǰһҳ�ĺ���
	private String wallbg="0";//Ĭ�ϵı�ֽ
	private ImageButton morepaper;
	private TextView titlename;
	private boolean frompictureorcamera=false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //����ȫ����ʾ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//��������ģʽ
		setContentView(R.layout.list_item_two);
		titlename=(TextView)findViewById(R.id.gallerytext);
		views = new ArrayList<View>();
		mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		checksql(); //��ʼ�������ݿ�
		mViewPager = (ViewPager)findViewById(R.id.whatsnew_viewpager); 
		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int arg0)
			{
				qiannum=marknum;
				marknum=arg0;//marknum��ʾ��ǰ��ҳ��
				views.add(qiannum, getview(qiannum));
				views.remove(qiannum+1);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0)
			{
							
			}
		});
		morepaper=(ImageButton)findViewById(R.id.morewallpaper);						
		morepaper.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//ҪeditpageΪtureʱ���ܱ༭����ͼƬ
				if(editpage){
					initPopuptWindow();
					popupWindow.showAtLocation(findViewById(R.id.btnback), Gravity.BOTTOM, 0,0);
				}
				
			}
		});
		btnback=(Button)findViewById(R.id.btnback);
		btnback.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				toHaveExist(); //����HaveExist.class				
			}
		});
		btnedit=(Button)findViewById(R.id.btnedit);
		btnedit.setOnClickListener(new OnClickListener()
		{					
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				editclick++;
				if(editclick%2==0){
					savedata();//�����ݱ��浽���ݿ⣬������ǰҳ��ı����͵�ǰҳ���ͼƬ��λ��
					editpage=false;
					btnedit.setText("�༭");
				}
				else{
					editpage=true; //Ҫeditpage=trueʱ�����ƶ�ͼ��͸�����ֽ
					btnedit.setText("����");
				}
				
			}
		});
		
		//���ViewPager������������
        PagerAdapter mPagerAdapter = new PagerAdapter() {     
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return views.size();
			}
						
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
							
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				return views.get(position);
			}
		};
		
		mViewPager.setAdapter(mPagerAdapter);
		if(totoseegallery){
			Bundle bundle=getIntent().getExtras();
			int position=bundle.getInt("MARKNUM");
			mViewPager.setCurrentItem(position);
		}		
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
            	BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(R.drawable.add_mate_icon);
        		bitmap = Bitmap.createScaledBitmap(bd.getBitmap(), width, height, true);      		
            }
        } catch (Exception e) {    
        }
        return bitmap;
    }
	
		
	@SuppressWarnings("deprecation")
	private View getview(int arg0){
		layout=null;
		layout = (AbsoluteLayout) mInflater.inflate(R.layout.tenphotos2, null);
		AbsoluteLayout abslayout=(AbsoluteLayout)layout.findViewById(R.id.abslayout);
		if(curpicture[arg0].length()>1){
			//˵������ͼ�Ǵ����������յõ���
			Bitmap bp=getDiskBitmap(curpicture[arg0], intScreenX,intScreenY);
			Drawable drawable = new BitmapDrawable(bp);
			abslayout.setBackground(drawable);
		}
		else{
			//����Ӧ�ı���ͼ���ַ���ת��Ϊ����
			int picid=Integer.parseInt(curpicture[arg0]);
			abslayout.setBackgroundResource(bgIds[picid]);
		}
		img[arg0][1]=(ImageView)layout.findViewById(R.id.img1);
		img[arg0][2]=(ImageView)layout.findViewById(R.id.img2);
		img[arg0][3]=(ImageView)layout.findViewById(R.id.img3);
		img[arg0][4]=(ImageView)layout.findViewById(R.id.img4);
		img[arg0][5]=(ImageView)layout.findViewById(R.id.img5);
		img[arg0][6]=(ImageView)layout.findViewById(R.id.img6);
		img[arg0][7]=(ImageView)layout.findViewById(R.id.img7);
		img[arg0][8]=(ImageView)layout.findViewById(R.id.img8);
		img[arg0][9]=(ImageView)layout.findViewById(R.id.img9);
		img[arg0][10]=(ImageView)layout.findViewById(R.id.img10);
		intWidth=100;
		intHeight=100;
		
		/**10��imageview�ĳ�ʼλ��**/
		img[arg0][1].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,20) ); 
		img[arg0][2].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,20) ); 
		img[arg0][3].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,40+intHeight) ); 
		img[arg0][4].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,40+intHeight) ); 
		img[arg0][5].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,60+2*intHeight) ); 
		img[arg0][6].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,60+2*intHeight) ); 
		img[arg0][7].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,80+3*intHeight) ); 
		img[arg0][8].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,80+3*intHeight) ); 
		img[arg0][9].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,30,100+4*intHeight) );
		img[arg0][10].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,intScreenX-intWidth-30,100+4*intHeight) );
		
		/**�������ݿ�õ������ݶ�����imageview��λ��**/
		if(!(position_x[arg0][0]==1000 && position_y[arg0][0]==1000)){
			img[arg0][1].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][0]-50,position_y[arg0][0]-50) );			
		}
		if(!(position_x[arg0][1]==1000 && position_y[arg0][1]==1000)){
			img[arg0][2].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][1]-50,position_y[arg0][1]-50) ); 
		}
		if(!(position_x[arg0][2]==1000 && position_y[arg0][2]==1000)){
			img[arg0][3].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][2]-50,position_y[arg0][2]-50) ); 
		}
		if(!(position_x[arg0][3]==1000 && position_y[arg0][3]==1000)){
			
			img[arg0][4].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][3]-50,position_y[arg0][3]-50) ); 
		}
		if(!(position_x[arg0][4]==1000 && position_y[arg0][4]==1000)){
			
			img[arg0][5].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][4]-50,position_y[arg0][4]-50) ); 
		}
		if(!(position_x[arg0][5]==1000 && position_y[arg0][5]==1000)){
			
			img[arg0][6].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][5]-50,position_y[arg0][5]-50) ); 
		}
		if(!(position_x[arg0][6]==1000 && position_y[arg0][6]==1000)){
			
			img[arg0][7].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][6]-50,position_y[arg0][6]-50) ); 
		}
		if(!(position_x[arg0][7]==1000 && position_y[arg0][7]==1000)){
			
			img[arg0][8].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][7]-50,position_y[arg0][7]-50) ); 
		}
		if(!(position_x[arg0][8]==1000 && position_y[arg0][8]==1000)){
			
			img[arg0][9].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][8]-50,position_y[arg0][8]-50) ); 
		}
		if(!(position_x[arg0][9]==1000 && position_y[arg0][9]==1000)){
			
			img[arg0][10].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[arg0][9]-50,position_y[arg0][9]-50) ); 
		}
		img[arg0][1].setImageBitmap(getDiskBitmap(imagepath[arg0*10+0], 80, 80));
		img[arg0][2].setImageBitmap(getDiskBitmap(imagepath[arg0*10+1], 80, 80));
		img[arg0][3].setImageBitmap(getDiskBitmap(imagepath[arg0*10+2], 80, 80));
		img[arg0][4].setImageBitmap(getDiskBitmap(imagepath[arg0*10+3], 80, 80));
		img[arg0][5].setImageBitmap(getDiskBitmap(imagepath[arg0*10+4], 80, 80));
		img[arg0][6].setImageBitmap(getDiskBitmap(imagepath[arg0*10+5], 80, 80));
		img[arg0][7].setImageBitmap(getDiskBitmap(imagepath[arg0*10+6], 80, 80));
		img[arg0][8].setImageBitmap(getDiskBitmap(imagepath[arg0*10+7], 80, 80));
		img[arg0][9].setImageBitmap(getDiskBitmap(imagepath[arg0*10+8], 80, 80));
		img[arg0][10].setImageBitmap(getDiskBitmap(imagepath[arg0*10+9], 80, 80));
	
		img[arg0][1].setOnLongClickListener(l);
		img[arg0][2].setOnLongClickListener(l);
		img[arg0][3].setOnLongClickListener(l);
		img[arg0][4].setOnLongClickListener(l);
		img[arg0][5].setOnLongClickListener(l);
		img[arg0][6].setOnLongClickListener(l);
		img[arg0][7].setOnLongClickListener(l);
		img[arg0][8].setOnLongClickListener(l);
		img[arg0][9].setOnLongClickListener(l);
		img[arg0][10].setOnLongClickListener(l);
		img[arg0][1].setOnTouchListener(listener2);
		img[arg0][2].setOnTouchListener(listener2);
		img[arg0][3].setOnTouchListener(listener2);
		img[arg0][4].setOnTouchListener(listener2);
		img[arg0][5].setOnTouchListener(listener2);
		img[arg0][6].setOnTouchListener(listener2);
		img[arg0][7].setOnTouchListener(listener2);
		img[arg0][8].setOnTouchListener(listener2);
		img[arg0][9].setOnTouchListener(listener2);
		img[arg0][10].setOnTouchListener(listener2);
		img[arg0][1].setOnClickListener(listener);
		img[arg0][2].setOnClickListener(listener);
		img[arg0][3].setOnClickListener(listener);
		img[arg0][4].setOnClickListener(listener);
		img[arg0][5].setOnClickListener(listener);
		img[arg0][6].setOnClickListener(listener);
		img[arg0][7].setOnClickListener(listener);
		img[arg0][8].setOnClickListener(listener);
		img[arg0][9].setOnClickListener(listener);
		img[arg0][10].setOnClickListener(listener);
		return layout;
		
	}
	
	private OnClickListener listener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if((!longtouch) && (!editpage)){
				switch (v.getId())
				{
				case R.id.img1:
					ToDetalPerson(1,position_x[marknum][0] , position_y[marknum][0]);
					break;
				case R.id.img2:
					ToDetalPerson(2,position_x[marknum][1] , position_y[marknum][1]);
					break;
				case R.id.img3:
					ToDetalPerson(3,position_x[marknum][2] , position_y[marknum][2]);
					break;
				case R.id.img4:
					ToDetalPerson(4,position_x[marknum][3] , position_y[marknum][3]);
					break;
				case R.id.img5:
					ToDetalPerson(5,position_x[marknum][4] , position_y[marknum][4]);
					break;
				case R.id.img6:
					ToDetalPerson(6,position_x[marknum][5] , position_y[marknum][5]);
					break;
				case R.id.img7:
					ToDetalPerson(7,position_x[marknum][6] , position_y[marknum][6]);
					break;
				case R.id.img8:
					ToDetalPerson(8,position_x[marknum][7] , position_y[marknum][7]);
					break;
				case R.id.img9:
					ToDetalPerson(9,position_x[marknum][8] , position_y[marknum][8]);
					break;
				case R.id.img10:
					ToDetalPerson(10,position_x[marknum][9] , position_y[marknum][9]);
					break;
				default:
					break;
				}
			}
			
		}
	};
	
	/*����PopupWindow */
	public void initPopuptWindow() {
		// TODO Auto-generated method stub
		// ��ȡ�Զ��岼���ļ�pop.xml����ͼ
		View popupWindow_view = getLayoutInflater().inflate(R.layout.choosewallpaper, null,
				false);
		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		// ���ö���Ч��
		
		popupWindow.setAnimationStyle(R.style.FromBottom);
		
		//��������ط���ʧ	
		
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
	        	generateDataList(), //����List
	        	R.layout.grid_row, new String[]{"col1"}, //�����б�
	        	new int[]{R.id.ImageView01}//�ж�Ӧ�ؼ�id�б�
	        );
		gv.setAdapter(sca);	
		gv.setOnItemClickListener( //����ѡ������ļ�����
	        	new OnItemClickListener(){	        

				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {//��дѡ������¼��Ĵ�����
					popupWindow.dismiss();
					listview=null;
					listview=(AbsoluteLayout)(views.get(marknum)).findViewById(R.id.abslayout);
	        		 switch (arg2)
					{
	        		 //ѡ��Ĭ�ϵı�ֽ
					case 0:
						listview.setBackgroundResource(bgIds[0]);
						wallbg="0";
						break;
					//ѡ���һ�ű�ֽ
					case 1:
						listview.setBackgroundResource(bgIds[1]);
						wallbg="1";
						break;
					//ѡ��ڶ��ű�ֽ
					case 2:
						listview.setBackgroundResource(bgIds[2]);
						wallbg="2";
						break;
					//ѡ������ű�ֽ
					case 3:
						listview.setBackgroundResource(bgIds[3]);
						wallbg="3";
						break;
					//ѡ������ű�ֽ
					case 4:
						listview.setBackgroundResource(bgIds[4]);
						wallbg="4";
						break;
					//ѡ������ű�ֽ
					case 5:
						listview.setBackgroundResource(bgIds[5]);
						wallbg="5";
						break;
					//ѡ������ű�ֽ
					case 6:
						listview.setBackgroundResource(bgIds[6]);
						wallbg="6";
						break;
					//ѡ������ű�ֽ
					case 7:
						listview.setBackgroundResource(bgIds[7]);
						wallbg="7";
						break;
					//ѡ��ڰ��ű�ֽ
					case 8:
						listview.setBackgroundResource(bgIds[8]);
						wallbg="8";
						break;
					//ѡ��ھ��ű�ֽ
					case 9:
						listview.setBackgroundResource(bgIds[9]);
						wallbg="10";
						break;
					//����ѡ���ֽ
					case 10:
						Intent intentCam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intentCam, 1);
						break;
					//�����ѡ���ֽ
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
				wallbg=getFilePathFromUri(uri); //��·���浽wallbg
			} catch ( Exception e )
			{
			}

		} else if ( resultCode == Activity.RESULT_OK && requestCode == 1 && data!=null)
		{
				Bundle extras = data.getExtras();
				if(extras!=null){
					Bitmap new_bitmap = (Bitmap) extras.get("data");
					String saveFilePath = "";
					try {
						File sdCardDir = Environment.getExternalStorageDirectory();
						File filePath = new File(sdCardDir.getAbsolutePath() + "/mygallery" );
						if(!filePath.exists()){
							filePath.mkdirs();
							Log.i(TAG, "�Ѿ�������·��");
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
		frompictureorcamera=true;
	}

	
	public String filename(){
		Random random = new Random(System.currentTimeMillis());
		java.util.Date dt = new java.util.Date(System.currentTimeMillis()); 
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss"); 
		String fileName = fmt.format(dt)+ Math.abs(random.nextInt()) % 100000; 		
		return fileName;
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
	
	public List<? extends Map<String, ?>> generateDataList(){
    	ArrayList<Map<String,Object>> list=new ArrayList<Map<String,Object>>();;
    	int rowCounter=12;//�õ���������
    	for(int i=0;i<rowCounter;i++){//ѭ������ÿ�еİ�����Ӧ���������ݵ�Map��col1��col2��col3Ϊ����
    		HashMap<String,Object> hmap=new HashMap<String,Object>();
    		hmap.put("col1", drawableIds[i]);   //��һ��ΪͼƬ 		
    		list.add(hmap);
    	}    	
    	return list;
	}
	
	//�������Ὣ���ݱ��浽���ݿ�
	private void savedata(){
		for(int i=0;i<10;i++){
			ContentValues cValues=new ContentValues();
			cValues.put(POSITION_X, position_x[marknum][i]);
			cValues.put(POSITION_Y, position_y[marknum][i]);
			cValues.put(BACKGROUND, wallbg);
			String whereClause="_id=?";
			String[] whereArgs={(marknum*10+i+1+"")};
			mdb.update(tablename, cValues, whereClause, whereArgs);
			cValues=null;
		}
	}
	
	private void ToDetalPerson(int id,int x,int y){
		Intent intent=new Intent();
		intent.setClass(ToSeeGallery.this, DetalPerson.class);
		Bundle bundle=new Bundle();
		bundle.putInt("ID", id);
		bundle.putString("TableName", tablename);
		bundle.putInt("POSITION_X", x);
		bundle.putInt("POSITION_Y", y);
		bundle.putInt("MARKNUM", marknum);
		bundle.putString("BACKGROUND", curpicture[marknum]);	
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	private OnTouchListener listener2=new OnTouchListener()
	{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{				
				if(editpage){
					switch (v.getId())
					{
					case R.id.img1:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][0] = (int)(event.getRawX());  
			                position_y[marknum][0] = (int)(event.getRawY() - 50);                 
			                v.layout(position_x[marknum][0] - 50, position_y[marknum][0] - 50, position_x[marknum][0] + 50, position_y[marknum][0] + 50); 	     
						}
						break;
					case R.id.img2:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][1] = (int)(event.getRawX());  
			                position_y[marknum][1] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][1] - 50, position_y[marknum][1] - 50, position_x[marknum][1] + 50, position_y[marknum][1] + 50); 	  			              
						}
						break;
					case R.id.img3:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][2] = (int)(event.getRawX());  
			                position_y[marknum][2] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][2] - 50, position_y[marknum][2] - 50, position_x[marknum][2] + 50, position_y[marknum][2] + 50); 	  		                
						}
						break;
					case R.id.img4:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][3] = (int)(event.getRawX());  
			                position_y[marknum][3] = (int)(event.getRawY() - 50);
			                v.layout(position_x[marknum][3] - 50, position_y[marknum][3] - 50, position_x[marknum][3] + 50, position_y[marknum][3] + 50); 	  			                
						}
						break;
					case R.id.img5:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][4] = (int)(event.getRawX());  
			                position_y[marknum][4] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][4] - 50, position_y[marknum][4] - 50, position_x[marknum][4] + 50, position_y[marknum][4] + 50); 	  			                
						}
						break;
					case R.id.img6:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][5] = (int)(event.getRawX());  
			                position_y[marknum][5] = (int)(event.getRawY() - 50);
			                v.layout(position_x[marknum][5] - 50, position_y[marknum][5] - 50, position_x[marknum][5] + 50, position_y[marknum][5] + 50); 	  			                
						}
						break;
					case R.id.img7:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][6] = (int)(event.getRawX());  
			                position_y[marknum][6] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][6] - 50, position_y[marknum][6] - 50, position_x[marknum][6] + 50, position_y[marknum][6] + 50); 	  		                
						}
						break;
					case R.id.img8:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][7] = (int)(event.getRawX());  
			                position_y[marknum][7] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][7] - 50, position_y[marknum][7] - 50, position_x[marknum][7] + 50, position_y[marknum][7] + 50); 	  		                
						}
						break;
					case R.id.img9:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][8] = (int)(event.getRawX());  
			                position_y[marknum][8] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][8] - 50, position_y[marknum][8] - 50, position_x[marknum][8] + 50, position_y[marknum][8] + 50); 	  		               
						}
						break;
					case R.id.img10:
						if(event.getAction()==MotionEvent.ACTION_MOVE){
							position_x[marknum][9] = (int)(event.getRawX());  
			                position_y[marknum][9] = (int)(event.getRawY() - 50); 
			                v.layout(position_x[marknum][9] - 50, position_y[marknum][9] - 50, position_x[marknum][9] + 50, position_y[marknum][9] + 50); 	  			               
						}
						break;

					default:
						break;
					}
					longtouch=false; //����������ʹlongtouch=false
					return false;
					//ֻ��return false�Ż���Ӧ�����onclick�¼�����return true�򲻻���Ӧonclick�¼�
				}
				else{
					longtouch=false;
					return false; 
				} 
			}
		
	};
	
	//Ҫ�����ƶ�ʱ������Ҫ�ȳ����������Ͱ�onclick��ontouch�������
	private OnLongClickListener l=new OnLongClickListener()
	{

		@Override
		public boolean onLongClick(View v)
		{
			// TODO Auto-generated method stub
			longtouch=true;
			return true;
		}
	};
	
	
	/**
	 * �������ݿ�
	 */
	private void checksql(){
		Bundle bundle=getIntent().getExtras();
		tablename=bundle.getString("TABLENAME"); //��HaveExist.class�еõ������ı����
		titlename.setText(tablename);
		mdb=PersonDetal.createOrOpenDatabase();
		Cursor cursor=mdb.query(tablename, new String[]{ID,NAME,QQ,PHONE,EMAIL,IMAGEDATA,PHOTOFRAME,POSITION_X,POSITION_Y,BACKGROUND,PHOTOSTYLE}, null, null, null, null, null);
		int count=cursor.getCount();
		pagenumber=count/10;
		if(cursor!=null && count>=0){
			int i=0,j=0,num=0;
			if(cursor.moveToFirst()){
				do{		
					imagepath[num]=cursor.getString(5);
					Log.i(TAG, "imagepath["+i+"]="+imagepath[i]);
					Log.i(TAG, "imgda["+num+"]="+imgda[num]);
					position_x[j][i]=cursor.getInt(7);
					position_y[j][i]=cursor.getInt(8);
					curpicture[j]=cursor.getString(9); //�õ�ҳ��ı���ͼ����0��ʼ
					num++;
					if((i+1)==10){
						i=0;
						j++;
					}
					else{
						i++;
					}
				}while(cursor.moveToNext());
			}
		}
		for(int i=0;i<pagenumber;i++){
			if(i==0)
				views.add(getview(i));
			else {
				views.add(getview(i));
				
				
			}
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
            case 0://ֻ����DetalPerson����ʱ�Ż����
            	if(totoseegallery){
            		Log.i(TAG, "marknum="+marknum);
            		totoseegallery=false;
            		if(!(position_x[marknum][0]==1000 && position_y[marknum][0]==1000)){
            			img[marknum][1].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][0]-50,position_y[marknum][0]-50) );
            			
            		}
            		if(!(position_x[marknum][1]==1000 && position_y[marknum][1]==1000)){
            			Log.i(TAG, "position_x[1]="+position_x[marknum][1]);
            			Log.i(TAG, "position_y[1]="+position_y[marknum][1]);
            			img[marknum][2].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][1]-50,position_y[marknum][1]-50) ); 
            		}
            		if(!(position_x[marknum][2]==1000 && position_y[marknum][2]==1000)){
            			Log.i(TAG, "position_x[2]="+position_x[2]);
            			Log.i(TAG, "position_y[2]="+position_y[2]);
            			img[marknum][3].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][2]-50,position_y[marknum][2]-50) ); 
            		}
            		if(!(position_x[marknum][3]==1000 && position_y[marknum][3]==1000)){
            			
            			img[marknum][4].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][3]-50,position_y[marknum][3]-50) ); 
            		}
            		if(!(position_x[marknum][4]==1000 && position_y[marknum][4]==1000)){
            			
            			img[marknum][5].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][4]-50,position_y[marknum][4]-50) ); 
            		}
            		if(!(position_x[marknum][5]==1000 && position_y[marknum][5]==1000)){
            			
            			img[marknum][6].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][5]-50,position_y[marknum][5]-50) ); 
            		}
            		if(!(position_x[marknum][6]==1000 && position_y[marknum][6]==1000)){
            			
            			img[marknum][7].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][6]-50,position_y[marknum][6]-50) ); 
            		}
            		if(!(position_x[marknum][7]==1000 && position_y[marknum][7]==1000)){
            			
            			img[marknum][8].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][7]-50,position_y[marknum][7]-50) ); 
            		}
            		if(!(position_x[marknum][8]==1000 && position_y[marknum][8]==1000)){
            			
            			img[marknum][9].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][8]-50,position_y[marknum][8]-50) ); 
            		}
            		if(!(position_x[marknum][9]==1000 && position_y[marknum][9]==1000)){
            			
            			img[marknum][10].setLayoutParams ( new AbsoluteLayout.LayoutParams (intWidth,intHeight,position_x[marknum][9]-50,position_y[marknum][9]-50) ); 
            		}
            	}
        			if(bottonid==1){
        				Log.i(TAG, "this is img1");
        				img[marknum][1].setImageBitmap(getDiskBitmap(imgpath, 80, 80));   
        			}
        			else if(bottonid==2){
        				Log.i(TAG, "this is img2");
        				Log.i(TAG, "imgpath="+imgpath);
        				img[marknum][2].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==3){
        				Log.i(TAG, "this is img3");
        				img[marknum][3].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==4){
        				Log.i(TAG, "this is img4");
        				img[marknum][4].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==5){
        				Log.i(TAG, "this is img5");
        				img[marknum][5].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==6){
        				Log.i(TAG, "this is img6");
        				img[marknum][6].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==7){
        				Log.i(TAG, "this is img7");
        				img[marknum][7].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==8){
        				Log.i(TAG, "this is img8");
        				img[marknum][8].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==9){
        				Log.i(TAG, "this is img9");
        				img[marknum][9].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			else if(bottonid==10){
        				Log.i(TAG, "this is img10");
        				img[marknum][10].setImageBitmap(getDiskBitmap(imgpath, 80, 80));
        			}
        			break; 
        		}          
            } 
    };
    
    private void updatesql(){
    	int count=1;
    	for(int j=0;j<pagenumber;j++){
    		for(int i=0;i<10;i++){
    			ContentValues cValues=new ContentValues();
    			cValues.put(POSITION_X, position_x[j][i]);
    			cValues.put(POSITION_Y, position_y[j][i]);
    			String whereClause="_id=?";
    			String[] whereArgs={count+""};
    			mdb.update(tablename, cValues, whereClause, whereArgs);
    			count++;
    			cValues=null;
		}
    	}
	}
	
	private void updateByMessage() 
    { 
        //�������� 
         new Thread() 
         { 
                public void run() 
                {              
                    mHandler.sendEmptyMessage(0);                   
                } 
         }.start(); 
    }
	
	/**
	 * ��ת��HaveExist.class
	 */
	private void toHaveExist(){
		if(bmpout!=null && !bmpout.isRecycled()){
			bmpout.recycle();
			bmpout=null;
		}
		System.gc();
		Intent intent1=new Intent();
		intent1.setClass(this, HaveExist.class);
		startActivity(intent1);
		this.finish();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if(frompictureorcamera){
			
		}else{
			updateByMessage();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			toHaveExist();
		}
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0)
	{
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
		
	}

	@Override
	public void onPageSelected(int arg0)
	{
		
	}
}