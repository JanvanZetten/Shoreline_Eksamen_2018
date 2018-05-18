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
import shoreline_exam_2018.dal.DALException;

/**
 *
 * @author alexl
 */
public class PropertiesWriter {

    public void updateDefaultDirectory(String[] directory) throws DALException, FileNotFoundException, IOException {

        FileInputStream in = new FileInputStream("test.properties");
        Properties props = new Properties();
        props.load(in);
        in.close();

        FileOutputStream out = new FileOutputStream("test.properties");
        props.setProperty(directory[0], directory[1]);
        props.store(out, null);
        out.close();
    }

}
