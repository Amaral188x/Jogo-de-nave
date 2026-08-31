import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Nave {

    private JFrame janela;
    public int x,y,vida,vel = 20,
        tamX = 150,
        tamY = 150;
    private Image naveImg;

    public Nave(Image img, JFrame janela){
        this.naveImg = img;
        this.janela = janela;
        x = janela.getWidth() / 2;
        y = janela.getHeight() / 2;
        

    }

    public void desenharNave(Graphics g){
        g.drawImage(naveImg, x, y,tamX,tamY ,null);
    }
    
    public Rectangle getbounds(){
        return new Rectangle(x,y,tamX,tamY);
    }
}
