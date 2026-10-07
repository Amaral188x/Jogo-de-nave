import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;

public class Nave {

    @SuppressWarnings("unused") //Só para tirar o  aviso que diz que não está sendo usada ( Esta sendo usada para pegar o tamanho da janela)
    private JFrame janela;
    private int x, y, vida, vidaMaxima, vel = 20,
        tamX = 150,
        tamY = 150;
    private Image naveImg;

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

    // =========================================================
    // ENCAPSULAMENTO
    // =========================================================

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVel() {
        return vel;
    }

    public Image getNaveImg() {
        return naveImg;
    }

    public void setNaveImg(Image naveImg) {
        this.naveImg = naveImg;
    }
}
