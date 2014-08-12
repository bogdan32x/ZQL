package org.gibello.zql.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utils class to be used in unit testing the project.
 * 
 * @author Bogdan Mariesan, Romania
 * @since 12.08.2014
 *
 */
public class TestUtils {

    public static String readInputStreamAsString(String inputStreamPath) throws IOException {
        InputStream inputStream = new DataInputStream(new FileInputStream(inputStreamPath));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString();
    }

}
