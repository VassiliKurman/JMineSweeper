package vkurman.jminesweeper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * <code>RecordsView</code> is a dialog that displays a list of records in the table
 * for specified difficulty level and enables player to reset difficulty records.
 *
 * <p>
 * Date : 25 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public class RecordsView extends JDialog implements ActionListener {

	private static final long serialVersionUID = -5166168559317947157L;
	
	private Record[] records;
	private RecordsPanel recordsPanel;
	private ControlRequestsListener controlRequestsListener = null;

	/**
	 * Constructor.
	 */
	public RecordsView(Component parent, ControlRequestsListener listener) {
		controlRequestsListener = listener;
		
		records = controlRequestsListener.getRecords();

		recordsPanel = new RecordsPanel();

		showUI(parent);
	}

	/**
	 * Creates and displays UI.
	 */
	private void showUI(Component parent) {
		setTitle("Records");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

		setLayout(new BorderLayout(5, 5));
		getContentPane().add(recordsPanel, BorderLayout.CENTER);
		getContentPane().add(getButtonPanel(), BorderLayout.PAGE_END);

		pack();
		setLocationRelativeTo(parent);
	}

	/**
	 * Creates and returns panel with button to make book reservation.
	 * 
	 * @return JPanel
	 */
	private JPanel getButtonPanel() {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton resetButton = new JButton("Reset");
		resetButton.setActionCommand("Reset");
		resetButton.addActionListener(this);

		JButton closeButton = new JButton("Close");
		closeButton.setActionCommand("Close");
		closeButton.addActionListener(this);

		buttonPane.add(resetButton);
		buttonPane.add(closeButton);

		return buttonPane;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		String ac = ev.getActionCommand();
		if (ac.equals("Reset")) {
			if (controlRequestsListener != null) {
				records = controlRequestsListener.resetRecords();
				recordsPanel.refreshTable();
			}
		} else if (ac.equals("Close")) {
			dispose();
		}
	}

	/**
	 * <code>RecordsPanel</code> displaying records.
	 *
	 * <p>
	 * Date : 23 Nov 2016
	 *
	 * @author Vassili Kurman
	 * @version 1.0
	 */
	class RecordsPanel extends JPanel {
		
		private static final long serialVersionUID = -6502410921065297327L;
		
		private JTable table;
		private RecordsTableModel model;

		/**
		 * Default Constructor.
		 */
		public RecordsPanel() {
			model = new RecordsTableModel();
			table = new JTable(model);

			// table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

			table.getColumnModel().getColumn(0).setPreferredWidth(25);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(2).setPreferredWidth(75);

			add(new JScrollPane(table));
		}

		/**
		 * This method is asking table model to update it's data.
		 */
		public void refreshTable() {
			model.refreshtRecords();
		}

		class RecordsTableModel extends AbstractTableModel {

			private static final long serialVersionUID = -7137172679652492427L;
			
			private String[] columnNames = { "Place", "Name", "Time" };

			public RecordsTableModel() {
				super();
			}

			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			@Override
			public int getRowCount() {
				return records.length;
			}

			@Override
			public Object getValueAt(int row, int col) {
				switch (col) {
				case 0:
					return row + 1;
				case 1:
					return records[row].getName();
				case 2:
					long time = records[row].getTime();
					return (time / 1000 / 60) + "min " + (time / 1000 % 60) + "s";
				default:
					return null;
				}
			}

			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			@Override
			public Class<?> getColumnClass(int col) {
				return (getValueAt(0, col) == null) ? String.class
						: getValueAt(0, col).getClass();
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void refreshtRecords() {
				this.fireTableDataChanged();
			}
		}
	}
}