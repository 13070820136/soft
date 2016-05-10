package yu.phoneshop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import yu.phoneshop.util.CommonUtil;
import yu.phoneshop.vo.ShouyeGuanggao;
import yu.phoneshop.vo.VOShopCart;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopCartActivity extends Activity{
	/**��Ʒ������*/
	private ShopAdapter shopAdapter;
	private ListView myListView;
	/**�ײ����Ե��ϲ��֣���̬������С*/
	private RelativeLayout relativeLayoutTop;
	/**�ײ������Բ���*/
	private RelativeLayout linearLayout_botton;
	/**������*/
	private RelativeLayout layout_main_container;
	/** volley����*/
	private RequestQueue requestQueue;
	private StringRequest stringRequest; 
	 /**ͼƬ����*/
    private ImageCache imageCache;
    private ImageLoader mImageLoader;
	/**���ﳵ����*/
    private List<VOShopCart> carts;
    /**ȫѡ��ť֮��*/
    private TextView quanxuan;
//    private TextView fanxuan;
    private TextView delete;
    private RelativeLayout relative_choise;
    /**ȫѡ��ť�ı�־*/
    private Map<Integer, Boolean> check_flags;
    /**Ҫɾ������Ʒ*/
    private List<VOShopCart> deleteCarts;
    /**Ҫ�������Ʒ*/
    private List<VOShopCart> orderList;
    /**���㰴ť*/
    private Button jiesuanBtn;
    /**�ܼ۸�textview*/
    private TextView textAllMoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_cart); 
		findViewById(R.id.icon_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		relativeLayoutTop = (RelativeLayout) findViewById(R.id.relative_top4);
		linearLayout_botton = (RelativeLayout) findViewById(R.id.relative_botton);
		layout_main_container = (RelativeLayout) findViewById(R.id.title_top);
		//�����ϲ���Բ��ֵĸ߶�
		relativeLayoutTop.setLayoutParams(new FrameLayout.LayoutParams
			(LayoutParams.MATCH_PARENT,getTopHeight()));
		relative_choise = (RelativeLayout) findViewById(R.id.choise);
		quanxuan = (TextView) findViewById(R.id.quanxuan); 
//		fanxuan = (TextView) findViewById(R.id.fanxuan);
		delete = (TextView) findViewById(R.id.delete);  
		textAllMoney = (TextView) findViewById(R.id.all_money);
		
		check_flags = new HashMap<Integer, Boolean>();
		quanxuan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(quanxuan.getText().equals("ȫѡ")){
					for(int i=0;i<carts.size();i++){
						check_flags.put(i, true);
						textAllMoney.setText("��"+changeAllMoney());
					}
					shopAdapter.notifyDataSetChanged();
					quanxuan.setText("ȡ��");
				}else if(quanxuan.getText().equals("ȡ��")){
					for(int i=0;i<carts.size();i++){
						check_flags.put(i, false);
						textAllMoney.setText("��"+changeAllMoney());
					}
					shopAdapter.notifyDataSetChanged();
					quanxuan.setText("ȫѡ");
				}
			}
		});
