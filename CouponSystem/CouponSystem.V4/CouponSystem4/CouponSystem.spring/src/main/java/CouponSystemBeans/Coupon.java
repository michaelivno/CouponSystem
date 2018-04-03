package CouponSystemBeans;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Simple been class that represent coupon object.
 * 
 * @author michael
 *
 */
public class Coupon {

	private long id;
	private String title;
	private Date startDate;
	private Date endDate;
	private int amount;
	private CouponType type;
	private String message;
	private double price;
	private String image;

	public Coupon() {
		super();
	}

	/**
	 * Class coupon constructor.
	 * 
	 * @param title
	 *            - Coupon Title must be specific for each coupon.
	 * @param endYear
	 *            - yyyy.
	 * @param endMonth
	 *            - mm.
	 * @param endDay
	 *            - dd.
	 * @param amount
	 * @param type
	 * @param message
	 * @param price
	 * @param image
	 */
	public Coupon(String title, int endYear, int endMonth, int endDay, int amount, CouponType type, String message,
			double price, String image) {
		super();
		this.title = title;
		this.startDate = initStartDate();
		this.endDate = initEndDate(endYear, endMonth, endDay);
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.price = price;
		this.image = image;
	}

	/**
	 * This method get NOW date for coupon (startDate) constructor.
	 * 
	 * @return Now time in java.util.date
	 */
	public java.util.Date initStartDate() {
		LocalDate localDate = LocalDate.now();
		DateTimeFormatter.ofPattern("yyyy/MM/DD").format(localDate);
		java.util.Date date = java.sql.Date.valueOf(localDate);
		return date;
	}

	/**
	 * This method help set end date of the coupon and send it to the coupon
	 * constructor.
	 * 
	 * @param year
	 *            - yyyy
	 * @param month
	 *            - mm
	 * @param day
	 *            -dd
	 * @return
	 */
	public java.util.Date initEndDate(int year, int month, int day) {
		month = month - 1;
		GregorianCalendar gcalender = new GregorianCalendar(year, month, day);
		return gcalender.getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public CouponType getType() {
		return type;
	}

	public void setType(CouponType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", amount=" + amount + ", type=" + type + ", message=" + message + ", price=" + price + ", image="
				+ image + "]";
	}

}
