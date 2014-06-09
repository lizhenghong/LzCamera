package cn.listudio.lzcamera;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceView;
public class CamActivity extends Activity {
	
	SurfaceView surfaceView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		surfaceView = (SurfaceView)findViewById(R.id.camera_surfaceView);
		
	}
}
