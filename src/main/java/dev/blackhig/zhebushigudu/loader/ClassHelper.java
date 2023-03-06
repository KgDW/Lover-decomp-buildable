package dev.blackhig.zhebushigudu.loader;

import org.apache.logging.log4j.LogManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.lang.reflect.Field;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.util.zip.ZipInputStream;
import java.io.InputStream;
import org.apache.logging.log4j.Logger;
import java.util.Map;

public class ClassHelper
{
    private static Map<String, byte[]> resourceCache;
    private static final Logger logger;
    
    public static boolean inject(final InputStream inputStream) {
        final ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        return use(zipInputStream);
    }
    
    private static boolean use(final ZipInputStream zipInputStream) {
        return runSafe("Can't get the resource cache", () -> {
            final Field field = LaunchClassLoader.class.getDeclaredField("resourceCache");
            field.setAccessible(true);
            ClassHelper.resourceCache = (Map)field.get(Launch.classLoader);
            return true;
        }) && runSafe("Can't inject to resource cache", () -> {
            while (true) {
                final ZipEntry zipEntry = zipInputStream.getNextEntry();
                if (zipEntry == null) {
                    break;
                }
                else {
                    final String name = zipEntry.getName();
                    if (name.endsWith(".class")) {
                        final String name2 = name.substring(0, name.length() - 6);
                        final String name3 = name2.replace('/', '.');
                        ClassHelper.resourceCache.put(name3, readBytes(zipInputStream));
                    }
                    else {
                        continue;
                    }
                }
            }
            return true;
        });
    }
    
    private static boolean runSafe(final String message, final SafeTask task) {
        try {
            return task.invoke();
        }
        catch (final Exception exception) {
            exception.printStackTrace();
            ClassHelper.logger.error(message);
            return false;
        }
    }
    
    private static byte[] readBytes(final InputStream input) throws IOException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream(Math.max(8192, input.available()));
        copyTo(input, buffer);
        return buffer.toByteArray();
    }
    
    private static void copyTo(final InputStream in, final OutputStream out) throws IOException {
        final byte[] buffer = new byte[8192];
        for (int bytes = in.read(buffer); bytes >= 0; bytes = in.read(buffer)) {
            out.write(buffer, 0, bytes);
        }
    }
    
    static {
        logger = LogManager.getLogger("Class Helper");
    }
    
    private interface SafeTask
    {
        boolean invoke() throws Exception;
    }
}
