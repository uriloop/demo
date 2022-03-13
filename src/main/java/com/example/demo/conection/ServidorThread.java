package com.example.demo.conection;

import com.example.demo.game.Player;
import com.example.demo.model.Joc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServidorThread implements Runnable {

// com comunico el serverThread amb el server per compartir les dades dels players???????
    // Que arranca que???? el servidor va apart, només escolta.
    /*   El servidor arranca el client i el client arranca el game     ?????           */

    PartidaLlesta partidaLlesta;
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintStream out = null;
    String msgEntrant, msgSortint;
    boolean acabat;
    Joc estatJoc;
    int idPropia;
    String nick;

    public ServidorThread(Socket clientSocket, Joc estatJoc, int numplayersConectats, PartidaLlesta partidaLlesta) {
        idPropia = numplayersConectats;
        this.estatJoc = estatJoc;
        this.clientSocket = clientSocket;
        acabat = false;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.partidaLlesta = partidaLlesta;
    }

    @Override
    public void run() {
        System.out.println("i. jug_" + (idPropia + 1) + " Conexió establerta");
        try {
            // primer missatge on li passem el num de player per determinar la posició inicial i el color del usuari
            if (idPropia == 0)
                msgSortint = String.valueOf(idPropia);
            else
                msgSortint = String.valueOf(idPropia);

            out.println(msgSortint);
            out.flush();
            System.out.println("o. jug_" + (idPropia + 1) + ": " + msgSortint);
            // arriba = "Conectat!NickName"
            msgEntrant = in.readLine();
            System.out.println("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
            if ((msgEntrant.substring(0, 9)).equals("Conectat!")) {
// Comprova si és el primer missatge de benvinguda i guarda el nickname o posa Player+num si no s'escriu res

                if (msgEntrant.length() > 9) {
                    nick = msgEntrant.substring(9, msgEntrant.length());
                } else {
                    nick = "Player" + (idPropia + 1);
                }
                if (idPropia == 0) {

                    estatJoc.getPlayers().add(new Player(0, 100, 100, Player.Direccio.S));

                } else {
                    estatJoc.getPlayers().add(new Player(1, 100, 600, Player.Direccio.S));

                }

                msgSortint = "Hola " + nick + ". Has entrat a una partida. Esperant a l'altre jugador...";
                out.println(msgSortint);
                out.flush();
                System.out.println("o. jug_" + (idPropia + 1) + ": " + msgSortint);


            }
            msgEntrant = in.readLine();
            JsonClass json = new JsonClass();
            // aki enviem missatges per comprovar que la comunicació segueix en peu, realment només cal no respondre y dins del while comprovar només si ja estan llestos els jugadors
            while (msgEntrant.equals("Espero...")) {
                System.out.println("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
                msgSortint = comprovaJugadorsPreparats() ? json.getJSON(estatJoc) : "Espera...";
                out.println(msgSortint);
                out.flush();
                System.out.println("o. jug_" + (idPropia + 1) + ": " + msgSortint);
                msgEntrant = in.readLine();
            }


            // akí hem de rebre el primer json de cada usuari
            while (!acabat) {
                msgSortint = generarResposta(msgEntrant);
                out.println(msgSortint);
                out.flush();
                msgEntrant = in.readLine();
                // bucle de missatges
            }
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println("Salta akí ");
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean comprovaJugadorsPreparats() {

        if (partidaLlesta.getPlayersConectats() == 2) return true;
        return false;

    }

    // juntar tots els msgEntrants dels diferents jugadors, posar en comu i retornar el json amb les posicions
    private String generarResposta(String msgEntrant) {

        JsonClass json = new JsonClass();
        estatJoc.actualitzaServidor(idPropia, msgEntrant);
        String resposta = json.getJSON(estatJoc);

        // per monitoritzar el que passa al servidor
        System.out.println("i. jug_" + (idPropia + 1) + ": " + msgEntrant);
        System.out.println("o. jug_" + (idPropia + 1) + ": " + resposta);

        return resposta;//new Scanner(System.in).nextLine();
    }
}
