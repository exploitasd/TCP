/*Firat Top - 101101047
Ecem Elvin Cevik - 111101037*/
import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class gonderici {
	public static void main(String[] args) {

		SSLServerSocket serverSocket = null;
		SSLSocket socket = null;
		PrintWriter fileOut = null;

		try {

			KeyStore serverKeys = KeyStore.getInstance("JKS");
			serverKeys.load(new FileInputStream("keystore.jks"), "firat123".toCharArray());
			KeyManagerFactory serverKeyManager = KeyManagerFactory.getInstance("SunX509");
			serverKeyManager.init(serverKeys, "firat123".toCharArray());

			KeyStore clientPublic = KeyStore.getInstance("JKS");
			clientPublic.load(new FileInputStream("truststore2.jks"), "firat1234".toCharArray());
			TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
			trustManager.init(clientPublic);

			SSLContext ssl = SSLContext.getInstance("SSL");
			ssl.init(serverKeyManager.getKeyManagers(), trustManager.getTrustManagers(),
					SecureRandom.getInstance("SHA1PRNG"));
			serverSocket = (SSLServerSocket) ssl.getServerSocketFactory().createServerSocket(8889);
			serverSocket.setNeedClientAuth(true);

		} catch (Exception e) {
		}

		while (true) {
			System.out.println("***** SERVER ISTEK BEKLIYOR *****");

			try {
				socket = (SSLSocket) serverSocket.accept();
				try {

					SSLSession session = socket.getSession();
					java.security.cert.Certificate[] clientcert = session.getPeerCertificates();
					for (int i = 0; i < clientcert.length; i++) {
						System.out.println("-Public Key-");
						System.out.println(clientcert[i].getPublicKey());
						System.out.println("-Certificate Type-");
						System.out.println(clientcert[i].getType());
					}
				} catch (Exception e) {
				}

				String fileName = "Gizli_Veriler.txt";
				fileOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
				fileOut.println(fileName);
				fileOut.flush();

				File transferFile = new File(fileName);

				byte[] bytearray = new byte[(int) transferFile.length()];
				BufferedInputStream bufferInput = new BufferedInputStream(new FileInputStream(transferFile));
				bufferInput.read(bytearray, 0, bytearray.length);

				OutputStream output = socket.getOutputStream();
				output.write(bytearray, 0, bytearray.length);
				output.flush();
				System.out.println("***** GONDERME ISLEMI TAMAMLANDI *****");

			} catch (Exception e) {
				System.out.println("***** HANDSHAKE BASARISIZ OLDU BAGLANTI SONLANDIRILDI *****");
			} finally {
				if (fileOut != null)
					fileOut.close();
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
