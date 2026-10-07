import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Inimigo {
    private boolean podeExplodir = false,podeExcluir = false, podeColidir = true,tocarExplosao = true;
    private int x,y,vida = 3,vel = 5, tamX = 150, tamY = 150,indice,indiceFumaca = 0;
    private ArrayList<Image> explosao,fumaca;
    private Image imgInimigo;
    private Som SomExplosao = new Som("/sons/inimigo/explosao.wav");

    @SuppressWarnings("unchecked")
    public Inimigo(Image imgInimigo,ArrayList<Image> explosao, @SuppressWarnings("rawtypes")  ArrayList fumaca){
        this.imgInimigo = imgInimigo;
        this.explosao = explosao;
        this.fumaca = fumaca;

        Random random = new Random();
        y = -50;
        x = random.nextInt(1800);

    }

    // =========================================================
    // ATUALIZAÇÃO (chamada pelo timerGeral do Jogo)
    // =========================================================

    public void atualizar(){
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

    public void desenhar(Graphics g){
        if(podeExplodir){
            g.drawImage(explosao.get(indice),x - 80,y - 60,tamX * 2,tamY * 2,null);
        }else{

            g.drawImage(imgInimigo,x,y,tamX,tamY,null);
            g.drawImage(fumaca.get(indiceFumaca), x + 60, y + 25, 80,80,null);
        }
    }

    public Rectangle getBounds(){
        return new Rectangle(x, y, tamX, tamY - 50);
    }

    // =========================================================
    // getters e setters
    // =========================================================

    public int getX() {
        return x;
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

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public boolean isPodeExcluir() {
        return podeExcluir;
    }

    public boolean isPodeColidir() {
        return podeColidir;
    }
}
