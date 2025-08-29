package system.transactions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import io.csv.CSVConverter;
import io.csv.CSVManager;
import io.csv.converters.TransactionCSVConverter;
import io.csv.converters.CartItemCSVConverter;
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
	private List<CartDetails> currCartDetails;
	
	/*
	 * CONSTRUCTOR
	 */
	public TransactionManager() {
		this.transactionConverter = new TransactionCSVConverter();
		this.cartDetailsConverter = new CartItemCSVConverter();
		this.currTransaction = null;
		this.currCartDetails = new ArrayList<>();
		this.loadCSV();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void saveCSV() {
		try {
			CSVManager.saveAll(CSV_FILENAME, this.transactions, this.transactionConverter);
			CSVManager.saveAll(CSV_JUNCT_FILENAME, this.cartDetails, this.cartDetailsConverter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCSV() {
		try {
			this.transactions = CSVManager.loadAll(CSV_FILENAME, this.transactionConverter);
			this.cartDetails = CSVManager.loadAll(CSV_JUNCT_FILENAME, this.cartDetailsConverter);
			// Populate all transactions with cart details
			for (CartDetails item : this.cartDetails) {
				Transaction transaction = findTransactionByID(item.getTransactionID());
				MenuItem menuItem = MenuManager.findItemById(item.getItemID());
				transaction.getCart().add(new CartItem(menuItem, item));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int findMaxID() {
		return this.transactions.stream()
				.mapToInt(Transaction::getID)
				.max()
				.orElse(0);
	}
	
	private Transaction findTransactionByID(int id) {
		List<Transaction> result = this.transactions.stream()
				.filter(t -> t.getID() == id)
				.toList();
		return (result.size() == 1) ? result.get(0) : null;
	}
	
	/*
	 * SERVICE METHODS
	 */
	public void resetTransactionDetails(int employeeId) {
		this.currTransaction = new Transaction(
				findMaxID() + 1,
				employeeId);
	}
	
	public TransactionOpResult createTransaction() {
		// Guard clauses
		if (this.currTransaction.getCart().isEmpty())
			return TransactionOpResult.INVALID_TRANSACTION_CART_EMPTY;
		/*
		 * TODO:
		 * Set similar guard clauses for:
		 * - no payment type (null)
		 * - no dine type (null)
		 * - negative/no payment
		 * 
		 * See TransactionOpResult for the appropriate return values.
		 */
		
		this.currTransaction.setTimestamp(LocalDateTime.now());
		
		/*
		 * TODO:
		 * Add current transaction to the list,
		 * Add all of currCartDetails to cartDetails,
		 * call saveCSV,
		 * return appropriate TransactionOpResult
		 */
		return null;
	}
	
	public void setPaymentType(PaymentType type) {
		this.currTransaction.setPaymentType(type);
	}
	
	public void setDineType(DineType type) {
		this.currTransaction.setDineType(type);
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
		
		/*
		 * TODO:
		 * Set current transaction payment,
		 * compute and set its change,
		 * return appropriate TransactionOpResult
		 */
		return null;
	}
	
	public void addToCart(CartItem item) {
		if (item == null)
			throw new NullPointerException("Cart item is null.");
		
		this.currTransaction.getCart().add(item);
		this.currCartDetails.add(new CartDetails(
				this.currTransaction.getID(),
				item));
		
		int newTotal = this.currTransaction.getTotalPrice() + item.getPrice();
		this.currTransaction.setTotalPrice(newTotal);
	}
	
	public void removeCartItemAt(int index) {
		CartItem item = this.currTransaction.getCart().get(index);
		int newTotal = this.currTransaction.getTotalPrice() - item.getPrice();
		this.currTransaction.setTotalPrice(newTotal);
		
		this.currTransaction.getCart().remove(index);
		// idk how to find it so YIPPEE JANK
		this.currCartDetails.remove(new CartDetails(0, item));
	}
	
}
