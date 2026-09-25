
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


public class Jogo extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener {

    // =========================================================
    // OBJETOS PRINCIPAIS
    // =========================================================

    public Nave nave;
    public Chefe chefe;

    private JFrame janela;
    private Menu menu;

    // =========================================================
    // TIMERS
    // =========================================================

    public Timer timerGeral;
    public Timer timerTiro;
    public Timer timerAnimacao;
    public Timer timerSpawnInimigo;
    public Timer timerCarregarFundo;
    public Timer timerTiroChefe;
    public Timer timerAtivarEspecialChefe;
    // =========================================================
    // SONS
    // =========================================================

    public Som perdeu = new Som("sons/fundo/perdeu.wav");
    public Som somTiro = new Som("/sons/nave/tiro.wav");
    public Som somExplosaoNave = new Som("/nave/explosao.wav");
    public Som somFundo = new Som("sons/fundo/fundo.wav");
    public Som somExplosao = new Som("/sons/inimigo/explosaoDanificarChefe.wav");
    public Som atingido = new Som("/sons/nave/atingido.wav");
    public Som especialChefeSom = new Som("/sons/inimigo/especialChefeSom.wav");

    // =========================================================
    // SPRITES
    // =========================================================

    private ArrayList<Image> spritesEspecialChefe = carregarsprites("/chefes/chefe1/Especial/", 11);

    private ArrayList<Image> spritesExplosaoChefe = carregarsprites("/chefes/chefe1/danificarArmas/", 10);

    private ArrayList<Image> curtoCircuito2 =carregarsprites("/chefes/chefe1/danificado2/", 40);

    private ArrayList<Image> curtoCircuito =carregarsprites("/chefes/chefe1/danificado/", 39);

    private ArrayList<Image> framesDerrota =new ArrayList<>();

    private ArrayList<Image> explosaoNave =carregarsprites("/nave/explosao/", 63);

    private ArrayList<Image> vidaNaveFrames =new ArrayList<>();

    private ArrayList<Image> fundo =new ArrayList<>();

    private ArrayList<Image> animacaoTiroNave =carregarsprites("/nave/animacaoTiro/", 4);

    private ArrayList<Image> animacaoTurbina =carregarsprites("/nave/turbina/", 6);

    private ArrayList<Image> acertoSprites =carregarsprites("/inimigo/acerto/", 15);

    private ArrayList<Image> explosaoInimigo =carregarsprites("/inimigo/explosao/", 10);

    private ArrayList<Image> fumaca =carregarsprites("/inimigo/fumaça/", 4);

    private ArrayList<Image> fumacaNaveSprites =carregarsprites("/nave/fumaça/", 45);

    //colocamos apenas 10 para previnir de dar erro de nullpoint (o restante dos frames serão carregados no timer fcarregarFundo)
    private ArrayList<Image> chefeDerrotadoSprites = new ArrayList<>();

    // =========================================================
    // OBJETOS DO JOGO
    // =========================================================

    public ArrayList<Tiro> tirosNave = new ArrayList<>();
    public ArrayList<Tiro> buracosDBala = new ArrayList<>();
    private ArrayList<Tiro> tirosParaRemover = new ArrayList<>();
    public ArrayList<Tiro> tiroschefe = new ArrayList<>();

    public ArrayList<Inimigo> inimigos = new ArrayList<>();
    private ArrayList<Inimigo> inimigosParaRemover = new ArrayList<>();

    private GridDebug malha = new GridDebug();

    // =========================================================
    // IMAGENS INDIVIDUAIS
    // =========================================================

    private Image tiroNaveImagem =carregarSprite("/nave/tiro.png");

    private Image inimigoImg =carregarSprite("/inimigo/inimigo.png");

    // =========================================================
    // ÍNDICES DAS ANIMAÇÕES
    // =========================================================

    private int indiceCurto = 0;
    private int indiceCurto2 = 0;
    private int indiceCurto3 = 0;
    private int indiceCurto4 = 0;

    private int indiceExplosaoChefe = 1;

    private int indiceSpritesExplosao = 0;

    private int indiceFumacaNave = 0;
    private int indiceFumacaNave2 = 0;

    private int indiceAnimacaoDerrota = 1;
    private int indiceAnimacaoTiro = 0;
    private int indiceTurbina = 0;

    private int indiceFundo = 1;
    private int indiceExplosaoNave = 0;

    private int numeroFrameAtualVidaNave = 1;

    // =========================================================
    // CONFIGURAÇÕES DE ANIMAÇÃO
    // =========================================================

    private final int totalFrames = 251;
    private final int totalFramesVida = 300;

    // =========================================================
    // PONTUAÇÃO
    // =========================================================

    public int pontos = 0;

    // =========================================================
    // CONTROLE DAS EXPLOSÕES DO CHEFE
    // =========================================================

