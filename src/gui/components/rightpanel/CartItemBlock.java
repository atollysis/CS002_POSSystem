package gui.components.rightpanel;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import gui.SystemPanel;
import runners.MainRunner;
import system.ordering.CartItem;

public class CartItemBlock extends SystemPanel {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final int NAME_LENGTH = 30;
	private static final char PADDING_CHAR = '.';
	private static final Font FONT = new Font("Monospaced", Font.BOLD, 12);
	// Components
	private JLabel lbl_text;
	private JButton btn_increment;
	private JButton btn_decrement;
	private JButton btn_delete;
	
	private CartPanel parent;
	// Back
	private CartItem item;
	private int itemIndex;
	
	/**
	 * Create the panel.
	 */
	public CartItemBlock(
			MainRunner runner,
			CartPanel parent,
			CartItem item,
			int itemIndex) {
		super(runner);
		this.parent = parent;
		this.item = item;
		this.itemIndex = itemIndex;
		
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
		
		this.updateString();
	}
	
	/*
	 * SERVICE METHOD
	 */
	
	public void updateString() {
		String name = item.getItem().getName();
		Integer quantity = item.getQuantity();
		Integer totalPrice = item.getPrice() * quantity;
		int padLength = NAME_LENGTH
				- name.length()
				- quantity.toString().length()
				- totalPrice.toString().length()
				- 3; // 2 spaces, then 'x'
		
		this.lbl_text.setText(String.format(
				"%s x%d.%s%d",
				name,
				quantity,
				getPadding(padLength),
				totalPrice)
				);
	}
	
	public void hideButtons() {
		this.btn_decrement.setVisible(false);
		this.btn_increment.setVisible(false);
		this.btn_delete.setVisible(false);
	}
	
	public void showButtons() {
		this.btn_decrement.setVisible(true);
		this.btn_increment.setVisible(true);
		this.btn_delete.setVisible(true);
	}
	
	/*
	 * SUPPORT METHOD
	 */
	
	private String getPadding(int length) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < length; i++)
			output.append(PADDING_CHAR);
		return output.toString();
	}

	/*
	 * OVERRIDDEN METHODS
	 */
	
	@Override
	protected void setupComponents() {
		this.setOpaque(false);
		
		this.lbl_text = new JLabel(" ");
		lbl_text.setFont(FONT);
		
		this.btn_increment = new JButton("+");
		this.btn_decrement = new JButton("-");
		this.btn_delete = new JButton("\u2715");
		btn_increment.setFont(FONT);
		btn_decrement.setFont(FONT);
		btn_delete.setFont(FONT);
	}

	@Override
	protected void setupLayout() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.add(lbl_text);
		this.add(Box.createRigidArea(new Dimension(5, 5)));
//		this.add(btn_decrement);
//		this.add(btn_increment);
		this.add(btn_delete);
	}

	@Override
	protected void setupInteractions() {
		this.btn_decrement.addActionListener(e -> {
			transactionManager.decrementQuantity();
			updateString();
		});
		
		this.btn_increment.addActionListener(e -> {
			transactionManager.incrementQuantity();
			updateString();
		});
		
		this.btn_delete.addActionListener(e -> {
			transactionManager.removeCartItemAt(this.itemIndex);
			this.parent.removeFromPanel(this.item);
		});
	}

	@Override
	public void clearFields() {
		// Not used
	}

}
