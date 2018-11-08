package Logica;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Coordinador {

	private static ServerSocket ss;
	private static final String MAESTRO = "MAESTRO: ";
	static java.security.cert.X509Certificate certSer; /* acceso default */
	static KeyPair keyPairServidor; /* acceso default */

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int numeroThreads = 4;
		int delay = 50;

		System.out.println(MAESTRO + "Establezca puerto de conexion:");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		int ip = Integer.parseInt(br.readLine());
		System.out.println(MAESTRO + "Empezando servidor maestro en puerto " + ip);

		// Adiciona la libreria como un proveedor de seguridad.
		// Necesario para crear certificados.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		keyPairServidor = Seg.grsa();
		certSer = Seg.gc(keyPairServidor);

		int idThread = 0;
		// Crea el socket que escucha en el puerto seleccionado.
		ss = new ServerSocket(ip);
		System.out.println(MAESTRO + "Socket creado.");
		ExecutorService executor = Executors.newFixedThreadPool(numeroThreads);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh-mm");
		String path = "./Reports/" + format.format(new Date()) + ".txt";

		try (PrintStream pr = new PrintStream(getFile(path))) {
			CpuLog cpu = new CpuLog(delay, pr);
			cpu.start();
			while (true) {
				try {
					Socket sc = ss.accept();
					System.out.println(MAESTRO + "Cliente " + idThread + " aceptado.");
					Delegado d3 = new Delegado(sc, idThread, pr);
					idThread++;
					executor.execute(d3);
					pr.flush();
				} catch (IOException e) {
					System.out.println(MAESTRO + "Error creando el socket cliente.");
					e.printStackTrace();
				}
			}
		}
	}

	public static File getFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists())
			file.createNewFile();
		return file;
	}
}
