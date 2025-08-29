package gui;

import javax.swing.JPanel;

public abstract class SystemPanel extends JPanel {
	/*
	 * ATTRIBUTES i guess
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * CONSTRUCTOR
	 */
	public SystemPanel() {
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}
	
	protected abstract void setupComponents();
	protected abstract void setupLayout();
	protected abstract void setupInteractions();
	
	/*
	 * SERVICE METHODS
	 */
	
	// GridBagConstraints
	// test commit
}
