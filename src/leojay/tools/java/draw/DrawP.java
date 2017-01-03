package leojay.tools.java.draw;

import java.awt.*;
import java.awt.image.*;
import java.util.Hashtable;

/**
 * <p>
 * time: 16/12/13__09:12
 *
 * @author leojay
 */
public class DrawP extends BufferedImage{

    public DrawP(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public DrawP(int width, int height, int imageType, IndexColorModel cm) {
        super(width, height, imageType, cm);
    }

    public DrawP(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        super(cm, raster, isRasterPremultiplied, properties);
    }

    @Override
    public Graphics2D createGraphics() {
        return super.createGraphics();
    }

    @Override
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    @Override
    public ImageProducer getSource() {
        return super.getSource();
    }

}
