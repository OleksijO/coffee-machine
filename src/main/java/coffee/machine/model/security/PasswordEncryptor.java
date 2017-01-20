package coffee.machine.model.security;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class encrypt pure string to md5 hash with specified salt word.
 *
 * @author oleksij.onysymchuk@gmail.com 24.11.2016.
 */
public class PasswordEncryptor {
    /**
     * The salt word for encryption
     */
    private static final String SALT = "The Salt";

    private PasswordEncryptor() {
    }

    /**
     * @param unencryptedPassword password to be encrypted
     * @return MD5 hash of input parameter
     */
    public static String encryptPassword(String unencryptedPassword) {
        return DigestUtils.md5Hex(unencryptedPassword + SALT);
    }

}
