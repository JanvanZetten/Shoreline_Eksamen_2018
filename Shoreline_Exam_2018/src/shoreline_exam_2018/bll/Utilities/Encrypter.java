/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_exam_2018.bll.Utilities;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import shoreline_exam_2018.bll.BLLException;

/**
 *
 * @author Asbamz
 */
public class Encrypter
{
    /**
     * Encrypt String to SHA256 hash.
     * @param base
     * @return
     * @throws BLLException
     */
    public static String encrypt(String base) throws BLLException
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++)
            {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException ex)
        {
            throw new BLLException(ex.getMessage(), ex.getCause());
        }
    }

}
