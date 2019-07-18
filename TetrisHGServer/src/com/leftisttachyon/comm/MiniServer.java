package com.leftisttachyon.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A class that handles the client
 *
 * @author Jed Wang
 * @since 0.9.1
 */
public class MiniServer {

    /**
     * A HashMap of every MiniServer created
     */
    private static final HashMap<String, MiniServer> SERVERS = new HashMap<>();

    /**
     * A Set of MiniServers which are busy
     */
    private static final Set<MiniServer> BUSY = new HashSet<>();

    /**
     * A reader coming from the user
     */
    private BufferedReader in;

    /**
     * A writer going to the user
     */
    private PrintWriter out;

    /**
     * The internal socket
     */
    public final Socket socket;

    /**
     * Whether the user is currently in a game
     */
    private boolean inGame;

    /**
     * The opponent, if any. If none, this is null.
     */
    private MiniServer opponent;

    /**
     * This user's name
     */
    private String name;

    /**
     * Creates a new MiniServer object with the given Socket
     *
     * @param s the Socket to use
     * @throws IOException if the in and out streams cannot be created
     */
    public MiniServer(Socket s) throws IOException {
        socket = s;
        inGame = false;
        opponent = null;
    }

    /**
     * Called to start interaction with the user
     */
    public void go() {
        try {
            // Create character streams for the socket.
            in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Request a name from this client.  Keep requesting until
            // a name is submitted that is not already used.  Note that
            // checking for the existence of a name and adding the name
            // must be done while locking the set of names.
            while (true) {
                System.out.println("SUBMITNAME");
                out.println("SUBMITNAME");
                // notify("SUBMITNAME", false);
                name = in.readLine();
                // notify(name, true);
                if (name == null) {
                    return;
                }
                if ("".equals(name) || "null".equals(name)) {
                    continue;
                }
                synchronized (SERVERS) {
                    if (!SERVERS.containsKey(name)) {
                        HashSet<String> copy = new HashSet<>(SERVERS.keySet());
                        for (MiniServer h : SERVERS.values()) {
                            h.out.println("NEWCLIENTtrue " + name);
                        }
                        SERVERS.put(name, this);
                        for (String s : copy) {
                            out.println("NEWCLIENTfalse " + s);
                        }
                        break;
                    }
                }
            }

            // Now that a successful name has been chosen, add the
            // socket's print writer to the set of all writers so
            // this client can receive broadcast messages.
            out.println("NAMEACCEPTED");
            // notify("NAMEACCEPTED", false);

            // Accept messages from this client and broadcast them.
            // Ignore other clients that cannot be broadcasted to.
            while (true) {
                String line = in.readLine();
                // notify(line, true);
                if (line == null) {
                    return;
                }

                println("\"" + line + "\"");

                // handle input
                if (line.equals("PING")) {
                    out.println("PONG");
                } else if (line.startsWith("NLM")) {
                    String message = "NLM" + name + ": " + line.substring(3);
                    for (MiniServer h : SERVERS.values()) {
                        h.out.println(message);
                    }
                } else if (inGame) {
                    if (line.startsWith("EXIT")) {
                        inGame = false;
                        BUSY.remove(this);
                        for (MiniServer h : SERVERS.values()) {
                            h.out.println("FREE" + name);
                        }
                        if (opponent != null) {
                            opponent.out.println("EXIT");
                            opponent.inGame = false;
                            BUSY.remove(opponent);
                            for (MiniServer h : SERVERS.values()) {
                                h.out.println("FREE" + opponent.name);
                            }

                            opponent = null;
                        }
                    } else {
                        opponent.out.println(line);
                    }
                } else {
                    if (line.startsWith("CHALLENGE_C")) {
                        // Challenging for a match
                        String toChallenge = line.substring(11);
                        if (toChallenge.equals(name)) {
                            continue;
                        }

                        if (SERVERS.containsKey(toChallenge)) {
                            SERVERS.get(toChallenge).out.println("CHALLENGE_C" + name);
                        } else {
                            System.err.println("Opponent " + toChallenge
                                    + " not found (149)");
                        }
                    } else if (line.startsWith("CHALLENGE_R")) {
                        // Challenge response: accept or reject
                        // Scanner temp = new Scanner(line.substring(11));
                        String[] data = line.substring(11).split(" ");

                        // Accepted!
                        String other = data[0];
                        if (SERVERS.containsKey(other)) {
                            MiniServer otherH = SERVERS.get(other);
                            if (Boolean.parseBoolean(data[1])
                                    && !BUSY.contains(otherH)) {
                                opponent = otherH;
                                inGame = true;
                                opponent.out.println("CHALLENGE_Rtrue");
                                opponent.opponent = this;
                                opponent.inGame = true;
                                for (MiniServer h : SERVERS.values()) {
                                    h.out.println("BUSY" + name);
                                    h.out.println("BUSY" + opponent.name);
                                }

                                BUSY.add(opponent);
                                BUSY.add(this);
                            } else {
                                otherH.out.println("CHALLENGE_Rfalse");
                            }
                        } else {
                            System.err.println("Opponent " + other
                                    + " not found (196)");
                        }
                    }
                }
            }
        } catch (IOException e) {
            println(e.toString());
        } finally {
            // This client is going down!  Remove its name and its print
            // writer from the sets, and close its socket.
            for (MiniServer h : SERVERS.values()) {
                h.out.println("REMOVECLIENT" + name);
            }
            if (opponent != null) {
                opponent.out.println("EXIT");
                opponent.inGame = false;
                for (MiniServer h : SERVERS.values()) {
                    h.out.println("FREE" + opponent.name);
                }
            }
            if (SERVERS != null) {
                SERVERS.remove(name);
            }
            out.close();
            try {
                in.close();
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Prints something with a carriage return afterwards
     *
     * @param s a string to println
     */
    public void println(String s) {
        System.out.println(name + ": " + s);
    }
}
