import javax.swing.JFrame;

public class TanksGUI extends JFrame implements Runnable{
    public TanksGUI() {
    }

    public void run()
    {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
