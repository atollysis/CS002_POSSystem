package gui.screens;

import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import gui.SystemPanel;
import gui.components.leftpanel.PaymentSelectionPanel;
import gui.components.leftpanel.ordering.OrderingPanel;
import gui.components.rightpanel.CartPanel;
import gui.components.rightpanel.DineTypeInfoPanel;
import runners.MainRunner;

public class _2_OrderingScreen extends SystemPanel {
	
	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final int INSETS = 30;
	public static final String ORDERING_PANEL = "ordering";
	public static final String PAYMENT_PANEL = "payment";
	// Left Panel
	private JPanel leftPanelWrapper;
	private OrderingPanel orderingPanel;
	private PaymentSelectionPanel paymentPanel;
	// Right Panel
	private CartPanel rightPanel;
	// Lower Right Panel
	private DineTypeInfoPanel lowerRightPanel;
	
	/**
	 * Create the panel.
	 */
	public _2_OrderingScreen(MainRunner runner) {
		super(runner);
		cardLayoutManager.setOrderingScreen(this);
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		// Right
		this.rightPanel = new CartPanel(runner);
		this.lowerRightPanel = new DineTypeInfoPanel(runner);
		// Left
		this.leftPanelWrapper = new JPanel();
		this.orderingPanel = new OrderingPanel(runner, this.rightPanel);
		this.paymentPanel = new PaymentSelectionPanel(runner);
	}

	@Override
	protected void setupLayout() {
		CardLayout layout = new CardLayout();
		this.leftPanelWrapper.setLayout(layout);
		leftPanelWrapper.add(this.orderingPanel, ORDERING_PANEL);
		leftPanelWrapper.add(this.paymentPanel, PAYMENT_PANEL);
		layout.show(this.leftPanelWrapper, ORDERING_PANEL);
		
		cardLayoutManager.setPaymentLayout(layout, this.leftPanelWrapper);
//		cardLayoutManager.setLowerRightLayout(this.lowerRightPanel.getCardLayout(), this.lowerRightPanel);
		cardLayoutManager.setLowerRightPanel(this.lowerRightPanel);
		
		this.setLayout(new GridBagLayout());
		this.add(this.leftPanelWrapper, getLeftPanelConstraints());
		this.add(this.rightPanel,       getRightPanelConstraints());
		this.add(this.lowerRightPanel,  getLowerRightPanelConstraints());
//		this.revalidate(); // doesn't work
	}

	@Override
	protected void setupInteractions() {
		// Handled by the components
	}

	@Override
	public void clearFields() {
		this.orderingPanel.clearFields();
		this.paymentPanel.clearFields();
		this.rightPanel.clearFields();
//		this.lowerRightPanel.clearFields();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private static GridBagConstraints getLeftPanelConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, 0, 0, INSETS);
		return c;
	}
	
	private static GridBagConstraints getRightPanelConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(0, INSETS, INSETS, 0);
		return c;
	}
	
	private static GridBagConstraints getLowerRightPanelConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		return c;
	}

}
