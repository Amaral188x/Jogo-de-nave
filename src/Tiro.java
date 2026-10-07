import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Tiro {
    private ArrayList<Image> acertoSprites;
    private boolean desenharAcerto = false, podeExcluir = false,podeCausarDano = true;
    private int x,y,tamX,tamY,indiceAcerto = 0,vel = 30, distanciaChefeX, distanciaChefeY;
    private Image img;

    public Tiro(int x, int y,Image img,ArrayList<Image> acertoSprites){
        this.x = x;
        this.y = y;
        this.img = img;
        tamX = 20;
        tamY = 32;
        this.acertoSprites = acertoSprites;
    }

    // =========================================================
    // ATUALIZAÇÃO (chamada pelo timerGeral do Jogo)
    // =========================================================

    public void atualizar() {
        if(this.y <= -10){
            this.podeExcluir = true;
        }

       if(desenharAcerto){
        if(indiceAcerto < acertoSprites.size() - 1){
            indiceAcerto ++;
        }else{
            desenharAcerto = false;
            podeExcluir = true;
        }
       }
    }

    public Rectangle getBounds(){
        if(!podeCausarDano){
            return new Rectangle(0,0,0,0);
        }
        return new Rectangle(x,y,tamX,tamY);
    }

    public void desenharTiro(Graphics g){
        if(desenharAcerto){img = acertoSprites.get(indiceAcerto);
            tamX = 64;
            tamY = 64;
            g.drawImage(img, x -20,y - 10 , tamX,tamY,null);
        }else{
            tamX = 16;
            tamY = 32;
            g.drawImage(img, x, y , tamX,tamY,null);
        }
    }

        public ArrayList<Image> carregarsprites(String caminho, int quantidade){
            ArrayList<Image> lista = new ArrayList<>();
            for(int i = 1; i < quantidade; i++){
                lista.add(new ImageIcon(getClass().getResource(caminho +"(" + i + ").png")).getImage());
            }
            return lista;

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

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public boolean isDesenharAcerto() {
        return desenharAcerto;
    }

    public void setDesenharAcerto(boolean desenharAcerto) {
        this.desenharAcerto = desenharAcerto;
    }

    public boolean isPodeExcluir() {
        return podeExcluir;
    }

    public boolean isPodeCausarDano() {
        return podeCausarDano;
    }

    public void setPodeCausarDano(boolean podeCausarDano) {
        this.podeCausarDano = podeCausarDano;
    }

    public int getDistanciaChefeX() {
        return distanciaChefeX;
    }

    public void setDistanciaChefeX(int distanciaChefeX) {
        this.distanciaChefeX = distanciaChefeX;
    }

    public int getDistanciaChefeY() {
        return distanciaChefeY;
    }

    public void setDistanciaChefeY(int distanciaChefeY) {
        this.distanciaChefeY = distanciaChefeY;
    }
}
