package cn.listudio.lzcamera;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

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

		handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub

				if (msg.what == 1) {
					camera.autoFocus(null);
				}
				return true;
			}

		});

		timer = new Timer(true);
		timer.schedule(task, 5000, 5000);
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
				// �走��������������ã����þͱ����������ֻ���֧�ְɣ�����lint��Ҫǿ�ƼӸ�try������
				// ����Ԥ����Ƭ�Ĵ�С
				// parameters.setPreviewSize(100 ,200);
				// ����Ԥ����Ƭʱÿ����ʾ����֡����Сֵ�����ֵ
				// parameters.setPreviewFpsRange(4, 10);
				// ����ͼƬ��ʽ
				// parameters.setPictureFormat(ImageFormat.JPEG);
				// ����JPG��Ƭ������
				// parameters.set("jpeg-quality", 85);
				// ������Ƭ�Ĵ�С
				// parameters.setPictureSize(100, 200);
				// ͨ��SurfaceView��ʾȡ������
				camera.setParameters(parameters);
				camera.setPreviewDisplay(surfaceHolder); // ��
				// ��ʼԤ��
				camera.startPreview(); // ��
			} catch (Exception e) {
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	// �Զ��Խ���Ҫֻ����tiemr�У���Ҫ�����ֶ�������򴫸�������ֻ��˶����Ⱥ�
	TimerTask task = new TimerTask() {
		public void run() {
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};

}
