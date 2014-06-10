package cn.listudio.lzcamera;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class CamActivity extends Activity {

	SurfaceView surfaceView;
	Camera camera;
	boolean isPreview = false;
	SurfaceHolder surfaceHolder;
	Handler handler;
	Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
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
				// TODO Auto-generated method stub

				//if (msg.what == 1) {
				//	camera.autoFocus(null);
			//	}
				return true;
			}

		});

		timer = new Timer(true);
		timer.schedule(task, 5000, 5000);
	}

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
				// 妈蛋不能用下面的设置，设置就报错，可能是手机不支持吧，所以lint才要强制加个try啊……
				// 设置预览照片的大小
				// parameters.setPreviewSize(100 ,200);
				// 设置预览照片时每秒显示多少帧的最小值和最大值
				// parameters.setPreviewFpsRange(4, 10);
				// 设置图片格式
				// parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				// parameters.set("jpeg-quality", 85);
				// 设置照片的大小
				// parameters.setPictureSize(100, 200);
				// 通过SurfaceView显示取景画面
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
