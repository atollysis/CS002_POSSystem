package system.transactions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import system.ordering.CartItem;

public class Transaction {
	/*
	 * ATTRIBUTES
	 */
	private final int id;
	
	private LocalDateTime timestamp;
	private int employeeID;
	private List<CartItem> cart;
	private PaymentType paymentType;
	private DineType dineType;
	private int totalPrice;
	private int payment;
	private int change;
	private String referenceNumber;
	
	/*
	 * CONSTRUCTORS
	 */
	public Transaction(int id, int employeeId) {
		this.id = id;
		this.timestamp = null;
		this.employeeID = employeeId;
		this.cart = new ArrayList<>();
		this.paymentType = null;
		this.dineType = null;
		this.totalPrice = 0;
		this.payment = 0;
		this.change = 0;
		this.referenceNumber = null;
	}
	
	public Transaction(
			int id,
			LocalDateTime timestamp,
			int employeeId,
			List<CartItem> cart,
			PaymentType paymentType,
			DineType dineType,
			int totalPrice,
			int payment,
			int change,
			String referenceNumber) {
		this.id = id;
		this.timestamp = timestamp;
		this.employeeID = employeeId;
		this.cart = cart;
		this.paymentType = paymentType;
		this.dineType = dineType;
		this.totalPrice = totalPrice;
		this.payment = payment;
		this.change = change;
		this.referenceNumber = referenceNumber;
	}
	
	/*
	 * GETTERS
	 */
	public int getID() {
		return id;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public int getEmployeeID() {
		return employeeID;
	}

	public List<CartItem> getCart() {
		return cart;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public DineType getDineType() {
		return dineType;
	}

	public int getTotalPrice() {
		return totalPrice;
	}
	
	public int getPayment() {
		return payment;
	}
	
	public int getChange() {
		return change;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	/*
	 * SETTERS
	 */
	public void setEmployeeID(int employeeID) {
		this.employeeID = employeeID;
	}
	
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setCart(List<CartItem> cart) {
		this.cart = cart;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	public void setDineType(DineType dineType) {
		this.dineType = dineType;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public void setPayment(int payment) {
		this.payment = payment;
	}
	
	public void setChange(int change) {
		this.change = change;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
}
