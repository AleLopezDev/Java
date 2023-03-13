import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;


public class Servidor {

    private static String dirClavePrivada = "/home/alex/Documents/ClavePrivada_Servidor.txt";

    public static void main(String[] args) {
        try {
            System.out.println("Leyendo la clave privada");
            FileInputStream fis = new FileInputStream(new File(dirClavePrivada));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            BigInteger modulus = new BigInteger(br.readLine());
            BigInteger privateExponent = new BigInteger(br.readLine());
            br.close();

            // Crear la clave privada RSA
            RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Servidor iniciado");

            // Esperando mensajes
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("CLIENTE CONECTADO");
                DataInputStream is = new DataInputStream(socket.getInputStream());
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());

                // Recibe la clave de parte de cliente
                int sizeDES = (int) Integer.parseInt(is.readUTF()); // Obtenemos el tamaño de la clave DES
                byte[] claveDesEncriptada = new byte[sizeDES]; // Indicamos el tamaño de la clave Des que vamos a recibir
                is.read(claveDesEncriptada); // Obtenemos la clave

                os.writeUTF("(SERVIDOR) : He recibido LA CLAVE, NEGOCIACIONES COMPLETADAS"); // Enviamos mensaje al cliente diciendo que se ha completado la negociacion

                // Recibe el mensaje del cliente
                int sizeMensaje = (int) Integer.parseInt(is.readUTF()); // Obtenemos el tamaño del mensaje
                byte[] mensaje = new byte[sizeMensaje]; // Indicamos el tamaño del mensaje
                is.read(mensaje); // Obtenemos el mensaje

                System.out.println(new String(mensaje));

                // Inicializamos el Cipher con la clave privada
                System.out.println("Desencriptando la clave Recibida");
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);

                // Desencriptar la clave, obteniendo el espectro de clave ( Transparente )
                byte[] spec = cipher.doFinal(claveDesEncriptada);
                DESKeySpec desKeySpec = new DESKeySpec(spec);
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
                SecretKey claveDES = secretKeyFactory.generateSecret(desKeySpec); // La transformamos a opaca para usarla en el Cipher

                // Crear cipher DES
                Cipher cipherDES = Cipher.getInstance("DES");
                cipherDES.init(Cipher.DECRYPT_MODE, claveDES);
                System.out.println("El mensaje DESENCRIPTADO es: " + new String(cipherDES.doFinal(mensaje)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
