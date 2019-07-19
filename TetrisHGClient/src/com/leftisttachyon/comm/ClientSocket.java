package com.leftisttachyon.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import javax.swing.JOptionPane;

/**
 * A wrapper class of a Socket that streamlines communication with a server.
 *
 * @author Jed Wang
 * @since 0.9.1
 */
public final class ClientSocket {

    /**
     * The socket in where the communications take place
     */
    private final Socket socket;

    /**
     * The BufferedReader which reads out of the socket
     */
    private BufferedReader in;

    /**
     * The PrintWriter which writes into the socket
     */
    private PrintWriter out;

    /**
     * A deque that queues up requests to the server
     */
    private final BlockingDeque<String> toSend;

    /**
     * A queue that queues up things to be read
     */
    private final BlockingQueue<String> toReceive;

    /**
     * Whether this ClientSocket is currently communicating with the server
     */
    private volatile boolean communicating = false;

    /**
     * Creates a new ClientSocket, which attempts to establish a connection to
     * the given host.
     *
     * @param host the host to connect to
     * @throws IOException if a connection could not be made
     * @see Socket#Socket(java.lang.String, int)
     */
    private ClientSocket(String host) throws IOException {
        in = null;
        out = null;
        toSend = new LinkedBlockingDeque<>();
        toReceive = new LinkedBlockingQueue<>();

        socket = new Socket(host, 9001);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        startCommunications();
    }

    /**
     * The current connection
     */
    private static ClientSocket CURRENT = null;

    /**
     * Opens a new connection to the given host and returns whether the
     * operation was successful. If there was a previous connection open, then
     * this method attempts to close it.
     *
     * @param host the host to connect to
     * @return whether the operation was successful
     */
    public static boolean connectTo(String host) {
        if (CURRENT == null) {
            try {
                CURRENT = new ClientSocket(host);
                return true;
            } catch (IOException ex) {
                CURRENT = null;
                return false;
            }
        } else {
            ClientSocket temp = CURRENT;
            try {
                CURRENT = new ClientSocket(host);
                temp.close();
                return true;
            } catch (IOException ex) {
                CURRENT = null;
                return false;
            }
        }
    }

    /**
     * Attempts to close the current connection
     *
     * @return whether the operation was successful
     */
    public static boolean closeCurrent() {
        boolean output = CURRENT.close();
        if (output) {
            CURRENT = null;
        }
        return output;
    }

    /**
     * Determines whether there exists a connection in this program to a server
     * somewhere
     *
     * @return whether there exists a connection
     */
    public static boolean isConnected() {
        return CURRENT != null;
    }

    /**
     * Returns the current connection, if any
     *
     * @return the current connection, if any
     */
    public static ClientSocket getConnection() {
        return CURRENT;
    }

