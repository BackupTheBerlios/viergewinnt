package de.fhhn.viergewinnt.ui.gui;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Box;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.*;
import java.awt.event.*;

/**
 * @author $Author: malte $
 * @version $Revision: 1.3 $
 * @since LCA
 */
public class NewGameDialog extends JDialog implements ActionListener {
    private JPanel dialogDesktop = new JPanel();
    private JPanel redDialogPane1 = new JPanel();
    private JPanel redDialogPane2 = new JPanel();
    private JPanel redPlayerTypePane = new JPanel();
    private JLabel redPlayerTypeLabel = new JLabel();
    private JComboBox redPlayerTypeSelector = new JComboBox();
    private JPanel redPlayerHostPane = new JPanel();
    private JLabel redPlayerHostLabel = new JLabel();
    private JTextField redPlayerHostTextField = new JTextField();
    private JPanel yellowDialogPane1 = new JPanel();
    private JPanel yellowDialogPane2 = new JPanel();
    private JPanel yellowPlayerTypePane = new JPanel();
    private JLabel yellowPlayerTypeLabel = new JLabel();
    private JComboBox yellowPlayerTypeSelector = new JComboBox();
    private JPanel yellowPlayerHostPane = new JPanel();
    private JLabel yellowPlayerHostLabel = new JLabel();
    private JTextField yellowPlayerHostTextField = new JTextField();
    private JPanel dialogButtonPane = new JPanel();
    private JButton jButton1 = new JButton();
    private JButton jButton2 = new JButton();

    NewGameDialog() {
        super();
        initGUI();
        setVisible(true);
    }

    private void initGUI() {
        getContentPane().add(Box.createRigidArea(new Dimension(10, 10)),
            java.awt.BorderLayout.NORTH);
        getContentPane().add(Box.createRigidArea(new Dimension(10, 10)),
            java.awt.BorderLayout.EAST);
        getContentPane().add(Box.createRigidArea(new Dimension(10, 10)),
            java.awt.BorderLayout.WEST);
        getContentPane().add(dialogButtonPane, java.awt.BorderLayout.SOUTH);
        getContentPane().add(dialogDesktop, java.awt.BorderLayout.CENTER);
        dialogDesktop.setLayout(new java.awt.GridLayout(1, 2, 10, 10));
        dialogDesktop.setBorder(null);
        dialogDesktop.add(redDialogPane1);
        dialogDesktop.add(yellowDialogPane1);
        redDialogPane1.setBorder(javax.swing.BorderFactory
            .createTitledBorder(javax.swing.BorderFactory
            .createLineBorder(new java.awt.Color(153, 153, 153), 1),
            "Spieler Rot", javax.swing.border.TitledBorder.LEADING,
            javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 0, 11),
            new java.awt.Color(60, 60, 60)));
        redDialogPane1.setLayout(new java.awt.GridLayout(1, 1));
        redDialogPane1.add(redDialogPane2);
        yellowDialogPane1.setBorder(javax.swing.BorderFactory
            .createTitledBorder(javax.swing.BorderFactory
            .createLineBorder(new java.awt.Color(153, 153, 153), 1),
            "Spieler Gelb", javax.swing.border.TitledBorder.LEADING,
            javax.swing.border.TitledBorder.TOP, new java.awt.Font("SansSerif", 0, 11),
            new java.awt.Color(60, 60, 60)));
        yellowDialogPane1.setLayout(new java.awt.GridLayout(1, 1));
        yellowDialogPane1.add(yellowDialogPane2);
        redDialogPane2.setLayout(new java.awt.GridLayout(2, 1));
        redDialogPane2.add(redPlayerTypePane);
        redDialogPane2.add(redPlayerHostPane);
        yellowDialogPane2.setLayout(new java.awt.GridLayout(2, 1));
        yellowDialogPane2.add(yellowPlayerTypePane);
        yellowDialogPane2.add(yellowPlayerHostPane);
        redPlayerTypeLabel.setText("Spielertyp");
        redPlayerTypePane.add(redPlayerTypeLabel);
        redPlayerTypePane.add(redPlayerTypeSelector);
        yellowPlayerTypeLabel.setText("Spielertyp");
        yellowPlayerTypeLabel.setToolTipText("");
        yellowPlayerTypePane.add(yellowPlayerTypeLabel);
        yellowPlayerTypePane.add(yellowPlayerTypeSelector);
        redPlayerHostLabel.setText("Hostadresse");
        redPlayerHostLabel.setToolTipText("");
        redPlayerHostPane.add(redPlayerHostLabel);
        redPlayerHostPane.add(redPlayerHostTextField);
        redPlayerHostTextField.setText("");
        redPlayerHostTextField.setPreferredSize(
            new java.awt.Dimension(118, 20));
        redPlayerHostTextField.setToolTipText("Hostadresse für KI (leer für lokal)");
        yellowPlayerHostLabel.setText("Hostadresse");
        yellowPlayerHostLabel.setToolTipText("");
        yellowPlayerHostPane.add(yellowPlayerHostLabel);
        yellowPlayerHostPane.add(yellowPlayerHostTextField);
        yellowPlayerHostTextField.setText("");
        yellowPlayerHostTextField.setPreferredSize(
            new java.awt.Dimension(118, 20));
        yellowPlayerHostTextField
            .setToolTipText("Hostadresse für KI (leer für lokal)");
        setBounds(new java.awt.Rectangle(0, 0, 485, 168));
        setModal(true);
        setTitle("Neues Spiel");
        setResizable(false);
        jButton1.setText("Ok");
        jButton1.setPreferredSize(new java.awt.Dimension(90, 27));
        dialogButtonPane.add(jButton1);
        dialogButtonPane.add(jButton2);
        jButton2.setText("Abbrechen");
        jButton2.setToolTipText("");
        jButton2.setSize(new java.awt.Dimension(90, 27));
        redPlayerTypeSelector.addItem("Spieler");
        redPlayerTypeSelector.addItem("KI");
        //redPlayerTypeSelector.setEditable(false);
        //redPlayerTypeSelector.setSize(130,24);
        yellowPlayerTypeSelector.addItem("Spieler");
        yellowPlayerTypeSelector.addItem("KI");
        //yellowPlayerTypeSelector.setSize(130,24);
        //yellowPlayerTypeSelector.setEditable(false);
    }

    public void actionPerformed(ActionEvent e) {
        //
    }

    public static void main(String[] args) {
        new NewGameDialog();
    }
}
