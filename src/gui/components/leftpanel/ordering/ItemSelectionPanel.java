package gui.components.leftpanel.ordering;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.components.rightpanel.CartPanel;
import system.menu.ItemSize;
import system.menu.MenuItem;
import system.menu.MenuManager;
import system.ordering.CartItem;
import system.transactions.TransactionManager;

public class ItemSelectionPanel extends JPanel {
	
	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final String ITEM_NAME_DEFAULT = "Please select an item from the menu.";
	// Components
	private JLabel lbl_error;
	
	private JPanel itemInfoPanel;
	private JLabel lbl_itemName;
	private JLabel currImg;
	private ImageIcon emptyImg;
	
	private JPanel itemSizePanel;
	private Map<ItemSize, JButton> buttons;
	
	private JPanel itemQuantityPanel;
	private JLabel lbl_itemSizePrice;
	private JTextField fld_itemQuantity;
	private JButton btn_submit;
	
	private CartPanel cartPanel;
	
	// Manager
	private TransactionManager manager;
	
	/**
	 * Create the panel.
	 */
	public ItemSelectionPanel(
			TransactionManager manager,
			CartPanel cartPanel) {
		this.manager = manager;
		this.cartPanel = cartPanel;
		this.buttons = new HashMap<>();
		
		this.setLayout(new GridBagLayout());
		
		this.lbl_error = new JLabel(" ");
		setupItemInfoPanel();
		setupItemSizePanel();
		setupItemQuantityPanel();
		
		this.add(this.lbl_error,             newLabelConstraint(0, 0));
		this.add(this.itemInfoPanel,     newComponentConstraint(0, 1));
		this.add(this.itemSizePanel,     newComponentConstraint(1, 1));
		this.add(this.itemQuantityPanel, newComponentConstraint(2, 1));
		
		setupInteractions();
		update();
	}
	
	/*
	 * SERVICE METHODS
	 */
	public void reset() {
		this.setItemInfoBlank();
		this.itemSizePanel.setVisible(false);
		this.itemQuantityPanel.setVisible(false);
	}
	
	public void update() {
		if (this.manager.getCurrItem() == null) {
			this.reset();
		}
		// Item not null
		else if (this.manager.getCurrItem().getSize() == null) {
			this.setCurrImg();
			setButtonVisibilities(this.manager.getCurrItem().getItem());
			this.itemSizePanel.setVisible(true);
			this.itemQuantityPanel.setVisible(false);
		}
		// Size not null
		else {
			this.setCurrImg();
			this.itemSizePanel.setVisible(true);
			this.itemQuantityPanel.setVisible(true);
		}
	}
	
