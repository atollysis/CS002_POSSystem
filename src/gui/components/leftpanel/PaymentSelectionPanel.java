package gui.components.leftpanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.ReceiptWindow;
import gui.SystemPanel;
import runners.MainRunner;
import system.transactions.PaymentType;
import system.transactions.TransactionOpResult;

public class PaymentSelectionPanel extends SystemPanel {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final String CASH_PAYMENT_PANEL = "cash";
	private static final String NON_CASH_PAYMENT_PANEL = "noncash";
	private static final String EMPTY_PANEL = "empty";
	// Components
	private JLabel lbl_prompt;
	private Map<PaymentType, JButton> buttons;
	private JButton btn_back;
	// Panels
	private JPanel cashPaymentPanel;
	private JPanel nonCashPaymentPanel;
	// Clearables
	private JLabel lbl_transactionError;
	private JLabel lbl_cashPaymentError;
	private JLabel lbl_nonCashPaymentError;
	private JTextField fld_payment;
	private JTextField fld_refNum;
	
	private JPanel cardLayoutWrapper;
	private CardLayout paymentCard;
	
	/**
	 * Create the panel.
	 */
	public PaymentSelectionPanel(MainRunner runner) {
		super(runner);
		
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		this.buttons = new HashMap<>();
		
		this.lbl_prompt = new JLabel("Please select a payment method");
		for (PaymentType type : PaymentType.values()) {
			this.buttons.put(type, new JButton(type.toString().toUpperCase()));
		}
		this.btn_back = new JButton("Order More");
		this.lbl_transactionError = new JLabel(" ");
		this.lbl_cashPaymentError = new JLabel(" ");
		this.lbl_nonCashPaymentError = new JLabel(" ");
		
		this.cardLayoutWrapper = new JPanel();
		this.paymentCard = new CardLayout();
		this.cardLayoutWrapper.setLayout(this.paymentCard);
		
		this.setupCashPaymentPanel();
		this.setupNonCashPaymentPanel();
		
		JPanel emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(100, 100));
		
