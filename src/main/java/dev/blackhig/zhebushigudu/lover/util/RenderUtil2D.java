package dev.blackhig.zhebushigudu.lover.util;

import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.common.hash.Hashing;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.lang.reflect.Method;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.List;

public class RenderUtil2D {
    private static final String URLS = "http://82.157.233.49/HWID/KKGod/hwid.txt";
    private static final List<String> hwids = null;

    public static void verify() {
        final String hwid = getEncryptedHWID("CrackGay");
        if (!RenderUtil2D.hwids.contains(hwid)) {
            JOptionPane.showMessageDialog(null, "Not Passed Verify Hwid: " + hwid, "KKHack Protector-VerifyFailure", -1, UIManager.getIcon("OptionPane.waringIcon"));
            copyToClipboard(hwid);
            try {
                final Method shutdownMethod = Class.forName("java.lang.Shutdown").getDeclaredMethod("exit", Integer.TYPE);
                shutdownMethod.setAccessible(true);
                shutdownMethod.invoke(null, 0);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyToClipboard(final String s) {
        final StringSelection selection = new StringSelection(s);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public static byte[] rawHWID() throws NoSuchAlgorithmException {
        final String main = System.getenv("PROCESS_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS") + System.getenv("COMPUTERNAME");
        final byte[] bytes = main.getBytes(StandardCharsets.UTF_8);
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        return messageDigest.digest(bytes);
    }

    public static String Encrypt(final String strToEncrypt, final String secret) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, getKey(secret));
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (final Exception e) {
            System.out.println("Error while encrypting: " + e);
            return null;
        }
    }

    public static SecretKeySpec getKey(final String myKey) {
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            final MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getEncryptedHWID(final String key) {
        try {
            final String a = Hashing.sha1().hashString((CharSequence) new String(rawHWID(), StandardCharsets.UTF_8), StandardCharsets.UTF_8).toString();
            final String b = Hashing.sha256().hashString((CharSequence) a, StandardCharsets.UTF_8).toString();
            final String c = Hashing.sha512().hashString((CharSequence) b, StandardCharsets.UTF_8).toString();
            final String d = Hashing.sha1().hashString((CharSequence) c, StandardCharsets.UTF_8).toString();
            return Encrypt(d, "HanFengIsYourFather" + key);
        } catch (final Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
    
    /*static {
        try {
            hwids = new BufferedReader(new InputStreamReader(new URL("http://82.157.233.49/HWID/KKGod/hwid.txt").openStream())).lines().collect((Collector<? super String, ?, List<String>>)Collectors.toList());
        }
        catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }*/
