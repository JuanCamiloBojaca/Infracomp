package entrega2;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
			String r = canal.recive().toString();
			certificadoEnBytes = DatatypeConverter.parseHexBinary(r);
			try (ByteArrayInputStream in = new ByteArrayInputStream(certificadoEnBytes)) {
				certificado = (X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(in);
			}

			certificado.checkValidity(); // mira que el certificado no esta vencido
			PublicKey llaveServidor = certificado.getPublicKey();
			certificado.verify(llaveServidor); // verifica la firma
			canal.send(Const.ok);
			long ini = System.nanoTime();

			// Etapa 4
			byte[] llaveEncriptada = DatatypeConverter.parseHexBinary(canal.recive().toString());

			Cipher cipher = Cipher.getInstance(config.getAlgotimoAsimetrico());
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
			byte[] llave = cipher.doFinal(llaveEncriptada);

			cipher = Cipher.getInstance(config.getAlgotimoAsimetrico());
			cipher.init(Cipher.ENCRYPT_MODE, llaveServidor);
			llaveEncriptada = cipher.doFinal(llave);

			canal.send(DatatypeConverter.printHexBinary(llaveEncriptada));
			CharSequence v = canal.recive();
			Verificacion.println(System.nanoTime() - ini);
			respErronea(Const.ok.getValue(), v);

			// Etapa5
			SecretKey SimmetricKey = new SecretKeySpec(llave, 0, llave.length, config.getAlgotimoSimetrico());
			cipher = Cipher.getInstance(config.getAlgotimoSimetrico());
			cipher.init(Cipher.ENCRYPT_MODE, SimmetricKey);
			byte[] mensajeEncriptado = cipher.doFinal(consulta.getBytes());
			canal.send(DatatypeConverter.printHexBinary(mensajeEncriptado));
			ini = System.nanoTime();

			Mac mac = Mac.getInstance("Hmac" + config.getHmacName());
			mac.init(SimmetricKey);
			byte[] codigoAutentificacion = mac.doFinal(consulta.getBytes());
			canal.send(DatatypeConverter.printHexBinary(codigoAutentificacion));

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
