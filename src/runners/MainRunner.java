package runners;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.CardLayoutManager;
import gui.SystemPanel;
import gui.screens._1_LoginScreen;
import gui.screens._2_OrderingScreen;
import system.accounts.AccountManager;
import system.menu.MenuManager;
import system.transactions.TransactionManager;

public class MainRunner extends JFrame {
	
	/*
	 * ATTRIBUTES
	 */
	// Panel Names
	public static final String LOGIN_PANEL_NAME = "login";
	public static final String ORDER_PANEL_NAME = "order";
	// Other
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Map<String, SystemPanel> panels;
	private CardLayout layout;
	// Managers
	protected AccountManager accountManager;
	protected MenuManager menuManager;
	protected TransactionManager transactionManager;
	protected CardLayoutManager cardLayoutManager;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainRunner frame = new MainRunner();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainRunner() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		setContentPane(contentPane);
		
		layout = new CardLayout();
		contentPane.setLayout(layout);
		
		// Back
		accountManager = new AccountManager();
		menuManager = new MenuManager();
		transactionManager = new TransactionManager();
		cardLayoutManager = new CardLayoutManager(this, layout);
		
		// Front
		panels = Map.ofEntries(
				Map.entry(LOGIN_PANEL_NAME, new _1_LoginScreen(this)),
				Map.entry(ORDER_PANEL_NAME, new _2_OrderingScreen(this))
				);
		
		for (String constraint : panels.keySet())
			contentPane.add(panels.get(constraint), constraint);
		
		layout.show(contentPane, LOGIN_PANEL_NAME);
		
		cardLayoutManager.setPanels(panels);
		
		setVisible(true);
	}
	
	/*
	 * GETTERS
	 */
	public AccountManager getAccountManager() {
		return this.accountManager;
	}
	
	public MenuManager getMenuManager() {
		return this.menuManager;
	}
	
	public TransactionManager getTransactionManager() {
		return this.transactionManager;
	}
	
	public CardLayoutManager getCardLayoutManager() {
		return this.cardLayoutManager;
	}
	
	public Map<String, SystemPanel> getPanels() {
		return this.panels;
	}

}