	public void clearFields() {
		this.lbl_error.setText(" ");
		this.lbl_itemSizePrice.setText(" ");
//		this.lbl_itemName.setText(ITEM_NAME_DEFAULT);
		this.fld_itemQuantity.setText("");
		this.reset();
		this.revalidate();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void setItemInfoBlank() {
		currImg.setIcon(emptyImg);
		this.lbl_itemName.setText(ITEM_NAME_DEFAULT);
	}
	
	private void setCurrImg() {
		Image img = MenuManager.getItemImage(this.manager.getCurrItem().getItem());
		if (img == null)
			this.currImg.setIcon(null);
		else
			this.currImg.setIcon(new ImageIcon(img));
		
		this.lbl_itemName.setText(manager.getCurrItem().getItem().getName());
	}
	
	private void setButtonVisibilities(MenuItem item) {
		for (ItemSize size : buttons.keySet()) {
			if (item.getSizePriceRange().get(size) == null)
				buttons.get(size).setVisible(false);
			else
				buttons.get(size).setVisible(true);
		}
	}
	
	// Setup
	
	private void setupItemInfoPanel() {
		this.itemInfoPanel = new JPanel();
		itemInfoPanel.setLayout(new BoxLayout(itemInfoPanel, BoxLayout.Y_AXIS));
		
		this.emptyImg = new ImageIcon("data/images/menu/empty.jpg");
		this.currImg = new JLabel(" ");
		currImg.setIcon(emptyImg);
		currImg.setAlignmentX(CENTER_ALIGNMENT);
		
		this.lbl_itemName = new JLabel(ITEM_NAME_DEFAULT);
		lbl_itemName.setAlignmentX(CENTER_ALIGNMENT);
		
		itemInfoPanel.add(currImg);
		itemInfoPanel.add(this.lbl_itemName);
		
		JLabel itemName = new JLabel(" ");
		itemInfoPanel.add(itemName);
	}
	
	private void setupItemSizePanel() {
		this.itemSizePanel = new JPanel();
		BoxLayout layout = new BoxLayout(itemSizePanel, BoxLayout.Y_AXIS);
		itemSizePanel.setLayout(layout);
		
		for (int i = 0; i < ItemSize.values().length; i++) {
			ItemSize size = ItemSize.values()[i];
			JButton button = new JButton(size.toString().toUpperCase());
			button.setAlignmentX(CENTER_ALIGNMENT);
			this.buttons.put(size, button);
			itemSizePanel.add(button, newSizeButtonConstraint(0, i));
		}
	}
	
	private void setupItemQuantityPanel() {
		this.itemQuantityPanel = new JPanel();
		itemQuantityPanel.setLayout(new GridBagLayout());
		// Components
		this.lbl_itemSizePrice = new JLabel(" ");
		JLabel lbl_quantity = new JLabel("Quantity");
		this.fld_itemQuantity = new JTextField();
		this.btn_submit = new JButton("Submit");
		// Layout
		itemQuantityPanel.add(lbl_itemSizePrice, newCenteredConstraint(0, 0));
		itemQuantityPanel.add(lbl_quantity,     newComponentConstraint(0, 1));
		itemQuantityPanel.add(fld_itemQuantity, newComponentConstraint(1, 1));
		itemQuantityPanel.add(btn_submit,        newCenteredConstraint(0, 2));
	}
	
	private void setupInteractions() {
		for (ItemSize size : this.buttons.keySet()) {
			JButton button = this.buttons.get(size);
			button.addActionListener(e -> {
				// Guard
				if (this.manager.getCurrItem() == null)
					return;
				// Idempotent
				if (this.manager.getCurrItem().getSize() == size)
					this.manager.getCurrItem().setSize(null);
				// Set Back
				this.manager.setItemSizePrice(size);
				// Set Front
				MenuItem item = this.manager.getCurrItem().getItem();
				this.lbl_itemSizePrice.setText(String.format(
						"%s: %d",
						size.toString().toUpperCase(),
						item.getPriceOf(size)));
				this.update();
			});
		}
		
		this.btn_submit.addActionListener(e -> {
			// Guards
			if (!currItemComplete()) {
				this.lbl_error.setText("Item details not fully set.");
				return;
			}
			// Guards part 2
			if (!this.fld_itemQuantity.getText().chars().allMatch(Character::isDigit)) {
				this.lbl_error.setText("Invalid quantity.");
				return;
			}
			int quantity = Integer.parseInt(this.fld_itemQuantity.getText());
			if (quantity <= 0) {
				this.lbl_error.setText("Quantity must be positive.");
				return;
			}
			// Set Back
			this.manager.getCurrItem().setQuantity(quantity);
			CartItem item = this.manager.getCurrItem();
			this.manager.addCurrItemToCart(); // resets current item
			// Set Front
			this.cartPanel.addToPanel(item, this.manager.getCartItemIndex(item));
			this.clearFields();
			this.update();
		});
	}
	
	private boolean currItemComplete() {
		CartItem item = this.manager.getCurrItem();
		return item.getItem() != null
			&& item.getSize() != null
			&& item.getPrice() > 0;
	}
	
	// GridBagConstraints
	
	private static GridBagConstraints newLabelConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1;
		c.gridwidth = 3;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}
	
	private static GridBagConstraints newComponentConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}

	private static GridBagConstraints newCenteredConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 2;
		c.weightx = 1;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}
	
	private static GridBagConstraints newSizeButtonConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 1;
		c.weightx = 1;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}
	
}
