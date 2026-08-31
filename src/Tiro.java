import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Tiro implements ActionListener{
    public ArrayList<Image> acertoSprites;
    public boolean desenharAcerto = false, podeExcluir = false,podeCausarDano = true;
    public int x,y,tamX,tamY,indiceAcerto = 0,vel = 30;
    private Image img;
    
    public Tiro(int x, int y,Image img,ArrayList<Image> acertoSprites){
        this.x = x;
        this.y = y;
        this.img = img;
        tamX = 16;
        tamY = 32;
        this.acertoSprites = acertoSprites;
        Timer timer = new Timer(50, this);
        timer.start();
    }

    public Rectangle getBounds(){
        return new Rectangle(x,y,tamX,tamY);
    }

    public void desenharTiro(Graphics g){
        if(desenharAcerto){
            img = acertoSprites.get(indiceAcerto);
            tamX = 64;
            tamY = 64;
            g.drawImage(img, x -10,y - 100, tamX,tamY,null);
        }else{
            tamX = 16;
            tamY = 32;
            g.drawImage(img, x,y, tamX,tamY,null);
        } 
    }

    public ArrayList<Image> carregarsprites(String caminho, int quantidade){
        ArrayList<Image> lista = new ArrayList<>();
        for(int i = 1; i < quantidade; i++){
            lista.add(new ImageIcon(getClass().getResource(caminho +"(" + i + ").png")).getImage());
        }
        return lista;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
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
    
}
