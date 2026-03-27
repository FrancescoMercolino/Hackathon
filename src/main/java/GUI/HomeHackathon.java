package GUI;

import controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HomeHackathon {

    private JPanel panel1;
    private JButton loginButton;
    private JButton registrarsiButton;
    private JTextField fieldNome;
    private JPasswordField fieldPassword;
    private JLabel pswLabel;
    private JLabel nomeLabel;


    private final ControllerHackathon controller;
    private static JFrame frame;


    public static void main(String[] args) {

        frame = new JFrame("Home Hackaton");
        frame.setContentPane(new HomeHackathon().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public HomeHackathon() {

        controller = new ControllerHackathon();

        registrarsiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataRegistrazione registrazione = new SchermataRegistrazione(controller, frame);
                registrazione.mostraPanelRegistrazione();
                frame.setVisible(false);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean log = controller.login(fieldNome.getText(), fieldPassword.getPassword());
                    if (log) {
                        JOptionPane.showMessageDialog(frame, "Login effettuato!");
                        SchermataUtente partecipante = new SchermataUtente(controller, frame, fieldNome.getText());
                        fieldNome.setText("");
                        partecipante.frameP.setVisible(true);
                        frame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore Login!");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage());
                }
            }
        });
    }

}
