package gui.screens;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import gui.SystemPanel;
import runners.MainRunner;
import system.accounts.AccountOpResult;

public class _1_LoginScreen extends SystemPanel {
	
	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	// Components
	private JLabel txt_welcome;
	private JLabel txt_error;
	
	private JLabel txt_promptName;
	private JTextField fld_name;
	
	private JLabel txt_promptPin;
	private JPasswordField fld_pin;
	
	private JButton btn_login;
	private JButton btn_create;
	private JButton btn_delete;
	
	/**
	 * Create the panel.
	 */
	public _1_LoginScreen(MainRunner runner) {
		super(runner);
		this.setupComponents();
		this.setupLayout();
		this.setupInteractions();
	}

	@Override
	protected void setupComponents() {
		this.txt_welcome = new JLabel("Welcome!");
		txt_welcome.setFont(new Font("Arial", Font.BOLD, 20));
		
		this.txt_error = new JLabel(" ");
		
		this.txt_promptName = new JLabel("Name");
		this.txt_promptPin = new JLabel("Pin");
		
		this.fld_name = new JTextField();
		this.fld_pin = new JPasswordField();
		
		this.btn_login = new JButton("Login");
		this.btn_create = new JButton("Create");
		this.btn_delete = new JButton("Delete");
	}

	@Override
	protected void setupLayout() {
		this.setLayout(new GridBagLayout());
		
		
		
		JPanel fieldWrapper = new JPanel();
		fieldWrapper.setLayout(new GridBagLayout());
		fieldWrapper.add(txt_promptName, newFieldTextConstraint(0, 0));
		fieldWrapper.add(fld_name,           newFieldConstraint(1, 0));
		fieldWrapper.add(txt_promptPin,  newFieldTextConstraint(0, 1));
		fieldWrapper.add(fld_pin,            newFieldConstraint(1, 1));
		
		JPanel finalWrapper = new JPanel();
		finalWrapper.setLayout(new GridBagLayout());
		finalWrapper.setPreferredSize(new Dimension(300, 500));
		
		finalWrapper.add(txt_welcome, newCenteredTextConstraint(0, 0, 30));
		finalWrapper.add(txt_error,   newCenteredTextConstraint(0, 1, 5));
		finalWrapper.add(fieldWrapper, newWrapperConstraint(0, 2));
		finalWrapper.add(btn_login,     newButtonConstraint(0, 3));
		finalWrapper.add(btn_create,    newButtonConstraint(1, 3));
		finalWrapper.add(btn_delete,    newButtonConstraint(2, 3));
		
		this.add(finalWrapper);
	}

	@Override
	protected void setupInteractions() {
		this.btn_login.addActionListener(e -> {
			AccountOpResult result = this.accountManager.login(
					fld_name.getText(),
					new String(fld_pin.getPassword()));
			
			switch (result) {
				case SUCCESS:
					transactionManager.newTransaction(accountManager.getCurrAccount().getID());
					this.cardLayoutManager.goToOrderScreen();
					break;
					
				case INVALID_ACCOUNT_FORMAT:
					this.txt_error.setText("Invalid account name.");
					break;
					
				case INVALID_NO_ACCOUNT:
					this.txt_error.setText("Account does not exist.");
					break;
					
				case INVALID_PIN_FORMAT:
					this.txt_error.setText("Invalid pin entered.");
					break;
					
				case INVALID_PIN_WRONG:
					this.txt_error.setText("Incorrect pin entered.");
					break;

				default:
					// INVALID_ACCOUNT_EXISTS
					// INVALID_DELETION_NO_ACCS
					throw new IllegalStateException("Unexpected operation result: " + result);
			}
		});
		
		this.btn_create.addActionListener(e -> {
			AccountOpResult result = this.accountManager.createAccount(
					fld_name.getText(),
					new String(fld_pin.getPassword()));
			
			switch (result) {
				case SUCCESS:
					this.txt_error.setText("Account successfully created!");
					break;
			
				case INVALID_ACCOUNT_EXISTS:
					this.txt_error.setText("Account already exists.");
					break;
					
				case INVALID_ACCOUNT_FORMAT:
					this.txt_error.setText("Invalid account format. No symbols like ,\";");
					break;
					
				case INVALID_PIN_FORMAT:
					this.txt_error.setText("Incorrect pin format. Numbers only.");
					break;

				default:
					// INVALID_DELETION_NO_ACCS
					// INVALID_NO_ACCOUNT
					// INVALID_PIN_WRONG
					throw new IllegalStateException("Unexpected operation result: " + result);
			}
		});
		
		this.btn_delete.addActionListener(e -> {
			AccountOpResult result = this.accountManager.deleteAccount(
					fld_name.getText(),
					new String(fld_pin.getPassword()));
			
			switch (result) {
			case SUCCESS:
				this.txt_error.setText("Account has been successfully deleted.");
				break;
				
			case INVALID_ACCOUNT_FORMAT:
				this.txt_error.setText("Invalid account name.");
				break;
				
			case INVALID_DELETION_NO_ACCS:
				this.txt_error.setText("No account found with that name.");
				break;
				
			case INVALID_NO_ACCOUNT:
				this.txt_error.setText("Account does not exist.");
				break;
				
			case INVALID_PIN_FORMAT:
				this.txt_error.setText("Invalid pin entered.");
				break;
				
			case INVALID_PIN_WRONG:
				this.txt_error.setText("Incorrect pin entered.");
				break;

			default:
				// INVALID_ACCOUNT_EXISTS
				throw new IllegalStateException("Unexpected operation result: " + result);
		}
		});
	}
	
	@Override
	public void clearFields() {
		this.fld_name.setText("");
		this.fld_pin.setText("");
		this.txt_error.setText(" ");
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private static GridBagConstraints newCenteredTextConstraint(int x, int y, int bottomInset) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 3;
		c.weightx = 1; // stretch horizontally to fill
		c.insets = new Insets(5, 5, bottomInset, 5);
		return c;
	}
	
	private static GridBagConstraints newWrapperConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = 3;
		c.weightx = 1; // stretch horizontally to fill
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0);
		return c;
	}
	
	private static GridBagConstraints newFieldTextConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(0, 0, 0, 0);
		return c;
	}
	
	private static GridBagConstraints newFieldConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1; // stretch horizontally to fill
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		return c;
	}
	
	private static GridBagConstraints newButtonConstraint(int x, int y) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		return c; 
	}

}