    /**
     * Attempts to close all connections within this object.
     *
     * @return whether the operation was successful
     */
    public boolean close() {
        if (communicating) {
            stopCommunications();
        }

        try {
            out.close();
            in.close();
            socket.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Clears all items from the send queue; essentially halts all
     * communications that have not happened.
     */
    public void clearSendQueue() {
        toSend.clear();
    }

    /**
     * Queues the given message to be sent to the server. After all preceding
     * messages have been sent, <i>then</i> this one will be sent.
     *
     * @param message the message to send once preceding ones have
     * @return whether the message has been queued successfully
     */
    public boolean send(String message) {
        return toSend.offer(message);
    }

    /**
     * Places this message at the front of the queue to be sent at the first
     * avaliable opportunity. This method places the highest priority on the
     * given message.
     *
     * @param message the message to send as soon as possible
     * @return whether the message has been queued successfully
     */
    public boolean sendImmediately(String message) {
        return toSend.offerFirst(message);
    }

    /**
     * Starts communications to the server. All queued messages will now be
     * sent.
     */
    public void startCommunications() {
        communicating = true;

        new Thread(() -> {
            while (communicating) {
                String read = null;
                try {
                    read = in.readLine();
                } catch (SocketException se) {
                    JOptionPane.showMessageDialog(null,
                            "You have been disconnected from the server.",
                            "Disconnected", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                } catch (IOException ex) {
                    System.err.println("Could not read line");
                }
                if (read == null) {
                    JOptionPane.showMessageDialog(null,
                            "The server has gone offline.",
                            "Disconnected", JOptionPane.WARNING_MESSAGE);
                    break;
                }

                // System.out.println("Received: " + read);
                if (hasServerListeners()) {
                    for (Consumer<String> listener : listeners) {
                        listener.accept(read);
                    }
                } else {
                    toReceive.offer(read);
                }
            }
        }).start();

        new Thread(() -> {
            while (communicating) {
                try {
                    out.println(toSend.take());
                } catch (InterruptedException ex) {
                    System.err.println("Take from toSend was interrupted");
                }
            }
        }).start();
    }

    /**
     * Stops all communications... for now. Stops sending and receiving
     * messages.
     */
    public void stopCommunications() {
        communicating = false;
    }

    /**
     * Attempts to read the next item from the server, waiting if need be. If a
     * listener is attached, however, null will always be returned.
     *
     * @return the next message from the server, if a listener is not attached
     * @throws InterruptedException if the waiting for the next item is
     * interrupted.
     */
    public String read() throws InterruptedException {
        return hasServerListeners() ? null : toReceive.take();
    }

    /**
     * The Consumers listening to the server
     */
    private List<Consumer<String>> listeners = null;

    /**
     * Sets the listener of this ClientSocket
     *
     * @param consumer the listener to add to this ClientSocket
     */
    public void addServerListener(Consumer<String> consumer) {
        if (listeners == null) {
            listeners = new LinkedList<>();
        }
        listeners.add(consumer);
        
        while (!toReceive.isEmpty()) {
            String read = toReceive.poll();
            for (Consumer<String> listener : listeners) {
                listener.accept(read);
            }
        }
    }

    /**
     * Removes all the currently listening listeners.
     */
    public void removeAllServerListeners() {
        listeners = null;
    }

    /**
     * Removes the given listener from the list of listeners
     *
     * @param listener the listener to remove
     */
    public void removeServerListener(Consumer<String> listener) {
        listeners.remove(listener);
    }

    /**
     * Determines whether this ClientSocket object has anything listening to its
     * server communications
     *
     * @return whether this ClientSocket object has anything listening to its
     * server communications
     */
    private boolean hasServerListeners() {
        return listeners != null && !listeners.isEmpty();
    }

    /**
     * Connects to a server via a server prompt
     *
     * @return whether the operation was successful
     */
    public static boolean connectViaGUI() {
        do {
            String host = getServerAddress();
            if (CURRENT == null) {
                try {
                    CURRENT = new ClientSocket(host);
                } catch (IOException ex) {
                    CURRENT = null;
                }
            } else {
                ClientSocket temp = CURRENT;
                try {
                    CURRENT = new ClientSocket(host);
                    temp.close();
                } catch (IOException ex) {
                    CURRENT = null;
                }
            }
            if (!isConnected()) {
                Object[] options = {"Reenter IP Adress", "Exit"};
                int returned = JOptionPane.showOptionDialog(null,
                        "Could not connect to server",
                        "Connection Error", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.ERROR_MESSAGE, null, options, options[0]);
                if (returned != JOptionPane.OK_OPTION) {
                    System.exit(0);
                    return false;
                }
            }
        } while (!isConnected());

        return true;
    }

    /**
     * Prompt for and return the address of the server.
     */
    private static String getServerAddress() {
        return JOptionPane.showInputDialog(
                null,
                "Enter IP Address of the Server:",
                "Welcome to Socket Room",
                JOptionPane.QUESTION_MESSAGE);
    }
}
