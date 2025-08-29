package src.system.ordering;

import src.system.menu.ItemSize;
import src.system.menu.MenuItem;

public class CartItem {
	/*
	 * ATTRIBUTES
	 */
	private MenuItem item;
	private ItemSize size;
	private int price;
	private int quantity;
	
	/*
	 * CONSTRUCTOR
	 */
	public CartItem(
			MenuItem item,
			CartDetails details) {
		this.item = item;
		this.size = details.getItemSize();
		this.price = details.getItemPrice();
		this.quantity = details.getSubtotal();
	}
	
	public CartItem(
			MenuItem item,
			ItemSize size,
			int price,
			int quantity) {
		this.item = item;
		this.size = size;
		this.price = price;
		this.quantity = quantity;
	}

	/*
	 * GETTERS
	 */
	public MenuItem getItem() {
		return item;
	}

	public ItemSize getSize() {
		return size;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	/*
	 * SETTERS
	 */
	public void setItem(MenuItem item) {
		this.item = item;
	}

	public void setSize(ItemSize size) {
		this.size = size;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
