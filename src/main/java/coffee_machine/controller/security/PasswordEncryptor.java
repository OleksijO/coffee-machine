package coffee_machine.controller.security;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;

/**
 * Created by oleksij.onysymchuk@gmail on 24.11.2016.
 */
public class PasswordEncryptor {
    private static final String SALT = "The Salt";

    public static String encryptPassword(String unencryptedPassword) throws NoSuchAlgorithmException {

        return DigestUtils.md5Hex(unencryptedPassword + SALT);
    }

}