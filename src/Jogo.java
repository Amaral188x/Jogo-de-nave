import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Jogo extends JPanel implements KeyListener,ActionListener, MouseListener, MouseMotionListener {
    private Nave nave;
    private JFrame janela;
    private Menu menu;
    public Timer timerGeral, timerTiro, timerAnimacao, timerSpawnInimigo,timerCarregarFundo;

    private Som somTiro = new Som("/sons/nave/tiro.wav");

    private ArrayList<Image> fundo = new ArrayList<>(), animacaoTiroNave = carregarsprites("/nave/animacaoTiro/", 4),
    animacaoTurbina = carregarsprites("/nave/turbina/", 6), acertoSprites = carregarsprites("/inimigo/acerto/", 10),
    explosaoInimigo = carregarsprites("/inimigo/explosao/", 10),fumaca = carregarsprites("/inimigo/fumaça/", 4);


    private ArrayList<Tiro> tirosNave = new ArrayList<>(), tirosParaRemover = new ArrayList<>();
    private ArrayList<Inimigo> inimigos = new ArrayList<>(), inimigosParaRemover = new ArrayList<>();

    @SuppressWarnings("unused") // //Só para tirar o  aviso que diz que pontos não está sendo usado ( Esta sendo usada para sistema progressão)
    private int indiceFundo = 1,totalFrames = 251, indiceAnimacaoTiro, pontos = 0, indiceTurbina = 0;
    private Image tiroNaveImagem = carregarSprite("/nave/tiro.png"), inimigoImg = carregarSprite("/inimigo/inimigo.png");

    private boolean 

    //controles via teclado
        ativarMouse = false, 
        atirar = false, 
        cima = false, 
        baixo = false, 
        esquerda = false, 
        direita = false;


    public Jogo(JFrame janela,Menu menu){
        try{
            this.janela = janela;
            this.menu = menu;
            setFocusable(true);
            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);
            
            nave = new Nave(new ImageIcon(getClass().getResource("/nave/nave.png")).getImage(),janela);
            

            for (int i = 1; i <= 10; i++){
                fundo.add(new ImageIcon(getClass().getResource("/jogo/fundo/(" + i + ").jpg")).getImage());
            }

            timerCarregarFundo = new Timer(5,new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    try{
                        if(indiceFundo <= totalFrames){
                            if(fundo.size() - 1 < 20){
                                BufferedImage img = ImageIO.read(getClass().getResource("jogo/fundo/(" + indiceFundo + ").jpg"));
                                fundo.add(img);
                                indiceFundo ++;
                            }
                        }else{
                            indiceFundo = 1;      
                        }

                    }catch(Exception Err){
                        System.out.println("ERRO AO CARREEGAR IMAGEM DO FUNDO! CÓDIGO DE ERRO: " + Err);
                    }
                }
            });
            

            //Timers==================================
            timerGeral = new Timer(16,this);
            timerTiro = new Timer(200,new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {

                    if(atirar){
                        tirosNave.add(new Tiro(nave.x + 70,nave.y,tiroNaveImagem,acertoSprites));
                        somTiro.tocarSom();
                    }
                }
            });

            timerSpawnInimigo = new Timer(1000,new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    inimigos.add(new Inimigo(inimigoImg,explosaoInimigo,fumaca));
                }
            });
            

            timerAnimacao = new Timer(50, new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    if(indiceAnimacaoTiro < animacaoTiroNave.size() - 1){
                        indiceAnimacaoTiro ++;
                    }else{
                        indiceAnimacaoTiro = 0;
                    }

                    if(indiceTurbina < animacaoTurbina.size() - 1){
                        indiceTurbina ++;
                    }else{
                        indiceTurbina = 0;
                    }
                }
            });

            //========================================



        }catch(Exception Err){
            System.out.print("ERRO NO CONSTRUTOR DA CLASSE JOGO! CÓDIGO DE ERRO: " + Err);
        }
        }
    

   
   //Desenhar coisas=========================================================
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(fundo != null){
            g.drawImage(fundo.get(1),0,0,getWidth(),getHeight(),null);
            fundo.remove(0);
        }

        
        if(atirar){
            if(animacaoTiroNave != null){
                g.drawImage(animacaoTiroNave.get(indiceAnimacaoTiro), nave.x + 60,nave.y,32,32,null);
            }
        }

        if(inimigos != null){
            for(Inimigo inimigo : inimigos){
                inimigo.desenhar(g);
            }
        }
        
        if(animacaoTurbina != null){
            g.drawImage(animacaoTurbina.get(indiceTurbina), nave.x + 42, nave.y + 110,64,64,null);
        }

        if(tirosNave != null){
            for(Tiro tiro : tirosNave){
                tiro.desenharTiro(g);    
            }
        }

        nave.desenharNave(g);
    }
    //========================================================================

    //Teclado=================================================================
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
           timerGeral.stop();
           timerAnimacao.stop();
           timerSpawnInimigo.stop();
           timerTiro.stop();
           timerCarregarFundo.stop();
           pontos = 0;

           inimigos.clear();
           inimigosParaRemover.clear();
           tirosNave.clear();
           tirosParaRemover.clear();

            janela.setContentPane(menu);
            menu.timerGeral.start();
            menu.requestFocusInWindow();
            janela.revalidate();
            janela.repaint();
        }

        if(e.getKeyCode() == KeyEvent.VK_M){
            ativarMouse = !ativarMouse;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            atirar = true;
        }

        if(e.getKeyCode() == KeyEvent.VK_A){
            esquerda = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            direita = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_W){
            cima = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            baixo = true;
        }
      
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            atirar = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_A){
            esquerda = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            direita = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_W){
            cima = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            baixo = false;
        }

    }
    //==================================================================================== 


    
    //Atualizar variáveis e fazer outras coisas===========================================
    @Override
    public void actionPerformed(ActionEvent e) {

        if(inimigos != null){
            for(Inimigo inimigo : inimigos){
                inimigo.y += inimigo.vel;

                if(inimigo.getBounds().intersects(nave.getbounds()) & inimigo.podeColidir){
                       
                        inimigo.vida = 0;
                        nave.vida -= 10;
                    }

                if(inimigo.podeExcluir){
                    inimigosParaRemover.add(inimigo);
                    pontos ++;
                }

                for(Tiro tiro : tirosNave){
                    

                    if(tiro.getBounds().intersects(inimigo.getBounds()) & inimigo.podeColidir){
                        if(tiro.podeCausarDano){
                            inimigo.vida --;
                            tiro.podeCausarDano = false;
                        }
                        
                        if(tiro.podeExcluir){
                            tirosParaRemover.add(tiro);
                        }

                        tiro.desenharAcerto = true;
                        tiro.vel = -inimigo.vel;
                    }
                }
            }
        }

        inimigos.removeAll(inimigosParaRemover);
        inimigosParaRemover.clear();

        if(tirosNave != null){
            for(Tiro tiro : tirosNave){
                tiro.y -= tiro.vel;
                if(tiro.podeExcluir){
                    tirosParaRemover.add(tiro);
                }
            }
            
        }
        tirosNave.removeAll(tirosParaRemover);
        tirosParaRemover.clear();

        
        

        if(cima){
            nave.y -= nave.vel;
        }
        if(baixo){
            nave.y += nave.vel;
        }
        if(esquerda){
            nave.x -= nave.vel;
        }
        if(direita){
            nave.x += nave.vel;
        }

        repaint();


    }
    //====================================================================================

    //Métodos para facilitar a vida e deixar o código menos redundante
    public ArrayList<Image> carregarsprites(String caminho, int quantidade){
        ArrayList<Image> lista = new ArrayList<>();
        for(int i = 1; i <= quantidade; i++){
            lista.add(new ImageIcon(getClass().getResource(caminho +"(" + i + ").png")).getImage());
        }
        return lista;

    }

    public Image carregarSprite(String caminho){
        return new ImageIcon(getClass().getResource(caminho)).getImage();
    }

    //Mouse===============================================================================

    @Override
    public void mouseDragged(MouseEvent e) {
        if(ativarMouse){
            nave.x = e.getX() - 55;
            nave.y = e.getY() - 65;
        } 
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(ativarMouse){
            nave.x = e.getX() - 55;
            nave.y = e.getY() - 65;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            atirar = true;
        }
    
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1){
            atirar = false;
        }
    
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }
    //==================================================================================
}
