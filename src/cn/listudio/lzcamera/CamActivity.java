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
					// ������ͷ
					initCamera();
				}

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					// ���camera��Ϊnull ,�ͷ�����ͷ
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
			// �˴�Ĭ�ϴ򿪺�������ͷ��
			// ͨ������������Դ�ǰ������ͷ
			camera = Camera.open(0); // ��
			camera.setDisplayOrientation(90);
		}
		if (camera != null && !isPreview) {
			try {
				Camera.Parameters parameters = camera.getParameters();
				// ����Ԥ����Ƭ�Ĵ�С
			//	parameters.setPreviewSize(screenWidth, screenHeight);
				// ����Ԥ����Ƭʱÿ����ʾ����֡����Сֵ�����ֵ
				parameters.setPreviewFpsRange(4, 10);
				// ����ͼƬ��ʽ
				parameters.setPictureFormat(ImageFormat.JPEG);
				// ����JPG��Ƭ������
				parameters.set("jpeg-quality", 85);
				// ������Ƭ�Ĵ�С
		//		parameters.setPictureSize(screenWidth, screenHeight);
				// ͨ��SurfaceView��ʾȡ������
				camera.setPreviewDisplay(surfaceHolder); // ��
				// ��ʼԤ��
				camera.startPreview(); // ��
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
	}
}
