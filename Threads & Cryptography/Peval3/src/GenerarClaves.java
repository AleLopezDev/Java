import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class GenerarClaves {

    private static final String dirClavePublica = "/home/alex/Documents/ClavePublica_Servidor.txt";
    private static final String dirClavePrivada = "/home/alex/Documents/ClavePrivada_Servidor.txt";

    public static void main(String[] args) {
        try {
            SecureRandom random = new SecureRandom();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(512, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            /* CLAVE PUBLICA */
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // Usar espectro de clave para generar la clave transparente privada usando el algoritmo RSA
            RSAPublicKeySpec publicKeySpec = keyFactory.getKeySpec(keyPair.getPublic(), RSAPublicKeySpec.class);
            System.out.println("Salvando la clave publica en un fichero");
            FileOutputStream fos = new FileOutputStream(new File(dirClavePublica));
            PrintWriter pw = new PrintWriter(fos);
            pw.println(publicKeySpec.getModulus());
            pw.println(publicKeySpec.getPublicExponent());
            pw.close();

            /* CLAVE PRIVADA */
            RSAPrivateKeySpec privateKeySpec = keyFactory.getKeySpec(keyPair.getPrivate(), RSAPrivateKeySpec.class);
            System.out.println("Salvando la clave privada en un fichero");
            FileOutputStream fos2 = new FileOutputStream(new File(dirClavePrivada));
            PrintWriter pw2 = new PrintWriter(fos2);
            pw2.println(privateKeySpec.getModulus());
            pw2.println(privateKeySpec.getPrivateExponent());
            pw2.close();



        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
