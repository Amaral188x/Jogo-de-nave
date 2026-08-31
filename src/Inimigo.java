import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class Inimigo implements ActionListener{
    public boolean podeExplodir = false,podeExcluir = false, podeColidir = true,tocarExplosao = true;
    public int x,y,vida = 3,vel = 5, tamX = 150, tamY = 150,indice,indiceFumaca = 0;
    private ArrayList<Image> explosao,fumaca;
    private Image imgInimigo;
    private Timer timer;
    private Som SomExplosao = new Som("/sons/inimigo/explosao.wav");

    public Inimigo(Image imgInimigo,ArrayList<Image> explosao, ArrayList fumaca){
        this.imgInimigo = imgInimigo;
        this.explosao = explosao;
        this.fumaca = fumaca;

        timer = new Timer(50,this);
        timer.start();

        Random random = new Random();
        y = -50;
        x = random.nextInt(1800);

    }

    public void desenhar(Graphics g){
        if(podeExplodir){
            g.drawImage(explosao.get(indice),x - 80,y - 60,tamX * 2,tamY * 2,null);
        }else{
           
            g.drawImage(imgInimigo,x,y,tamX,tamY,null);
            g.drawImage(fumaca.get(indiceFumaca), x + 60, y + 25, 80,80,null);
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, tamX, tamY);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(vida <= 0){
            if(tocarExplosao){
                SomExplosao.tocarSom();
                tocarExplosao = false;
            }
            podeColidir = false;
            vel = 5;
            podeExplodir = true;
        }
        
        if(indiceFumaca < fumaca.size() - 1){
            indiceFumaca ++;
        }else{
            indiceFumaca = 0;
        }

       if(podeExplodir){
            if(explosao != null){
                if(indice < explosao.size() - 1){
                    indice ++;
                }else{
                    indice --;
                    podeExcluir = true;
                }
            }
        }
    }
    
}
