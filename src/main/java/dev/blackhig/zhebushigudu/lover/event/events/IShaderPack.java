package dev.blackhig.zhebushigudu.lover.event.events;

import java.io.InputStream;

public interface IShaderPack
{
    String getName();
    
    InputStream getResourceAsStream(final String p0);
    
    boolean hasDirectory(final String p0);
    
    void close();
}