//		fanxuan.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				for(int i=0;i<carts.size();i++){
//					check_flags.put(i, false);
//					shopAdapter.notifyDataSetChanged();
//				}
//			}
//		});
		orderList = new ArrayList<VOShopCart>();
		deleteCarts = new ArrayList<VOShopCart>();
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteCarts.clear();
				for(int i=0;i<carts.size();i++){
					if(check_flags.get(i)){
						VOShopCart shopCart = new VOShopCart();
						shopCart.setColor(carts.get(i).getColor());
						shopCart.setZhishi(carts.get(i).getZhishi());
						shopCart.setShopid(carts.get(i).getShopid());
						shopCart.setUserid(CommonUtil.MYUSER.getUserName());
						deleteCarts.add(shopCart);
					}
				}
				deleteCarts(deleteCarts);
			}
		});
		jiesuanBtn = (Button) findViewById(R.id.btn_shopcart);
		jiesuanBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float m = 0;                                                                                                              
				orderList.clear();
				for(int i=0;i<carts.size();i++){
					if(check_flags.get(i)){
						m = m +Float.parseFloat(carts.get(i).getPrice());
						orderList.add(carts.get(i));
					}
				}
				if(orderList.size()==0){
					Toast.makeText(ShopCartActivity.this, "��ѡ����Ʒ", Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(ShopCartActivity.this,Settlement.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("shops", (Serializable) orderList);
					bundle.putFloat("money", m);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
		shopAdapter = new ShopAdapter(this);
		myListView = (ListView) findViewById(R.id.cart_istview);
		myListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(check_flags.get(position)){
					check_flags.put(position, false);
					shopAdapter.notifyDataSetChanged();
				}else{
					check_flags.put(position, true);
					shopAdapter.notifyDataSetChanged();
				}
				textAllMoney.setText("��"+changeAllMoney());
			}
		});
		requestQueue = Volley.newRequestQueue(this);
		carts = new ArrayList<VOShopCart>();
		final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(  
                20);  
		imageCache = new ImageCache() {  
            @Override  
            public void putBitmap(String key, Bitmap value) {  
                mImageCache.put(key, value);  
            }  
  
            @Override  
            public Bitmap getBitmap(String key) {  
                return mImageCache.get(key);  
            }  
        };  
        mImageLoader = new ImageLoader(requestQueue, imageCache);
