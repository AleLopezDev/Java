import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;


public class Cliente {

    static Scanner sc = new Scanner(System.in);
    private static String dirClavePublica = "/home/alex/Documents/ClavePublica_Servidor.txt";


    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IOException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {

        // Se conecta al servidor
        System.out.println("Conectado al servidor");
        Socket socket = new Socket("localhost", 5000);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        DataInputStream is = new DataInputStream(socket.getInputStream());
        
        // Generamos nuestra clave DES
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(secureRandom);
        SecretKey key = keyGenerator.generateKey();

        // Recreamos la clave pública RSA con el modulo y el exponente
        System.out.println("Leyendo la clave pública del servidor");
        FileInputStream fis = new FileInputStream(new File(dirClavePublica));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        BigInteger modulus = new BigInteger(br.readLine());
        BigInteger publicExponent = new BigInteger(br.readLine());
        br.close();

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        // Encriptar la clave DES con la clave pública del servidor
        System.out.println("Encriptando la clave DES");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        SecretKeyFactory keyFactoryDES = SecretKeyFactory.getInstance("DES");
        DESKeySpec keySpec = (DESKeySpec) keyFactoryDES.getKeySpec(key, DESKeySpec.class);
        byte[] encryptedKey = cipher.doFinal(keySpec.getKey());

        System.out.println("Enviando la clave al servidor");

        //  Envio la clave encriptada al servidor
        os.writeUTF(String.valueOf(encryptedKey.length));
        os.write(encryptedKey);

        // Recibo el mensaje de que el servidor ha recibido la clave (Se ha completado la negociación de claves)
        String mensajeVuelta = is.readUTF();
        System.out.println(mensajeVuelta);

        // Pedimos que el usuario envie un mensaje
        System.out.println("Escribe un mensaje para enviarle al servidor:  ");
        String mensaje = sc.nextLine();

        // Encriptar el mensaje usando la clave DES
        System.out.println("Encriptando el mensaje");
        Cipher desCipher = Cipher.getInstance("DES");
        desCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] mensajeEncriptado = desCipher.doFinal(mensaje.getBytes());
        System.out.println("Mensaje encriptado: " + new String(mensajeEncriptado));

        // Le envio el mensaje Encriptado
        os.writeUTF(String.valueOf(mensajeEncriptado.length));
        os.write(mensajeEncriptado);

        os.close();
    }
}