    public boolean podeTocarSomExplosaoCorpo = true;
    public boolean podeTocarSomExplosaoAsaEsquerda = true;
    public boolean podeTocarSomExplosaoAsaDireita = true;

    public boolean podeExplodirCorpoChefe = true;
    public boolean podeExplodirAsaEsquerdaChefe = true;
    public boolean podeExplodirAsaDireitaChefe = true;

    public boolean desenharMalha = false, desenharMalhaNoChefe = false;

    // =========================================================
    // CONTROLES
    // =========================================================

    private boolean ativarMouse = false;
    private boolean atirar = false;

    private boolean cima = false;
    private boolean baixo = false;
    private boolean esquerda = false;
    private boolean direita = false;

    private boolean terminouExplosaoChefe = false;

    private boolean fumacaNave = false;


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public Jogo(JFrame janela, Menu menu) {

        try {
            this.janela = janela;
            this.menu = menu;

            setFocusable(true);

            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);

            // =====================================================
            // NAVE E CHEFE
            // =====================================================

            nave = new Nave(new ImageIcon(getClass().getResource("/nave/nave.png")).getImage(),janela);

            chefe = new Chefe("/chefes/chefe1/chefe.png","/chefes/chefe1/tiroChefe1.png", spritesEspecialChefe);

            // =====================================================
            // MÚSICA
            // =====================================================

            somFundo.setVolume(2.0f);
            somFundo.tocarLoop();

            // =====================================================
            // FUNDO
            // =====================================================

            for (int i = 1; i <= 10; i++) {
                fundo.add(new ImageIcon(getClass().getResource("/jogo/fundo/(" + i + ").jpg")).getImage());
            }

            // =====================================================
            // VIDA DA NAVE
            // =====================================================

            for (int i = 1; i <= 10; i++) {
                fundo.add(new ImageIcon(getClass().getResource("/nave/vida_normal/(" + i + ").png")).getImage());
            }


            // =====================================================
            // TIMER PARA CARREGAR IMAGENS
            // =====================================================

            timerCarregarFundo = new Timer(5, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    try {

                        // -------------------------------------------------
                        // CARREGAR FUNDO
                        // -------------------------------------------------

                        if (indiceFundo <= totalFrames) {

                            if (fundo.size() - 1 < 20) {

                                BufferedImage img = ImageIO.read(getClass().getResource("/jogo/fundo/(" +indiceFundo +").jpg"));

                                fundo.add(img);
                                indiceFundo++;
                            }

                        } else {

                            indiceFundo = 1;
                        }

                        // -------------------------------------------------
                        // CARREGAR EXPLOSAO DO CHEFE
                        // -------------------------------------------------
                        if(chefe.vida < 30 && !terminouExplosaoChefe){
                            if(chefeDerrotadoSprites.size() < 20){
                                if(indiceExplosaoChefe < 352 ){
                                    BufferedImage img = ImageIO.read(getClass().getResource("/chefes/chefe1/explosao/(" + indiceExplosaoChefe + ").png"));
                                    chefeDerrotadoSprites.add(img);
                                    indiceExplosaoChefe ++;
                                }else{
                                    terminouExplosaoChefe = true;
                                    indiceExplosaoChefe = 1;
                                    chefeDerrotadoSprites.clear();

                                }
                            }
                        }

                        // -------------------------------------------------
                        // CARREGAR DERROTA
                        // -------------------------------------------------

                        if (nave.vida <= 0 && indiceExplosaoNave >= explosaoNave.size() - 1) {

                            if (framesDerrota.size() < 20) {

                                if (indiceAnimacaoDerrota < 114) {

                                    BufferedImage img =ImageIO.read(getClass().getResource("/jogo/derrota/(" +indiceAnimacaoDerrota +").png"));

                                    framesDerrota.add(img);
                                    indiceAnimacaoDerrota++;

                                    if (indiceAnimacaoDerrota == 57) {
                                        perdeu.setVolume(2.0f);
                                        perdeu.tocarSom();
                                    }

                                } else {

                                    indiceAnimacaoDerrota = 1;

                                    timerGeral.stop();
                                    timerAnimacao.stop();
                                    timerSpawnInimigo.stop();
                                    timerTiro.stop();
                                    timerCarregarFundo.stop();
                                    timerAtivarEspecialChefe.stop();
                                    somFundo.parar();

                                    pontos = 0;
                                    nave.vida = nave.vidaMaxima;

                                    inimigos.clear();
                                    inimigosParaRemover.clear();

                                    tirosNave.clear();
                                    tirosParaRemover.clear();

                                    framesDerrota.clear();

                                    janela.setContentPane(menu);

                                    menu.timerGeral.start();
                                    menu.requestFocusInWindow();

                                    janela.revalidate();
                                    janela.repaint();
                                }
                            }
                        }


                        // -------------------------------------------------
                        // VIDA DA NAVE
                        // -------------------------------------------------

                        if (numeroFrameAtualVidaNave < totalFramesVida) {

                            if (vidaNaveFrames.size() < 20 && nave.vida > nave.vidaMaxima / 2) {

                                BufferedImage img = ImageIO.read(getClass().getResource("/nave/vida_normal/(" +numeroFrameAtualVidaNave +").png"));

                                vidaNaveFrames.add(img);
                                numeroFrameAtualVidaNave++;

                            } else if (vidaNaveFrames.size() < 20 &&nave.vida <= nave.vidaMaxima / 2 &&nave.vida > 5) {

                                nave.naveImg =carregarSprite("/nave/nave_Danificada.png");

                                fumacaNave = true;

                                BufferedImage img = ImageIO.read(getClass().getResource("/nave/vida_metade/(" +numeroFrameAtualVidaNave +").png"));

                                vidaNaveFrames.add(img);
                                numeroFrameAtualVidaNave++;

                            } else if (vidaNaveFrames.size() < 20 &&nave.vida <= 5) {

                                BufferedImage img =ImageIO.read(getClass().getResource("/nave/vida_baixa/(" +numeroFrameAtualVidaNave +").png"));

                                vidaNaveFrames.add(img);
                                numeroFrameAtualVidaNave++;
                            }

                        } else {
                            numeroFrameAtualVidaNave = 1;
                        }

                    } catch (Exception erro) {
                        System.out.println("ERRO AO CARREGAR IMAGEM DO FUNDO! " +"CÓDIGO DE ERRO: " + erro);
                    }
                }
            });


            // =====================================================
            // TIMER GERAL
            // =====================================================

            timerGeral = new Timer(16, this);


            // =====================================================
            // TIMER DE TIRO
            // =====================================================

            timerTiro = new Timer(200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (atirar) {

                        tirosNave.add(new Tiro(nave.x + 70,nave.y,tiroNaveImagem,acertoSprites));

                        somTiro.tocarSom();
                        somTiro.setVolume(0.5f);
                    }
                }
            });

            timerTiroChefe = new Timer(700,new ActionListener() {
                @Override 
                public void actionPerformed(ActionEvent e){

                    if(!chefe.especial){
                        if(chefe.vidaAsaEsquerda > 0){
                            tiroschefe.add(new Tiro(chefe.chefeX - 100, chefe.chefeY + 300, tiroNaveImagem, acertoSprites));
                        }

                        tiroschefe.add(new Tiro(chefe.chefeX + 84, chefe.chefeY + 300, tiroNaveImagem, acertoSprites));

                        if(chefe.vidaaAsaDireita > 0){
                            tiroschefe.add(new Tiro(chefe.chefeX + 270, chefe.chefeY + 300, tiroNaveImagem, acertoSprites));
                        }
                        
                    }
                }  
            });

            timerAtivarEspecialChefe = new Timer(6000, new ActionListener() {
                @Override 
                public void actionPerformed(ActionEvent e){
                    chefe.especial = true;
                }
            });

            

            // =====================================================
            // TIMER DOS INIMIGOS
            // =====================================================

            timerSpawnInimigo = new Timer(2000, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    inimigos.add(new Inimigo(inimigoImg,explosaoInimigo,fumaca));
                }
            });


            // =====================================================
            // TIMER DAS ANIMAÇÕES
            // =====================================================

            timerAnimacao = new Timer(50, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if(chefe.especial){
                        chefe.especialCrescer += 40;
                        if(chefe.indiceEspecial < chefe.especialSprites.size() - 1){
                            if(chefe.indiceEspecial == 7 && chefe.especialCrescer < 1800){
                                if(especialChefeSom.terminou()){
                                    especialChefeSom.tocarSom();
                                }
                                return;
                            }else{
                                chefe.indiceEspecial ++;
                            }
                            
                        }else{
                            chefe.especial = false;
                            chefe.indiceEspecial = 0;
                            chefe.especialCrescer = 0;
                            chefe.especialPodeCausarDano = true;
                        }
                    }
                    // Animação do tiro
                    if (indiceAnimacaoTiro < animacaoTiroNave.size() - 1) {
                        indiceAnimacaoTiro++;
                    } else {
                        indiceAnimacaoTiro = 0;
                    }

                    // Animação da turbina
                    if (indiceTurbina < animacaoTurbina.size() - 1) {
                        indiceTurbina++;
                    } else {
                        indiceTurbina = 0;
                    }

                    // Fumaça
                    if (fumacaNave ||chefe.vidaAsaEsquerda <= 0) {
                        if (indiceFumacaNave < fumacaNaveSprites.size() - 1) {
                           indiceFumacaNave++;
                        } else {
                            indiceFumacaNave = 0;
                        }
                    }
                }
            });

        } catch (Exception erro) {
            System.out.println("ERRO NO CONSTRUTOR DA CLASSE JOGO! " +"CÓDIGO DE ERRO: " + erro);
        }
    }


    // =========================================================
    // DESENHAR
    // =========================================================

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        // =====================================================
        // FUNDO
        // =====================================================

        if (!fundo.isEmpty() && fundo.size() >= 2) {

            g.drawImage(fundo.get(1),0,0,getWidth(),getHeight(),null);

            if (fundo.size() >= 3) {
                fundo.remove(0);
            }
        }


        // =====================================================
        // VIDA DA NAVE
        // =====================================================

        if (vidaNaveFrames.size() >= 2) {

            g.drawImage(vidaNaveFrames.get(1),20,20,200,100,null);

            if (vidaNaveFrames.size() >= 3) {
                vidaNaveFrames.remove(0);
            }
        }


        // =====================================================
        // TIRO DA NAVE
        // =====================================================

        if (atirar) {
            g.drawImage(animacaoTiroNave.get(indiceAnimacaoTiro),nave.x + 60,nave.y,32,32,null);

        }


        // =====================================================
        // INIMIGOS
        // =====================================================

        for (Inimigo inimigo : inimigos) {
            inimigo.desenhar(g);
        }


        // =====================================================
        // TURBINA
        // =====================================================

        if (!animacaoTurbina.isEmpty() && nave.vida > 0) {
            g.drawImage(animacaoTurbina.get(indiceTurbina),nave.x + 42,nave.y + 110,64,64,null);
        }


        // =====================================================
        // EXPLOSÃO DA NAVE
        // =====================================================

        if (nave.vida <= 0) {
            g.drawImage(explosaoNave.get(indiceExplosaoNave),nave.x,nave.y,256,256,null);
        }


       

       

        // =====================================================
        // CHEFE
        // =====================================================

        if (pontos >= 5 && chefe.vida > 0) {
            
            // =====================================================
            // TIROS CHEFE
            // =====================================================

            if(!tiroschefe.isEmpty()){
                for(Tiro tiro : tiroschefe){
                    tiro.desenharTiro(g);
                }

            }
            chefe.desenharchefe1(g);
            chefe.desenharEspecial(g);

            // =====================================================
            // MALHA
            // =====================================================
            
            if(desenharMalha){
                malha.desenharNoChefe(g, chefe, 20);
            }

            if (desenharMalhaNoChefe){

                malha.desenhar(g,getWidth(), getHeight(), 50);
            }
            

            // Asa esquerda destruída
            if (chefe.vidaAsaEsquerda <= 0) {
               
                g.drawImage(curtoCircuito.get(indiceCurto),chefe.chefeX - 180,chefe.chefeY + 270,150,150,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave),chefe.chefeX - 120,chefe.chefeY + 230,150,150,null);

                g.drawImage(acertoSprites.get(indiceCurto2),chefe.chefeX - 130,chefe.chefeY + 230,150,150,null);
            }


            // Asa direita destruída
            if (chefe.vidaaAsaDireita <= 0) {

                g.drawImage(curtoCircuito2.get(indiceCurto3),chefe.chefeX + 160,chefe.chefeY + 250,200,200,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave2),chefe.chefeX + 120,chefe.chefeY + 230,200,200,null);

                g.drawImage(acertoSprites.get(indiceCurto4),chefe.chefeX + 130,chefe.chefeY + 230,150,150,null);
            }


            // Corpo danificado
            if (chefe.vida < 50) {

                g.drawImage(curtoCircuito2.get(indiceCurto3),chefe.chefeX - 10,chefe.chefeY + 300,200,200,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave2),chefe.chefeX + 10,chefe.chefeY + 270,200,200,null);

                g.drawImage(curtoCircuito.get(indiceCurto),chefe.chefeX - 30,chefe.chefeY + 240,150,150,null);
            }


            // Explosão da asa esquerda
            if (chefe.vidaAsaEsquerda <= 0 && podeExplodirAsaEsquerdaChefe) {

                g.drawImage(spritesExplosaoChefe.get(indiceSpritesExplosao),chefe.chefeX - 365,chefe.chefeY + 120,500,500,null);
            }


            // Explosão da asa direita
            if (chefe.vidaaAsaDireita <= 0 &&podeExplodirAsaDireitaChefe) {

                g.drawImage(spritesExplosaoChefe.get(indiceSpritesExplosao), chefe.chefeX,chefe.chefeY + 120, 500, 500, null);
            }


            // Explosão do corpo
            if (chefe.vida < 50 && podeExplodirCorpoChefe) {

                g.drawImage( spritesExplosaoChefe.get(indiceSpritesExplosao), chefe.chefeX - 200, chefe.chefeY,500, 500, null);
            }

            for (Tiro buraco : buracosDBala) {
            
                g.drawImage(new ImageIcon(getClass().getResource("/nave/marca de tiro.png")).getImage(), chefe.chefeX + buraco.distanciaChefeX, chefe.chefeY + buraco.distanciaChefeY, 20, 20, null);
            }

            
        }

        if(chefe.vida <= 0 && !terminouExplosaoChefe && !chefeDerrotadoSprites.isEmpty()){
            chefe.vel = 0;
            g.drawImage(chefeDerrotadoSprites.get(2), chefe.chefeX, chefe.chefeY, null);
            if(chefeDerrotadoSprites.size() >= 4){
                chefeDerrotadoSprites.remove(0);
            }
        }

        // =====================================================
        // TIROS DA NAVE
        // =====================================================

        for (Tiro tiro : tirosNave) {
            tiro.desenharTiro(g);
        }


        // =====================================================
        // MARCAS DE TIRO NO CHEFE
        // =====================================================

       

        
       
        // =====================================================
        // NAVE
        // =====================================================

        nave.desenharNave(g);



        if (fumacaNave && nave.vida <= nave.vidaMaxima / 2 && nave.vida > 0) {

            g.drawImage( fumacaNaveSprites.get(indiceFumacaNave), nave.x + 10, nave.y + 35, 200, 200, null );
        }


        // =====================================================
        // TEXTOS
        // =====================================================

        g.setFont(new java.awt.Font( "Arial", java.awt.Font.BOLD, 24));

        g.setColor(java.awt.Color.RED);

        g.drawString("LIFE: ", 10, 30);

        g.drawString( "vida asas: " + chefe.vidaAsaEsquerda + " " + chefe.vidaaAsaDireita, 10, 200);

        g.drawString("vida corpo " + chefe.vida,10,370);

        g.setColor(java.awt.Color.GREEN);

        g.drawString("SCORE: " + pontos, 10, 150);


        // =====================================================
        // TELA DE DERROTA
        // =====================================================

        if (!framesDerrota.isEmpty() && framesDerrota.size() >= 3 && nave.vida <= 0) {

            g.drawImage( framesDerrota.get(1), 0, 0, getWidth(), getHeight(), null);

            if (framesDerrota.size() >= 4) {
                framesDerrota.remove(0);
            }
        }

    }


    // =========================================================
    // TECLADO
    // =========================================================

    @Override
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {

            resetarJogo();

            janela.setContentPane(menu);

            menu.timerGeral.start();
            menu.requestFocusInWindow();

            janela.revalidate();
            janela.repaint();
        }


        if (e.getKeyCode() == KeyEvent.VK_M) {
            ativarMouse = !ativarMouse;
        }


        if (e.getKeyCode() == KeyEvent.VK_SPACE &&
                nave.vida > 0) {

            atirar = true;
        }


        if (e.getKeyCode() == KeyEvent.VK_A) {
            esquerda = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            direita = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            cima = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            baixo = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_1){
            desenharMalha = !desenharMalha;
        }
        if(e.getKeyCode() == KeyEvent.VK_2){
            desenharMalhaNoChefe = !desenharMalhaNoChefe;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            atirar = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            esquerda = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            direita = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_W) {
            cima = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            baixo = false;
        }
    }


    // =========================================================
    // ATUALIZAÇÃO DO JOGO
    // =========================================================

    @Override
    public void actionPerformed(ActionEvent e) {

        somExplosao.setVolume(2.0f);


        // =====================================================
        // EXPLOSÃO DA ASA ESQUERDA
        // =====================================================

        if (chefe.vidaAsaEsquerda <= 0 && podeExplodirAsaEsquerdaChefe) {

            if (podeTocarSomExplosaoAsaEsquerda) {
                somExplosao.tocarSom();
                podeTocarSomExplosaoAsaEsquerda = false;
            }

            if (indiceSpritesExplosao < spritesExplosaoChefe.size() - 1) {

                indiceSpritesExplosao++;
            } else {
                indiceSpritesExplosao = 0;
                podeExplodirAsaEsquerdaChefe = false;
            }
        }


        // =====================================================
        // EXPLOSÃO DA ASA DIREITA
        // =====================================================

        if (podeExplodirAsaDireitaChefe && chefe.vidaaAsaDireita <= 0) {

            if (podeTocarSomExplosaoAsaDireita) {

                somExplosao.tocarSom();
                podeTocarSomExplosaoAsaDireita = false;
            }

            if (indiceSpritesExplosao < spritesExplosaoChefe.size() - 1) {

                indiceSpritesExplosao++;

            } else {

                indiceSpritesExplosao = 0;
                podeExplodirAsaDireitaChefe = false;
            }
        }


        // =====================================================
        // EXPLOSÃO DO CORPO
        // =====================================================

        if (podeExplodirCorpoChefe && chefe.vida < 50) {

            if (podeTocarSomExplosaoCorpo) {

                somExplosao.tocarSom();
                podeTocarSomExplosaoCorpo = false;
            }

            if (indiceSpritesExplosao < spritesExplosaoChefe.size() - 1) {
                indiceSpritesExplosao++;

            } else {

                indiceSpritesExplosao = 0;
                podeExplodirCorpoChefe = false;
            }
        }


        // =====================================================
        // CURTO-CIRCUITO ASA ESQUERDA / CORPO
        // =====================================================

        if (chefe.vidaAsaEsquerda <= 0 || chefe.vida < 50) {

            if (indiceCurto < curtoCircuito.size() - 1) {
                indiceCurto++;

            } else {
                indiceCurto = 0;
            }


            if (indiceCurto2 < acertoSprites.size() - 1) {
                indiceCurto2++;

            } else {
                indiceCurto2 = 0;
            }
        }


        // =====================================================
        // CURTO-CIRCUITO ASA DIREITA / CORPO
        // =====================================================

        if (chefe.vidaaAsaDireita <= 0 || chefe.vida < 50) {

            if (indiceCurto4 < acertoSprites.size() - 1) {
                indiceCurto4++;

            } else {

                indiceCurto4 = 2;
            }


            if (indiceCurto3 < curtoCircuito2.size() - 1) {
                indiceCurto3++;

            } else {
                indiceCurto3 = 3;
            }


            if (indiceFumacaNave2 < fumacaNaveSprites.size() - 1) {
                indiceFumacaNave2++;

            } else {
                indiceFumacaNave2 = 0;
            }
        }


        // =====================================================
        // MÚSICA
        // =====================================================

        if (somFundo.terminou() && nave.vida > 0) {
            somFundo.setVolume(2.0f);
            somFundo.tocarLoop();

        } else if (nave.vida <= 0) {
            somFundo.parar();
        }


        // =====================================================
        // EXPLOSÃO DA NAVE
        // =====================================================

        if (nave.vida <= 0) {

            if (indiceExplosaoNave < explosaoNave.size() - 1) {
                indiceExplosaoNave++;

                if (indiceExplosaoNave == 1) {
                    somExplosaoNave.setVolume(2.0f);
                    somExplosaoNave.tocarSom();
                }

                nave.naveImg = carregarSprite( "/nave/explosao/(1).png" );

            } else {
                indiceExplosaoNave = explosaoNave.size() - 1;
            }

        } else {

            if (nave.vida > nave.vidaMaxima / 2) {

                indiceExplosaoNave = 0;

                nave.naveImg = carregarSprite( "/nave/nave.png");

            } else {

                nave.naveImg = carregarSprite( "/nave/nave_Danificada.png");
            }
        }


        // =====================================================
        // INIMIGOS
        // =====================================================

        for (Inimigo inimigo : inimigos) {

            inimigo.y += inimigo.vel;


            if (inimigo.getBounds().intersects(nave.getbounds()) && inimigo.podeColidir && nave.vida > 0) {

                inimigo.vida = 0;
                nave.vida -= 2;
            }


            if (inimigo.podeExcluir) {
                inimigosParaRemover.add(inimigo);
                pontos++;
            }


            for (Tiro tiro : tirosNave) {

                if (tiro.getBounds().intersects(inimigo.getBounds()) && inimigo.podeColidir) {

                    if (tiro.podeCausarDano) {
                        inimigo.vida--;
                        tiro.podeCausarDano = false;
                    }

                    if (tiro.podeExcluir) {
                        tirosParaRemover.add(tiro);
                    }

                    tiro.desenharAcerto = true;
                    tiro.vel = -inimigo.vel;
                }
            }
        }


        // =====================================================
        // CHEFE
        // =====================================================

        if (pontos >= 5 && chefe.vida > 0) {
        // =====================================================
        // Movimentação
        // =====================================================

            if(chefe.chefeX > nave.x){
                chefe.chefeX -= chefe.vel;
            }
            if(chefe.chefeX < nave.x){
                chefe.chefeX += chefe.vel;
            }

            if(pontos == 5){
                especialChefeSom.tocarSom();
            }
            if(chefe.vidaAsaEsquerda <= 0 && chefe.vidaaAsaDireita <= 0 || chefe.vida <= 0){
                timerAtivarEspecialChefe.stop();
            }

            timerTiroChefe.start();
            timerAtivarEspecialChefe.start();
            if(chefe.especial && chefe.especialPodeCausarDano){
                if(chefe.getBoundsEspecialDireia().intersects(nave.getbounds())){
                    nave.vida -= 5;
                    chefe.especialPodeCausarDano = false;
                }

                if(chefe.getBoundsEspecialEsquerda().intersects(nave.getbounds())){
                    nave.vida -= 5;
                    chefe.especialPodeCausarDano = false;
                }
            }

            for(Tiro tiro : tiroschefe){
                tiro.y += 20;
                if(tiro.y > 1100){
                    tirosParaRemover.add(tiro);
                }

                if(tiro.getBounds().intersects(nave.getbounds()) && nave.vida > 0){
                    atingido.tocarSom();
                    nave.vida -= 1;
                    tiro.desenharAcerto = true;
                    tiro.podeCausarDano = false;
                }

                if(tiro.podeExcluir){
                    tirosParaRemover.add(tiro);
                }
            }

            if (pontos == 5) {

                somFundo.parar();

                somFundo = new Som("/sons/fundo/fundochefe1.wav");

                somFundo.tocarLoop();

                pontos++;
            }


            for (Tiro tiro : tirosNave) {

                timerSpawnInimigo.stop();


                // ---------------------------------------------
                // CORPO
                // ---------------------------------------------

                if (tiro.getBounds().intersects(chefe.getBounds())) {

                    chefe.vida -= 3;
                    chefe.areaColisãoCorpo -= 5;

                    if (chefe.areaColisãoCorpo <= 20) {
                        chefe.areaColisãoCorpo = 110;
                    }

                    tiro.desenharAcerto = true;
                    tiro.podeCausarDano = false;
                    tiro.distanciaChefeX = tiro.x - chefe.chefeX;
                    tiro.distanciaChefeY = tiro.y - chefe.chefeY;
                    buracosDBala.add(tiro);

                    tiro.vel = 0;

                    if (tiro.podeExcluir) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA / CORPO
                // ---------------------------------------------

                if (tiro.getBounds().intersects(chefe.getBoundsAsaDireitaCorpo()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerdaCorpo()) && tiro.podeCausarDano) {

                    if (chefe.areaColisaoAsasCorpo <= 6) {
                        chefe.areaColisaoAsasCorpo = 50;
                    }

                    chefe.areaColisaoAsasCorpo -= 5;
                    chefe.vida -= 3;
                    chefe.vidaaAsaDireita--;

                    tiro.desenharAcerto = true;
                    tiro.podeCausarDano = false;
                    tiro.vel = 0;
                    tiro.distanciaChefeX = tiro.x - chefe.chefeX;
                    tiro.distanciaChefeY = tiro.y - chefe.chefeY;

                    buracosDBala.add(tiro);

                    if (tiro.podeExcluir) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA DIREITA
                // ---------------------------------------------

                if (tiro.getBounds().intersects(chefe.getBoundsAsaDireira2()) || tiro.getBounds().intersects( chefe.getBoundsAsaDireita3() ) || tiro.getBounds().intersects( chefe.getBoundsAsaDireita4()) || tiro.getBounds().intersects(chefe.getBoundsAsaDireita5()) && tiro.podeCausarDano
                ) {

                    chefe.areaColisaoAsaDireita -= 5;

                    if (chefe.areaColisaoAsaDireita <= 6) {
                        chefe.areaColisaoAsaDireita = 50;
                    }

                    chefe.vida -= 1;
                    chefe.vidaaAsaDireita--;

                    tiro.desenharAcerto = true;
                    tiro.podeCausarDano = false;
                    tiro.distanciaChefeX = tiro.x - chefe.chefeX;
                    tiro.distanciaChefeY = tiro.y - chefe.chefeY;
                    tiro.vel = 0;

                    buracosDBala.add(tiro);

                    if (tiro.podeExcluir) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA ESQUERDA
                // ---------------------------------------------

                if (
                    tiro.getBounds().intersects( chefe.getBoundsAsaEsquerda2()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda3()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda4()) ||tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda5()) && tiro.podeCausarDano) {

                    chefe.areaColisaoAsaEsquerda -= 5;

                    if (chefe.areaColisaoAsaEsquerda <= 6) {
                        chefe.areaColisaoAsaEsquerda = 50;
                    }

                    chefe.vidaAsaEsquerda--;

                    tiro.desenharAcerto = true;
                    tiro.podeCausarDano = false;
                    tiro.distanciaChefeX = tiro.x - chefe.chefeX;
                    tiro.distanciaChefeY = tiro.y - chefe.chefeY;
                    tiro.vel = 0;

                    buracosDBala.add(tiro);

                    if (tiro.podeExcluir) {
                        tirosParaRemover.add(tiro);
                    }
                }
            }
        }

        if(chefe.vida <= 0){
            timerAtivarEspecialChefe.stop();
            if(terminouExplosaoChefe){
                timerSpawnInimigo.start();
            }
            
        }
        // =====================================================
        // LIMPAR BURACOS
        // =====================================================

        if (buracosDBala.size() >= 40) {
            buracosDBala.remove(0);
        }

        // =====================================================
        // REMOVER INIMIGOS
        // =====================================================

        inimigos.removeAll(inimigosParaRemover);
        inimigosParaRemover.clear();

        // =====================================================
        // MOVER TIROS
        // =====================================================

        for (Tiro tiro : tirosNave) {
            tiro.y -= tiro.vel;
            if (tiro.podeExcluir) {
                tirosParaRemover.add(tiro);
            }
        }

        tirosNave.removeAll(tirosParaRemover);
        tirosParaRemover.clear();


        // =====================================================
        // MOVIMENTO DA NAVE
        // =====================================================

        if (cima && nave.y > 0) {
            nave.y -= nave.vel;
        }

        if (baixo && nave.y < 900) {
            nave.y += nave.vel;
        }

        if (esquerda && nave.x > 0) {
            nave.x -= nave.vel;
        }

        if (direita && nave.x < 1800) {
            nave.x += nave.vel;
        }


        repaint();
    }


    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    public ArrayList<Image> carregarsprites(String caminho,int quantidade   ) {

        ArrayList<Image> lista = new ArrayList<>();
        for (int i = 1; i <= quantidade; i++) {
            lista.add(new ImageIcon(getClass().getResource(caminho + "(" + i + ").png")).getImage());
        }
        return lista;
    }

    
    public void resetarJogo() {

        // =====================================================
        // PARAR TIMERS
        // =====================================================

        timerGeral.stop();
        timerTiro.stop();
        timerAnimacao.stop();
        timerSpawnInimigo.stop();
        timerCarregarFundo.stop();
        timerTiroChefe.stop();
        timerAtivarEspecialChefe.stop();

        // =====================================================
        // PARAR MÚSICA
        // =====================================================

        somFundo.parar();

        // =====================================================
        // RESETAR NAVE
        // =====================================================

        nave.vida = nave.vidaMaxima;
        nave.naveImg = carregarSprite("/nave/nave.png");

        fumacaNave = false;

        indiceExplosaoNave = 0;
        indiceFumacaNave = 0;
        indiceFumacaNave2 = 0;

        // =====================================================
        // RESETAR CHEFE
        // =====================================================

        chefe.vida = 50;
        chefe.vidaAsaEsquerda = 5;
        chefe.vidaaAsaDireita = 10;
        chefe.vel = 5;

        chefe.especial = false;
        chefe.especialPodeCausarDano = false;

        chefe.indiceEspecial = 0;
        chefe.especialCrescer = 0;

        // =====================================================
        // RESETAR PONTUAÇÃO
        // =====================================================

        pontos = 5;

        // =====================================================
        // LIMPAR OBJETOS
        // =====================================================

        inimigos.clear();
        inimigosParaRemover.clear();

        tirosNave.clear();
        tirosParaRemover.clear();

        tiroschefe.clear();

        buracosDBala.clear();


        // =====================================================
        // RESETAR EXPLOSÕES DO CHEFE
        // =====================================================

        podeTocarSomExplosaoCorpo = true;
        podeTocarSomExplosaoAsaEsquerda = true;
        podeTocarSomExplosaoAsaDireita = true;

        podeExplodirCorpoChefe = true;
        podeExplodirAsaEsquerdaChefe = true;
        podeExplodirAsaDireitaChefe = true;

        terminouExplosaoChefe = false;
        // =====================================================
        // RESETAR ANIMAÇÕES DO CHEFE
        // =====================================================

        indiceExplosaoChefe = 1;
        indiceSpritesExplosao = 0;

        indiceCurto = 0;
        indiceCurto2 = 0;
        indiceCurto3 = 0;
        indiceCurto4 = 0;

        chefeDerrotadoSprites.clear();

        // =====================================================
        // RESETAR ANIMAÇÕES
        // =====================================================

        indiceAnimacaoTiro = 0;
        indiceTurbina = 0;

        framesDerrota.clear();
        vidaNaveFrames.clear();

        numeroFrameAtualVidaNave = 1;
        // =====================================================
        // RESETAR CONTROLES
        // =====================================================

        atirar = false;

        cima = false;
        baixo = false;
        esquerda = false;
        direita = false;

        // =====================================================
        // RESETAR FUNDO
        // =====================================================

        fundo.clear();

        indiceFundo = 1;

        for (int i = 1; i <= 10; i++) {
            fundo.add(
                new ImageIcon(
                    getClass().getResource("/jogo/fundo/(" + i + ").jpg")
                ).getImage()
            );
        }
        // =====================================================
        // RESETAR MÚSICA
        // =====================================================

        somFundo = new Som("sons/fundo/fundo.wav");

        somFundo.setVolume(2.0f);
    }




    public Image carregarSprite(String caminho) {
        return new ImageIcon(getClass().getResource(caminho)).getImage();
    }


    // =========================================================
    // MOUSE
    // =========================================================

    @Override
    public void mouseDragged(MouseEvent e) {

        if (ativarMouse) {
            nave.x = e.getX() - 55;
            nave.y = e.getY() - 65;
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {

        if (ativarMouse) {
            nave.x = e.getX() - 55;
            nave.y = e.getY() - 65;
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }


    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1 &&nave.vida > 0) {
            atirar = true;
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1) {
            atirar = false;
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
    }


    @Override
    public void mouseExited(MouseEvent e) {
    }
}

