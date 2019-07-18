package com.leftisttachyon.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A wrapper class of a Socket that streamlines communication with a server.
 *
 * @author Jed Wang
 * @since 0.9.1
 */
public class ClientSocket {

    /**
     * The socket in where the communications take place
     */
    private final Socket socket;

    /**
     * The BufferedReader which reads out of the socket
     */
    private final BufferedReader in;

    /**
     * The PrintWriter which writes into the socket
     */
    private final PrintWriter out;

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
        socket = new Socket(host, 9001);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        toSend = new LinkedBlockingDeque<>();
        toReceive = new LinkedBlockingQueue<>();
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
                return false;
            }
        } else {
            ClientSocket temp = CURRENT;
            try {
                CURRENT = new ClientSocket(host);
                temp.close();
                return true;
            } catch (IOException ex) {
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
        return CURRENT == null;
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
                try {
                    out.println(toSend.take());
                } catch (InterruptedException ex) {
                    System.err.println("Take from toSend was interrupted");
                }
            }
        }).start();

        new Thread(() -> {
            while (communicating) {
                try {
                    toReceive.offer(in.readLine());
                } catch (IOException ex) {
                    System.err.println("Could not read line");
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
     * Attempts to read the next item from the server, waiting if need be.
     *
     * @return the next message from the server
     * @throws InterruptedException if the waiting for the next item is
     * interrupted.
     */
    public String read() throws InterruptedException {
        return toReceive.take();
    }
}
