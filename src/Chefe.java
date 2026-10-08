import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Chefe {

    private int vel, vida, chefeX, chefeY, tamX, tamY;
    private int tiroX, tiroY, tamTiroX, tamTiroY;

    private int areaColisaoCorpo;
    private int areaColisaoAsaDireita;
    private int areaColisaoAsaEsquerda;
    private int areaColisaoAsasCorpo;
    private int indiceEspecial = 0;
    private int especialCrescer = 0;

    private int vidaaAsaDireita;
    private int vidaAsaEsquerda;

    // Imagens
    private Image corpoNormal;
    private Image corpoDanificado;
    private Image especialMeio = new ImageIcon(getClass().getResource("/chefes/chefe1/Especial/energiaMeio.png")).getImage();
    private Image especialFim= new ImageIcon(getClass().getResource("/chefes/chefe1/Especial/energiaFim.png")).getImage();

    private Image asaDireitaNormal;
    private Image asaDireitaDanificada;

    private Image asaEsquerdaNormal;
    private Image asaEsquerdaDanificada;

    private Image imgTiro;
    private Image imgAreaDeColisao;

    private ArrayList<Image> especialSprites = new ArrayList<>();

    public boolean podecrescerEspecial = false;
    private boolean desenhaColisao = false;
    private boolean especial = false;
    private boolean especialPodeCausarDano = true;

    public Chefe( String caminhoTiroImg, ArrayList<Image> especialSprites) {

        chefeY = 100;
        chefeX = 500;
        this.especialSprites = especialSprites;

        // Corpo
        corpoNormal = new ImageIcon(getClass().getResource("/chefes/chefe1/corpoNormal.png")).getImage();

        corpoDanificado = new ImageIcon(getClass().getResource("/chefes/chefe1/corpoDanificado.png")).getImage();

        // Asa direita
        asaDireitaNormal = new ImageIcon(getClass().getResource("/chefes/chefe1/asaDireitaNormal.png")).getImage();

        asaDireitaDanificada = new ImageIcon(getClass().getResource("/chefes/chefe1/asaDireitaDanificada.png")).getImage();

        // Asa esquerda
        asaEsquerdaNormal = new ImageIcon(getClass().getResource("/chefes/chefe1/asaEsquerdaNormal.png")).getImage();

        asaEsquerdaDanificada = new ImageIcon(getClass().getResource("/chefes/chefe1/asaEsquerdaDanificada.png")).getImage();

        // Tiro
        imgTiro = new ImageIcon(getClass().getResource(caminhoTiroImg)).getImage();

        // Imagem para mostrar as áreas de colisão
        imgAreaDeColisao = new ImageIcon(getClass().getResource("/chefes/chefe1/coli.jpg")).getImage();

        tamX = 480;
        tamY = 480;

        vida = 200;
        vel = 200;

        // Vida das asas
        vidaAsaEsquerda = 25;
        vidaaAsaDireita = 25;

        // Áreas de colisão
        areaColisaoCorpo = 110;
        areaColisaoAsaDireita = 50;
        areaColisaoAsaEsquerda = 50;
        areaColisaoAsasCorpo = 50;
    }

    public void desenharchefe1(Graphics g) {

        // =====================================================
        // ASA ESQUERDA
        // =====================================================

        if (vidaAsaEsquerda > 0) {

            g.drawImage(asaEsquerdaNormal, chefeX - 150,  chefeY, tamX, tamY, null);

        } else {

            g.drawImage( asaEsquerdaDanificada, chefeX - 150, chefeY, tamX, tamY, null);
        }


        // =====================================================
        // ASA DIREITA
        // =====================================================

        if (vidaaAsaDireita > 0) {

            g.drawImage(asaDireitaNormal,  chefeX - 150, chefeY, tamX, tamY, null);

        } else {

            g.drawImage(asaDireitaDanificada,  chefeX - 150, chefeY, tamX, tamY,null);
        }


        // =====================================================
        // CORPO
        // =====================================================

        if (vida > 49) {

            g.drawImage(corpoNormal, chefeX - 150, chefeY, tamX, tamY, null);

        } else {

            g.drawImage( corpoDanificado, chefeX - 150, chefeY, tamX, tamY, null);
        }


        // =====================================================
        // ÁREAS DE COLISÃO
        // =====================================================

        if (desenhaColisao) {

            // Corpo
            g.drawImage( imgAreaDeColisao, chefeX + 45, chefeY + 300,tamX - 390, areaColisaoCorpo, null);


            // Asa esquerda

            g.drawImage( imgAreaDeColisao, chefeX - 13, chefeY + 100, 55, areaColisaoAsasCorpo,null);

            g.drawImage( imgAreaDeColisao, chefeX, chefeY + 200, 15, areaColisaoAsaEsquerda, null);

            g.drawImage(imgAreaDeColisao, chefeX - 27, chefeY + 225, 30, areaColisaoAsaEsquerda, null);

            g.drawImage(imgAreaDeColisao, chefeX - 50, chefeY + 275, 30, areaColisaoAsaEsquerda, null);

            g.drawImage(imgAreaDeColisao, chefeX - 110, chefeY + 325, 65 ,areaColisaoAsaEsquerda, null);


            // Asa direita

            g.drawImage(imgAreaDeColisao, chefeX + 137, chefeY + 100, 55, areaColisaoAsasCorpo, null);

            g.drawImage(imgAreaDeColisao, chefeX + 160, chefeY + 200, 15, areaColisaoAsaDireita, null);

            g.drawImage(imgAreaDeColisao, chefeX + 175, chefeY + 225, 30, areaColisaoAsaDireita, null
            );

            g.drawImage(imgAreaDeColisao, chefeX + 200, chefeY + 275, 30, areaColisaoAsaDireita, null
            );

            g.drawImage(imgAreaDeColisao, chefeX + 225, chefeY + 325, 65, areaColisaoAsaDireita, null );
        }
    }


    // =========================================================
    // COLISÃO DO CORPO
    // =========================================================

    public Rectangle getBounds() {

        return new Rectangle(chefeX + 45, chefeY + 300, tamX - 390, areaColisaoCorpo);
    }


    // =========================================================
    // ASA ESQUERDA
    // =========================================================

    public Rectangle getBoundsAsaEsquerdaCorpo() {

        return new Rectangle(chefeX - 13,chefeY + 100,55,areaColisaoAsasCorpo);
    }

    public Rectangle getBoundsAsaEsquerda2() {

        return new Rectangle(chefeX,chefeY + 200,15,areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda3() {

        return new Rectangle(chefeX - 27,chefeY + 225,30,areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda4() {

        return new Rectangle(chefeX - 50,chefeY + 275,30,areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda5() {

        return new Rectangle(chefeX - 110,chefeY + 325,65,areaColisaoAsaEsquerda );
    }


    // =========================================================
    // ASA DIREITA
    // =========================================================

    public Rectangle getBoundsAsaDireitaCorpo() {

        return new Rectangle(chefeX + 137,chefeY + 100,55,areaColisaoAsasCorpo
        );
    }

    public Rectangle getBoundsAsaDireira2() {

        return new Rectangle(chefeX + 160,chefeY + 200,15,areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita3() {

        return new Rectangle(chefeX + 175,chefeY + 225,30,areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita4() {

        return new Rectangle(chefeX + 200,chefeY + 275,30,areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita5() {

        return new Rectangle(chefeX + 225,chefeY + 325,65,areaColisaoAsaDireita);
    }


    // =========================================================
    // TIRO
    // =========================================================

    public void desenharTiroChefe1(Graphics g, String caminho) {

        g.drawImage(imgTiro,tiroX,tiroY,tamTiroX,tamTiroY,null);
    }

    // =========================================================
    // TIRO
    // =========================================================

    public void desenharEspecial(Graphics g){
        if(especial){
            if(vidaaAsaDireita > 0){
                g.drawImage(especialSprites.get(indiceEspecial), chefeX + 160 + 64, chefeY + 350 + 64, 128, 128, null);
            }
            if(vidaAsaEsquerda > 0){
                g.drawImage(especialSprites.get(indiceEspecial), chefeX - 210 + 64, chefeY + 350 + 64, 128, 128, null);
            }
            if(indiceEspecial == 7){
                
                if(vidaaAsaDireita > 0){

                    g.drawImage(especialFim, chefeX + 147 + 64, chefeY + 570 + 64 + especialCrescer, 128 ,128,null);
                    g.drawImage(especialMeio, chefeX + 122 + 64, chefeY + 455 + 64, 160 , 128 + especialCrescer, null);
                }
                if(vidaAsaEsquerda > 0){

                    g.drawImage(especialFim, chefeX - 222 + 64, chefeY + 570 + 64 + especialCrescer, 128,128,null);
                    g.drawImage(especialMeio, chefeX - 247 + 64, chefeY + 455 + 64, 160 , 128 + especialCrescer, null);
                }
            }

        }
    }

    public void desenharAreaColisaoEspecial(Graphics g){
        g.drawImage(imgAreaDeColisao, chefeX + 122 + 64, chefeY + 350 + 64, 128, 128 + especialCrescer, null );
        g.drawImage(imgAreaDeColisao, chefeX - 247 + 64, chefeY + 350 + 64, 128, 128 + especialCrescer, null );
    }

    public Rectangle getBoundsEspecialDireia(){
        if(vidaaAsaDireita <= 0 || !especialPodeCausarDano){
            return new Rectangle(00, 0, 0,0);
        }else{
            return new Rectangle(chefeX + 160 + 64, chefeY + 350 + 64, 128, 384 + especialCrescer);
        }

    }

    public Rectangle getBoundsEspecialEsquerda(){

        if(vidaAsaEsquerda <= 0 || !especialPodeCausarDano){
            return new Rectangle(00, 0, 0,0);
        }else{
            return new Rectangle(chefeX - 210 + 64, chefeY + 350 + 64, 128, 384 + especialCrescer);
        }
    }

    // =========================================================
    // ENCAPSULAMENTO
    // =========================================================

    public int getVel() {
        return vel;
    }

    public void setVel(int vel) {
        this.vel = vel;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getVidaAsaEsquerda() {
        return vidaAsaEsquerda;
    }

    public void setVidaAsaEsquerda(int vidaAsaEsquerda) {
        this.vidaAsaEsquerda = vidaAsaEsquerda;
    }

    public int getVidaaAsaDireita() {
        return vidaaAsaDireita;
    }

    public void setVidaaAsaDireita(int vidaaAsaDireita) {
        this.vidaaAsaDireita = vidaaAsaDireita;
    }

    public int getChefeX() {
        return chefeX;
    }

    public void setChefeX(int chefeX) {
        this.chefeX = chefeX;
    }

    public int getChefeY() {
        return chefeY;
    }

    public int getTamX() {
        return tamX;
    }

    public int getTamY() {
        return tamY;
    }

    public int getAreaColisãoCorpo() {
        return areaColisaoCorpo;
    }

    public void setAreaColisãoCorpo(int areaColisaoCorpo) {
        this.areaColisaoCorpo = areaColisaoCorpo;
    }

    public int getAreaColisaoAsaDireita() {
        return areaColisaoAsaDireita;
    }

    public void setAreaColisaoAsaDireita(int areaColisaoAsaDireita) {
        this.areaColisaoAsaDireita = areaColisaoAsaDireita;
    }

    public int getAreaColisaoAsaEsquerda() {
        return areaColisaoAsaEsquerda;
    }

    public void setAreaColisaoAsaEsquerda(int areaColisaoAsaEsquerda) {
        this.areaColisaoAsaEsquerda = areaColisaoAsaEsquerda;
    }

    public int getAreaColisaoAsasCorpo() {
        return areaColisaoAsasCorpo;
    }

    public void setAreaColisaoAsasCorpo(int areaColisaoAsasCorpo) {
        this.areaColisaoAsasCorpo = areaColisaoAsasCorpo;
    }

    public int getIndiceEspecial() {
        return indiceEspecial;
    }

    public void setIndiceEspecial(int indiceEspecial) {
        this.indiceEspecial = indiceEspecial;
    }

    public int getEspecialCrescer() {
        return especialCrescer;
    }

    public void setEspecialCrescer(int especialCrescer) {
        this.especialCrescer = especialCrescer;
    }

    public ArrayList<Image> getEspecialSprites() {
        return especialSprites;
    }

    public boolean isEspecial() {
        return especial;
    }

    public void setEspecial(boolean especial) {
        this.especial = especial;
    }

    public boolean isEspecialPodeCausarDano() {
        return especialPodeCausarDano;
    }

    public void setEspecialPodeCausarDano(boolean especialPodeCausarDano) {
        this.especialPodeCausarDano = especialPodeCausarDano;
    }
}