

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Som {
    private Clip clip;
    private String caminho;

    public Som(String caminho){
        this.caminho = caminho;
        
    }

    public void tocarSom(){
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(caminho)); //prepara o audio com dados 
            clip = AudioSystem.getClip();//cria uma obeto Clip e armazena o som inteiro na memória para poder usar dps
            clip.open(audioStream);
            clip.start();
        }catch( Exception e){System.out.println("Erro na classe som " + e); }
    }

    public void tocarLoop(){
        try{
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(caminho));
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.loop(clip.LOOP_CONTINUOUSLY);
        clip.start();
        }catch(Exception e){
            System.out.println("\n Erro na classe som ao tocar loop" + e);
        }
        
    }

    public void parar(){
        if(clip != null && clip.isRunning()){
            clip.stop();
        }
    }

    public boolean terminou(){
        return clip != null && !clip.isRunning();

    }
}

