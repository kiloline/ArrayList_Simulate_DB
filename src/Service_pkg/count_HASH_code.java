package Service_pkg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author rkppo
 */
public class count_HASH_code 
{
    //public static String getHash(String string) throws FileNotFoundException, IOException
    public static String getHash(LinkedList<File> files) throws FileNotFoundException, IOException
    {
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        int loop;
        for(loop=0;loop<files.size();loop++)
        {
            FileInputStream fis=new FileInputStream(files.get(loop));
            byte[] b = new byte[fis.available()];
            fis.read(b);
            bos.write(b);  
        }
        return returnHash(bos.toByteArray());
    }
    public static String getHash(File file) throws FileNotFoundException, IOException
    {
        if (file != null)
        {
            FileInputStream fis=new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
            byte[] b = new byte[fis.available()];
            fis.read(b);
            bos.write(b);  
            b=bos.toByteArray();
            return returnHash(b);
        }
        return null;
    }
    public static String getHash(String string) throws IOException
    {
        return returnHash(string.getBytes());
    }
    private static String returnHash(byte[] byteBuffer) throws UnsupportedEncodingException 
    {
        //Properties prop;
        String hash,hr,Encoding="GBK";
        /*try {
        prop=new Properties();
        prop.load(new FileInputStream("src/Service_pkg/db_env_conf.properties"));
        hash=prop.getProperty("HASHmethod","SHA-256");
        Encoding=prop.getProperty("Encoding","GBK");
        } catch (Exception ex) {
        hash="SHA-256";
        Encoding="GBK";
        }
        MessageDigest md;
        try {
        md = MessageDigest.getInstance(hash);
        } catch (NoSuchAlgorithmException ex) {
        return null;
        }
        md.update(byteBuffer);
            hr=new String(md.digest(),Encoding);*/
        Base64 b6e=new Base64();
        hr=new String(b6e.encode(byteBuffer));
        return hr;
    }
}
