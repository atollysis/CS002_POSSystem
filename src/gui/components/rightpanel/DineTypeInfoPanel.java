package gui.components.rightpanel;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.SystemPanel;
import runners.MainRunner;
import system.transactions.DineType;

public class DineTypeInfoPanel extends SystemPanel {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	public static final String EMPTY_PANEL = "empty";
	public static final String SELECTED_PANEL = "selected";
	public static final String PAYMENT_PANEL = "payment";
	// Components
	private JButton btn_dinein;
	private JButton btn_takeout;
	private JButton btn_toPayment;
//	private JButton btn_switchDineType;
	
	private JPanel pnl_empty;
	private JPanel pnl_selected;
	private JPanel pnl_payment;
	
	private JLabel lbl_totalPrice;
	private JLabel lbl_dineType;
	
	private JPanel cardWrapper;
	
	private CardLayout layout;

	/*
	 * CONSTRUCTOR
	 */
	public DineTypeInfoPanel(MainRunner runner) {
		super(runner);
		
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		this.btn_dinein = new JButton("Dine-In");
		this.btn_takeout = new JButton("Takeout");
		this.btn_toPayment = new JButton("To Payment");
//		this.btn_switchDineType = new JButton("Switch");
		
		this.lbl_totalPrice = new JLabel("TOTAL: 0");
		lbl_totalPrice.setFont(new Font("Monospaced", Font.BOLD, 16));
		this.lbl_dineType = new JLabel("No dine type selected");
		lbl_dineType.setFont(new Font("Monospaced", Font.ITALIC, 16));
	}

	@Override
	protected void setupLayout() {
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		this.pnl_empty = new JPanel();
		pnl_empty.setLayout(new GridBagLayout());
//		pnl_empty.add(this.lbl_totalPrice,  newLabelConstraint(0, 0));
//		pnl_empty.add(this.lbl_dineType,    newLabelConstraint(0, 1));
		pnl_empty.add(this.btn_dinein,     newConstraint(0, 0, 1));
		pnl_empty.add(this.btn_takeout,    newConstraint(1, 0, 1));
		
		this.pnl_selected = new JPanel();
		pnl_selected.setLayout(new GridBagLayout());
//		pnl_selected.add(this.lbl_totalPrice,  newLabelConstraint(0, 0));
//		pnl_selected.add(this.lbl_dineType,    newLabelConstraint(0, 1));
		pnl_selected.add(newSwitchDineTypeButton(), newConstraint(0, 0, 1));
		pnl_selected.add(this.btn_toPayment,        newConstraint(1, 0, 1));
		
		this.pnl_payment = new JPanel();
		pnl_payment.setLayout(new GridBagLayout());
//		pnl_payment.add(this.lbl_totalPrice,  newLabelConstraint(0, 0));
//		pnl_payment.add(this.lbl_dineType,    newLabelConstraint(0, 1));
		pnl_payment.add(newSwitchDineTypeButton(), newConstraint(0, 0, 2));
		
		this.cardWrapper = new JPanel();
		this.layout = new CardLayout();
		cardWrapper.setLayout(layout);
		cardWrapper.add(this.pnl_empty, EMPTY_PANEL);
		cardWrapper.add(this.pnl_selected, SELECTED_PANEL);
		cardWrapper.add(this.pnl_payment, PAYMENT_PANEL);
		layout.show(cardWrapper, EMPTY_PANEL);
		
		this.setLayout(new GridBagLayout());
		this.add(this.lbl_totalPrice, newLabelConstraint(0, 0));
		this.add(this.lbl_dineType,   newLabelConstraint(0, 1));
		this.add(cardWrapper,              newConstraint(0, 2, 1));
		
		cardLayoutManager.setLowerRightLayout(this.layout, cardWrapper);
	}

	@Override
	protected void setupInteractions() {
		this.btn_dinein.addActionListener(e -> {
			transactionManager.setDineType(DineType.DINE_IN);
			updateDineString();
			this.layout.show(this.cardWrapper, SELECTED_PANEL);
		});
		
		this.btn_takeout.addActionListener(e -> {
			transactionManager.setDineType(DineType.TAKEOUT);
			updateDineString();
			this.layout.show(this.cardWrapper, SELECTED_PANEL);
		});
		
		this.btn_toPayment.addActionListener(e -> {
			if (transactionManager.getCurrTransaction().getCart().isEmpty()) {
				this.lbl_totalPrice.setText("Cart is empty!");
				return;
			}
			
			cardLayoutManager.setAllToPaymentMode();
		});
	}

	@Override
	public void clearFields() {
		this.lbl_totalPrice.setText("TOTAL: 0");
		this.lbl_dineType.setText("No dine type selected");
	}
	
	/*
	 * SERVICE METHODS
	 */
	public void updateTotalPrice() {
		String str = "TOTAL: " + Integer.toString(transactionManager.getTotalPrice());
		this.lbl_totalPrice.setText(str);
	}
	
	public CardLayout getCardLayout() {
		return this.layout;
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void updateDineString() {
		String str = transactionManager.getCurrTransaction().getDineType().toString().toUpperCase();
		this.lbl_dineType.setText("For " + str);
	}
	
	private JButton newSwitchDineTypeButton() {
		JButton button = new JButton("Switch Dine Type");
		
		button.addActionListener(e -> {
			DineType type = (transactionManager.getCurrTransaction().getDineType() == DineType.DINE_IN)
					? DineType.TAKEOUT
					: DineType.DINE_IN;
			
			transactionManager.setDineType(type);
			updateDineString();
		});
		
		return button;
	}
	
	private GridBagConstraints newConstraint(int x, int y, int width) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		c.weightx = 1;
		c.weighty = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		return c;
	}
	
	private GridBagConstraints newLabelConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 2;
		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		return c;
	}

}
