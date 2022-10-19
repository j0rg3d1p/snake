import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import  java.lang.System.*;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends JFrame {
    private int ancho = 750;
    private int alto = 750;

    int limite = 5;

    int anchoPunto = 10;
    int altoPunto = 10;

    int direccion = KeyEvent.VK_LEFT;

    long frecuencia = 40; // ESTABLECEMOS LA VELOCIDAD DE LA SERPIENTE

    ArrayList<Point> lista;

    ImgSnake imgSnake;

    Point snake, comida;

    boolean gameover = false;

    boolean comienzo = true;

    public Snake(){
        setTitle("Snake"); // Creamos el titulo del juego

        startGame();

        imgSnake = new ImgSnake();
        this.getContentPane().add(imgSnake);

        setSize(ancho, alto); // Ajustamos las dimensiones de la pantalla

        this.addKeyListener(new Teclas());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cuando cerramos la ventana salimos del juego
        JFrame.setDefaultLookAndFeelDecorated(false);
        setUndecorated(true);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); // Para que la pantalla este centrada

        setVisible(true); // Para que se vea

        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
    }

    public void startGame(){
        comida = new Point(200,200);
        snake = new Point(ancho/2, alto/2);

        lista = new ArrayList<Point>();
        lista.add(snake);
        crearComida();
    }

    public static void main (String[] args) {

        Snake s = new Snake();

    }

    public void crearComida(){
        Random rnd = new Random();

        // APARICION ALEATORIA DE LA COMIDA

        comida.x = rnd.nextInt(ancho);
        if ((comida.x % 5)>0){
            comida.x = comida.x - (comida.x % 5);
        }

        if (comida.x < 5){
            comida.x = comida.x + 10;
        }

        comida.y = rnd.nextInt(alto);
        if((comida.y % 5) > 0){
            comida.y = comida.y - (comida.y % 5);
        }
        if (comida.y < 5){
            comida.y = comida.y + 10;
        }
    }


    public class ImgSnake extends JPanel{
        public void paintComponent(Graphics graphics){
            super.paintComponent(graphics);
            graphics.setColor(new Color(0, 0, 0));
            graphics.fillRect(0, 0, ancho, alto);
            if(comienzo){
                graphics.setColor(new Color(255,255,255));
                graphics.setFont(new Font("TimesRoman", Font.BOLD, 70));
                graphics.drawString("READY ?\n" ,200, 300);
                graphics.setFont(new Font("TimesRoman", Font.BOLD, 50));
                graphics.drawString("PRESS \"N\" TO START ", 100, 450);
            }
            else {
                graphics.setColor(new Color(0, 150, 0));
                if (lista.size() > 0) {
                    for (int i = 0; i < lista.size(); i++) {
                        Point p = (Point) lista.get(i);
                        graphics.fillRect(p.x, p.y, anchoPunto, altoPunto); // se crea la serpiente
                    }
                }

                graphics.setColor(new Color(255, 0, 0));
                graphics.fillRect(comida.x, comida.y, anchoPunto, altoPunto); // se crea la comida
            }

            if(gameover){
                if(lista.size() == limite){
                    graphics.setColor(new Color(255,255,255));
                    graphics.setFont(new Font("TimesRoman", Font.BOLD, 70));
                    graphics.drawString("YOU WIN !!!\n" ,200, 300);
                    graphics.drawString("SCORE = "+(lista.size()-1), 200, 450);
                }
                else {
                    graphics.setColor(new Color(255, 255, 255));
                    graphics.setFont(new Font("TimesRoman", Font.BOLD, 50));
                    graphics.drawString("GAME OVER", 400, 400);
                    graphics.drawString("SCORE " + (lista.size() - 1), 300, 240);

                    graphics.setFont(new Font("TimesRoman", Font.BOLD, 20));
                    graphics.drawString("Press \"N\" to Start New Game", 100, 320);
                    graphics.drawString("Press \"ESC\" to Exit", 100, 340);
                }
            }

        }
    }

    public class Teclas extends KeyAdapter{
        public void keyPressed(KeyEvent event){
            if(event.getKeyCode() == KeyEvent.VK_ESCAPE) // Si presionamos "esc" cerramos el juego
                System.exit(0);
            else if(event.getKeyCode() == KeyEvent.VK_UP){
                if(direccion != KeyEvent.VK_DOWN){
                    direccion = KeyEvent.VK_UP;
                }
            }
            else if(event.getKeyCode() == KeyEvent.VK_RIGHT){
                if (direccion != KeyEvent.VK_LEFT)
                    direccion = KeyEvent.VK_RIGHT;

            }
            else if(event.getKeyCode() == KeyEvent.VK_DOWN){
                if (direccion != KeyEvent.VK_UP)
                    direccion = KeyEvent.VK_DOWN;
            }
            else if(event.getKeyCode() == KeyEvent.VK_LEFT){
                if (direccion != KeyEvent.VK_RIGHT)
                    direccion = KeyEvent.VK_LEFT;
            }
            else if(event.getKeyCode() == KeyEvent.VK_N){
                gameover = false;
                comienzo = false;
                startGame();
            }
        }
    }

    public class Momento extends Thread{
        long last = 0;

        public Momento(){}


        public void run(){
            while(true) {
                    if ((java.lang.System.currentTimeMillis() - last) > frecuencia) {
                        if (!gameover && !comienzo) {
                            if(direccion == KeyEvent.VK_N){
                                startGame();
                            }
                            else if (direccion == KeyEvent.VK_UP) {
                                snake.y = snake.y - altoPunto;
                                if (snake.y > alto) {
                                    snake.y = 0;
                                }
                                if (snake.y < 0) {
                                    snake.y = alto - altoPunto;
                                }
                            } else if (direccion == KeyEvent.VK_DOWN) {
                                snake.y = snake.y + altoPunto;
                                if (snake.y > alto) {
                                    snake.y = 0;
                                }
                                if (snake.y < 0) {
                                    snake.y = alto - altoPunto;
                                }
                            } else if (direccion == KeyEvent.VK_RIGHT) {
                                snake.x = snake.x + anchoPunto;
                                if (snake.x > ancho) {
                                    snake.x = 0;
                                }
                                if (snake.x < 0) {
                                    snake.x = ancho - anchoPunto;
                                }
                            } else if (direccion == KeyEvent.VK_LEFT) {
                                snake.x = snake.x - anchoPunto;
                                if (snake.x > ancho) {
                                    snake.x = 0;
                                }
                                if (snake.x < 0) {
                                    snake.x = ancho - anchoPunto;
                                }
                            }
                        }
                        acutualizar();
                        last = java.lang.System.currentTimeMillis();
                }
            }
        }
    }

    private void acutualizar() {


        lista.add(0, new Point(snake.x,snake.y));
        lista.remove(lista.size() - 1);

        for (int i = 1; i<lista.size(); i++){
            Point punto = lista.get(i);
            if ((snake.x == punto.x && snake.y == punto.y) || lista.size() == limite){
                gameover = true;
            }
        }
        if ((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10)) && (snake.y > (comida.y - 10)) && (snake.y < (comida.y + 10))){
            lista.add(0, new Point(snake.x, snake.y));
            crearComida();
        }
        imgSnake.repaint();
    }
}
