package com.example.demo.game;

import com.example.demo.conection.Client;
import com.example.demo.model.Joc;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TheGameMain extends Application {

    private double cicles = 0;

    ///////////7

    Player playerA;
    Player playerB;
    Joc joc;
    Client client;

    /////////////777

    float viewPortX = 1200;
    float viewPortY = 800;

    private Pane root = new Pane();
    private int margeJugadors = 25;
    private boolean carrega;
    private Sprite player1 = new Sprite("player", Color.DARKOLIVEGREEN, 100, 100, 60, 90, Player.Direccio.S, 25);
    private Sprite player2 = new Sprite("enemy", Color.RED, 100, 500, 60, 90, Player.Direccio.S, 25);

    private List<String> input = new ArrayList<>();
    private int id;


    private Parent createContent() {
        root.setPrefSize(viewPortX, viewPortY);
        root.getChildren().add(player1);
        root.getChildren().add(player2);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();

            }
        };
        timer.start();

        return root;
    }

    double ciclesMov = cicles;

    private void update() {

        if (cicles == 0) {

            List<Player> players = new ArrayList<>();
            playerA = new Player();
            playerB = new Player();
            players.add(playerA);
            players.add(playerB);
            joc = new Joc(players);
            client = new Client("localhost", 5555, joc);
            client.start();
            while (!client.isReady()) {
            }
            this.id = client.getIdPlayer();
            joc.getPlayers().get((int) id).setDireccio(Player.Direccio.S);
            joc.getPlayers().get((int) id).setPosX(100 + (id * 500));
            joc.getPlayers().get((int) id).setPosY(100);
            joc.getPlayers().get((int) id).setId((int) id);

            cicles++;


        } else {


            if (id==0){

                joc.getPlayers().get(id).setPosX((float) player1.getTranslateX());
                joc.getPlayers().get(id).setPosY((float) player1.getTranslateY());
                joc.getPlayers().get(id).setDireccio(player1.getDireccio());

                input.forEach(s -> {

                    /////  PROVANT DIAGONALS


                    List<String> dir = new ArrayList<>();


                    if (s.equals("D") || s.equals("RIGHT")) {
                        player1.setDireccio(Player.Direccio.E);
                        if (player1.getTranslateX() + player1.getWidth() + margeJugadors < viewPortX)
                            dir.add("-Dreta");

                    }
                    if (s.equals("A") || s.equals("LEFT")) {
                        player1.setDireccio(Player.Direccio.W);
                        if (player1.getTranslateX() - margeJugadors > 0)
                            dir.add("-Esquerre");

                    }
                    if (s.equals("W") || s.equals("UP")) {
                        player1.setDireccio(Player.Direccio.N);
                        if (player1.getTranslateY() - (float) margeJugadors * 0.8 > 0)
                            dir.add("-Dalt");


                    }
                    if (s.equals("S") || s.equals("DOWN")) {

                        player1.setDireccio(Player.Direccio.S);
                        if (player1.getTranslateY() + player1.getHeight() + margeJugadors < viewPortY)
                            dir.add("-Baix");


                    }


                    StringBuilder direccioFinal = new StringBuilder();
                    dir.forEach(direccioFinal::append);

                    switch (direccioFinal.toString()) {
                        case "-Dalt-Baix":
                        case "-Baix-Dalt":
                        case "-Esquerre-Dreta":
                        case "Dreta-Esquerre":
                            break;
                        case "-Dalt-Dreta":
                        case "-Dreta-Dalt":
                            player1.moveRightUp();
                            break;
                        case "-Dalt-Esquerre":
                        case "-Esquerre-Dalt":
                            player1.moveLeftUp();
                            break;
                        case "-Baix-Esquerre":
                        case "-Esquerre-Baix":
                            player1.moveLeftDown();
                            break;
                        case "-Baix-Dreta":
                        case "-Dreta-Baix":
                            player1.moveRightDown();
                            break;
                        case "-Baix":
                            player1.moveDown();
                            break;
                        case "-Dalt":
                            player1.moveUp();
                            break;
                        case "-Esquerre":
                            player1.moveLeft();
                            break;
                        case "-Dreta":
                            player1.moveRight();
                            break;
                    }

                    dir = new ArrayList<>();
                    if (s.equals("COMMA")) {
                        if (cicles - ciclesDispar > 150) {
                            ciclesDispar = cicles;
                            Sprite sp = player1.atacar(player1);
                            root.getChildren().add(sp);
                        }

                    }




/*

                if (cicles - ciclesDispar > 150) {

                    if (s.equals("COMMA")) {
                        carrega = false;
                        ciclesDispar = cicles;
                        Sprite sp = player.atacar(player);
                        root.getChildren().add(sp);
                    }
                } else if (!carrega) {
                    player.carregar(player);
                    carrega = true;
                }

*/

                });
            }else{
                joc.getPlayers().get(id).setPosX((float) player2.getTranslateX());
                joc.getPlayers().get(id).setPosY((float) player2.getTranslateY());
                joc.getPlayers().get(id).setDireccio(player2.getDireccio());


                input.forEach(s -> {

                    /////  PROVANT DIAGONALS


                    List<String> dir = new ArrayList<>();


                    if (s.equals("D") || s.equals("RIGHT")) {
                        player1.setDireccio(Player.Direccio.E);
                        if (player2.getTranslateX() + player2.getWidth() + margeJugadors < viewPortX)
                            dir.add("-Dreta");

                    }
                    if (s.equals("A") || s.equals("LEFT")) {
                        player2.setDireccio(Player.Direccio.W);
                        if (player2.getTranslateX() - margeJugadors > 0)
                            dir.add("-Esquerre");

                    }
                    if (s.equals("W") || s.equals("UP")) {
                        player2.setDireccio(Player.Direccio.N);
                        if (player2.getTranslateY() - (float) margeJugadors * 0.8 > 0)
                            dir.add("-Dalt");


                    }
                    if (s.equals("S") || s.equals("DOWN")) {

                        player2.setDireccio(Player.Direccio.S);
                        if (player2.getTranslateY() + player2.getHeight() + margeJugadors < viewPortY)
                            dir.add("-Baix");


                    }


                    StringBuilder direccioFinal = new StringBuilder();
                    dir.forEach(direccioFinal::append);

                    switch (direccioFinal.toString()) {
                        case "-Dalt-Baix":
                        case "-Baix-Dalt":
                        case "-Esquerre-Dreta":
                        case "Dreta-Esquerre":
                            break;
                        case "-Dalt-Dreta":
                        case "-Dreta-Dalt":
                            player2.moveRightUp();
                            break;
                        case "-Dalt-Esquerre":
                        case "-Esquerre-Dalt":
                            player2.moveLeftUp();
                            break;
                        case "-Baix-Esquerre":
                        case "-Esquerre-Baix":
                            player2.moveLeftDown();
                            break;
                        case "-Baix-Dreta":
                        case "-Dreta-Baix":
                            player2.moveRightDown();
                            break;
                        case "-Baix":
                            player2.moveDown();
                            break;
                        case "-Dalt":
                            player2.moveUp();
                            break;
                        case "-Esquerre":
                            player2.moveLeft();
                            break;
                        case "-Dreta":
                            player2.moveRight();
                            break;
                    }

                    dir = new ArrayList<>();
                    if (s.equals("COMMA")) {
                        if (cicles - ciclesDispar > 150) {
                            ciclesDispar = cicles;
                            Sprite sp = player2.atacar(player2);
                            root.getChildren().add(sp);
                        }

                    }




/*

                if (cicles - ciclesDispar > 150) {

                    if (s.equals("COMMA")) {
                        carrega = false;
                        ciclesDispar = cicles;
                        Sprite sp = player.atacar(player);
                        root.getChildren().add(sp);
                    }
                } else if (!carrega) {
                    player.carregar(player);
                    carrega = true;
                }

*/

                });

            }

            ciclesMov = cicles;
            input = new ArrayList<>();


            sprites().stream()
                    .filter(s -> s.getType().equals("atac"))
                    .filter(s -> s.getDireccio() == Player.Direccio.S)
                    .forEach(Sprite::moveDown);
            sprites().stream()
                    .filter(s -> s.getType().equals("atac"))
                    .filter(s -> s.getDireccio() == Player.Direccio.N)
                    .forEach(Sprite::moveUp);
            sprites().stream()
                    .filter(s -> s.getType().equals("atac"))
                    .filter(s -> s.getDireccio() == Player.Direccio.W)
                    .forEach(Sprite::moveLeft);
            sprites().stream()
                    .filter(s -> s.getType().equals("atac"))
                    .filter(s -> s.getDireccio() == Player.Direccio.E)
                    .forEach(Sprite::moveRight);


            // Aixó per que morin els players
       /* sprites().stream()
                .filter(sprite -> sprite.getType().equals("atac"))
                .forEach(atac -> {
                    if(atac.getBoundsInParent().intersects(player.getBoundsInParent())){
                        player.setDead(true);

                    }
                });*/

            // eliminar atacs que han sortit del joc per cada costat de la pantalla :  només els poso a DEAD=true;

            sprites().stream()
                    .filter(s -> s.getType().equals("atac"))
                    .filter(s -> s.getTranslateY() + s.getHeight() < 0 || s.getTranslateY() - s.getHeight() > viewPortY
                            || s.getTranslateX() + s.getWidth() < 0 || s.getTranslateX() - s.getWidth() > viewPortX)
                    .forEach(s -> s.setDead(true));

            // Esborrar els Sprites que estan DEAD=true


            // intentant esborrar les bales que surten de pantalla o xoquen, o... que estan      dead = true;


            sprites().forEach(sprite -> {
                if (sprite.isDead()) {
                    root.getChildren().remove(sprite);
                }
            });

            cicles++;
        }


    }


    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n -> (Sprite) n).collect(Collectors.toList());
    }


    double ciclesDispar;

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(createContent());

        // posem a escoltar diferents tecles per als inputs

        scene.setOnKeyPressed(e -> {
            input.add(e.getCode().toString());
        });


        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {


        launch();

    }


}
