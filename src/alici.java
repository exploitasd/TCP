/*Firat Top - 101101047
Ecem Elvin Cevik - 111101037*/
import java.io.*;
import java.security.*;

import javax.net.ssl.*;

public class alici {
	public static void main(String[] args) throws IOException {

		SSLSocket socket = null;
		BufferedReader fromServer = null;
		BufferedReader fromClient = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Sunucunun IP adresini giriniz: ");
		String IP = fromClient.readLine();

		try {

			KeyStore clientKeys = KeyStore.getInstance("JKS");
			clientKeys.load(new FileInputStream("keystore2.jks"), "firat1234".toCharArray());
			KeyManagerFactory clientKeyManager = KeyManagerFactory.getInstance("SunX509");
			clientKeyManager.init(clientKeys, "firat1234".toCharArray());

			KeyStore serverPublic = KeyStore.getInstance("JKS");
			serverPublic.load(new FileInputStream("truststore.jks"), "firat123".toCharArray());
			TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
			trustManager.init(serverPublic);

			SSLContext ssl = SSLContext.getInstance("SSL");
			ssl.init(clientKeyManager.getKeyManagers(), trustManager.getTrustManagers(),
					SecureRandom.getInstance("SHA1PRNG"));
			socket = (SSLSocket) ssl.getSocketFactory().createSocket(IP, 8889);

			try {
				socket.startHandshake();
				SSLSession session = socket.getSession();
				java.security.cert.Certificate[] servercert = session.getPeerCertificates();

				for (int i = 0; i < servercert.length; i++) {

					System.out.println("-Public Key-");
					System.out.println(servercert[i].getPublicKey());
					System.out.println("-Certificate Type-");
					System.out.println(servercert[i].getType());

				}
			}

			catch (Exception e) {
				System.out.println("***** HANDSHAKE BASARISIZ OLDU BAGLANTI SONLANDIRILDI ******");
				System.exit(0);
			}

			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			byte[] mybytearray = new byte[1];
			InputStream incomingInput = socket.getInputStream();

			String filename = fromServer.readLine();
			String[] arg = filename.split("\\.");
			filename = arg[0] + "_alindi." + arg[1];

			FileOutputStream fileOutput = new FileOutputStream(filename);
			BufferedOutputStream bufferOutput = new BufferedOutputStream(fileOutput);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			int readBytes = 0;

			do {
				readBytes = incomingInput.read(mybytearray, 0, mybytearray.length);
				bos.write(mybytearray);
			} while (readBytes != -1);

			bufferOutput.write(bos.toByteArray(), 0, bos.toByteArray().length);
			bufferOutput.close();
			bufferOutput.close();

			System.out.println("***** ALMA ISLEMI TAMAMLANDI *****");
			socket.close();

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				if (fromServer != null)
					fromServer.close();

				if (socket != null)
					socket.close();

				if (socket != null)
					socket.close();
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}