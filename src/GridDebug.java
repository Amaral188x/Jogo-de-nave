import java.awt.*;
//Desenha linhas na horizontal e vertical com cooredenadas para facilitar o posicionamento de elementos
public class GridDebug {
        // ==========================================
        // Plano cartesiano do fundo
        // ==========================================

    public void desenhar(Graphics g, int largura, int altura, int espacamento) {

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

         // ==========================================
        // Plano cartesiano do chefe
        // ==========================================
    public void desenharNoChefe(Graphics g, Chefe chefe, int espacamento) {

        // Calcular o centro exato do chefe na tela
        int centroX = chefe.chefeX + (chefe.tamX / 2) - 150; 
        int centroY = chefe.chefeY + (chefe.tamY / 2);

        // Definira área que a malha vai cobrir ao redor do centro
        int metadeLargura = chefe.tamX / 2;
        int metadeAltura = chefe.tamY / 2;

        int inicioX = centroX - metadeLargura;
        int fimX = centroX + metadeLargura;
        int inicioY = centroY - metadeAltura;
        int fimY = centroY + metadeAltura;

        
        g.setColor(new Color(0, 255, 255));

        // ==========================================
        // LINHAS VERTICAIS (Eixo X)
        // ==========================================

        for (int x = centroX; x <= fimX; x += espacamento) {
            g.drawLine(x, inicioY, x, fimY);
            int valorRelativoX = x - centroX;
            g.drawString(String.valueOf(valorRelativoX), x + 2, centroY - 2);
        }

        for (int x = centroX - espacamento; x >= inicioX; x -= espacamento) {
            g.drawLine(x, inicioY, x, fimY);
            int valorRelativoX = x - centroX; // Vai gerar valores negativos (-50, -100...)
            g.drawString(String.valueOf(valorRelativoX), x + 2, centroY - 2);
        }

        // ==========================================
        // LINHAS HORIZONTAIS (Eixo Y)
        // ==========================================
        for (int y = centroY; y <= fimY; y += espacamento) {
            g.drawLine(inicioX, y, fimX, y);
            int valorRelativoY = y - centroY;
            if (valorRelativoY != 0) { // Evita sobrepor o zero
                g.drawString(String.valueOf(valorRelativoY), centroX + 5, y - 2);
            }
        }

        for (int y = centroY - espacamento; y >= inicioY; y -= espacamento) {
            g.drawLine(inicioX, y, fimX, y);
            int valorRelativoY = y - centroY; // Vai gerar valores negativos para cima
            g.drawString(String.valueOf(valorRelativoY), centroX + 5, y - 2);
        }

        // Destaca o ponto (0,0) central com uma cruz vermelha
        g.setColor(Color.RED);
        g.drawLine(centroX - 10, centroY, centroX + 10, centroY);
        g.drawLine(centroX, centroY - 10, centroX, centroY + 10);
        g.drawString("(0,0)", centroX + 12, centroY - 5);
    }

}