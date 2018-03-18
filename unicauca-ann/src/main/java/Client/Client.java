package Client;

import Server.BuilderTask;
import cliente.db.implemente.TaskDao;
import interfaces.Task;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class ClientThread extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    ReentrantLock lock = new ReentrantLock();

    //By default
    private int port = 444;
    private String address = "127.0.0.1";//local host
    private long id;

    public ClientThread(long id) {
        this.id = id;
    }

    public ClientThread(long id, int puerto, String address) {
        this.id = id;
        this.port = puerto;
        this.address = address;
    }

    @Override
    public void run() {
        //Connect With server
        try {
            boolean builtarea = true;
            sk = new Socket(address, port);
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());
            while (builtarea) {
                try {

                    String tareasTexto = dis.readUTF();

                    if (tareasTexto.contains("Sin tareas")) {
                        builtarea = false;
                    } else {
                        Task t = new Task(tareasTexto);
                        tareasTexto = procesar(t);
                        System.out.println("cliente " + id + " " + t.toString() + "," + t.getAlgorimo() + "," + t.getId());
                    }
                    dos.writeUTF(tareasTexto + ",Cliente " + id + " result ");

                } catch (IOException ex) {
                    dis.close();
                    dos.close();
                    sk.close();
                    run();
                }
            }
            dis.close();
            dos.close();
            sk.close();
        } catch (IOException ex) {

            System.out.println("Error de Sk");

        }

    }

    public String procesar(Task T) {
        lock.lock();
        try {
            String comandoCMD = "-p " + T.getProblemName() + " -a " + T.getAlgorimo() + " -T " + T.getAlgorimo() + "parametros.txt";
            String[] args = comandoCMD.split(" ");
            CommandProcessor exp = new CommandProcessor();
            exp.setTarea(T);
            try {
                exp.execute(args);
            } catch (IOException e) {

            }
            T = exp.getTarea();
        } finally {
            lock.unlock();
        }

        return T.toString();
    }
}

public class Client {

    public static void main(String[] args) {
        //executeClientSockt(args); /* Can use "java -cp target\\unicauca-ann-5.2-jar-with-dependencies.jar cliente.Cliente port 444 ip localhost"*/

        executeClientDB();//DB implementation

    }

    public static void executeClientSockt(String[] args) {//sockets implementation
        // can use a CMD whith cliente.Cliente port 444 ip localhost
        int puerto = 444;
        String address = "localhost";
        if (args.length > 0) {
            puerto = Integer.parseInt(args[1]);
            address = args[3];
            System.out.println("Conexion con " + address + " Port : " + puerto);
        }
        ArrayList<Thread> clients = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            clients.add(new ClientThread(i, puerto, address));
        }
        for (Thread thread : clients) {
            thread.start();
        }
    }

    public static void executeClientDB() {

        //**************with the following lines you can get the tasks for the database  ***********     
//        BuilderTask b = new BuilderTask();
//        b.getSQLInsert(b.getTaks());//See the output Terminal
        //the following implement 
        TaskDao controllerTarea = new TaskDao();
        Task t = null;
        ClientThread client = new ClientThread(4);
        do {
            try {
                t = controllerTarea.getTasksListToMake().get(0);
                client.procesar(t);
                controllerTarea.updateTaskDetailsInDB(t);
                t = null;

            } catch (Exception e) {
                System.out.println(" Sin Tareas ");
                break;
            }

        } while (t == null);
        System.out.println("......... .... ... ... Client Finished");

    }

}
