/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.dal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author alexl
 */
public class PropertiesWriter {

    /**
     * Updates the default updatedProps in the properties file. Requires a String Array.
     * @param updatedProps = [0] = identifier. [1] = value.
     * @throws DALException
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void updateProperties(String[] updatedProps) throws DALException, FileNotFoundException, IOException {

        FileInputStream in = new FileInputStream("properties.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("properties.properties");
        props.setProperty(updatedProps[0], updatedProps[1]);
        props.store(out, null);
        out.close();
    }

}
