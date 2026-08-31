import java.util.ArrayList;

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

public class Menu extends JPanel implements KeyListener,ActionListener{
    private JFrame janela;
    private ArrayList<Image> fundo = new ArrayList<>();
    public Timer timerGeral;
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

        timerGeral = new Timer(16,this);
        timerGeral.start(); 
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(fundo.get(1), 0,0,getWidth(),getHeight(),null);

        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
        

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            jogo.timerAnimacao.start();
            jogo.timerSpawnInimigo.start();
            jogo.timerTiro.start();
            jogo.timerGeral.start();

            janela.setContentPane(jogo);
            jogo.requestFocusInWindow();
            janela.revalidate();
            janela.repaint();
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
       

        if(indiceFundo <= totalFrames){
            fundo.add(new ImageIcon(getClass().getResource("/menu/fundo/(" + indiceFundo + ").jpg")).getImage());
            fundo.remove(0);
            indiceFundo ++;
        }else{
            indiceFundo = 1;      
        }
        repaint();

        
    }

    
}
