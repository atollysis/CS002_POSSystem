package system.ordering;

import java.util.Objects;

import system.menu.ItemSize;
import system.menu.MenuItem;

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
	public CartItem() {
		// These are all default values but it's good to be sure lol
		this.item = null;
		this.size = null;
		this.price = 0;
		this.quantity = 0;
	}
	
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

	@Override
	public int hashCode() {
		return Objects.hash(item, price, quantity, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		CartItem other = (CartItem) obj;
		return Objects.equals(item, other.item)
				&& price == other.price
				&& quantity == other.quantity
				&& size == other.size;
	}
}