//		RelativeLayout.LayoutParams layoutParams =  (android.widget.RelativeLayout.LayoutParams) myListView.getLayoutParams();
//		layoutParams.addRule(RelativeLayout.BELOW,R.id.choise);
//		layoutParams.height = getTopHeight();
//		myListView.setLayoutParams(layoutParams);
		myListView.setAdapter(shopAdapter);
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		init();
	}
	/**
	 * ���ط�����
	 */
    private void init(){
    	stringRequest = new StringRequest(CommonUtil.SERVER_URL1+"ShowCart?userid="+CommonUtil.MYUSER.getUserName()+"", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONArray jsonArray = new JSONArray(response);
					List<VOShopCart> c = new ArrayList<VOShopCart>();
					for(int i=0;i<jsonArray.length();i++){
						JSONObject object= jsonArray.getJSONObject(i);
						VOShopCart shopCart = new VOShopCart(object.getString("shopid"), 
								object.getString("number"), object.getString("price"),
								object.getString("imginfo"),object.getString("zhishi"),
								object.getString("color"),object.getString("pic"));
						c.add(shopCart);
					}
					carts.clear();
					carts.addAll(c);
					if(check_flags.isEmpty()){
						for(int i=0;i<carts.size();i++){
							check_flags.put(i, false);
						}
					}
					myHandler.sendEmptyMessage(0);
				} catch (JSONException e) {
					Log.e("JSONException","JSONException"+e.toString());
				}
			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("StringRequest", "onErrorResponse"+error.getMessage());
			}
		});
		requestQueue.add(stringRequest);
    }
    /**
	 * handler
	 * */
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Log.e("init", "notifyDataSetChanged");
				refeshChecks();
				shopAdapter.notifyDataSetChanged();
				textAllMoney.setText("��"+changeAllMoney());
				break;
			case 1:
				
				break;
			}
		}
	};
	/**
	 * ���ﳵlist��������
	 * */
	private class ShopAdapter extends BaseAdapter{
		private Context context;
		private Tag tag;
		public ShopAdapter(Context context) {
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return carts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return carts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				tag = new Tag();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_cart, parent, false);
				tag.textView1 = (TextView) convertView.findViewById(R.id.textView1);
				tag.price = (TextView) convertView.findViewById(R.id.price);
				tag.zhishi = (TextView) convertView.findViewById(R.id.zhishi);
				tag.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
				tag.editText = (EditText) convertView.findViewById(R.id.number);
				tag.checkBox = (CheckBox) convertView.findViewById(R.id.cart_chenkbox);
				convertView.setTag(tag);
			}else{
				tag = (Tag) convertView.getTag();
			}
			tag.textView1.setText(carts.get(position).getImginfo());
			tag.price.setText("��"+carts.get(position).getPrice());
			tag.zhishi.setText("��ʽ��"+carts.get(position).getZhishi()+" ��ɫ:"+carts.get(position).getColor());
			tag.editText.setText(carts.get(position).getNumber());
			ImageListener listener = ImageLoader  
	                .getImageListener(tag.imageView1, android.R.drawable.ic_menu_rotate,  
	                        android.R.drawable.ic_delete);  
	        mImageLoader.get(carts.get(position).getPic(), listener); 
	        
	        tag.checkBox.setChecked(check_flags.get(position));
	        Log.e("setChecked", "setChecked"+check_flags.get(position));
			return convertView;
		}
		private class Tag{
			private TextView textView1;
			private TextView price;
			private ImageView imageView1;
			private TextView zhishi;
			private EditText editText;
			private CheckBox checkBox;
		}
	}
	
	private int getTopHeight(){
		int s = getWindowManager().getDefaultDisplay().getHeight();
		findViewById(R.id.framelayout_products).measure(0, 0);
		linearLayout_botton.measure(0, 0);
		Log.e("height","height"+getStatusheight());
			 	Log.e("height", "height"+(s-linearLayout_botton.getMeasuredHeight()-getStatusheight()));
		return s-linearLayout_botton.getMeasuredHeight()-getStatusheight();
	}	
	/**
	 * ��ȡ״̬���߶�
	 * @return �߶�
	 */
	 private int getStatusheight(){
		 int statusHeight = 0;
	        Rect localRect = new Rect();
	        getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
	        statusHeight = localRect.top;
	        if (0 == statusHeight){
	            Class<?> localClass;
	            try {
	                localClass = Class.forName("com.android.internal.R$dimen");
	                Object localObject = localClass.newInstance();
	                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
	                statusHeight = getResources().getDimensionPixelSize(i5);
	            } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	            } catch (IllegalAccessException e) {
	                e.printStackTrace();
	            } catch (InstantiationException e) {
	                e.printStackTrace();
	            } catch (NumberFormatException e) {
	                e.printStackTrace();
	            } catch (IllegalArgumentException e) {
	                e.printStackTrace();
	            } catch (SecurityException e) {
	                e.printStackTrace();
	            } catch (NoSuchFieldException e) {
	                e.printStackTrace();
	            }
	        }
	        return statusHeight;
	 }
	 /**ɾ�����ﳵ������*/
	 private void deleteCarts(List<VOShopCart> carts){
		 if(carts.size()==0){
			 return;
		 }
		 Gson gson = new Gson();
		 final String string = gson.toJson(carts);
		 requestQueue.add(new StringRequest(Method.POST,CommonUtil.SERVER_URL1+"DeleteCart",new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("response", "response"+response);
				if(Boolean.valueOf(response)){
					init();
					
				}else{
					Toast.makeText(ShopCartActivity.this, "ɾ��ʧ�ܣ�", Toast.LENGTH_SHORT);
				}
			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		}){
			 @Override
			protected Map<String, String> getParams() throws AuthFailureError {
				 
				Map<String, String> params = new HashMap<String, String>();
				params.put("deletes", string);
				return params;
			}
		 });
	 }
	 //����ѡ����Ʒ�ܼ۸�
	 private float changeAllMoney(){
		 float m = 0;
		 for(int i=0;i<check_flags.size();i++){
				if(check_flags.get(i)){
					m = m +Float.parseFloat(carts.get(i).getPrice());
				}
		 }
		 return m;
	 }
	 //ɾ����Ʒ�� ˢ����Ʒѡ��״̬
	 private void refeshChecks(){
		 check_flags.clear();
		 for(int i=0;i<carts.size();i++){
			 check_flags.put(i, false);
		}
	 }
}
