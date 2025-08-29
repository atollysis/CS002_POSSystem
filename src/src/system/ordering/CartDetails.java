/**
 * Wrapper POJO class for consolidating the junction table.
 */

package src.system.ordering;

import java.util.Objects;

import src.system.menu.ItemSize;

public class CartDetails {
	/*
	 * ATTRIBUTES
	 */
	private final int transactionID;
	private final int itemID;
	private final ItemSize itemSize;
	private final int itemPrice;
	private final int itemQuantity;
	private final int subtotal;
	
	/*
	 * CONSTRUCTOR
	 */
	public CartDetails(
			int transactionID,
			CartItem cartItem) {
		this.transactionID = transactionID;
		this.itemID = cartItem.getItem().getId();
		this.itemSize = cartItem.getSize();
		this.itemPrice = cartItem.getItem().getPriceOf(cartItem.getSize());
		this.itemQuantity = cartItem.getQuantity();
		this.subtotal = cartItem.getPrice();
	}
	
	public CartDetails(
			int transactionID,
			int itemID,
			ItemSize itemSize,
			int itemPrice,
			int itemQuantity,
			int subtotal) {
		this.transactionID = transactionID;
		this.itemID = itemID;
		this.itemSize = itemSize;
		this.itemPrice = itemPrice;
		this.itemQuantity = itemQuantity;
		this.subtotal = subtotal;
	}

	/*
	 * GETTERS
	 */
	public int getTransactionID() {
		return transactionID;
	}

	public int getItemID() {
		return itemID;
	}
	
	public ItemSize getItemSize() {
		return itemSize;
	}

	public int getItemPrice() {
		return itemPrice;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public int getSubtotal() {
		return subtotal;
	}
	
	/*
	 * OVERRIDDEN METHODS
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (this.getClass() != obj.getClass())
			return false;
		
		CartDetails other = (CartDetails) obj;
		return this.itemID == other.itemID
			&& this.itemSize == other.itemSize
			&& this.itemPrice == other.itemPrice
			&& this.itemQuantity == other.itemQuantity;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(
				this.itemID,
				this.itemSize,
				this.itemPrice,
				this.itemQuantity);
	}
	
}
