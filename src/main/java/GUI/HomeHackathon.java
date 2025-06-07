package GUI;

import Controller.ControllerHackathon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class HomeHackathon {

    private JPanel panel1;
    private static JFrame frame;
    private JButton mostraTeamInGaraButton;
    private JButton loginButton;
    private JButton registrarsiButton;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JLabel pswLabel;
    private JLabel nomeLabel;
    private final ControllerHackathon controller;



    public static void main(String[] args) {

        frame = new JFrame("Home Hackaton");
        frame.setContentPane(new HomeHackathon().panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public HomeHackathon() {

        controller = new ControllerHackathon();

        registrarsiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataRegistrazione registrazione = new SchermataRegistrazione(controller, frame);
                registrazione.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean log = controller.login(textField1.getText(), passwordField1.getPassword());
                    if (log) {
                        JOptionPane.showMessageDialog(frame, "Login efettuato!");
                        SchermataPartecipante partecipante = new SchermataPartecipante(controller, frame);
                        partecipante.frameP.setVisible(true);
                        frame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Errore Login!");
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        mostraTeamInGaraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SchermataClassifica classifica = new SchermataClassifica(controller, frame);
                classifica.frame.setVisible(true);
                frame.setVisible(false);
            }
        });
    }
}
