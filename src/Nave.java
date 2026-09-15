import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Nave {

    @SuppressWarnings("unused") //Só para tirar o  aviso que diz que não está sendo usada ( Esta sendo usada para pegar o tamanho da janela)
    private JFrame janela;
    public int x, y, vida, vidaMaxima, vel = 20,
        tamX = 150,
        tamY = 150;
    public Image naveImg;

    public Nave(Image img, JFrame janela){
        this.naveImg = img;
        this.janela = janela;
        x = janela.getWidth() / 2;
        y = janela.getHeight() / 2;
        vida = 20;
        vidaMaxima = 20;
        

    }

    public void desenharNave(Graphics g){
        g.drawImage(naveImg, x, y,tamX,tamY ,null);
    }
    
    public Rectangle getbounds(){
        return new Rectangle(x,y,tamX,tamY);
    }
}
