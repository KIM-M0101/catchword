package game;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class Button extends BasicButtonUI {
    @Override
    public void paint(Graphics g, JComponent c) {
    	super.paint(g, c);
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();

        // 버튼의 모양과 배경 색상 설정
       // g2.setColor(new Color(8, 136, 248));
        
        //그라데이션 인데 해보고 괜찮은거찾기
        //GradientPaint gradient = new GradientPaint(0, 0, Color.blue, 0, b.getHeight(), Color.LIGHT_GRAY);
        //g2.setPaint(gradient);
        //g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 40, 40);
        //버튼에 이미지 입히기
        Image brick=new ImageIcon("imgs/brick.png").getImage();
        g2.drawImage(brick, 0, 0, b.getWidth(), b.getHeight(), b);
        // 텍스트 색상과 스타일 설정
        g2.setFont(new Font("돋움", Font.BOLD, 27));
        g2.setColor(Color.black);
        
        FontMetrics fm = g2.getFontMetrics();
        int x = (b.getWidth() - fm.stringWidth(b.getText())) / 2;
        int y = (b.getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(b.getText(), x, y);

        g2.dispose();
    }
}