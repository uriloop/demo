package com.example.demo.conection;

import com.example.demo.game.Player;
import com.example.demo.model.Joc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    PartidaLlesta partidaLlesta= new PartidaLlesta();

    int maxPlayers = 1;
    private Joc estatJoc;
    private List<Player> players;
    int port;
    // un map per tenir els players en una llista numerada
    private List<ServidorThread> playersConectats;
    private List<Thread> clients;
    private int numplayersConectats;

    public Servidor(int port) {
        this.port = port;
        playersConectats = new ArrayList<>();
        clients = new ArrayList<>();

        players= new ArrayList<>();
        numplayersConectats=0;
        estatJoc = new Joc(players);

    }

    public void listen() {

        try {
            System.out.println("*********************************************************");
            System.out.println("                    SERVIDOR");
            System.out.println("            Nom servidor: "+ InetAddress.getLocalHost().getHostName());
            System.out.println("            IP servidor: "+InetAddress.getLocalHost().getHostAddress());
            System.out.println();
            System.out.println(" i = msgFromClient     o = msgToClient     > = StatusMsg");
            System.out.println("*********************************************************");
            System.out.println(">");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (numplayersConectats <= maxPlayers) {
                //esperar connexió del client i llançar thread  // si hi ha 2 clients deixa d'esperar conexions
                // TODO He de mantenir aqest bucle. Quan caigui un client s'ha de reiniciar per a que torni a escoltar a dos clients, reinicialitzar totes les variables al respecte, etc.
                System.out.println("> Estic escoltant peticions...");

                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació

                playersConectats.add(new ServidorThread(clientSocket, estatJoc,numplayersConectats,partidaLlesta));
                clients.add(new Thread(playersConectats.get(numplayersConectats)));

                clients.get(numplayersConectats).start();
                partidaLlesta.setPlayersConectats(partidaLlesta.getPlayersConectats()+1);
                numplayersConectats++;
                // si ja estan els dos conectats comença el joc, ja veuré com cambiar-ho per a emparellar i posar més usuaris,   he d'esborrar-los si hi ha menys


            }
            System.out.println("> Iniciant partida... (Ja no escolto peticions)");
            while(true){}

        } catch (IOException ex) {
            ex.printStackTrace();

        }
        try {
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public static void main(String[] args) {

        Servidor srv = new Servidor(5555);
        srv.listen();

    }
}
