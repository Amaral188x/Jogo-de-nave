

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;


public class Som {
    private Clip clip;
    private String caminho;
    private float volume = 1.0f;

    public Som(String caminho){
        this.caminho = caminho;
        
    }

    public void setVolume(float volume){
        this.volume = volume;
        if(clip != null && clip.isOpen() && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)){
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(0.001f, this.volume)) * 20.0);
            gain.setValue(dB);

        }
    }

    public void tocarSom(){
        try {
            java.net.URL url = getClass().getResource(caminho);
            if(url == null){
                System.err.println("Som nao encontrado: " + caminho);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url); //prepara o audio com dados
            clip = AudioSystem.getClip();//cria um objeto Clip e armazena o som inteiro na memória para poder usar dps

            clip.open(audioStream);
            setVolume(volume); // aplica o volume ao novo Clip
            clip.start();
        }catch( Exception e){
            System.err.println("Erro na classe Som ao tocar o som:");
            e.printStackTrace();
        }
    }

    public void tocarLoop(){
        try{
        java.net.URL url = getClass().getResource(caminho);
        if(url == null){
            System.err.println("Som nao encontrado: " + caminho);
            return;
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.loop(clip.LOOP_CONTINUOUSLY) ;
        clip.start();
        }catch(Exception e){
            System.err.println("Erro na classe Som ao tocar o loop:");
            e.printStackTrace();
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

