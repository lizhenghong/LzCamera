package cn.listudio.lzcamera;

import java.io.IOException;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class CamActivity extends Activity {

	SurfaceView surfaceView;
	Camera camera;
	boolean isPreview = false;
	SurfaceHolder surfaceHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		surfaceView = (SurfaceView) findViewById(R.id.camera_surfaceView);
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
				// 设置预览照片的大小
			//	parameters.setPreviewSize(screenWidth, screenHeight);
				// 设置预览照片时每秒显示多少帧的最小值和最大值
				parameters.setPreviewFpsRange(4, 10);
				// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 85);
				// 设置照片的大小
		//		parameters.setPictureSize(screenWidth, screenHeight);
				// 通过SurfaceView显示取景画面
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
