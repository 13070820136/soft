package yu.phoneshop.vo;
public class Product {
	/** ��Ʒ���� */
	private String prodNum;
	/** ID */
	private String id;
	/** ��Ʒ���� */
	private String name;
	/** ��Ա�� */
	private String price;
	/** ��Ʒ���������0Ϊȱ�����¼� */
	private String number;
	/** ��Ʒ������������ */
	private String uplimit;
	/** ͼƬ */
	private String pic;
	/** ��Ʒ���*/
	private String introduction;
	/** ��Ʒ������*/
	private String imginfo;
	/** ��Ʒ��ʽ*/
	private String zhishi;
	/** ��Ʒ��ɫ*/
	private String color;
	public String getProdNum() {
		return prodNum;
	}
	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getUplimit() {
		return uplimit;
	}
	public void setUplimit(String uplimit) {
		this.uplimit = uplimit;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Product() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Product(String prodNum, String id, String name, String price,
			String number, String uplimit, String pic, String introduction,
			String imginfo) {
		super();
		this.prodNum = prodNum;
		this.id = id;
		this.name = name;
		this.price = price;
		this.number = number;
		this.uplimit = uplimit;
		this.pic = pic;
		this.introduction = introduction;
		this.imginfo = imginfo;
	}
	public String getImginfo() {
		return imginfo;
	}
	public void setImginfo(String imginfo) {
		this.imginfo = imginfo;
	}
	public String getZhishi() {
		return zhishi;
	}
	public void setZhishi(String zhishi) {
		this.zhishi = zhishi;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
