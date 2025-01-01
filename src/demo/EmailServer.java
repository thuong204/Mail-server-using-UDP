package demo;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

public class EmailServer {
    private static final String ROOT_DIR = "server_accounts/";

    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(8080);

        byte[] receiveBuffer = new byte[1024];

        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            serverSocket.receive(receivePacket);

            String command = new String(receivePacket.getData(), 0, receivePacket.getLength());
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            if (command.startsWith("CREATE_ACCOUNT")) {
                String accountName = command.split(" ")[1];
                createAccount(accountName);
            } else if (command.startsWith("SEND_EMAIL")) {
                String[] parts = command.split(" ", 3);
                String accountName = parts[1];
                String emailContent = parts[2];
                saveEmail(accountName, emailContent);
            } else if (command.startsWith("LOGIN")) {
                String accountName = command.split(" ")[1];
                sendFileList(accountName, serverSocket, clientAddress, clientPort);
            }
        }
    }

    private static void createAccount(String accountName) throws IOException {
        File accountDir = new File(ROOT_DIR + accountName);
        if (!accountDir.exists()) {
            accountDir.mkdirs();
            File newEmail = new File(accountDir, "new_email.txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(newEmail))) {
                writer.write("Thank you for using this service. We hope that you will feel comfortable...");
            }
        }
    }

    private static void saveEmail(String accountName, String emailContent) throws IOException {
        File accountDir = new File(ROOT_DIR + accountName);
        if (accountDir.exists()) {
            File emailFile = new File(accountDir, "email_" + System.currentTimeMillis() + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(emailFile))) {
                writer.write(emailContent);
            }
        }
    }

    private static void sendFileList(String accountName, DatagramSocket socket, InetAddress clientAddress, int clientPort) throws IOException {
        File accountDir = new File(ROOT_DIR + accountName);
        if (accountDir.exists()) {
            String[] fileList = accountDir.list();
            StringBuilder fileListString = new StringBuilder();
            for (String fileName : fileList) {
                fileListString.append(fileName).append("\n");
            }

            byte[] sendData = fileListString.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            socket.send(sendPacket);
        } else {
            String error = "Account not found.";
            byte[] sendData = error.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            socket.send(sendPacket);
        }
    }
}
