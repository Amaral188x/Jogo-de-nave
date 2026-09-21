import java.awt.*;
//Desenha linhas na horizontal e vertical com cooredenadas para facilitar o posicionamento de elementos
public class GridDebug {

    public static void desenhar(Graphics g, int largura, int altura, int espacamento) {

        g.setColor(new Color(255, 255, 255, 80)); // branco transparente

        // Linhas verticais
        for (int x = 0; x <= largura; x += espacamento) {
            g.drawLine(x, 0, x, altura);
            g.drawString(String.valueOf(x), x + 5, 15);
        }

        // Linhas horizontais
        for (int y = 0; y <= altura; y += espacamento) {
            g.drawLine(0, y, largura, y);
            g.drawString(String.valueOf(y), 5, y - 5);
        }
    }
}