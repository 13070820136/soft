package yu.phoneshop.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
	private final String TAG = "MyViewPager";
	/** ����ʱ���µĵ� **/
	PointF downP = new PointF();
	/** ����ʱ��ǰ�ĵ� **/
	PointF curP = new PointF();
	OnSingleTouchListener onSingleTouchListener;

	// /** 控件是否是单击的标识，如果是单击，那么不拦截�? */
	// private boolean clickFlag = true;

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// �����ش����¼������λ�õ�ʱ�򣬷���true��
		// ˵����onTouch�����ڴ˿ؼ�������ִ�д˿ؼ���onTouchEvent
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// ÿ�ν���onTouch�¼�����¼��ǰ�İ��µ�����
		curP.x = arg0.getX();
		curP.y = arg0.getY();

		if (arg0.getAction() == MotionEvent.ACTION_DOWN) {
			// ��¼����ʱ�������
			// �мǲ����� downP = curP �������ڸı�curP��ʱ��downPҲ��ı�
			downP.x = arg0.getX();
			downP.y = arg0.getY();
			// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
			// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ���ҵĲ������и���
			getParent().requestDisallowInterceptTouchEvent(true);
		}

		if (arg0.getAction() == MotionEvent.ACTION_UP) {
			// ��upʱ�ж��Ƿ��º����ֵ�����Ϊһ����
			// �����һ���㣬��ִ�е���¼����������Լ�д�ĵ���¼���������onclick
			if (downP.x == curP.x && downP.y == curP.y) {
				onSingleTouch();
				// clickFlag = onInterceptTouchEvent(arg0);
				// clickFlag = true;
				// Log.i(TAG, "clickFlag==" + clickFlag);
				// clickFlag = true;
				// 设置单击
				return false;
			}
		}

		// return super.onTouchEvent(arg0);
		return super.onTouchEvent(arg0);
	}

	/**
	 * 单击
	 */
	public void onSingleTouch() {
		Log.i(TAG, "单击");
		if (onSingleTouchListener != null) {
			onSingleTouchListener.onSingleTouch();
		}
	}

	/**
	 * 创建点击事件接口
	 */
	public interface OnSingleTouchListener {
		public void onSingleTouch();
	}

	public void setOnSingleTouchListener(
			OnSingleTouchListener onSingleTouchListener) {
		this.onSingleTouchListener = onSingleTouchListener;
	}

}