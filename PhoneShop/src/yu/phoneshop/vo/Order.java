package yu.phoneshop.vo;

import java.io.Serializable;

public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 277719484068150957L;

	/** ������� */
	private String orderId;

	/** ������ʾ״̬ */
	private String status;

	/** ������� */
	private double price;

	/** �µ�ʱ�� */
	private String time;
	
	/** ��ַid*/
	private String adressId;
	
	/** ��Ʒ��Ϣ*/
	private String shoplist;
	
	/** �û�id*/
	private String userid;
	
	
	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getAdressId() {
		return adressId;
	}


	public void setAdressId(String adressId) {
		this.adressId = adressId;
	}


	public String getShoplist() {
		return shoplist;
	}


	public void setShoplist(String shoplist) {
		this.shoplist = shoplist;
	}


	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	public Order(String orderId, String status, double price, String time,
			String adressId, String shoplist, String userid) {
		super();
		this.orderId = orderId;
		this.status = status;
		this.price = price;
		this.time = time;
		this.adressId = adressId;
		this.shoplist = shoplist;
		this.userid = userid;
	}


	public Order() {
	}
	
}
