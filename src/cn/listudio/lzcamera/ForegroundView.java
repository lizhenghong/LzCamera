package cn.listudio.lzcamera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class ForegroundView extends View {

	//如果是在xml中声明此控件，则必须有次构造函数，否则运行不起来，记住喽啊
	public ForegroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ForegroundView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint paint = new Paint();
		canvas.drawLine(11, 55, 66, 99, paint);
	}

}
