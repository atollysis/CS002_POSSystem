package system.transactions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import io.csv.CSVConverter;
import io.csv.CSVManager;
import io.csv.converters.TransactionCSVConverter;
import io.csv.converters.CartItemCSVConverter;
import system.menu.ItemSize;
import system.menu.MenuItem;
import system.menu.MenuManager;
import system.ordering.CartDetails;
import system.ordering.CartItem;

public class TransactionManager {
	/*
	 * ATTRIBUTES
	 */
	private static final String CSV_FILENAME = "transactions.csv";
	private static final String CSV_JUNCT_FILENAME = "cartitems.csv";
	
	private CSVConverter<Transaction> transactionConverter;
	private CSVConverter<CartDetails> cartDetailsConverter;
	private List<Transaction> transactions;
	private List<CartDetails> cartDetails;
	
	private Transaction currTransaction;
	private List<CartDetails> cartDetailList;
	
	private CartItem currItem;
	
	/*
	 * CONSTRUCTOR
	 */
	public TransactionManager() {
		this.transactionConverter = new TransactionCSVConverter();
		this.cartDetailsConverter = new CartItemCSVConverter();
		this.currTransaction = null;
		this.cartDetailList = null;
		this.currItem = null;
//		this.loadCSV();
	}
	
	/*
	 * SUPPORT METHODS
	 */
//	private void saveCSV() {
//		try {
//			CSVManager.saveAll(CSV_FILENAME, this.transactions, this.transactionConverter);
//			CSVManager.saveAll(CSV_JUNCT_FILENAME, this.cartDetails, this.cartDetailsConverter);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
//	private void loadCSV() {
//		try {
//			this.transactions = CSVManager.loadAll(CSV_FILENAME, this.transactionConverter);
//			this.cartDetails = CSVManager.loadAll(CSV_JUNCT_FILENAME, this.cartDetailsConverter);
//			// Populate all transactions with cart details
//			for (CartDetails item : this.cartDetails) {
//				Transaction transaction = findTransactionByID(item.getTransactionID());
//				MenuItem menuItem = MenuManager.findItemById(item.getItemID());
//				transaction.getCart().add(new CartItem(menuItem, item));
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	private int findMaxID() {
		return 0;
//		return this.transactions.stream()
//				.mapToInt(Transaction::getID)
//				.max()
//				.orElse(0);
	}
	
//	private Transaction findTransactionByID(int id) {
//		List<Transaction> result = this.transactions.stream()
//				.filter(t -> t.getID() == id)
//				.toList();
//		return (result.size() == 1) ? result.get(0) : null;
//	}
	
	// Cart Details
	
	private CartDetails getCartDetailsOf(CartItem item) {
		int index = this.cartDetailList.indexOf(new CartDetails(0, item));
		return (index == -1) ? null : this.cartDetailList.get(index);
	}
	
	private void removeCartDetailsOf(CartItem item) {
		CartDetails detail = getCartDetailsOf(item);
		if (detail != null)
			this.cartDetailList.remove(detail);
	}
	
//	private void addCartDetailsOf(CartItem item) {
//		this.cartDetailList.add(new CartDetails(
//				this.currTransaction.getID(),
//				item));
//	}
	
	// Validation
	
	private boolean isValidRefNum(String refNum) {
		return refNum.chars().allMatch(Character::isDigit);
	}
	
	/*
	 * SERVICE METHODS
	 */
	public void newTransaction(int employeeId) {
		this.currTransaction = new Transaction(this.findMaxID(), employeeId);
		this.cartDetailList = new ArrayList<>();
	}
	
	public void resetTransactionDetails(int employeeId) {
		this.currTransaction = new Transaction(
				findMaxID() + 1,
				employeeId);
		this.currItem = new CartItem();
	}
	
	public TransactionOpResult createTransaction() {
//		System.out.println("Cart Size: " + this.currTransaction.getCart().size());
		
		// Guard clauses
		if (this.currTransaction.getCart().isEmpty())
			return TransactionOpResult.INVALID_TRANSACTION_CART_EMPTY;
		if (this.currTransaction.getPaymentType() == null)
			return TransactionOpResult.INVALID_TRANSACTION_NO_PAY_TYPE;
		if (this.currTransaction.getDineType() == null)
			return TransactionOpResult.INVALID_TRANSACTION_NO_DINE_TYPE;
		if (this.currTransaction.getPayment() <= 0)
			return TransactionOpResult.INVALID_TRANSACTION_NO_PAYMENT;
		if (this.currTransaction.getPaymentType().isNotNullAndCash()
				&& this.currTransaction.getReferenceNumber() == null)
			return TransactionOpResult.INVALID_TRANSACTION_NO_REF_NUM;
		
		this.currTransaction.setTimestamp(LocalDateTime.now());
		
//		this.transactions.add(this.currTransaction);
//		this.cartDetails.addAll(this.cartDetailList);
//		saveCSV();
		return TransactionOpResult.SUCCESS;
	}
	
