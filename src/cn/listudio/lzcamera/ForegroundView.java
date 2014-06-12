package cn.listudio.lzcamera;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ForegroundView extends View {
	
	Rect rect;
	StateFocus stateFocus;
	
	
	//如果是在xml中声明此控件，则必须有次构造函数，否则运行不起来，记住喽啊
	public ForegroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		rect = new Rect(0,0,0,0);
		stateFocus = StateFocus.FOCUSDONE;
	}

	public ForegroundView(Context context) {
		super(context);
		rect = new Rect(0,0,0,0);
		stateFocus = StateFocus.FOCUSDONE;
	}
	
	 public enum StateFocus{ FOCUSING,FOCUSED,FOCUSFAIL,FOCUSDONE}; 
	 
	public void setFocusPoint(Rect rect, StateFocus stateFocus)
	{
		this.rect = rect;
		this.stateFocus = stateFocus;
		invalidate();
	}	

	
	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		
		if(stateFocus == StateFocus.FOCUSDONE){
			//donoting maybe need clear
		}
		else
		{
			Paint paint = new Paint();		
			if(stateFocus == StateFocus.FOCUSING){
				paint.setColor(0xfff8);
				
			}
			else if(stateFocus == StateFocus.FOCUSED){
				paint.setColor(0x0f08);
				//timer
			}
			else if(stateFocus == StateFocus.FOCUSFAIL){
				paint.setColor(0xf008);
				//timer
			}
			canvas.drawRect(rect, paint);
		}
		
		Paint paint1 = new Paint();		
		canvas.drawLine(33, 66, 99, 123, paint1);
		canvas.drawRect(rect, paint1);
	}


	TimerTask timerTask = new TimerTask()
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			stateFocus = StateFocus.FOCUSDONE;
			invalidate();
		}		
	};
	
}
