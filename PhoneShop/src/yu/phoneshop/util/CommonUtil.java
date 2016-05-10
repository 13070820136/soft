package yu.phoneshop.util;

import yu.phoneshop.vo.User;
import android.content.Context;

public class CommonUtil {
	/**�û��ѵ�½��Ϣ*/
	public static final User MYUSER = new User();  
	/**���ʷ�������ַ1*/
	public static final String SERVER_URL1 = "http://192.168.191.1:8080//ECServer_D/";
	/**
	* �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	*/
	public static int dip2px(Context context, float dpValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	}

	/**
	* �����ֻ��ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	*/
	public static int px2dip(Context context, float pxValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (pxValue / scale + 0.5f);
	}
	
}
