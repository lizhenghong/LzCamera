package cn.listudio.lzcamera;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
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
				paint.setColor(Color.WHITE);
				
			}
			else if(stateFocus == StateFocus.FOCUSED){
				paint.setColor(Color.GREEN);
				//timer
			}
			else if(stateFocus == StateFocus.FOCUSFAIL){
				paint.setColor(Color.RED);
				//timer
			}
			int rectWidth = rect.width();
			int len = rect.width() / 3;
			canvas.drawLine(rect.left,rect.top,rect.left+ len,rect.top,paint);
			canvas.drawLine(rect.right-len,rect.top,rect.right,rect.top,paint);
			
			canvas.drawLine(rect.left,rect.bottom,rect.left+ len,rect.bottom,paint);
			canvas.drawLine(rect.right-len,rect.bottom,rect.right,rect.bottom,paint);
			
			canvas.drawLine(rect.left,rect.top,rect.left,rect.top+len,paint);
			canvas.drawLine(rect.left,rect.bottom,rect.left,rect.bottom-len,paint);
			
			canvas.drawLine(rect.right,rect.top,rect.right,rect.top+len,paint);
			canvas.drawLine(rect.right,rect.bottom,rect.right,rect.bottom-len,paint);

			canvas.drawLine(rect.left+len/2,rect.top+ rectWidth/2, rect.right-len/2,rect.top+ rectWidth/2,paint);
			canvas.drawLine(rect.left+ rectWidth/2,rect.top+len/2,rect.left+ rectWidth/2,rect.bottom-len/2,paint);

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}	
}
