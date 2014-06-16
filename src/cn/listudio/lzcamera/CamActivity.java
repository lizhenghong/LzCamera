package cn.listudio.lzcamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CamActivity extends Activity {

	final int MESSAGEFOCUS = 0x0001;

	SurfaceView surfaceView;
	ImageView thumbNailImgView;
	ForegroundView foregroundView;
	Camera camera;
	boolean isPreview = false;
	SurfaceHolder surfaceHolder;
	Handler handler;
	ViewGroup _root;
	DisplayMetrics metrics;
	Rect pixelRect;//对焦区域，以pixel为单位
	private MyTimerTask timerTask;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		metrics = getResources().getDisplayMetrics();
		 _root = (ViewGroup) findViewById(R.id.cameraframe);  
		surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceView);
		foregroundView = (ForegroundView)findViewById(R.id.foreground_view);
		thumbNailImgView = (ImageView)findViewById(R.id.thumbnail_view);
		timerTask = new MyTimerTask();
		foregroundView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (!isPreview) {
					return true;
				}
				
				Camera.Parameters params = camera.getParameters();
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

				if (params.getMaxNumFocusAreas() == 0) {
					return true;
				}					
				
				if (event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP)
			    {
			        int x = (int)(event.getX());
			        int y = (int)event.getY();	
			        int rectWidth = metrics.widthPixels/4;
			        pixelRect =new  Rect(x-rectWidth/2,y-rectWidth/2,x+rectWidth/2,y+rectWidth/2);	
			        if(pixelRect.left <0) pixelRect.offset( - pixelRect.left, 0);
			        if(pixelRect.top < 0) pixelRect.offset(0,  - pixelRect.top);
			        if(pixelRect.right > surfaceView.getWidth()) pixelRect.offset(surfaceView.getWidth() - pixelRect.right, 0);
			        if(pixelRect.bottom >surfaceView.getHeight()) pixelRect.offset(0,surfaceView.getHeight() - pixelRect.bottom);
					        
			        if (event.getAction() == MotionEvent.ACTION_UP) //begin focus
				    {				    				  
				        Rect cameraRect = new Rect(pixelRect);
				        cameraRect.left = cameraRect.left*2000/surfaceView.getWidth()-1000;
				        cameraRect.right = cameraRect.right*2000/surfaceView.getWidth()-1000;
				        cameraRect.top = cameraRect.top*2000/surfaceView.getHeight()-1000;
				        cameraRect.bottom= cameraRect.bottom*2000/surfaceView.getHeight()-1000;

				        if(cameraRect.left < -1000) cameraRect.offset(-1000 - cameraRect.left, 0);
				        if(cameraRect.top < -1000) cameraRect.offset(0, -1000 - cameraRect.top);
				        if(cameraRect.right > 1000) cameraRect.offset(1000 - cameraRect.right, 0);
				        if(cameraRect.bottom >1000) cameraRect.offset(0,1000 - cameraRect.bottom);
				        
				        Camera.Area cameraArea = new Camera.Area(cameraRect,1000);
						List<Camera.Area> listArea = new 	ArrayList<Camera.Area>();
						listArea.add(cameraArea);
						params.setFocusAreas(listArea);
						params.setMeteringAreas(listArea);
						params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						camera.setParameters(params);
						foregroundView.setFocusPoint(pixelRect, ForegroundView.StateFocus.FOCUSING);
						camera.autoFocus(autoFocusCallback);
						
				    }				
			    }	
				return true;
			}
		});
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.shot_button);
		imageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera.autoFocus(autoFocusTakePictureCallback);
			}
			
		});
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// 打开摄像头
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// 如果camera不为null ,释放摄像头
				if (camera != null) {
					if (isPreview)
						camera.stopPreview();
					camera.release();
					camera = null;
				}
			}
		});
		
				
		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {

				if (msg.what == MESSAGEFOCUS) {
					foregroundView.setFocusPoint(pixelRect, ForegroundView.StateFocus.FOCUSDONE);
				}
				return true;
			}

		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {	
			
			camera.autoFocus(autoFocusTakePictureCallback);
			//camera.startPreview();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	AutoFocusCallback autoFocusTakePictureCallback = new AutoFocusCallback()
	{
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			camera.takePicture(null, null, new  PictureCallback()
			{
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					
				     String ExternalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath(); //SDCard Path
				        String targetPath = ExternalStorageDirectoryPath + "/LzCamera/pictures/ssssssss.jpg";       

				        File targetFile = new File(targetPath);
				        FileOutputStream outStream = null;
						try {
							// 打开指定文件对应的输出流
							outStream = new FileOutputStream(targetFile);
							// 把位图输出到指定文件中
							Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
									data.length);
							bm.compress(CompressFormat.JPEG, 100, outStream);
							outStream.close();
							
							thumbNailImgView.setImageBitmap(bm);
							
							
						} catch (IOException e) {
							e.printStackTrace();
						}
				        camera.startPreview();
				}
			});
		}		
	};
	
	
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback()
	{
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			
			if(success){
				foregroundView.setFocusPoint(pixelRect, ForegroundView.StateFocus.FOCUSED);
			}
			else{
				foregroundView.setFocusPoint(pixelRect, ForegroundView.StateFocus.FOCUSFAIL);
			}			
	
			Timer timer = new Timer(true);		
			if(timerTask!= null)
				timerTask.cancel();
			timerTask = new MyTimerTask();			
			timer.schedule(timerTask, 2000);
		}
	};
		
	class MyTimerTask extends TimerTask {
		public void run() {
			Message message = new Message();
			message.what = MESSAGEFOCUS;
			handler.sendMessage(message);					
		}
	};

	private void initCamera() {
		if (!isPreview) {
			// 此处默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			camera = Camera.open(0); // ①
			camera.setDisplayOrientation(90);
		}
		if (camera != null && !isPreview) {
			try {
				Camera.Parameters parameters = camera.getParameters();

				camera.setParameters(parameters);
				camera.setPreviewDisplay(surfaceHolder); // ②
				// 开始预览
				camera.startPreview(); // ③
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	

}
