package dev.blackhig.zhebushigudu.lover.util;

import java.util.Random;

public class RandomUtil
{
    private static final Random random;
    
    public static Random getRandom() {
        return RandomUtil.random;
    }
    
    public static int nextInt(final int startInclusive, final int endExclusive) {
        return (endExclusive - startInclusive <= 0) ? startInclusive : (startInclusive + new Random().nextInt(endExclusive - startInclusive));
    }
    
    public final long randomDelay(final int minDelay, final int maxDelay) {
        return nextInt(minDelay, maxDelay);
    }
    
    public static double nextDouble(final double startInclusive, final double endInclusive) {
        return (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0) ? startInclusive : (startInclusive + (endInclusive - startInclusive) * Math.random());
    }
    
    public static long nextLong(final long startInclusive, final long endInclusive) {
        return (endInclusive - startInclusive <= 0L) ? startInclusive : ((long)(startInclusive + (endInclusive - startInclusive) * Math.random()));
    }
    
    public static float nextFloat(final float startInclusive, final float endInclusive) {
        return (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f) ? startInclusive : ((float)(startInclusive + (endInclusive - startInclusive) * Math.random()));
    }
    
    static {
        random = new Random();
    }
}
