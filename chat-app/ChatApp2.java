import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

class ChatDesign2 extends JFrame {
    DefaultListModel<String> messageModel;
    JList<String> messageList;
    JTextField t2;
    DataOutputStream dos;
    DataInputStream dis;

    ChatDesign2() {

        Font f1 = new Font("Arial", Font.BOLD, 30);
        Font f2 = new Font("Arial", Font.PLAIN, 18);

        JLabel title = new JLabel("Client Chat", JLabel.CENTER);
        title.setFont(f1);
        title.setBounds(0, 10, 650, 40);
        title.setForeground(new Color(37, 211, 102));

        messageModel = new DefaultListModel<>();
        messageList = new JList<>(messageModel);
        messageList.setFont(f2);
        messageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane js = new JScrollPane(messageList);
        js.setBounds(20, 60, 640, 460);

        t2 = new JTextField();
        t2.setFont(f2);
        t2.setBounds(20, 550, 380, 45);
        t2.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JButton sendButton = new JButton("Send");
        sendButton.setFont(f2);
        sendButton.setBounds(420, 550, 120, 45);
        sendButton.setBackground(new Color(37, 211, 102));
        sendButton.setForeground(Color.WHITE);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(f2);
        deleteButton.setBounds(550, 550, 120, 45);
        deleteButton.setBackground(new Color(255, 69, 0));
        deleteButton.setForeground(Color.WHITE);

        Container c = getContentPane();
        c.setLayout(null);
        c.add(title);
        c.add(js);
        c.add(t2);
        c.add(sendButton);
        c.add(deleteButton);

        sendButton.addActionListener(
                a -> {
                    try {
                        String message = t2.getText().trim();
                        if (!message.isEmpty()) {
                            dos.writeUTF(message);
                            dos.flush();
                            messageModel.addElement("You: " + message);
                            t2.setText("");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Failed to send message.");
                    }
                }
        );

        deleteButton.addActionListener(
                a -> {
                    int[] selectedIndices = messageList.getSelectedIndices();
                    if (selectedIndices.length == 0) {
                        JOptionPane.showMessageDialog(null, "Please select a message to delete.");
                        return;
                    }

                    String[] options = {"Delete for Me", "Delete for Everyone", "Cancel"};
                    int choice = JOptionPane.showOptionDialog(
                            this, "Do you want to delete for yourself or everyone?",
                            "Delete Message", JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

                    if (choice == 0) {
                        deleteForMe();
                    } else if (choice == 1) {
                        deleteForEveryone();
                    }
                }
        );

        setTitle("Client Chat App");
        setSize(700, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        connectToServer();
    }

    void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 6666);
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            Thread t = new Thread(() -> {
                try {
                    messageModel.addElement("Connected to server...");

                    while (true) {
                        String message = dis.readUTF();
                        if (message.startsWith("DELETE:")) {
                            String target = message.substring(7);
                            for (int i = 0; i < messageModel.size(); i++) {
                                if (messageModel.getElementAt(i).equals(target)) {
                                    messageModel.remove(i);
                                    break;
                                }
                            }
                        } else {
                            messageModel.addElement("Server: " + message);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Connection lost.");
                }
            });
            t.start();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to connect to server.");
        }
    }

    void deleteForMe() {
        int[] a = messageList.getSelectedIndices();
        for (int i = a.length - 1; i >= 0; i--) {
            messageModel.remove(a[i]);
        }
    }

    void deleteForEveryone() {
        int[] b = messageList.getSelectedIndices();

        try {
            for (int i = b.length - 1; i >= 0; i--) {
                String delete = messageModel.getElementAt(b[i]);
                dos.writeUTF("DELETE:" + delete);
                dos.flush();
                messageModel.remove(b[i]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ChatDesign2();
    }
}
