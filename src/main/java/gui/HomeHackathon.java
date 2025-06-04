package GUI;

import Controller.ControllerHackathon;
import Model.Piattaforma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        frame.setSize(900, 600);
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
                controller.login(textField1.getText(), passwordField1.getText());
            }
        });
    }
}
