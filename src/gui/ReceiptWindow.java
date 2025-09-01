package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import system.accounts.AccountManager;
import system.ordering.CartItem;
import system.transactions.PaymentType;
import system.transactions.Transaction;

public class ReceiptWindow extends JFrame {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final String HEADER =    "             ORDERING XYZTEM            \n\n"
			                              + "================ Receipt ===============\n\n";
	private static final String SEPARATOR = "========================================";
	private static final int NAME_LENGTH = 40;
	
	/*
	 * CONSTRUCTOR
	 */
	public ReceiptWindow(Transaction transaction, AccountManager manager) {
		super("SIMULATED RECEIPT");
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(360, 600);
		
		// Build text
		StringBuilder text = new StringBuilder("<html><pre>");
		text.append(HEADER);
		
		String employeeName = manager.getAccountName(transaction.getEmployeeID());
		text.append("Cashier: " + employeeName + "\n");
		text.append("For " + transaction.getDineType().toString().toUpperCase() + "\n\n");
		text.append(SEPARATOR + "\n\n");
		
		for (CartItem item : transaction.getCart()) {
			text.append(constructItemString(item));
		}
		text.append("\n" + SEPARATOR + "\n\n");
		
		String total = Integer.toString(transaction.getTotalPrice());
		text.append(constructPaddedString("TOTAL:", total));
		
		if (transaction.getPaymentType() == PaymentType.CASH) {
			String change = Integer.toString(transaction.getChange());
			text.append(constructPaddedString("CASH CHANGE:", change));
		} else {
			text.append(constructPaddedString("REF. NUM:", transaction.getReferenceNumber()));
		}
		
		text.append("\n           Thank you for dining!      \n\n");
		text.append("</pre></html>");
		
		// Layout
		JLabel label = new JLabel(text.toString());
		label.setBackground(Color.WHITE);
		label.setOpaque(true);
//		label.setFont(new Font("Monospaced", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane(label);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
		scrollPane.setBackground(Color.WHITE);
		
		this.add(scrollPane, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	/*
	 * SUPPORT METHODS
	 * I just took this from CartItemBlock lol
	 */
	private String constructItemString(CartItem item) {
		String name = item.getItem().getName();
		Integer quantity = item.getQuantity();
		Integer totalPrice = item.getPrice() * quantity;
		int padLength = NAME_LENGTH
				- name.length()
				- quantity.toString().length()
				- totalPrice.toString().length()
				- 3; // 2 spaces, then 'x'
		
		return String.format(
				"%s x%d.%s%d\n",
				name,
				quantity,
				getPadding(padLength, '.'),
				totalPrice)
				;
	}
	
	private String constructPaddedString(String key, String amount) {
		int padLength = NAME_LENGTH
				- key.length()
				- amount.length();
		return String.format("%s%s%s\n",
				key,
				getPadding(padLength, ' '),
				amount
				);
	}
	
	private String getPadding(int length, char padChar) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < length; i++)
			output.append(padChar);
		return output.toString();
	}

}
