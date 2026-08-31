import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) throws Exception {
        JFrame janela = new JFrame();

        janela.setSize(1920,1080);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setResizable(false);

        Menu menu = new Menu(janela);
        janela.setContentPane(menu);
        menu.requestFocusInWindow();
        janela.revalidate();
        janela.setVisible(true);
    }
}
