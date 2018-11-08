package carga;

import java.io.PrintStream;

import logic.Cliente;
import logic.Config;
import uniandes.gload.core.Task;

public class ClientTask extends Task {
	public static Object obj = new Object();
	public static int bien = 0;
	public static int mal = 0;

	private PrintStream fileVerificacion;
	private PrintStream fileConsulta;
	private Config config;

	public ClientTask(Config config, PrintStream fileVerificacion, PrintStream fileConsulta) {
		this.fileVerificacion = fileVerificacion;
		this.fileConsulta = fileConsulta;
		this.config = config;
	}

	public static int numberOfClients() {
		synchronized (obj) {
			return mal + bien;
		}
	}

	public static void clear() {
		synchronized (obj) {
			mal = 0;
			bien = 0;
		}
	}

	@Override
	public void fail() {
		synchronized (obj) {
			mal++;
		}
	}

	@Override
	public void success() {
		synchronized (obj) {
			bien++;
		}
	}

	@Override
	public void execute() {
		try {
			Cliente cliente = new Cliente(config, fileVerificacion, fileConsulta, System.out);
			cliente.protocolo((int) (Math.random() * (int) 100));

			success();
		} catch (Exception e) {
			fail();
		}
	}
}
