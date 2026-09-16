import javax.swing.*;
import java.awt.*;

public class Chefe {
    
    int vel,vida,chefeX,chefeY,tamX,tamY,tiroX,tiroY,tamTiroX,tamTiroY,areaColisãoCorpo,areaColisaoAsaDireita,areaColisaoAsaEsquerda,areaColisaoAsasCorpo,vidaaAsaDireita,vidaAsaEsquerda;
    Image chefeImg,imgTiro,imgAreaDeColisão;
    boolean descer = true;

    

    public Chefe(String caminho,String caminhoTiroImg){
        chefeY = 100;
        chefeX = 500;

        chefeImg = new ImageIcon(getClass().getResource(caminho)).getImage();
        imgTiro = new ImageIcon(getClass().getResource(caminhoTiroImg)).getImage();
        imgAreaDeColisão = new ImageIcon(getClass().getResource("/chefes/chefe1/coli.jpg")).getImage();

        tamX = 480;
        tamY = 480;
        vida = 100;
        vel = 0;

        vidaAsaEsquerda = 50;
        vidaaAsaDireita = 50;

        areaColisãoCorpo = 110;
        areaColisaoAsaDireita = 50;
        areaColisaoAsaEsquerda = 50;
        areaColisaoAsasCorpo = 50;
    }

    public void desenharchefe1(Graphics g){
        
        

        g.drawImage(chefeImg, chefeX -150, chefeY,tamX,tamY, null);
        g.drawImage(imgAreaDeColisão, chefeX + 45, chefeY + 300, tamX - 390,areaColisãoCorpo,null);
        

       

        //asa esquerda 
      
        g.drawImage(imgAreaDeColisão, chefeX - 13, chefeY + 100, 55,areaColisaoAsasCorpo,null);


        g.drawImage(imgAreaDeColisão, chefeX - 0, chefeY + 200, 15,areaColisaoAsaEsquerda ,null);
        g.drawImage(imgAreaDeColisão, chefeX - 27, chefeY + 225, 30,areaColisaoAsaEsquerda ,null);
        g.drawImage(imgAreaDeColisão, chefeX - 50, chefeY + 275, 30,areaColisaoAsaEsquerda ,null);
        g.drawImage(imgAreaDeColisão, chefeX - 110, chefeY + 325, 65,areaColisaoAsaEsquerda ,null);


        //asa direita 
        
        g.drawImage(imgAreaDeColisão, chefeX + 137, chefeY + 100, 55,areaColisaoAsasCorpo ,null);


        g.drawImage(imgAreaDeColisão, chefeX + 160, chefeY + 200, 15,areaColisaoAsaDireita ,null);
        g.drawImage(imgAreaDeColisão, chefeX + 175, chefeY + 225, 30,areaColisaoAsaDireita ,null);
        g.drawImage(imgAreaDeColisão, chefeX + 200, chefeY + 275, 30,areaColisaoAsaDireita ,null);
        g.drawImage(imgAreaDeColisão, chefeX + 225, chefeY + 325, 65,areaColisaoAsaDireita ,null);
         
    }

    
    public Rectangle getBounds(){
        return new Rectangle(chefeX + 45,chefeY + 300,tamX - 390,areaColisãoCorpo);
    }
    
    //esse aqui não causa dano na arma
    public Rectangle getBoundsAsaEsquerdaCorpo(){
        return new Rectangle(chefeX -13 , chefeY + 100, 55,areaColisaoAsasCorpo);
    }

    //Asa esquerda
    // Asa esquerda
    public Rectangle getBoundsAsaEsquerda2(){
        return new Rectangle(chefeX - 0, chefeY + 200, 15, areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda3(){
        return new Rectangle(chefeX - 27, chefeY + 225, 30, areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda4(){
        return new Rectangle(chefeX - 50, chefeY + 275, 30, areaColisaoAsaEsquerda);
    }

    public Rectangle getBoundsAsaEsquerda5(){
        return new Rectangle(chefeX - 110, chefeY + 325, 65, areaColisaoAsaEsquerda);
    }

   
    

    //esse aqui não causa dano na arma
    public Rectangle getBoundsAsaDireitaCorpo(){
        return new Rectangle(chefeX + 137, chefeY + 100, 55,areaColisaoAsasCorpo );
    }


    //Asa direita
    // Asa direita
    public Rectangle getBoundsAsaDireira2(){
        return new Rectangle(chefeX + 160, chefeY + 200, 15, areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita3(){
        return new Rectangle(chefeX + 175, chefeY + 225, 30, areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita4(){
        return new Rectangle(chefeX + 200, chefeY + 275, 30, areaColisaoAsaDireita);
    }

    public Rectangle getBoundsAsaDireita5(){
        return new Rectangle(chefeX + 225, chefeY + 325, 65, areaColisaoAsaDireita);
    }




    public void desenharTiroChefe1(Graphics g ,String caminho){
        g.drawImage(imgTiro,tiroX,tiroY,tamTiroX,tamTiroY,null);
    }
    
}
