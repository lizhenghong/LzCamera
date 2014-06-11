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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class CamActivity extends Activity {

	SurfaceView surfaceView;
	Camera camera;
	boolean isPreview = false;
	SurfaceHolder surfaceHolder;
	Handler handler;
	Timer timer;
	ImageView imageView;
	ViewGroup _root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		imageView = (ImageView)findViewById(R.id.focus_button);
		imageView.getBackground().setAlpha(200);	
		 _root = (ViewGroup) findViewById(R.id.cameraframe);  

		surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceView);		
		surfaceView.setOnTouchListener(new OnTouchListener() {
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
			
				if (event.getAction() == MotionEvent.ACTION_DOWN)
			    {
			        float x = event.getX();
			        float y = event.getY();
			        
			        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageView.getLayoutParams();  
			        layoutParams.leftMargin = (int)x;  
			        layoutParams.topMargin = (int)y;  
			         
			        //layoutParams.rightMargin = layoutParams.leftMargin + width;  
			         // layoutParams.bottomMargin = layoutParams.topMargin + height;  
			        imageView.setLayoutParams(layoutParams);  			        
			       imageView.setLayoutParams(layoutParams);  
			       _root.invalidate();
			    }
				
			    if (event.getAction() == MotionEvent.ACTION_UP)
			    {
			        float x = event.getX();
			        float y = event.getY();
			        float touchMajor = event.getTouchMajor();
			        float touchMinor = event.getTouchMinor();		    	
			  
			        Rect rect = new Rect();
			        rect.set((int)((x - touchMajor/2)* 2000 / surfaceView.getWidth() - 1000),
			        		(int)((y - touchMajor/2) * 2000 / surfaceView.getHeight() - 1000),
			        		(int)((x + touchMajor/2) * 2000 / surfaceView.getWidth() - 1000),
			        		(int)((y + touchMajor/2) * 2000 / surfaceView.getHeight() - 1000));

			  //      Log.i("cameraArea rect","(" + rect.left + "," + rect.top + "," + rect.right + ","+ rect.bottom+")");
			        Camera.Area cameraArea = new Camera.Area(rect,1000);
					List<Camera.Area> listArea = new 	ArrayList<Camera.Area>();
					listArea.add(cameraArea);
					params.setFocusAreas(listArea);
					camera.setParameters(params);
					camera.autoFocus(null);
			    }				
				return true;
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

				//if (msg.what == 1) {
				//	camera.autoFocus(null);
			//	}
				return true;
			}

		});

	//	timer = new Timer(true);
	//	timer.schedule(task, 5000, 5000);
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
			
			camera.autoFocus(autoFocusCallback);
			//camera.startPreview();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback()
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
				    
				   //     targetFile.listFiles()
				        
				        FileOutputStream outStream = null;
						try {
							// 打开指定文件对应的输出流
							outStream = new FileOutputStream(targetFile);
							// 把位图输出到指定文件中
							Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
									data.length);
							bm.compress(CompressFormat.JPEG, 100, outStream);
							outStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
				        
				       // 
				        
				    //    OutputStream stream = new OutputStream(targetDirector);
				        camera.startPreview();
				}
			});
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

	// 自动对焦不要只放在tiemr中，还要放在手动点击，或传感器检测手机运动幅度后。
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

}
