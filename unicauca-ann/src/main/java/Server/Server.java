package Server;

import interfaces.Task;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.*;

public class Server {

    
    //SERVER implemetation for SOCKETS
    private static int PORT = 444;

    public static void main(String args[]) throws IOException {
        ServerSocket ss;
        BuilderTask bt = new BuilderTask();
        ArrayList<Task> tareas = new ArrayList<>();
        System.out.println("Socket Server ....Getting Taks ..... .... ....");
        tareas = bt.getTaks();
        ArrayList algoritmos = new ArrayList();
        if (bt.getTypeValidacion().size() == 1) {//CV o TT
            algoritmos = bt.getAlgorithms();
        } else {
            ArrayList aux = bt.getAlgorithms();
            algoritmos = bt.getAlgorithms();
            for (Object alg : aux) {
                algoritmos.add(alg);
            }
        }
        if (bt.getSolutionRepresentation().size() > 1) {
            ArrayList tmp = (ArrayList) algoritmos.clone();
            for (Object alg : tmp) {
                algoritmos.add(alg);
            }
        }

        algoritmos.add("last");//to identify last algorithm
        System.out.print("Stating Server... ");

        try {
            ss = new ServerSocket(PORT);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                ((ServerThread) new ServerThread(socket, idSession, tareas, algoritmos)).start();
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