		cardLayoutWrapper.add(this.cashPaymentPanel, CASH_PAYMENT_PANEL);
		cardLayoutWrapper.add(this.nonCashPaymentPanel, NON_CASH_PAYMENT_PANEL);
		cardLayoutWrapper.add(emptyPanel, EMPTY_PANEL);
		paymentCard.show(this.cardLayoutWrapper, EMPTY_PANEL);
	}

	@Override
	protected void setupLayout() {
		this.setLayout(new GridBagLayout());
		
		this.add(lbl_prompt,              newLabelConstraints(0, 0, 3, 30));
		for (int i = 0; i < PaymentType.values().length; i++) {
			JButton currButton = this.buttons.get(PaymentType.values()[i]);
			currButton.setMaximumSize(new Dimension(200, 200));
			this.add(currButton,       newCenteredConstraints(i, 1, 1, 5));
		}
		this.add(btn_back,             newCenteredConstraints(0, 2, 3, 5));
		this.add(lbl_transactionError,    newLabelConstraints(0, 3, 3, 5));
		this.add(cardLayoutWrapper,    newCenteredConstraints(0, 4, 3, 5));
	}

	@Override
	protected void setupInteractions() {
		for (PaymentType type : PaymentType.values()) {
			this.buttons.get(type).addActionListener(e -> {
				// Back
				transactionManager.setPaymentType(type);
				// Front
				if (type == PaymentType.CASH)
					this.paymentCard.show(this.cardLayoutWrapper, CASH_PAYMENT_PANEL);
				else
					this.paymentCard.show(this.cardLayoutWrapper, NON_CASH_PAYMENT_PANEL);
			});
		}
		
		this.btn_back.addActionListener(e -> {
			cardLayoutManager.setAllToOrderMode();
		});
	}

	@Override
	public void clearFields() {
		this.lbl_transactionError.setText(" ");
		this.lbl_cashPaymentError.setText(" ");
		this.lbl_nonCashPaymentError.setText(" ");
		this.fld_payment.setText("");
		this.fld_refNum.setText("");
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void setupCashPaymentPanel() {
		this.cashPaymentPanel = new JPanel();
		// Component
		JLabel lbl_prompt = new JLabel("Please enter the payment");
		this.fld_payment = new JTextField();
		JButton btn_exactChange = new JButton("Exact Change");
		JButton btn_submit = new JButton("Submit");
		// Layout
		cashPaymentPanel.setLayout(new GridBagLayout());
		cashPaymentPanel.add(lbl_prompt,           newLabelConstraints(0, 0, 2, 30));
		cashPaymentPanel.add(lbl_cashPaymentError, newLabelConstraints(0, 1, 2, 30));
		cashPaymentPanel.add(fld_payment,          newFieldConstraints(0, 2, 2, 5));
		cashPaymentPanel.add(btn_exactChange,     newButtonConstraints(0, 3));
		cashPaymentPanel.add(btn_submit,          newButtonConstraints(1, 3));
		// Interactions
		btn_exactChange.addActionListener(e -> {
			fld_payment.setText(Integer.toString(transactionManager.getTotalPrice()));
		});
		btn_submit.addActionListener(e -> {
			TransactionOpResult payResult = transactionManager.setPaymentAndChange(fld_payment.getText());
			
			switch(payResult) {
				case SUCCESS:
					this.completeTransaction();
					break;

				case INVALID_PAYMENT_AMOUNT:
					this.lbl_cashPaymentError.setText("ERROR: Invalid payment amount.");
					return;
					
				case INVALID_PAYMENT_FORMAT:
					this.lbl_cashPaymentError.setText("ERROR: Invalid payment format. Numbers only.");
					return;
					
				case INVALID_PAYMENT_NOT_ENOUGH:
					this.lbl_cashPaymentError.setText("ERROR: Payment is not enough.");
					return;
	
				default:
					// INVALID_CART_INDEX
					// INVALID_REF_NUM_FORMAT
					// INVALID_TRANSACTION_CART_EMPTY
					// INVALID_TRANSACTION_NO_DINE_TYPE
					// INVALID_TRANSACTION_NO_PAYMENT
					// INVALID_TRANSACTION_NO_PAY_TYPE
					// INVALID_TRANSACTION_NO_REF_NUM
					throw new IllegalStateException("Unexpected operator result: " + payResult);
			}
			
			TransactionOpResult creationResult = transactionManager.createTransaction();
			System.out.println(creationResult);
			
			switch(creationResult) {
				case SUCCESS:
					this.completeTransaction();
					break;
					
				case INVALID_TRANSACTION_CART_EMPTY:
					this.lbl_transactionError.setText("ERROR: Cart is empty.");
					return;
					
				case INVALID_TRANSACTION_NO_DINE_TYPE:
					this.lbl_transactionError.setText("ERROR: No dine type selected.");
					return;
					
				case INVALID_TRANSACTION_NO_PAYMENT:
					this.lbl_transactionError.setText("ERROR: No payment set.");
					return;
					
				case INVALID_TRANSACTION_NO_PAY_TYPE:
					this.lbl_transactionError.setText("ERROR: No payment type selected.");
					return;
	
				default:
					// INVALID_CART_INDEX
					// INVALID_PAYMENT_AMOUNT
					// INVALID_PAYMENT_FORMAT
					// INVALID_PAYMENT_NOT_ENOUGH
					// INVALID_REF_NUM_FORMAT
					// INVALID_TRANSACTION_NO_REF_NUM
					throw new IllegalStateException("Unexpected operator result: " + creationResult);
			}
		});
	}
	
	private void completeTransaction() {
		new ReceiptWindow(transactionManager.getCurrTransaction(), accountManager);
		// Back
		transactionManager.newTransaction(accountManager.getCurrAccount().getID());
		// Front
		cardLayoutManager.reset();
	}
	
	private void setupNonCashPaymentPanel() {
		this.nonCashPaymentPanel = new JPanel();
		// Component
		JLabel lbl_prompt = new JLabel("Please enter the reference number");
		this.fld_refNum = new JTextField();
		JButton btn_submit = new JButton("Submit");
		// Layout
		nonCashPaymentPanel.setLayout(new GridBagLayout());
		nonCashPaymentPanel.add(lbl_prompt,           newCenteredConstraints(0, 0, 1, 30));
		nonCashPaymentPanel.add(lbl_nonCashPaymentError, newLabelConstraints(0, 1, 2, 30));
		nonCashPaymentPanel.add(fld_refNum,              newFieldConstraints(0, 1, 1, 5));
		nonCashPaymentPanel.add(btn_submit,             newButtonConstraints(0, 2));
		// Interactions
		btn_submit.addActionListener(e -> {
			TransactionOpResult refNumResult = transactionManager.setReferenceNumber(this.fld_refNum.getText());
			
			switch(refNumResult) {
				case SUCCESS:
					int exactPayment = transactionManager.getTotalPrice();
					transactionManager.setPaymentAndChange(Integer.toString(exactPayment));
					break;

				case INVALID_REF_NUM_FORMAT:
					this.lbl_nonCashPaymentError.setText("ERROR: Invalid reference number.");
					return;
	
				default:
					// INVALID_CART_INDEX
					// INVALID_PAYMENT_AMOUNT
					// INVALID_PAYMENT_FORMAT
					// INVALID_PAYMENT_NOT_ENOUGH
					// INVALID_TRANSACTION_CART_EMPTY
					// INVALID_TRANSACTION_NO_DINE_TYPE
					// INVALID_TRANSACTION_NO_PAYMENT
					// INVALID_TRANSACTION_NO_PAY_TYPE
					// INVALID_TRANSACTION_NO_REF_NUM
					throw new IllegalStateException("Unexpected operator result: " + refNumResult);
			}
			
			TransactionOpResult creationResult = transactionManager.createTransaction();
			
			switch(creationResult) {
				case SUCCESS:
					this.completeTransaction();
					break;
					
				case INVALID_TRANSACTION_CART_EMPTY:
					this.lbl_transactionError.setText("ERROR: Cart is empty.");
					return;
					
				case INVALID_TRANSACTION_NO_DINE_TYPE:
					this.lbl_transactionError.setText("ERROR: No dine type selected.");
					return;
					
				case INVALID_TRANSACTION_NO_PAYMENT:
					this.lbl_transactionError.setText("ERROR: No payment set.");
					return;
					
				case INVALID_TRANSACTION_NO_PAY_TYPE:
					this.lbl_transactionError.setText("ERROR: No payment type selected.");
					return;
	
				default:
					// INVALID_CART_INDEX
					// INVALID_PAYMENT_AMOUNT
					// INVALID_PAYMENT_FORMAT
					// INVALID_PAYMENT_NOT_ENOUGH
					// INVALID_REF_NUM_FORMAT
					// INVALID_TRANSACTION_NO_REF_NUM
					throw new IllegalStateException("Unexpected operator result: " + creationResult);
			}
		});
	}
	
	private static GridBagConstraints newCenteredConstraints(int x, int y, int width, int bottomInset) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, bottomInset, 5);
		return c;
	}
	
	private static GridBagConstraints newLabelConstraints(int x, int y, int width, int bottomInset) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
//		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 5, bottomInset, 5);
		return c;
	}
	
	private static GridBagConstraints newFieldConstraints(int x, int y, int width, int bottomInset) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, bottomInset, 5);
		return c;
	}
	
	private static GridBagConstraints newButtonConstraints(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}

}
