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
    public Timer timerGeral,timerAdicionarFrame;
    private int indiceFundo = 1,totalFrames = 286;
    private Jogo jogo;
    
    public Menu(JFrame janela){
        this.janela = janela;
        setFocusable(true);
        addKeyListener(this);
        janela.setTitle("Menu");

        jogo = new Jogo(janela,this);

        for (int i = 1; i <= 10; i++){
            fundo.add(new ImageIcon(getClass().getResource("/menu/fundo/(" + i + ").jpg")).getImage());
        }

        timerAdicionarFrame = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    if(indiceFundo <= totalFrames){
                        if(fundo.size() < 20){
                            BufferedImage img = ImageIO.read(getClass().getResource("/menu/fundo/(" + indiceFundo + ").jpg"));
                            fundo.add(img);
                            indiceFundo ++;
                        }else{
                            return;
                        }
                    }else{
                        indiceFundo = 1;      
                    }      
                }  
             catch(Exception Err){
                System.out.println("ERRO AO CARREGAR FUNDO NO MENU! CÓDIGO DE ERRO: " + Err);
            }
        }
        });

        timerAdicionarFrame.start();
        
        

        timerGeral = new Timer(20,this);
        timerGeral.start(); 
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(fundo.size() >= 3){
            g.drawImage(fundo.get(0), 0,0,getWidth(),getHeight(),null);
            fundo.remove(0);
        }

        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
        

   
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            jogo.resetarJogo();

            // Iniciar os timers da partida
            jogo.timerAnimacao.start();
            jogo.timerSpawnInimigo.start();
            jogo.timerTiro.start();
            jogo.timerGeral.start();
            jogo.timerCarregarFundo.start();
            jogo.timerTiroChefe.start();
            jogo.timerAtivarEspecialChefe.start();

            // Iniciar música
            jogo.somFundo.tocarLoop();

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

    
}
