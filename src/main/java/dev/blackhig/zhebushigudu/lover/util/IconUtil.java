package dev.blackhig.zhebushigudu.lover.util;

import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import java.io.InputStream;

public class IconUtil
{
    public static final IconUtil INSTANCE;
    
    public ByteBuffer readImageToBuffer(final InputStream inputStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(inputStream);
        final int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        Arrays.stream(aint).map(i -> i << 8 | (i >> 24 & 0xFF)).forEach(bytebuffer::putInt);
        bytebuffer.flip();
        return bytebuffer;
    }
    
    static {
        INSTANCE = new IconUtil();
    }
}
