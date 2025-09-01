package gui.components.leftpanel.ordering;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import system.menu.ItemType;
import system.menu.MenuItem;
import system.menu.MenuManager;
import system.transactions.TransactionManager;

public class MenuPanel extends JTabbedPane {
	
	/*
	 * ATTRIBUTES
	 */
	private static final long serialVersionUID = 1L;
	private static final int GRID_GAPS = 10;
	
	private List<MenuItem> menu;
	private TransactionManager manager;
	private ItemSelectionPanel selectionPanel;
	
	/**
	 * Create the panel.
	 */
	public MenuPanel(List<MenuItem> menu, TransactionManager manager, ItemSelectionPanel selectionPanel) {
		this.menu = menu;
		this.manager = manager;
		this.selectionPanel = selectionPanel;
		this.setupSelf();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void setupSelf() {
		this.setPreferredSize(new Dimension(600, 400));
		
		for (ItemType type : ItemType.values()) {
			JScrollPane pane = new JScrollPane(createPanelBy(type));
			this.addTab(type.toString().toUpperCase(), pane);
		}
	}
	
	private JPanel createPanelBy(ItemType type) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 3, GRID_GAPS, GRID_GAPS));
		
		this.menu.stream()
				.filter(item -> item.getItemType() == type)
				.forEach(item -> panel.add(newItemButton(item)));
		
		return panel;
	}
	
	private JButton newItemButton(MenuItem item) {
		// Image
		Image resized = MenuManager.getItemImage(item);
		JButton button = new JButton(new ImageIcon(resized));
		// Interaction
		button.addActionListener(e -> {
			// Idempotent
			if (this.manager.getCurrItem() != null
					&& this.manager.getCurrItem().getItem().equals(item)) {
				manager.unsetItem();
			} else {
				this.manager.setItem(item);
			}
			this.selectionPanel.update();
		});
		
		return button;
	}
}