	// Getters
	
	public Transaction getCurrTransaction() {
		return this.currTransaction;
	}
	
	// Setters
	
	public void setPaymentType(PaymentType type) {
		this.currTransaction.setPaymentType(type);
	}
	
	public void setDineType(DineType type) {
		this.currTransaction.setDineType(type);
	}
	
	public TransactionOpResult setReferenceNumber(String referenceNumber) {
		if (!isValidRefNum(referenceNumber))
			return TransactionOpResult.INVALID_REF_NUM_FORMAT;
		
		this.currTransaction.setReferenceNumber(referenceNumber);
		return TransactionOpResult.SUCCESS;
	}
	
	public TransactionOpResult setPaymentAndChange(String payment) {
		// Guard clauses
		if (payment == null)
			throw new NullPointerException("Payment is null.");
		
		int converted;
		try {
			converted = Integer.parseInt(payment);
		} catch (NumberFormatException e) {
			return TransactionOpResult.INVALID_PAYMENT_FORMAT;
		}
		if (converted <= 0)
			return TransactionOpResult.INVALID_PAYMENT_AMOUNT;
		if (this.currTransaction.getTotalPrice() > converted)
			return TransactionOpResult.INVALID_PAYMENT_NOT_ENOUGH;
		
		this.currTransaction.setPayment(converted);
		int change = converted - this.currTransaction.getTotalPrice();
		this.currTransaction.setChange(change);
		return TransactionOpResult.SUCCESS;
	}
	
	// Item methods
	
	public CartItem getCurrItem() {
		return this.currItem;
	}
	
	public void unsetItem() {
		this.currItem = null;
	}
	
	public void setItem(MenuItem item) {
		this.currItem = new CartItem();
		this.currItem.setItem(item);
	}
	
	public void setItemSizePrice(ItemSize size) {
		if (this.currItem == null)
			return;
		
		this.currItem.setSize(size);
		this.currItem.setPrice(this.currItem.getItem().getPriceOf(size));
	}
	
	public void setItemQuantity(int quantity) {
		if (this.currItem == null)
			return;
		
		this.currItem.setQuantity(quantity);
	}
	
	public void decrementQuantity() {
		if (this.currItem == null)
			return;
		
		removeCartDetailsOf(this.currItem);
		if (this.currItem.getQuantity() > 1)
			this.currItem.setQuantity(this.currItem.getQuantity() - 1);
//		addCartDetailsOf(this.currItem);
	}
	
	public void incrementQuantity() {
		if (this.currItem == null)
			return;
		
		removeCartDetailsOf(this.currItem);
		if (this.currItem.getQuantity() < Integer.MAX_VALUE)
			this.currItem.setQuantity(this.currItem.getQuantity() + 1);
//		addCartDetailsOf(this.currItem);
	}
	
	public int getCartItemIndex(CartItem item) {
		return this.currTransaction.getCart().indexOf(item);
	}
	
	public void addCurrItemToCart() {
		if (this.currItem == null)
			throw new NullPointerException("Cart item is null.");
		
		this.currTransaction.getCart().add(this.currItem);
//		addCartDetailsOf(this.currItem);
		
//		System.out.println("ADDED ITEM: " + this.currItem.getItem().getName());
//		System.out.println("Cart Size: " + this.currTransaction.getCart().size());
		
		int amountToAdd = this.currItem.getPrice() * this.currItem.getQuantity();
		int newTotal = this.currTransaction.getTotalPrice() + amountToAdd;
		this.currTransaction.setTotalPrice(newTotal);
		this.currItem = null;
	}
	
	public void removeCartItemAt(int index) {
		CartItem item = this.currTransaction.getCart().get(index);
		int amountToRemove = item.getPrice() * item.getQuantity();
		int newTotal = this.currTransaction.getTotalPrice() - amountToRemove;
		this.currTransaction.setTotalPrice(newTotal);
		
		this.currTransaction.getCart().remove(index);
		// idk how to find it so YIPPEE JANK
		removeCartDetailsOf(item);
	}
	
	// Cart
	
	public int getTotalPrice() {
		return this.currTransaction.getTotalPrice();
	}
	
}
