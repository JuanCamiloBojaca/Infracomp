package logic;

import java.io.PrintStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.X509Certificate;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Cliente {
	private Config config;
	private KeyPair keyPair;
	private PrintStream Verificacion;
	private PrintStream Consulta;
	private PrintStream general;

	public Cliente() throws Exception {
		this(new Config(), System.out, System.out, System.out);
	}

	public Cliente(Config config, PrintStream verificacion, PrintStream consulta, PrintStream general)
			throws NoSuchAlgorithmException {
		Verificacion = verificacion;
		Consulta = consulta;
		this.general = general;
		Security.addProvider(new BouncyCastleProvider());
		this.config = config;
		KeyPairGenerator generator = KeyPairGenerator.getInstance(config.getAlgotimoAsimetrico());
		generator.initialize(config.getAsimetricoSize());
		keyPair = generator.generateKeyPair();
	}

	public String protocolo(int codigo) throws Exception {
		general.println("inicia Protocolo");
		String consulta = "" + codigo;
		try (Canal canal = new Canal(new Socket(config.getHost(), config.getPuerto()))) {
			// Etapa 1
			canal.send(Const.hola);
			respErronea(Const.ok.getValue(), canal.recive());

			canal.send(Const.algoritmos.getValue() + Const.sep.getValue() + config.getAlg());
			respErronea(Const.ok.getValue(), canal.recive());

			// Etapa 2
			X509Certificate certificado = GeneradorDeCertificados.generate(keyPair, config.getHmacName(),
					config.getAlgotimoAsimetrico());
			byte[] certificadoEnBytes = certificado.getEncoded();
			String certificadoEnString = DatatypeConverter.printHexBinary(certificadoEnBytes);
			canal.send(certificadoEnString);

			// Etapa 3
			respErronea(Const.ok.getValue(), canal.recive());
			//recibe el certificado.
			canal.recive().toString();
			canal.send(Const.ok);
			long ini = System.nanoTime();

			// Etapa 4
			CharSequence ls = canal.recive();
			canal.send(ls);
			CharSequence v = canal.recive();
			Verificacion.println(System.nanoTime() - ini);
			respErronea(Const.ok.getValue(), v);

			// Etapa5

			canal.send(consulta);
			ini = System.nanoTime();
			canal.send(consulta);
			String[] ans = canal.recive().toString().split(Const.sep.getValue());
			Consulta.println(System.nanoTime() - ini);
			respErronea(Const.ok.getValue(), ans[0]);

			general.println(codigo + " -> " + ans[1]);
			return ans[1];
		}
	}

	private static void respErronea(CharSequence esp, CharSequence real) throws Exception {
		if (!esp.equals(real))
			throw new Exception("repuesta Erronea del servidor : (esperado " + esp + ", recibido" + real + ")");
	}
}
