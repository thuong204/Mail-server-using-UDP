package demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class EmailClient {

    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Email Client");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField usernameField = new JTextField();
        JButton loginButton = new JButton("Login");
        JTextArea fileListArea = new JTextArea();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(usernameField, BorderLayout.NORTH);
        panel.add(loginButton, BorderLayout.CENTER);
        panel.add(new JScrollPane(fileListArea), BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    socket = new Socket("localhost", 8080);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out = new PrintWriter(socket.getOutputStream(), true);

                    String username = usernameField.getText();
                    out.println("LOGIN " + username);

                    fileListArea.setText("");
                    String line;
                    while ((line = in.readLine()) != null) {
                        fileListArea.append(line + "\n");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
