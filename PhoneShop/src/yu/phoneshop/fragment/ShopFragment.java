package yu.phoneshop.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import yu.phoneshop.ProductsDetail;
import yu.phoneshop.R;
import yu.phoneshop.SearchActivity;
import yu.phoneshop.util.CommonUtil;
import yu.phoneshop.view.MyGridview;
import yu.phoneshop.vo.ListProduct;
import yu.phoneshop.vo.ShouyeGuanggao;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") public class ShopFragment extends Fragment{
	//ʵ������ˢ�µ�ScrollView
//	private PullToRefreshScrollView mPullRefreshScrollView;
	/**����view page*/
	private ViewPager viewPager;
	/** ����ϵĸ��ؼ���Ϊ��ȷ����С����ȡ */
	private RelativeLayout relative_fragment_content;
	/**����ͼƬ*/
	private List<ImageView> imageViews;
	/**����ͼƬ��ַ*/
	private List<ShouyeGuanggao> imageViewUrl;
	/** ��ѡ��ť�飬��ЩС�� */
	private RadioGroup radioGroup_fragment;
	/**����������Ʒ�б�*/
	private MyGridview shopGridView;
	/**��Ʒ�б�������*/
	private ShopFragmentAdapter fragmentAdapter;
	/** volley����*/
	private RequestQueue requestQueue;
	private StringRequest stringRequest; 
	/** ��־λ����־�Ѿ���ʼ�����*/  
    private boolean isPrepared;  
    /**��־����Ƭ�ɼ�*/
    private boolean isVisible;
    /**��Ʒ����*/
    private ArrayList<ListProduct> listProducts;
    /**ͼƬ����*/
    private ImageCache imageCache;
    private ImageLoader mImageLoader;
    /**������*/
    private TextView editTextSearch;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestQueue = Volley.newRequestQueue(getActivity());
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
		listProducts = new ArrayList<ListProduct>();
		imageViewUrl = new ArrayList<ShouyeGuanggao>();
		imageViews = new ArrayList<ImageView>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		/**�����̳�fragment��view*/
		View view = inflater.inflate(yu.phoneshop.R.layout.shop_fragment, container, false);
		editTextSearch = (TextView) view.findViewById(R.id.edit_search);
		editTextSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),SearchActivity.class);
				startActivity(intent);
				
			}
		});
		//���
		viewPager = (ViewPager) view.findViewById(R.id.viewPager_fragment);
		relative_fragment_content = (RelativeLayout) view
				.findViewById(R.id.relative_fragment_content);
		for(int i=0;i<4;i++){
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(R.drawable.huawei1);
			imageViews.add(imageView);
		}
		viewPager.setAdapter(new MyAdvertisementAdapter(imageViews));
		//������Ļ��ȵ������߶�
		relative_fragment_content
		.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, getButtonViewPager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				Log.e("viewPager", "onPageSelected"+arg0);
				if(arg0 >= 0&&arg0 < 4){
					radioGroup_fragment.getChildAt(arg0).performClick();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//��������С��
		radioGroup_fragment = (RadioGroup) view.findViewById(R.id.radioGroup_fragment);
		//����Ĭ�ϵ�һҳѡ��
		radioGroup_fragment.getChildAt(0).performClick();
		for (int i = 0; i < radioGroup_fragment.getChildCount(); i++) {
			RadioButton rButton = (RadioButton) radioGroup_fragment.getChildAt(i);
			// ����tag���±��뻬��������±걣��һ��
			rButton.setTag(i);
			// ����״̬����
			rButton.setBackgroundResource(R.drawable.slide_image_dot_c2);
			// ȥ����ѡ��ťǰ��ĵ���ɫΪ��
			rButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		/* ��ѡ��ť�鱻ѡ�еļ��� */
		radioGroup_fragment.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						
						RadioButton rButton = (RadioButton) group
								.findViewById(checkedId);
						int index = (Integer) (rButton.getTag());
						viewPager.setCurrentItem(index);
					}
				});
		//��ƷList View��ʼ��
		shopGridView = (MyGridview) view.findViewById(R.id.shop_gridview);
		fragmentAdapter = new ShopFragmentAdapter(getActivity());
		shopGridView.setAdapter(fragmentAdapter);
		shopGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.e("onItemClick", "onItemClick"+id);
				Intent intent = new Intent(getActivity(),ProductsDetail.class);
				String s = listProducts.get(position).getId();
				intent.putExtra("proid", s);
				startActivity(intent);
			}
		});
