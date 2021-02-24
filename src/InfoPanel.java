import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InfoPanel extends JPanel implements ActionListener {
    private final int WIDTH = 100;
    private final int HEIGHT = 500;
    private Image panel;
    private int player_1_score;
    private int player_2_score;

    private Gameplay gameplay;

    private JButton display1;
    private JButton display2;

    public InfoPanel(Gameplay g) {
        gameplay = g;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        try {
            panel = ImageIO.read(getClass().getResource("/panel.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        player_1_score = 0;
        player_1_score = 0;

        Font f = new Font("Calibri",Font.BOLD, 30);
        this.setLayout(null);
        display1 = new JButton("0");
        display1.setBackground(Color.BLACK);
        display1.setFont(f);
        display1.setEnabled(false);
        display1.setBorderPainted(false);
        display1.setContentAreaFilled(false);
        display1.setBounds(20,120,60,60);
        display2 = new JButton("0");
        display2.setBackground(Color.BLACK);
        display2.setFont(f);
        display2.setEnabled(false);
        display2.setBorderPainted(false);
        display2.setContentAreaFilled(false);
        display2.setBounds(20,210,60,60);
        this.add(display1);
        this.add(display2);
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        Toolkit.getDefaultToolkit().sync();
        display1.setText(Integer.toString(player_1_score));
        display2.setText(Integer.toString(player_2_score));

        if (player_1_score == 5)
            gameplay.endGame(1);
        else if (player_2_score == 5)
            gameplay.endGame(2);



    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(panel, 0, 0, this);
    }

    public void addPoint() {
        player_1_score += 1;
    }
    public void addEnemyPoint() {
        player_2_score += 1;
    }
}
