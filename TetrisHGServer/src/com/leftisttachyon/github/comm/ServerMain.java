package com.leftisttachyon.github.comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 * The main class; entry point of the application
 *
 * @author Jed Wang
 * @since 0.9.1
 */
public class ServerMain {

    /**
     * The main method; the entry point
     *
     * @param args the command line arguments
     * @throws java.io.IOException if something goes wrong
     */
    public static void main(String[] args) throws IOException {
        System.out.println("The tetris server is running.");
        
        /*MainWindow mw = new MainWindow();
        Handler.setMainWindow(mw);*/
        
        try(ServerSocket listener = new ServerSocket(9001)) {
            while(true) {
                Socket socket = listener.accept();
                
                MiniServer h = new MiniServer(socket);
                
                new Thread(h::go).start();
                
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.println("egg");
                // mw.addHandler(h);
            }
        } catch(BindException be) {
            System.err.println("Cannot start server: " + be.getMessage());
            JOptionPane.showMessageDialog(null, "Cannot start server", 
                    be.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
