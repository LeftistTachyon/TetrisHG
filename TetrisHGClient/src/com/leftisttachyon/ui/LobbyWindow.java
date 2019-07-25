package com.leftisttachyon.ui;

import com.leftisttachyon.comm.ClientSocket;
import com.leftisttachyon.tetris.ui.TetrisFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

/**
 * A window that displays the GUI
 *
 * @author Oracle, Jed Wang
 */
public class LobbyWindow extends JFrame {

    /**
     * A storage of the statuses of each user
     */
    private HashMap<String, Boolean> busy;

    /**
     * Are you in a game?
     */
    private boolean inGame;

    /**
     * A temp variable
     */
    private TetrisFrame tFrame = null;

    /**
     * Your name
     */
    private final String[] name;
    
    /**
     * Whether you are challenging someone or not
     */
    private boolean challenging;

    /**
     * Creates new form LobbyWindow
     */
    public LobbyWindow() {
        initComponents();

        busy = new HashMap<>();
        inGame = false;
        challenging = false;

        name = new String[]{null};
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        infoPanel = new InfoPanel();
        chatSP = new JScrollPane();
        chatTP = new JTextPane();
        playerLabel = new JLabel();
        chatLabel = new JLabel();
        chatTextField = new JTextField();
        playerListSP = new JScrollPane();
        playerList = new JList<>();
        playerLModel = new DefaultListModel<>();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Lobby");

        infoPanel.setBackground(new Color(255, 255, 255));
        infoPanel.setBorder(BorderFactory.createLineBorder(
                new Color(130, 135, 144)));
        infoPanel.setPreferredSize(new Dimension(0, 150));

        chatTP.setEditable(false);
        chatSP.setViewportView(chatTP);

        playerLabel.setFont(new Font("Segoe UI Semilight", 0, 20)); // NOI18N
        playerLabel.setText("Players");

        chatLabel.setFont(new Font("Segoe UI Semilight", 0, 20)); // NOI18N
        chatLabel.setText("Lobby Chat");

        chatTextField.setFont(new Font("Segoe UI", 0, 11)); // NOI18N
        chatTextField.addActionListener(this::sendLobbyMessage);

        playerList.setFont(new Font("Segoe UI", 0, 14)); // NOI18N
        playerList.setModel(playerLModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // playerList.addListSelectionListener(this::playerSelected);
        playerListSP.setViewportView(playerList);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(infoPanel, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(playerLabel, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                                                        .addComponent(playerListSP))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(chatSP)
                                                        .addComponent(chatLabel, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                                                        .addComponent(chatTextField))))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(playerLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(chatLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(chatSP, GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(chatTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(playerListSP))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(infoPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>

    /**
     * Invoked when the client hits the ENTER key after typing in the box.
     *
     * @param evt a description of the event
     */
    private void sendLobbyMessage(ActionEvent evt) {
        String message = chatTextField.getText();
        chatTextField.setText("");

        if (!message.equals("") && ClientSocket.isConnected()) {
            ClientSocket.getConnection().send("NLM" + message);
        }
    }

    /**
     * Invoked when a new player is selected
     *
     * @param evt a description of the event
     */
    /*private void playerSelected(ListSelectionEvent evt) {                                        
        
    }*/
    /**
     * Adds a message to the lobby chat
     *
     * @param message the message to add
     */
    public void addLobbyMessage(String message) {
        chatTP.setText(chatTP.getText() + message + "\n");
    }

    /**
     * Adds a player to the lobby list
     *
     * @param name the name of the player
     */
    public void addPlayer(String name) {
        int i;
        for (i = 0; i < playerLModel.getSize(); i++) {
            if (name.compareTo(playerLModel.getElementAt(i)) < 0) {
                break;
            }
        }
        playerLModel.add(i, name);
        busy.put(name, false);
    }

    /**
     * Removes a player from the lobby list
     *
     * @param name the name of the player
     */
    public void removePlayer(String name) {
        playerLModel.removeElement(name);
    }

    /**
     * Creates and shows a LobbyWindow
     *
     * @return the LobbyWindow which is on screen
     */
    public static LobbyWindow run() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.LookAndFeelInfo[] installedLookAndFeels
                    = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
                if ("Nimbus".equals(installedLookAndFeel.getName())) {
                    UIManager.setLookAndFeel(installedLookAndFeel.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        LobbyWindow lw = new LobbyWindow();
        EventQueue.invokeLater(() -> {
            lw.setMinimumSize(new Dimension(658, 326));
            lw.setResizable(true);
            lw.setVisible(true);
        });

        new Thread(lw.infoPanel).start();
        
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        lw.setLocation(
                (ss.width - lw.getWidth()) / 2,
                (ss.height - lw.getHeight()) / 2);
        
        //<editor-fold defaultstate="collapsed" desc="listener">
        Consumer<String> listener = (line) -> {
            int temp = 0;

            if (line.startsWith("NEWCLIENT")) {
                // add a client to the pool
                String[] data = line.substring(9).split(" ");

                String newClient = data[1];
                if ("null".equals(newClient)) {
                    return;
                }
                System.out.println("new client: " + newClient);
                lw.addPlayer(newClient);
                lw.busy.put(newClient, false);

                if (Boolean.parseBoolean(data[0])) {
                    lw.addLobbyMessage(newClient + " has joined");
                }
            } else if (line.startsWith("REMOVECLIENT")) {
                // remove a client from the pool
                String toRemove = line.substring(12);
                if ("null".equals(toRemove)) {
                    return;
                }
                lw.removePlayer(toRemove);
                lw.busy.remove(toRemove);
                lw.addLobbyMessage(toRemove + " has left");
            } else if (line.startsWith("BUSY")) {
                lw.busy.put(line.substring(4), true);
            } else if (line.startsWith("FREE")) {
                String toFree = line.substring(4);
                lw.busy.put(toFree, false);
            } else if (line.startsWith("NLM")) {
                lw.addLobbyMessage(line.substring(3));
            } else {
                if (lw.inGame) {
                    if (line.equals("ST")) {
                        new Thread(() -> {
                            lw.tFrame = new TetrisFrame();
                            Dimension geg = lw.getSize();
                            lw.tFrame.setLocation(
                                    lw.getX() + (geg.width - lw.tFrame.getWidth()) / 2,
                                    lw.getY() + (geg.height - lw.tFrame.getHeight()) / 2);
                            lw.tFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent e) {
                                    System.err.println("Closin\'!");
                                    super.windowClosing(e);
                                    lw.inGame = false;
                                    ((TetrisFrame) e.getWindow()).stop();
                                    ClientSocket.getConnection().sendImmediately("EXIT");
                                }
                            });
                            lw.tFrame.start();
                        }).start();
                    } else if (line.equals("EXIT")) {
                        System.err.println("The other person has left "
                                + "the match.");
                        lw.inGame = false;
                        lw.tFrame.stop();
                        JOptionPane.showMessageDialog(lw,
                                "Your opponent has disconnected.",
                                "Opponent disconnect",
                                JOptionPane.INFORMATION_MESSAGE);
                        lw.tFrame.dispose();
                    } else if (line.startsWith("NM")) {
                        String message = line.substring(2);
                        System.err.println("OPPONENT: " + message);
                    }
                } else {
                    if (line.startsWith("SUBMITNAME")) {
                        // submit your name, duh
                        lw.name[0] = lw.getName(temp++ == 0);
                        ClientSocket.getConnection().send(lw.name[0]);
                        System.out.println("Your name is: " + lw.name[0]);
                        lw.busy.put(lw.name[0], false);
                    } else if (line.startsWith("NAMEACCEPTED")) {
                        // the server has accepted your name
                        temp = 0;
                        // init stuff
                    } else if (line.startsWith("CHALLENGE_C")) {
                        // I'm being challenged!
                        lw.challenging = true;
                        
                        String challenger = line.substring(11);
                        int choice = JOptionPane.showConfirmDialog(lw,
                                challenger + " has challenged you!\nDo you accept?",
                                "Challenge", JOptionPane.YES_NO_OPTION,
                                JOptionPane.INFORMATION_MESSAGE);
                        // whether I accept the challenge
                        boolean accepted = choice == JOptionPane.YES_OPTION;
                        if (lw.busy.get(challenger)) {
                            JOptionPane.showMessageDialog(lw, 
                                    "Well, it appears that your opponent is busy right now.", 
                                    "Match cancelled", 
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            lw.inGame = accepted;
                            ClientSocket.getConnection().send("CHALLENGE_R"
                                    + challenger + " " + accepted);
                        }
                        
                        lw.challenging = false;
                    } else if (line.startsWith("CHALLENGE_R")) {
                        lw.inGame = Boolean.parseBoolean(line.substring(11));
                        System.out.println(lw.inGame);
                        lw.challenging = false;
                    }
                }
            }
        };
        //</editor-fold>
        
        ClientSocket.setFrame(lw);
        ClientSocket.connectViaGUI();
        ClientSocket.getConnection().addServerListener(listener);

        return lw;
    }

    //<editor-fold defaultstate="collapsed" desc="Variables declaration - do not modify">
    private JLabel chatLabel;
    private JScrollPane chatSP;
    private JTextPane chatTP;
    private JTextField chatTextField;
    private InfoPanel infoPanel;
    private JLabel playerLabel;
    private JList<String> playerList;
    private JScrollPane playerListSP;
    private DefaultListModel<String> playerLModel;
    //</editor-fold>

    /**
     * A JPanel that self-updates and displays information about the player.
     */
    private final class InfoPanel extends JPanel implements Runnable {

        /**
         * The MouseListener that is listening in to this InfoPanel.
         */
        private InfoMouseListener iml;

        /**
         * Creates a new InfoPanel.
         */
        public InfoPanel() {
            iml = new InfoMouseListener();
            addMouseListener(iml);
        }

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ex) {
                    System.err.println("Sleeping interrupted.");
                }
            }
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            String selectedPlayer = playerList.getSelectedValue();
            if (selectedPlayer != null) {
                Graphics2D g2D = (Graphics2D) g;
                g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int cbh = challengeButtonHeight();
                g2D.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g2D.setFont(new Font("Consolas", Font.PLAIN, cbh));
                g2D.setColor(Color.black);
                g2D.drawString(selectedPlayer, 5, cbh + 5);

                boolean isBusy = busy.get(selectedPlayer);
                if (isBusy) {
                    g2D.setColor(Color.red);
                } else {
                    g2D.setColor(Color.green);
                }
                int x = (int) (cbh * 0.55) * selectedPlayer.length();
                g2D.fillOval(x, cbh, 10, 10);
                g2D.setColor(Color.black);
                g2D.drawOval(x, cbh, 10, 10);

                g2D.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                int cbx = challengeButtonX(), cby = challengeButtonY(),
                        cbw = challengeButtonWidth();
                if (isBusy || challenging || busy.get(name[0])) {
                    g2D.setColor(Color.lightGray);
                    g2D.fillRect(cbx, cby, cbw, cbh);
                    g2D.setColor(Color.gray);
                } else {
                    g2D.setColor(Color.gray);
                    g2D.fillRect(cbx, cby, cbw, cbh);
                    g2D.setColor(Color.darkGray);
                }
                g2D.drawRect(cbx, cby, cbw, cbh);
                g2D.setFont(new Font("Consolas", Font.PLAIN,
                        Math.min(cbh - 10, cbw / 5)));
                g2D.drawString("CHALLENGE", cbx + 5, cby + cbh - 10);
            }
        }

        /**
         * Notifies this InfoPanel that the mouse was released here
         *
         * @param p the point where the mouse was released
         */
        private void mouseReleased(Point p) {
            int cbx = challengeButtonX(), cby = challengeButtonY();
            if (p.x >= cbx && p.x <= cbx + challengeButtonWidth()
                    && p.y >= cby && p.y <= cby + challengeButtonHeight()
                    && !challenging
                    && !busy.get(name[0])
                    && !busy.get(playerList.getSelectedValue())
                    && ClientSocket.isConnected()) {
                // challenge(playerList.getSelectedValue());
                // out.println("CHALLENGE_C" + player);
                ClientSocket.getConnection().send("CHALLENGE_C"
                        + playerList.getSelectedValue());

                challenging = true;
                
                JOptionPane.showMessageDialog(LobbyWindow.this,
                        "Successfully challenged "
                        + playerList.getSelectedValue(),
                        "Challenge sent", JOptionPane.PLAIN_MESSAGE);
            }
        }

        /**
         * Determines the width of the challenge button.
         *
         * @return the width of the challenge button
         */
        private int challengeButtonWidth() {
            return getWidth() / 3;
        }

        /**
         * Determines the height of the challenge button.
         *
         * @return the height of the challenge button
         */
        private int challengeButtonHeight() {
            return getHeight() / 3;
        }

        /**
         * Determines the x-coordinate of the challenge button.
         *
         * @return the x-coordinate of the challenge button
         */
        private int challengeButtonX() {
            return (getWidth() * 2) / 3 - 5;
        }

        /**
         * Determines the y-coordinate of the challenge button.
         *
         * @return the y-coordinate of the challenge button
         */
        private int challengeButtonY() {
            return (getHeight() * 2) / 3 - 5;
        }
    }

    /**
     * A MouseListener that listens into mouse events inside the InfoPanel.
     */
    private class InfoMouseListener extends MouseAdapter {

        private boolean mouseDown;

        /**
         * Creates a new InfoMouseListener.
         */
        private InfoMouseListener() {
            this.mouseDown = false;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mouseDown = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mouseDown = false;
                infoPanel.mouseReleased(e.getPoint());
            }
        }

        /**
         * Returns whether the left mouse button is pressed down
         *
         * @return whether the left mouse button is pressed down
         */
        public boolean isMouseDown() {
            return mouseDown;
        }
    }

    /**
     * Prompt for and return the desired screen name.
     *
     * @param again whether this method needs to state not to enter the same
     * name again
     */
    private String getName(boolean again) {
        String s = null;
        do {
            s = JOptionPane.showInputDialog(
                    this,
                    again ? "Choose a screen name (no spaces):" : "Choose a different screen name (no spaces):",
                    "Screen name selection",
                    JOptionPane.PLAIN_MESSAGE);
            if (s == null) {
                System.exit(0);
            }
        } while (s.contains(" ") || "".equals(s));
        return s;
    }

    /**
     * Exits the current game.
     */
    public void exitGame() {
        ClientSocket.getConnection().sendImmediately("EXIT");
        inGame = false;
    }
}
