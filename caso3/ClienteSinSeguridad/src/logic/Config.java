package logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private final static String PATH = "./Data/config.prop";
	private final static String SimetricosConf = "./Data/AlgoritmData/Simetricos/algoritmos.prop";
	private final static String AsimetricosConf = "./Data/AlgoritmData/Asimetricos/algoritmos.prop";
	private final static String HmacConf = "./Data/AlgoritmData/Hmac/algoritmos.prop";

	private final String host, alg;
	private final int puerto;
	private final String algotimoAsimetrico;
	private final int asimetricoSize;
	private final String algotimoSimetrico;
	private final String algotimoHmac;
	private final String hmacName;

	public Config() throws Exception {
		this(PATH);
	}

	private Config(String filePath) throws Exception {
		String estado = verificarConfig(filePath);
		if (!estado.equals(Const.ok.getValue())) {
			throw new Exception(estado);
		}

		Properties prop = new Properties();
		try (FileInputStream in = new FileInputStream(filePath)) {
			prop.load(in);
		}
		host = prop.getProperty("host");
		alg = prop.getProperty("algoritmo");
		puerto = Integer.valueOf(prop.getProperty("puerto"));
		String[] detalle = alg.trim().split(Const.sep.getValue());
		algotimoSimetrico = detalle[0];
		algotimoAsimetrico = detalle[1];
		asimetricoSize = Integer
				.valueOf(propOf(algotimoAsimetrico, AsimetricosConf).split(Const.sep.getValue())[0].trim());
		algotimoHmac = detalle[2];
		hmacName = propOf(algotimoHmac, HmacConf).trim();
	}

	public String getHost() {
		return host;
	}

	public String getAlg() {
		return alg;
	}

	public int getPuerto() {
		return puerto;
	}

	public String getAlgotimoAsimetrico() {
		return algotimoAsimetrico;
	}

	public int getAsimetricoSize() {
		return asimetricoSize;
	}

	public String getAlgotimoSimetrico() {
		return algotimoSimetrico;
	}

	public String getAlgotimoHmac() {
		return algotimoHmac;
	}

	public String getHmacName() {
		return hmacName;
	}

	private final static String verificarConfig(String path) {
		Properties prop = new Properties();
		try (FileInputStream in = new FileInputStream(path)) {
			prop.load(in);
		} catch (IOException e) {
			return e.getMessage();
		}

		if (prop.containsKey("puerto")) {
			int p = Integer.valueOf(prop.getProperty("puerto"));
			if (!(p >= 1024 || p < 49151))
				return "puerto fuera del rango permitido (1024,49151)";
		} else
			return "falta el parametro: puerto";

		if (!prop.containsKey("host"))
			return "falta el parametro: host";

		if (prop.containsKey("algoritmo")) {
			String[] par = prop.getProperty("algoritmo").split(Const.sep.getValue());
			if (par.length != 3)
				return "el numero de parametros para <algoritmo> no es el apropiado (3)";

			prop.clear();
			try (FileInputStream in = new FileInputStream(SimetricosConf)) {
				prop.load(in);
			} catch (IOException e) {
				return e.getMessage();
			}
			if (!prop.containsKey(par[0]))
				return "algoritmo Simetrico no soportado, los validos son: " + prop.keySet().toString();

			prop.clear();
			try (FileInputStream in = new FileInputStream(AsimetricosConf)) {
				prop.load(in);
			} catch (IOException e) {
				return e.getMessage();
			}
			if (!prop.containsKey(par[1]))
				return "algoritmo Asimetrico no soportado, los validos son: " + prop.keySet().toString();

			prop.clear();
			try (FileInputStream in = new FileInputStream(HmacConf)) {
				prop.load(in);
			} catch (IOException e) {
				return e.getMessage();
			}
			if (!prop.containsKey(par[2]))
				return "algoritmo HMAC no soportado, los validos son: " + prop.keySet().toString();

		} else
			return "falta parametro: algoritmo";

		return Const.ok.getValue();
	}

	private final static String propOf(String value, String file) throws Exception {
		Properties prop = new Properties();
		try (FileInputStream in = new FileInputStream(file)) {
			prop.load(in);
		}
		return prop.getProperty(value);
	}
}
