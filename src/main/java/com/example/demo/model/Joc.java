package com.example.demo.model;

import com.example.demo.conection.JsonClass;
import com.example.demo.game.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Joc implements Serializable {


    public Joc() {
    }

    private List<Player> players = new ArrayList<>();


    public Joc(List<Player> players) {
        this.players = players;
    }
    public List<Player> getPlayers() {
        return players;
    }

    public void actualitzaClient(int idClient, String recivedDataFromServer) {

        int id = idClient == 0 ? 1 : 0;
        Joc estatJocRebut = new JsonClass().getObject(recivedDataFromServer);

        try{

            this.players.get(id).setPosX(estatJocRebut.players.get(id).getPosX());
            this.players.get(id).setPosY(estatJocRebut.players.get(id).getPosY());
            this.players.get(id).setDireccio(estatJocRebut.players.get(id).getDireccio());
        }catch (Exception e) {
            System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEeiiiiii "+e);
        }




    }
    public void actualitzaServidor(int idDeQuiEnvia, String json) {

        Joc estatJocRebut;

        try{
            estatJocRebut = new JsonClass().getObject(json);
            this.players.get(idDeQuiEnvia).setPosX(estatJocRebut.players.get(idDeQuiEnvia).getPosX());
            this.players.get(idDeQuiEnvia).setPosY(estatJocRebut.players.get(idDeQuiEnvia).getPosY());
            this.players.get(idDeQuiEnvia).setDireccio(estatJocRebut.players.get(idDeQuiEnvia).getDireccio());
        }catch(Exception e){

        }

        // akí tria de dades entre el estatjocRebut i this tenint en compte qui ha enviat en quant al player

        // actualitzem les dades del jugador que ens ha enviat el json.
        // comprovem la llista de bales en busca de bales noves

        // he de fer un stream o varios:   quedar-me amb les noves i generar-les al joc...   afegir-les a la llista propia de bales
    }
}
