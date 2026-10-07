import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Menu extends JPanel implements KeyListener,ActionListener{
    private JFrame janela;
    private ArrayList<Image> fundo = new ArrayList<>();
    private Timer timerGeral,timerAdicionarFrame;
    private int indiceFundo = 1,totalFrames = 143;
    private Jogo jogo;
    private boolean carregarRecursos = true;
    private Runnable threadRecursos;
    private Thread threadCarregarRecursos;

    public Menu(JFrame janela){
        this.janela = janela;
        setFocusable(true);
        addKeyListener(this);
        janela.setTitle("Menu");

        jogo = new Jogo(janela,this);

        for (int i = 1; i <= 10; i++){
            fundo.add(new ImageIcon(getClass().getResource("/menu/fundo/(" + i + ").jpg")).getImage());
        }


        threadRecursos = new Runnable() {

            @Override
            public void run() {
                while(carregarRecursos){
                    try{
                        if(indiceFundo <= totalFrames){
                            if(fundo.size() < 20){
                                BufferedImage img = ImageIO.read(getClass().getResource("/menu/fundo/(" + indiceFundo + ").jpg"));
                                synchronized(fundo){
                                    fundo.add(img);
                                }
                                indiceFundo ++;

                            }else{
                                Thread.sleep(30);
                            }
                        }else{
                            indiceFundo = 1;
                        }

                    }catch(Exception err){
                        System.err.println("Erro na classe Menu ao carregar recursos (fundo " + indiceFundo + "):");
                        err.printStackTrace();
                    }
                }
            }

        };
        iniciarCarregamentoRecursos();

        timerGeral = new Timer(30,this);
        timerGeral.start();
    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(!fundo.isEmpty()){
            g.drawImage(fundo.get(0), 0,0,null);
            if(fundo.size() >= 4){
                fundo.remove(0);
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }



    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            // Delega para o jogo: timers, recursos e música
            jogo.iniciarPartida();

            // Trocar para o jogo
            janela.setContentPane(jogo);

            jogo.requestFocusInWindow();

            janela.revalidate();
            janela.repaint();

            // Parar o timer do menu
            timerGeral.stop();
        }

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {



        repaint();


    }

    public void iniciarCarregamentoRecursos() {
        carregarRecursos = true;
        threadCarregarRecursos = new Thread(threadRecursos);
        threadCarregarRecursos.start();
    }

    // =========================================================
    // ENCAPSULAMENTO
    // =========================================================

    public void iniciarTimer() {
        timerGeral.start();
    }

    
}
