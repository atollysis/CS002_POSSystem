package gui.components.rightpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import gui.SystemPanel;
import runners.MainRunner;
import system.ordering.CartItem;

public class CartPanel extends SystemPanel {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension DIMS = new Dimension(300, 500);
	/*
	 * This is for the new CartItemBlocks which also
	 * inherit from SystemPanel. THIS IS SO JANK
	 */
	private MainRunner runner;
	// Components
	private JScrollPane scrollPane;
	private JPanel cartItemBlockWrapper;
	private Map<CartItem, CartItemBlock> cartItemMap;
	
	
	public CartPanel(MainRunner runner) {
		super(runner);
		this.runner = runner;
		this.cartItemMap = new HashMap<>();
		cardLayoutManager.setCartPanel(this);
		
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		this.cartItemBlockWrapper = new JPanel();
		cartItemBlockWrapper.setBackground(Color.WHITE);
		cartItemBlockWrapper.setOpaque(true);
		this.scrollPane = new JScrollPane();
	}

	@Override
	protected void setupLayout() {
//		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setPreferredSize(DIMS);
		this.setBorder(BorderFactory.createEmptyBorder());
		
		BoxLayout layout = new BoxLayout(this.cartItemBlockWrapper, BoxLayout.Y_AXIS);
		this.cartItemBlockWrapper.setLayout(layout);
		
		this.scrollPane.setViewportView(this.cartItemBlockWrapper);
		this.scrollPane.setPreferredSize(DIMS);
		this.add(this.scrollPane);
	}

	@Override
	protected void setupInteractions() {
		// Not used
	}

	@Override
	public void clearFields() {
		// not used
	}
	
	/*
	 * SERVICE
	 */
	public void addToPanel(CartItem item, int index) {
		CartItemBlock block = new CartItemBlock(this.runner, this, item, index);
		this.cartItemMap.put(item, block);
		this.cartItemBlockWrapper.add(block);
		cardLayoutManager.updateLowerRightTotalPrice();
	}
	
	public void removeFromPanel(CartItem item) {
		CartItemBlock block = this.cartItemMap.get(item);
		this.cartItemBlockWrapper.remove(block);
//		this.cartItemBlockWrapper.revalidate();
		cardLayoutManager.updateLowerRightTotalPrice();
		this.getParent().revalidate();
	}
	
	public void hideAllButtons() {
		for (CartItemBlock block : this.cartItemMap.values())
			block.hideButtons();
	}
	
	public void showAllButtons() {
		for (CartItemBlock block : this.cartItemMap.values())
			block.showButtons();
	}
	
	public void reset() {
		resetWrapper();
//		for (CartItem item : this.cartItemMap.keySet())
//			removeFromPanel(item);
		this.cartItemMap.clear();
	}
	
	/*
	 * SUPPORT
	 */
	private void resetWrapper() {
		this.cartItemBlockWrapper = new JPanel();
		cartItemBlockWrapper.setBackground(Color.WHITE);
		cartItemBlockWrapper.setOpaque(true);
		
		BoxLayout layout = new BoxLayout(this.cartItemBlockWrapper, BoxLayout.Y_AXIS);
		this.cartItemBlockWrapper.setLayout(layout);
		
		this.scrollPane.setViewportView(this.cartItemBlockWrapper);
	}

}
