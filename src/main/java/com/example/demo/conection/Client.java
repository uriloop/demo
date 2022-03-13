package com.example.demo.conection;

import com.example.demo.model.Joc;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {

    String hostname;
    int port;
    boolean continueConnected;

    private Joc joc;


    private int idPlayer;
    private boolean ready;


    public Client(String hostname, int port, Joc joc) {
        this.hostname = hostname;
        this.port = port;
        continueConnected = true;
        this.joc = joc;


    }


    public void run() {

        String serverData;
        String request;


        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());

            // Tractem el primer missatge on rebem la id del player i el creem
            serverData = in.readLine();
            System.out.println("i. " + serverData);

          /*  if (serverData == null) {
                this.id = 1;
                System.out.println("Conexió establerta amb el servidor. Ets el player 2");
            } else*/
            if (serverData.equals("0")) {
                this.idPlayer = 0;

                System.out.println("Conexió establerta amb el servidor. Ets el player 1");
            } else if (serverData.equals("1")) {
                this.idPlayer = 1;
                System.out.println("Conexió establerta amb el servidor. Ets el player 2");
            }
            // enviem el nick
            request = "Conectat!";
            out.println(request);
            out.flush();
            System.out.println("o. " + request);

            // revem segon missatge d'espera
            serverData = in.readLine();
            System.out.println("i. " + serverData);


            // confirmem que esperem
            request = "Espero...";
            out.println(request);
            out.flush();
            System.out.println("o. " + request);

            // mentre no ens envia l'estat del joc amb el nou client, esperem
            while ((serverData = in.readLine()).equals("Espera...")) {
                System.out.println("i. " + serverData);
                out.println(request);
                out.flush();
                System.out.println("o. " + request);
            }
            ready=true;

            //processament de les dades rebudes aki rebem el primer json, hem de posicionar els players

            // comencem a enviar json de l'estat del joc quan hi hagi dos players i iniciem joc
            request = getRequest(serverData);

            out.println(request);
            out.flush();
            while (continueConnected) {
                serverData = in.readLine();

                //processament de les dades rebudes i obtenció d'una nova petició
                request = getRequest(serverData);
                //enviament el número i els intents
                out.println(request);
                System.out.println("o. " + request);
                out.flush();
            }
            close(socket);
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error de connexió indefinit: " + ex.getMessage());
        }

    }

    public Joc getJoc() {
        return joc;
    }

// Tractem la rebuda de dades

    public String getRequest(String recivedDataFromServer) {

        JsonClass json = new JsonClass();
        joc.actualitzaClient(idPlayer, recivedDataFromServer);
        String resposta = json.getJSON(joc);
        // monitoritzar el que rebem del servidor
        System.out.println("i.  " + recivedDataFromServer);


        return resposta;



    }

    public boolean mustFinish(String dades) {
        if (dades.equals("exit")) return false;
        return true;

    }

    private void close(Socket socket) {
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public boolean isReady() {
        return ready;
    }

    public int getIdPlayer() {
        return idPlayer;
    }
}
