package gui;

import javax.swing.JPanel;

import runners.MainRunner;
import system.accounts.AccountManager;
import system.menu.MenuManager;
import system.transactions.TransactionManager;

public abstract class SystemPanel extends JPanel {
	/*
	 * ATTRIBUTES i guess
	 */
	private static final long serialVersionUID = 1L;
	protected MainRunner runner;
	// Managers
	protected AccountManager accountManager;
	protected MenuManager menuManager;
	protected TransactionManager transactionManager;
	protected CardLayoutManager cardLayoutManager;
	
	/*
	 * CONSTRUCTOR
	 */
	public SystemPanel(MainRunner runner) {
		this.runner = runner;
		this.accountManager = runner.getAccountManager();
		this.menuManager = runner.getMenuManager();
		this.transactionManager = runner.getTransactionManager();
		this.cardLayoutManager = runner.getCardLayoutManager();
//		this.setupComponents();
//		this.setupLayout();
//		this.setupInteractions();
	}
	
	protected abstract void setupComponents();
	protected abstract void setupLayout();
	protected abstract void setupInteractions();
	
	public abstract void clearFields();
	
	/*
	 * SERVICE METHODS
	 */
	
	// GridBagConstraints
}
