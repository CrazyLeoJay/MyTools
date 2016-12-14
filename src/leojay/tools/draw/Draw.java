package leojay.tools.draw;

import leojay.tools.QWcode.QRCodeUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * <p>
 * time: 16/12/12__21:49
 *
 * @author leojay
 */
public class Draw {
    public static void main(String[] args) throws Exception {
        String url = "/Users/leojay/Desktop/123456.jpg";
        File file = new File(url);
        BufferedImage ceshi = QRCodeUtil.createImage("dfsdfsghefrgwefgsdgresdfagsesdfsgdfgsrgdf", null, false);
        BufferedImage image = new BufferedImage(ceshi.getWidth(), ceshi.getHeight()+ 100, BufferedImage.TYPE_INT_RGB);
        Graphics grap = image.getGraphics();
        grap.drawImage(ceshi, 0, 0, null);
//        grap.setColor(Color.red);
        String text = "测试文本！！！";
        
        Font f = new Font("宋体",Font.BOLD ,20); //把字体对象放到这new
        grap.setFont(f);
        grap.drawString(text, 10, ceshi.getHeight() + 50);
        boolean jpg = ImageIO.write(image, "jpg", file);
    }
}
