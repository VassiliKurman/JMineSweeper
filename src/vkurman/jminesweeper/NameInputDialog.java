package vkurman.jminesweeper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * <code>NameInputDialog</code> is a basic JDialog that asks user to input his name to save
 * new record.
 *
 * <p>
 * Date : 25 Nov 2016
 *
 * @author Vassili Kurman
 * @version 1.0
 */
public class NameInputDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 3690609757781858213L;

	private JTextField input;
	private String text;
	private boolean okPressed;

	/**
	 * Constructor
	 */
	public NameInputDialog(Component parent) {

		showUI(parent);
	}

	/**
	 * Creates and displays UI.
	 */
	private void showUI(Component parent) {
		setTitle("Name entry dialog");
		getContentPane().setLayout(new BorderLayout(10, 10));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());

		input = new JTextField(text, 30);

		textPanel.add(input);
		getContentPane().add(textPanel, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);

		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	/**
	 * Returns input text.
	 * 
	 * @return String
	 */
	public String getInput() {
		return text;
	}

	/**
	 * Returns true if <code>OK</code> button was pressed.
	 * 
	 * @return boolean
	 */
	public boolean isOkPressed() {
		return okPressed;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String action = ae.getActionCommand();

		if (action.equals("OK")) {
			text = input.getText();
			okPressed = true;
			dispose();
		} else {
			dispose();
		}
	}
}