
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
    // objetos principais
    // =========================================================

    private Nave nave;
    private Chefe chefe;

    private JFrame janela;
    private Menu menu;

    // =========================================================
    // timers
    // =========================================================

    private Timer timerGeral;
    private Timer timerTiro;
    private Timer timerAnimacao;
    private Timer timerSpawnInimigo;
    private Thread threadCarregarRecursos;
    private Runnable runnableCarregarRecursos;
    private boolean carregandoRecursos = false;
    private Timer timerTiroChefe;
     
    //Inicia (ou reinicia) a thread que carrega as imagens em segundo plano.
    //* Uma Thread não pode ser reiniciada, então criamos uma nova a cada partida.
     
    public void iniciarCarregamentoRecursos() {
        carregandoRecursos = true;
        threadCarregarRecursos = new Thread(runnableCarregarRecursos);
        threadCarregarRecursos.start();
    }

    // =========================================================
    // INICIAR PARTIDA (encapsula timers, recursos e música)
    // =========================================================

    public void iniciarPartida() {
        resetarJogo();

        // Iniciar os timers da partida
        timerAnimacao.start();
        timerSpawnInimigo.start();
        timerTiro.start();
        timerGeral.start();

        // Carrega as imagens em uma thread separada (fora da thread que desenha na tela)
        iniciarCarregamentoRecursos();

        timerTiroChefe.start();
        timerAtivarEspecialChefe.start();

        // Iniciar música
        somFundo.tocarLoop();
    }

    private Timer timerAtivarEspecialChefe;
    // =========================================================
    // sons
    // =========================================================

    private Som perdeu = new Som("sons/fundo/perdeu.wav");
    private Som somTiro = new Som("/sons/nave/tiro.wav");
    private Som somExplosaoNave = new Som("/nave/explosao.wav");
    private Som somFundo = new Som("sons/fundo/fundo.wav");
    private Som somExplosao = new Som("/sons/inimigo/explosaoDanificarChefe.wav");
    private Som atingido = new Som("/sons/nave/atingido.wav");
    private Som especialChefeSom = new Som("/sons/inimigo/especialChefeSom.wav");
    private Som naveLevouDanoEspecialChefe = new Som("/sons/nave/levouDanoDoChefe.wav");
    private Som chefeDerrotadoExplosaoSom = new Som("/sons/inimigo/chefeDerrotadoExplosao.wav");

    // =========================================================
    // sprites
    // =========================================================

    private ArrayList<Image> spritesEspecialChefe = carregarsprites("/chefes/chefe1/Especial/", 11);

    private ArrayList<Image> spritesExplosaoChefe = carregarsprites("/chefes/chefe1/DanificarArmas/", 10);

    private ArrayList<Image> curtoCircuito2 =carregarsprites("/chefes/chefe1/danificado2/", 40);

    private ArrayList<Image> curtoCircuito =carregarsprites("/chefes/chefe1/Danificado/", 39);

    private ArrayList<Image> framesDerrota =new ArrayList<>();

    private ArrayList<Image> explosaoNave =carregarsprites("/nave/explosao/", 63);

    private ArrayList<Image> vidaNaveFrames =new ArrayList<>();

    private ArrayList<Image> fundo =new ArrayList<>();

    private ArrayList<Image> animacaoTiroNave =carregarsprites("/nave/animacaoTiro/", 4);

    private ArrayList<Image> animacaoTurbina =carregarsprites("/nave/turbina/", 6);

    private ArrayList<Image> acertoSprites =carregarsprites("/inimigo/acerto/", 15);

    private ArrayList<Image> explosaoInimigo =carregarsprites("/inimigo/explosao/", 10);

    private ArrayList<Image> fumaca =carregarsprites("/inimigo/fumaca/", 4);

    private ArrayList<Image> fumacaNaveSprites =carregarsprites("/nave/fumaca/", 45);

    private ArrayList<Image> chefeDerrotadoSprites = new ArrayList<>();

    // =========================================================
    // objetos
    // =========================================================

    private ArrayList<Tiro> tirosNave = new ArrayList<>();
    private ArrayList<Tiro> buracosDBala = new ArrayList<>();
    private ArrayList<Tiro> tirosParaRemover = new ArrayList<>();
    private ArrayList<Tiro> tiroschefe = new ArrayList<>();

    private ArrayList<Inimigo> inimigos = new ArrayList<>();
    private ArrayList<Inimigo> inimigosParaRemover = new ArrayList<>();

    private GridDebug malha = new GridDebug();

    // =========================================================
    // imagens individuais
    // =========================================================

    private Image tiroNaveImagem =carregarSprite("/nave/tiro.png");
    private Image buracoImg = new ImageIcon(getClass().getResource("/nave/marca_de_tiro.png")).getImage();
    private Image inimigoImg =carregarSprite("/inimigo/inimigo.png");

    // =========================================================
    // indices da animacoes
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
    // configuracoes de animacoes
    // =========================================================

    private final int totalFrames = 251;
    private final int totalFramesVida = 150;

    // =========================================================
    // pontuacao
    // =========================================================

    private int pontos = 0;

    // =========================================================
    // controle das exploseoes do chefe
    // =========================================================

    private boolean podeTocarSomExplosaoCorpo = true;
    private boolean podeTocarSomExplosaoAsaEsquerda = true;
    private boolean podeTocarSomExplosaoAsaDireita = true;
    private boolean podeTocarSomExplosaoChefe = true;

    private boolean podeExplodirCorpoChefe = true;
    private boolean podeExplodirAsaEsquerdaChefe = true;
    private boolean podeExplodirAsaDireitaChefe = true;

    private boolean desenharMalha = false, desenharMalhaNoChefe = false;

    // =========================================================
    // controles
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
    //construtor
    // =========================================================

    public Jogo(JFrame janela, Menu menu) {

        try {
            this.janela = janela;
            this.menu = menu;

            setFocusable(true);

            addKeyListener(this);
            addMouseListener(this);
            addMouseMotionListener(this);

    
            perdeu.setVolume(2.0f);

            // =====================================================
            // nave e chefe
            // =====================================================

            nave = new Nave(new ImageIcon(getClass().getResource("/nave/nave.png")).getImage(),janela);

            chefe = new Chefe("/chefes/chefe1/tiroChefe1.png", spritesEspecialChefe);

            // =====================================================
            // Música
            // =====================================================

            somFundo.setVolume(2.0f);
            somFundo.tocarLoop();

            // =====================================================
            // Carregar alguns frames antes de exibir para que n d null point
            // =====================================================

            for (int i = 1; i <= 10; i++) {
                fundo.add(new ImageIcon(getClass().getResource("/jogo/fundo/(" + i + ").jpg")).getImage());
            }

            

            for (int i = 1; i <= 10; i++) {
                vidaNaveFrames.add(new ImageIcon(getClass().getResource("/nave/vida_normal/(" + i + ").png")).getImage());
            }


            // =====================================================
            // Thread para carregar os frames de fundo e etc
            // =====================================================

            runnableCarregarRecursos = new Runnable() {

                @Override
                public void run() {

                    while (carregandoRecursos) {

                        try {

                            // -------------------------------------------------
                            // Carregar fundo
                            // -------------------------------------------------

                            if (indiceFundo <= totalFrames) {

                                if (fundo.size() < 20) {

                                    BufferedImage img = ImageIO.read(getClass().getResource("/jogo/fundo/(" + indiceFundo + ").jpg"));

                                    synchronized (fundo) {
                                        fundo.add(img);
                                    }

                                    indiceFundo++;

                                }

                            } else {

                                indiceFundo = 1;
                            }


                            // -------------------------------------------------
                            // carregar explosao do chefe
                            // -------------------------------------------------

                            if (chefe.getVida() < 30 && !terminouExplosaoChefe) {

                                if (chefeDerrotadoSprites.size() < 20) {

                                    if (indiceExplosaoChefe < 176) {

                                        BufferedImage img = ImageIO.read(getClass().getResource("/chefes/chefe1/explosao/(" + indiceExplosaoChefe + ").png"));

                                        synchronized (chefeDerrotadoSprites) {
                                            chefeDerrotadoSprites.add(img);
                                        }

                                        indiceExplosaoChefe++;

                                    } else {

                                        terminouExplosaoChefe = true;
                                        podeTocarSomExplosaoChefe = true;
                                        indiceExplosaoChefe = 1;

                                        synchronized (chefeDerrotadoSprites) {
                                            chefeDerrotadoSprites.clear();
                                        }
                                    }
                                }
                            }


                            // -------------------------------------------------
                            // CARREGAR DERROTA
                            // -------------------------------------------------

                            if (nave.getVida() <= 0 && indiceExplosaoNave >= explosaoNave.size() - 1) {

                                if (framesDerrota.size() < 20) {

                                    if (indiceAnimacaoDerrota < 57) {

                                        BufferedImage img = ImageIO.read(getClass().getResource("/jogo/derrota/(" + indiceAnimacaoDerrota + ").png" ));

                                        synchronized (framesDerrota) {
                                            framesDerrota.add(img);
                                        }

                                        indiceAnimacaoDerrota++;

                                        if (indiceAnimacaoDerrota == 12) {
                                            
                                            perdeu.tocarSom();
                                        }

                                    } else {
                                        indiceAnimacaoDerrota = 1;

                                        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                resetarJogo();

                                                janela.setContentPane(menu);

                                                menu.iniciarTimer();
                                                menu.requestFocusInWindow();

                                                janela.revalidate();
                                                janela.repaint();
                                            }
                                        });
                                    }
                                }
                            }


                            // -------------------------------------------------
                            // VIDA DA NAVE
                            // -------------------------------------------------

                            if (numeroFrameAtualVidaNave < totalFramesVida) {

                                if (vidaNaveFrames.size() < 20 && nave.getVida() > nave.getVidaMaxima() / 2) {

                                    BufferedImage img = ImageIO.read(getClass().getResource("/nave/vida_normal/(" + numeroFrameAtualVidaNave + ").png"));

                                    synchronized (vidaNaveFrames) {
                                        vidaNaveFrames.add(img);
                                    }

                                    numeroFrameAtualVidaNave++;

                                } else if (
                                    vidaNaveFrames.size() < 20 &&
                                    nave.getVida() <= nave.getVidaMaxima() / 2 &&
                                    nave.getVida() > 5) {

                                    nave.setNaveImg(carregarSprite("/nave/nave_Danificada.png"));

                                    fumacaNave = true;

                                    BufferedImage img = ImageIO.read(getClass().getResource("/nave/vida_metade/(" + numeroFrameAtualVidaNave + ").png"));

                                    synchronized (vidaNaveFrames) {
                                        vidaNaveFrames.add(img);
                                    }

                                    numeroFrameAtualVidaNave++;

                                } else if (
                                    vidaNaveFrames.size() < 20 &&
                                    nave.getVida() <= 5) {

                                    BufferedImage img = ImageIO.read(getClass().getResource("/nave/vida_baixa/(" + numeroFrameAtualVidaNave + ").png" ));

                                    synchronized (vidaNaveFrames) {
                                        vidaNaveFrames.add(img);
                                    }

                                    numeroFrameAtualVidaNave++;
                                }

                            } else {

                                numeroFrameAtualVidaNave = 1;
                            }


                            // Evita a thread consumir CPU sem parar
                            Thread.sleep(5);

                        } catch (Exception erro) {

                            System.err.println("Erro na classe Jogo ao carregar recursos (thread de streaming): " + erro);
                            erro.printStackTrace();
                        }
                    }
                }
            };


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

                        tirosNave.add(new Tiro(nave.getX() + 70,nave.getY(),tiroNaveImagem,acertoSprites));

                        somTiro.tocarSom();
                        somTiro.setVolume(0.5f);
                    }
                }
            });

            timerTiroChefe = new Timer(700,new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){

                    if(!chefe.isEspecial()){
                        if(chefe.getVidaAsaEsquerda() > 0){
                            tiroschefe.add(new Tiro(chefe.getChefeX() - 100, chefe.getChefeY() + 300, tiroNaveImagem, acertoSprites));
                        }

                        tiroschefe.add(new Tiro(chefe.getChefeX() + 84, chefe.getChefeY() + 300, tiroNaveImagem, acertoSprites));

                        if(chefe.getVidaaAsaDireita() > 0){
                            tiroschefe.add(new Tiro(chefe.getChefeX() + 270, chefe.getChefeY() + 300, tiroNaveImagem, acertoSprites));
                        }

                    }
                }
            });

            timerAtivarEspecialChefe = new Timer(6000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    chefe.setEspecial(true);
                }
            });



            // =====================================================
            // TIMER DOS INIMIGOS
            // =====================================================

            timerSpawnInimigo = new Timer(1000, new ActionListener() {

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
                    if(chefe.isEspecial()){
                        chefe.setEspecialCrescer(chefe.getEspecialCrescer() + 40);
                        if(chefe.getIndiceEspecial() < chefe.getEspecialSprites().size() - 1){
                            if(chefe.getIndiceEspecial() == 7 && chefe.getEspecialCrescer() < 1800){
                                if(especialChefeSom.terminou()){
                                    especialChefeSom.tocarSom();
                                }
                                return;
                            }else{
                                chefe.setIndiceEspecial(chefe.getIndiceEspecial() + 1);
                            }

                        }else{
                            chefe.setEspecial(false);
                            chefe.setIndiceEspecial(0);
                            chefe.setEspecialCrescer(0);
                            chefe.setEspecialPodeCausarDano(true);
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
                    if (fumacaNave ||chefe.getVidaAsaEsquerda() <= 0) {
                        if (indiceFumacaNave < fumacaNaveSprites.size() - 1) {
                           indiceFumacaNave++;
                        } else {
                            indiceFumacaNave = 0;
                        }
                    }
                }
            });

        } catch (Exception erro) {
            System.err.println("Erro no construtor da classe Jogo:");
            erro.printStackTrace();
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

            g.drawImage(fundo.get(1),0,0,null);

            if (fundo.size() >= 3) {
                fundo.remove(0);
            }
        }


        // =====================================================
        // VIDA DA NAVE
        // =====================================================

        if (vidaNaveFrames.size() >= 2) {

            g.drawImage(vidaNaveFrames.get(1),20,20,null);

            if (vidaNaveFrames.size() >= 3) {
                vidaNaveFrames.remove(0);
            }
        }


        // =====================================================
        // TIRO DA NAVE
        // =====================================================

        if (atirar) {
            g.drawImage(animacaoTiroNave.get(indiceAnimacaoTiro),nave.getX() + 60,nave.getY(),null);

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

        if (!animacaoTurbina.isEmpty() && nave.getVida() > 0) {
            g.drawImage(animacaoTurbina.get(indiceTurbina),nave.getX() + 42,nave.getY() + 110,null);
        }


        // =====================================================
        // EXPLOSÃO DA NAVE
        // =====================================================

        if (nave.getVida() <= 0) {
            g.drawImage(explosaoNave.get(indiceExplosaoNave),nave.getX(),nave.getY(),null);
        }






        // =====================================================
        // CHEFE
        // =====================================================

        if (pontos >= 5 && chefe.getVida() > 0) {

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
            if (chefe.getVidaAsaEsquerda() <= 0) {

                g.drawImage(curtoCircuito.get(indiceCurto),chefe.getChefeX() - 180,chefe.getChefeY() + 270,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave),chefe.getChefeX() - 120,chefe.getChefeY() + 230,null);

                g.drawImage(acertoSprites.get(indiceCurto2),chefe.getChefeX() - 130,chefe.getChefeY() + 230,150,150,null);
            }


            // Asa direita destruída
            if (chefe.getVidaaAsaDireita() <= 0) {

                g.drawImage(curtoCircuito2.get(indiceCurto3),chefe.getChefeX() + 160,chefe.getChefeY() + 250,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave2),chefe.getChefeX() + 120,chefe.getChefeY() + 230,null);

                g.drawImage(acertoSprites.get(indiceCurto4),chefe.getChefeX() + 130,chefe.getChefeY() + 230,150,150,null);
            }


            // Corpo danificado
            if (chefe.getVida() < 50) {

                g.drawImage(curtoCircuito2.get(indiceCurto3),chefe.getChefeX() - 10,chefe.getChefeY() + 300,null);

                g.drawImage(fumacaNaveSprites.get(indiceFumacaNave2),chefe.getChefeX() + 10,chefe.getChefeY() + 270,null);

                g.drawImage(curtoCircuito.get(indiceCurto),chefe.getChefeX() - 30,chefe.getChefeY() + 240,null);
            }


            // Explosão da asa esquerda
            if (chefe.getVidaAsaEsquerda() <= 0 && podeExplodirAsaEsquerdaChefe) {

                g.drawImage(spritesExplosaoChefe.get(indiceSpritesExplosao),chefe.getChefeX() - 365,chefe.getChefeY() + 120,null);
            }


            // Explosão da asa direita
            if (chefe.getVidaaAsaDireita() <= 0 &&podeExplodirAsaDireitaChefe) {

                g.drawImage(spritesExplosaoChefe.get(indiceSpritesExplosao), chefe.getChefeX(),chefe.getChefeY() + 120, null);
            }


            // Explosão do corpo
            if (chefe.getVida() < 50 && podeExplodirCorpoChefe) {

                g.drawImage( spritesExplosaoChefe.get(indiceSpritesExplosao), chefe.getChefeX() - 200, chefe.getChefeY(), null);
            }

            for (Tiro buraco : buracosDBala) {

                g.drawImage(buracoImg, chefe.getChefeX() + buraco.getDistanciaChefeX(), chefe.getChefeY() + buraco.getDistanciaChefeY(), null);
            }


        }

        if(chefe.getVida() <= 0 && !terminouExplosaoChefe && !chefeDerrotadoSprites.isEmpty()){
            chefe.setVel(0);
            g.drawImage(chefeDerrotadoSprites.get(2), chefe.getChefeX(), chefe.getChefeY(), null);
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
        // NAVE
        // =====================================================

        nave.desenharNave(g);



        if (fumacaNave && nave.getVida() <= nave.getVidaMaxima() / 2 && nave.getVida() > 0) {

            g.drawImage( fumacaNaveSprites.get(indiceFumacaNave), nave.getX() + 10, nave.getY() + 35, null );
        }


        // =====================================================
        // TEXTOS
        // =====================================================

        g.setFont(new java.awt.Font( "Arial", java.awt.Font.BOLD, 24));

        g.setColor(java.awt.Color.RED);

        g.drawString("LIFE: ", 10, 30);

        g.drawString("BOSS " + chefe.getVida(),10,370);

        g.setColor(java.awt.Color.GREEN);

        g.drawString("SCORE: " + pontos, 10, 150);


        // =====================================================
        // TELA DE DERROTA
        // =====================================================

        if (!framesDerrota.isEmpty() && framesDerrota.size() >= 3 && nave.getVida() <= 0) {

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

            menu.iniciarTimer();
            menu.requestFocusInWindow();

            janela.revalidate();
            janela.repaint();
        }


        if (e.getKeyCode() == KeyEvent.VK_M) {
            ativarMouse = !ativarMouse;
        }


        if (e.getKeyCode() == KeyEvent.VK_SPACE &&
                nave.getVida() > 0) {

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

        if (chefe.getVidaAsaEsquerda() <= 0 && podeExplodirAsaEsquerdaChefe) {

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

        if (podeExplodirAsaDireitaChefe && chefe.getVidaaAsaDireita() <= 0) {

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

        if (podeExplodirCorpoChefe && chefe.getVida() < 50) {

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

        if (chefe.getVidaAsaEsquerda() <= 0 || chefe.getVida() < 50) {

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

        if (chefe.getVidaaAsaDireita() <= 0 || chefe.getVida() < 50) {

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

        if (somFundo.terminou() && nave.getVida() > 0) {
            somFundo.setVolume(2.0f);
            somFundo.tocarLoop();

        } else if (nave.getVida() <= 0) {
            somFundo.parar();
        }


        // =====================================================
        // EXPLOSÃO DA NAVE
        // =====================================================

        if (nave.getVida() <= 0) {

            if (indiceExplosaoNave < explosaoNave.size() - 1) {
                indiceExplosaoNave++;

                if (indiceExplosaoNave == 1) {
                    somExplosaoNave.setVolume(2.0f);
                    somExplosaoNave.tocarSom();
                }

                nave.setNaveImg(carregarSprite("/nave/explosao/(1).png"));

            } else {
                indiceExplosaoNave = explosaoNave.size() - 1;
            }

        } else {

            if (nave.getVida() > nave.getVidaMaxima() / 2) {

                indiceExplosaoNave = 0;

                nave.setNaveImg(carregarSprite("/nave/nave.png"));

            } else {

                nave.setNaveImg( carregarSprite( "/nave/nave_Danificada.png"));
            }
        }


        // =====================================================
        // INIMIGOS
        // =====================================================

        for (Inimigo inimigo : inimigos) {

            inimigo.atualizar();

            inimigo.setY(inimigo.getY() + inimigo.getVel());


            if (inimigo.getBounds().intersects(nave.getbounds()) && inimigo.isPodeColidir() && nave.getVida() > 0) {

                inimigo.setVida(0);
                nave.setVida(nave.getVida() - 2);
            }


            if (inimigo.isPodeExcluir()) {
                inimigosParaRemover.add(inimigo);
                pontos++;
            }


            for (Tiro tiro : tirosNave) {

                if (tiro.getBounds().intersects(inimigo.getBounds()) && inimigo.isPodeColidir()) {

                    if (tiro.isPodeCausarDano()) {
                        inimigo.setVida(inimigo.getVida() - 1);
                        tiro.setPodeCausarDano(false);
                    }

                    if (tiro.isPodeExcluir()) {
                        tirosParaRemover.add(tiro);
                    }

                    tiro.setDesenharAcerto(true);
                    tiro.setVel(-inimigo.getVel());
                }
            }
        }


        // =====================================================
        // CHEFE
        // =====================================================

        if (pontos >= 5 && chefe.getVida() > 0) {

        // =====================================================
        // Movimentação
        // =====================================================

            if(chefe.getChefeX() > nave.getX()){
                chefe.setChefeX(chefe.getChefeX() - chefe.getVel());
            }
            if(chefe.getChefeX() < nave.getX()){
                chefe.setChefeX(chefe.getChefeX() + chefe.getVel());
            }

            if(pontos == 5){
                //Inicializar esses efeitos especiais
                naveLevouDanoEspecialChefe.setVolume(2.0f);
                naveLevouDanoEspecialChefe.tocarSom();
                especialChefeSom.tocarSom();
            }

            if(chefe.getVidaAsaEsquerda() <= 0 && chefe.getVidaaAsaDireita() <= 0 || chefe.getVida() <= 0){
                timerAtivarEspecialChefe.stop();
            }

            timerTiroChefe.start();
            timerAtivarEspecialChefe.start();
            if(chefe.isEspecial() && chefe.isEspecialPodeCausarDano()){
                if(chefe.getBoundsEspecialDireia().intersects(nave.getbounds())){
                    nave.setVida(nave.getVida() - 5);
                    chefe.setEspecialPodeCausarDano(false);
                    if(naveLevouDanoEspecialChefe.terminou()){
                        naveLevouDanoEspecialChefe.tocarSom();
                    }
                    
                }

                if(chefe.getBoundsEspecialEsquerda().intersects(nave.getbounds())){
                    nave.setVida(nave.getVida() - 5);
                    chefe.setEspecialPodeCausarDano(false);
                    if(naveLevouDanoEspecialChefe.terminou()){
                        naveLevouDanoEspecialChefe.tocarSom();
                    }
                }
            }

            for(Tiro tiro : tiroschefe){
                tiro.atualizar();
                tiro.setY(tiro.getY() + 20);
                if(tiro.getY() > 1100){
                    tirosParaRemover.add(tiro);
                }

                if(tiro.getBounds().intersects(nave.getbounds()) && nave.getVida() > 0){
                    atingido.tocarSom();
                    nave.setVida(nave.getVida() - 1);
                    tiro.setDesenharAcerto(true);
                    tiro.setPodeCausarDano(false);
                }

                if(tiro.isPodeExcluir()){
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

                    chefe.setVida(chefe.getVida() - 3);
                    chefe.setAreaColisãoCorpo(chefe.getAreaColisãoCorpo() - 5);

                    if (chefe.getAreaColisãoCorpo() <= 20) {
                        chefe.setAreaColisãoCorpo(110);
                    }

                    tiro.setDesenharAcerto(true);
                    tiro.setPodeCausarDano(false);
                    tiro.setDistanciaChefeX(tiro.getX() - chefe.getChefeX());
                    tiro.setDistanciaChefeY(tiro.getY() - chefe.getChefeY());
                    buracosDBala.add(tiro);

                    tiro.setVel(0);

                    if (tiro.isPodeExcluir()) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA / CORPO
                // ---------------------------------------------

                if (tiro.getBounds().intersects(chefe.getBoundsAsaDireitaCorpo()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerdaCorpo()) && tiro.isPodeCausarDano()) {

                    if (chefe.getAreaColisaoAsasCorpo() <= 6) {
                        chefe.setAreaColisaoAsasCorpo(50);
                    }

                    chefe.setAreaColisaoAsasCorpo(chefe.getAreaColisaoAsasCorpo() - 5);
                    chefe.setVida(chefe.getVida() - 3);
                    chefe.setVidaaAsaDireita(chefe.getVidaaAsaDireita() - 1);

                    tiro.setDesenharAcerto(true);
                    tiro.setPodeCausarDano(false);
                    tiro.setVel(0);
                    tiro.setDistanciaChefeX(tiro.getX() - chefe.getChefeX());
                    tiro.setDistanciaChefeY(tiro.getY() - chefe.getChefeY());

                    buracosDBala.add(tiro);

                    if (tiro.isPodeExcluir()) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA DIREITA
                // ---------------------------------------------

                if (tiro.getBounds().intersects(chefe.getBoundsAsaDireira2()) || tiro.getBounds().intersects( chefe.getBoundsAsaDireita3() ) || tiro.getBounds().intersects( chefe.getBoundsAsaDireita4()) || tiro.getBounds().intersects(chefe.getBoundsAsaDireita5()) && tiro.isPodeCausarDano()
                ) {

                    chefe.setAreaColisaoAsaDireita(chefe.getAreaColisaoAsaDireita() - 5);

                    if (chefe.getAreaColisaoAsaDireita() <= 6) {
                        chefe.setAreaColisaoAsaDireita(50);
                    }

                    chefe.setVida(chefe.getVida() - 1);
                    chefe.setVidaaAsaDireita(chefe.getVidaaAsaDireita() - 1);

                    tiro.setDesenharAcerto(true);
                    tiro.setPodeCausarDano(false);
                    tiro.setDistanciaChefeX(tiro.getX() - chefe.getChefeX());
                    tiro.setDistanciaChefeY(tiro.getY() - chefe.getChefeY());
                    tiro.setVel(0);

                    buracosDBala.add(tiro);

                    if (tiro.isPodeExcluir()) {
                        tirosParaRemover.add(tiro);
                    }
                }


                // ---------------------------------------------
                // ASA ESQUERDA
                // ---------------------------------------------

                if (
                    tiro.getBounds().intersects( chefe.getBoundsAsaEsquerda2()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda3()) || tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda4()) ||tiro.getBounds().intersects(chefe.getBoundsAsaEsquerda5()) && tiro.isPodeCausarDano()) {

                    chefe.setAreaColisaoAsaEsquerda(chefe.getAreaColisaoAsaEsquerda() - 5);

                    if (chefe.getAreaColisaoAsaEsquerda() <= 6) {
                        chefe.setAreaColisaoAsaEsquerda(50);
                    }

                    chefe.setVidaAsaEsquerda(chefe.getVidaAsaEsquerda() - 1);

                    tiro.setDesenharAcerto(true);
                    tiro.setPodeCausarDano(false);
                    tiro.setDistanciaChefeX(tiro.getX() - chefe.getChefeX());
                    tiro.setDistanciaChefeY(tiro.getY() - chefe.getChefeY());
                    tiro.setVel(0);

                    buracosDBala.add(tiro);

                    if (tiro.isPodeExcluir()) {
                        tirosParaRemover.add(tiro);
                    }
                }
            }
        }



        if(chefe.getVida() <= 0 && podeTocarSomExplosaoChefe){
            chefeDerrotadoExplosaoSom.setVolume(2.0f);
            chefeDerrotadoExplosaoSom.tocarSom();
            podeTocarSomExplosaoChefe = false;
        }

        if(chefe.getVida() <= 0 || pontos < 5){
            timerAtivarEspecialChefe.stop();
            timerTiroChefe.stop();
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
            tiro.atualizar();
            tiro.setY(tiro.getY() - tiro.getVel());
            if (tiro.isPodeExcluir()) {
                tirosParaRemover.add(tiro);
            }
        }

        tirosNave.removeAll(tirosParaRemover);
        tirosParaRemover.clear();


        // =====================================================
        // MOVIMENTO DA NAVE
        // =====================================================

        if (cima && nave.getY() > 0) {
            nave.setY(nave.getY() - nave.getVel());
        }

        if (baixo && nave.getY() < 900) {
            nave.setY(nave.getY() + nave.getVel());
        }

        if (esquerda && nave.getX() > 0) {
            nave.setX(nave.getX() - nave.getVel());
        }

        if (direita && nave.getX() < 1800) {
            nave.setX(nave.getX() + nave.getVel());
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
        carregandoRecursos = false;
        timerTiroChefe.stop();
        timerAtivarEspecialChefe.stop();

        // =====================================================
        // PARAR MÚSICA
        // =====================================================

        somFundo.parar();

        // =====================================================
        // RESETAR NAVE
        // =====================================================

        nave.setVida(nave.getVidaMaxima());
        nave.setNaveImg( carregarSprite("/nave/nave.png"));

        fumacaNave = false;

        indiceAnimacaoDerrota = 1;
        indiceExplosaoNave = 0;
        indiceFumacaNave = 0;
        indiceFumacaNave2 = 0;

        // =====================================================
        // RESETAR CHEFE
        // =====================================================

        chefe.setVida(200);
        chefe.setVidaAsaEsquerda(50);
        chefe.setVidaaAsaDireita(50);
        chefe.setVel(10);

        chefe.setEspecial(false);
        chefe.setEspecialPodeCausarDano(false);

        chefe.setIndiceEspecial(0);
        chefe.setEspecialCrescer(0);

        // =====================================================
        // RESETAR PONTUAÇÃO
        // =====================================================

        pontos = 0;

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


        // =====================================================
        // RESETAR ANIMAÇÕES
        // =====================================================

        indiceAnimacaoTiro = 0;
        indiceTurbina = 0;



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


        indiceFundo = 1;


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
            nave.setX(e.getX() - 55);
            nave.setY(e.getY() - 65);
        }
    }


    @Override
    public void mouseMoved(MouseEvent e) {

        if (ativarMouse) {
            nave.setX(e.getX() - 55);
            nave.setY(e.getY() - 65);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }


    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1 &&nave.getVida() > 0) {
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

