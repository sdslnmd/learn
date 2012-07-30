package nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * User: sunluning
 * Date: 12-7-20 下午10:25
 */
public class BioCopy {

    public static void main(String[] args) throws IOException {

        long begin = System.currentTimeMillis();
        FileInputStream fileInputStream = new FileInputStream("/Users/sunluning/tmp/source.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/sunluning/tmp/dest.txt");

        byte[] buffer=new byte[1024];

        int read=0;

        StringBuilder re=new StringBuilder();
        while((read=fileInputStream.read(buffer))!=-1){

            fileOutputStream.write(buffer,0,read);
            re.append(new String(buffer,"utf-8"));
        }

        System.out.println(re);

        long over = System.currentTimeMillis();
        System.out.println(over-begin);



    }
}
