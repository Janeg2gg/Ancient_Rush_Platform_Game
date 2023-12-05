package Main;

import Levels.LevelHandler;
import entieties.Player;

import java.awt.*;

public class Game implements Runnable {

        private GameWindow gameWindow;
        private GamePanel gamePanel;

        private Thread gameThread;
        private final int FPS_SET = 120;
        private final int UPS_SET = 200;

        private Player player1;
        private LevelHandler levelHandler;

        //Some constants
        public final static int TILES_DEFAULT_SIZE = 32;
        public final static float SCALE = 1.5f;
        public final static int TILES_IN_WIDTH = 26;
        public final static int TILES_IN_HEIGHT = 14;
        public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
        public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
        public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


        public Game() {
            initClasses();
            gamePanel = new GamePanel(this);
            gameWindow = new GameWindow(gamePanel);
            gamePanel.requestFocus();

            startGameLoop();
        }

    private void initClasses() {
            player1 = new Player(200, 200);
            levelHandler = new LevelHandler(this);
    }

    private void startGameLoop() {
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void update(){
            player1.update();
            levelHandler.update();
        }

        public void render(Graphics g){
            levelHandler.draw(g);
            player1.render(g);
        }

        @Override
        public void run() {

            double timePerFrame = 1000000000.0 / FPS_SET;
            double timePerUpdate = 1000000000.0 / UPS_SET;

            long previousTime = System.nanoTime();

            int frames = 0;
            int updates = 0;
            long lastCheck = System.currentTimeMillis();

            double deltaU = 0;
            double deltaF = 0;

            while (true) {
                long currentTime = System.nanoTime();

                deltaU += (currentTime - previousTime) / timePerUpdate;
                deltaF += (currentTime - previousTime) / timePerFrame;
                previousTime = currentTime;

                if(deltaU >= 1){
                    update();
                    updates++;
                    deltaU--;
                }

                if(deltaF >= 1){
                    gamePanel.repaint();
                    frames++;
                    deltaF--;
                }

                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                    System.out.println("FPS: " + frames + " | UPS: " + updates);
                    frames = 0;
                    updates = 0;
                }
            }


        }
        public Player getPlayer1(){
            return player1;
        }

        public void windowFocusLost(){
            player1.resetBooleans();
        }
    }