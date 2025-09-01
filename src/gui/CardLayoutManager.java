package gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gui.components.rightpanel.CartPanel;
import gui.components.rightpanel.DineTypeInfoPanel;
import gui.screens._2_OrderingScreen;
import runners.MainRunner;

public class CardLayoutManager {
	
	/*
	 * ATTRIBUTES
	 */
	private CardLayout screenLayout;
	private CardLayout lowerRightLayout;
	private CardLayout paymentLayout;
	
	private Container screenPane;
	private Container lowerRightPane;
	private Container paymentPane;
	
	private Map<String, SystemPanel> panels;
	private CartPanel cartPanel;
	
	private List<SystemPanel> clearablePanels;
	
	private DineTypeInfoPanel lowerRightPanel;
	
	/*
	 * CONSTRUCTOR
	 */
	public CardLayoutManager(MainRunner runner, CardLayout screenLayout) {
		this.screenLayout = screenLayout;
		this.screenPane = runner.getContentPane();
		this.panels = runner.getPanels();
		this.clearablePanels = new ArrayList<>();
	}
	
	/*
	 * SERVICE METHODS
	 */
	public void goToLoginScreen() {
		clearAllFields();
		this.screenLayout.show(this.screenPane, MainRunner.LOGIN_PANEL_NAME);
	}
	
	public void goToOrderScreen() {
		clearAllFields();
		this.screenLayout.show(this.screenPane, MainRunner.ORDER_PANEL_NAME);
	}
	
	public void setAllToPaymentMode() {
		flipPaymentLayout(_2_OrderingScreen.PAYMENT_PANEL);
		flipLowerRightLayout(DineTypeInfoPanel.PAYMENT_PANEL);
		this.cartPanel.hideAllButtons();
		clearAllFields();
	}
	
	public void setAllToOrderMode() {
		flipPaymentLayout(_2_OrderingScreen.ORDERING_PANEL);
		flipLowerRightLayout(DineTypeInfoPanel.SELECTED_PANEL);
		this.cartPanel.showAllButtons();
		clearAllFields();
	}
	
	public void reset() {
		flipPaymentLayout(_2_OrderingScreen.ORDERING_PANEL);
		flipLowerRightLayout(DineTypeInfoPanel.EMPTY_PANEL);
		this.cartPanel.reset();
		this.lowerRightPanel.clearFields();
		clearAllFields();
	}
	
	// I don't know where to put this lmao
	public void clearAllFields() {
		for (String name : panels.keySet()) {
			SystemPanel panel = panels.get(name);
			panel.clearFields();
			panel.revalidate();
		}
		for (SystemPanel panel : this.clearablePanels) {
			panel.clearFields();
			panel.revalidate();
		}
	}
	
	public void updateLowerRightTotalPrice() {
		this.lowerRightPanel.updateTotalPrice();
	}
	
	/*
	 * SETTERS + FLIPPERS(?)
	 * We love a god class
	 */
	
	public void setPanels(Map<String, SystemPanel> panels) {
		this.panels = panels;
	}
	
	public void setOrderingScreen(SystemPanel orderingScreen) {
		this.clearablePanels.add(orderingScreen);
	}
	
	public void setLowerRightPanel(DineTypeInfoPanel panel) {
		this.lowerRightPanel = panel;
	}
		
	// Right Panel
	public void setCartPanel(CartPanel panel) {
		this.cartPanel = panel;
	}
	
	// Lower Right
	public void setLowerRightLayout(CardLayout layout, Container parent) {
		this.lowerRightLayout = layout;
		this.lowerRightPane = parent;
	}
	
	public void flipLowerRightLayout(String constraint) {
		this.lowerRightLayout.show(lowerRightPane, constraint);
	}
	
	// Payment
	public void setPaymentLayout(CardLayout layout, Container parent) {
		this.paymentLayout = layout;
		this.paymentPane = parent;
	}
	
	public void flipPaymentLayout(String constraint) {
		this.paymentLayout.show(paymentPane, constraint);
	}
	
	// Screen - set in the constructor
	public void flipScreen(String constraint) {
		this.screenLayout.show(screenPane, constraint);
	}
	
}
