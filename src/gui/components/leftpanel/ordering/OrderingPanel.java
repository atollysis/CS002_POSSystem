package gui.components.leftpanel.ordering;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

import gui.SystemPanel;
import gui.components.rightpanel.CartPanel;
import runners.MainRunner;
import system.menu.MenuManager;

public class OrderingPanel extends SystemPanel {

	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	// Components
	private MenuPanel menuPanel;
	private ItemSelectionPanel itemSelectionPanel;
	
	private CartPanel cartPanel;
	
	/**
	 * Create the panel.
	 */
	public OrderingPanel(MainRunner runner, CartPanel cartPanel) {
		super(runner);
		this.cartPanel = cartPanel;
		
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		this.itemSelectionPanel = new ItemSelectionPanel(
				transactionManager,
				this.cartPanel);
		this.menuPanel = new MenuPanel(
				MenuManager.getAllItems(), 
				transactionManager,
				this.itemSelectionPanel);
	}

	@Override
	protected void setupLayout() {
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.menuPanel);
		this.add(this.itemSelectionPanel);
	}

	@Override
	protected void setupInteractions() {
		// Already done within the components
	}

	@Override
	public void clearFields() {
		this.itemSelectionPanel.clearFields();
	}

}