//		mPullRefreshScrollView = (PullToRefreshScrollView) view;
		viewPager.setFocusable(true);
		viewPager.setFocusableInTouchMode(true);
		viewPager.requestFocus();
		isPrepared = true;
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
	/**����Ƭ�ɼ�ʱΪtrue*/
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()){
			isVisible = true;
		}else{
			isVisible = false;
		}
	}
	/**
	 * �ӷ�������������
	 */
	private void initData(){
		Log.e("ShopFragment", "initData");
		//������Ʒ�б�����
		stringRequest = new StringRequest(CommonUtil.SERVER_URL1+"productList", new Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("StringRequest", "onResponse"+response);
				try {
					ArrayList<ListProduct> list = new ArrayList<ListProduct>();
					JSONObject jsonObject = new JSONObject(response);
					JSONArray jsonArray = (JSONArray) jsonObject.get("product");
					for(int i=0;i<jsonArray.length();i++){
						
						ListProduct product = new ListProduct(jsonArray.getJSONObject(i).getString("id"), 
								jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).
								getString("price"), jsonArray.getJSONObject(i).getString("pic"),jsonArray.
								getJSONObject(i).getString("introduction"));
						list.add(product);
					}
					ArrayList<ShouyeGuanggao> list2 = new ArrayList<ShouyeGuanggao>();
					JSONArray jsonArray2 = (JSONArray) jsonObject.get("guanggao");
					for(int i=0;i<jsonArray2.length();i++){
						ShouyeGuanggao shouyeGuanggao = new ShouyeGuanggao(jsonArray2.getJSONObject(i).getString("id"),
							jsonArray2.getJSONObject(i).getString("shopid"), jsonArray2.getJSONObject(i).getString("imgurl"));
						list2.add(shouyeGuanggao);
					}
					if(!list.isEmpty()){
						listProducts.clear();
						listProducts.addAll(list);
						imageViewUrl.clear();
						imageViewUrl.addAll(list2);
						myHandler.sendEmptyMessage(0);
					}
				} catch (JSONException e) {
					Log.e("JSONException","JSONException"+e.toString());
				}
			}
		},new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("StringRequest", "onErrorResponse"+error.getMessage());
				myHandler.sendEmptyMessage(3);
			}
		});
		
		requestQueue.add(stringRequest);
	}
	/**
	 * 
	 * */
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				fragmentAdapter.notifyDataSetChanged();
				for(int i=0;i<4;i++){
					ImageListener listener = ImageLoader  
			                .getImageListener(imageViews.get(i), android.R.drawable.ic_menu_rotate,  
			                        android.R.drawable.ic_delete);  
			        mImageLoader.get(imageViewUrl.get(i).getImgurl(), listener); 
				}
				break;
			case 3:
				Toast.makeText(getActivity(), "������·����", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};
	/**
	 * ʵ�����һ������Adapter
	 */
	class MyAdvertisementAdapter extends PagerAdapter {
		private List<ImageView> list = null;
		public MyAdvertisementAdapter() {
		}
		public MyAdvertisementAdapter(List<ImageView> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			
			return 4;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
		
			container.addView(list.get(position));// ÿһ��itemʵ��������
			
			return list.get(position);
		}
	}
	
	/**
	 * ��Ʒ�б��������
	 * @author y
	 *
	 */
	class ShopFragmentAdapter extends BaseAdapter{
		private Context context;
		private MyTag myTag;
		public ShopFragmentAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listProducts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listProducts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){ 
				myTag = new MyTag();
				convertView = LayoutInflater.from(context).inflate(yu.phoneshop.R.layout.item_shop_listview, null);
				Log.e("getView", "getView"+listProducts.get(position).getIntroduction());
				myTag.imageView = (ImageView) convertView.findViewById(R.id.shop_imageview);
				myTag.textView_name = (TextView) convertView.findViewById(R.id.shop_name);
				myTag.textView_introduction = (TextView) convertView.findViewById(R.id.shop_introduction);
				myTag.textView_price = (TextView) convertView.findViewById(R.id.shop_price);
				convertView.setTag(myTag);
			}else{
				myTag = (MyTag) convertView.getTag();
			};
			  
	        // imageView��һ��ImageViewʵ��  
	        // ImageLoader.getImageListener�ĵڶ���������Ĭ�ϵ�ͼƬresource id  
	        // ����������������ʧ��ʱ�����Դid������ָ��Ϊ0  
	        ImageListener listener = ImageLoader  
	                .getImageListener(myTag.imageView, android.R.drawable.ic_menu_rotate,  
	                        android.R.drawable.ic_delete);  
	        mImageLoader.get(listProducts.get(position).getPic(), listener);  
	        myTag.textView_name.setText(listProducts.get(position).getName());
	        myTag.textView_introduction.setText(listProducts.get(position).getIntroduction());
	        myTag.textView_price.setText(listProducts.get(position).getPrice());
			return convertView;
		}
		private class MyTag{
			ImageView imageView;
			TextView textView_name;
			TextView textView_introduction;
			TextView textView_price;
		}
	}
	/**
	 * ������Ļ��Ȼ��view page�߶�
	 */
	@SuppressLint("InflateParams") @SuppressWarnings("deprecation")
	private int getButtonViewPager() {
		Display display = getActivity().getWindowManager().getDefaultDisplay(); // Activity#getWindowManager()
		int lenButton = 0;
		//�˴�����ȡ����ͼƬ�ĸ߿��
		lenButton = display.getWidth() * 20 / 48;
		return lenButton;
	}
	
	 //���캯��
	public ShopFragment() {
			
	}
}
