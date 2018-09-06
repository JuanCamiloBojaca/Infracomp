package caso1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Buffer {
	private static int CAPACIDAD;
	private static Queue<Mensaje> mensajes = new LinkedList<>();
	private static int lecturasDisponibles;
	private static int escriturasDisponibles;

	public static synchronized void escribir(Mensaje mensaje) {
		mensajes.add(mensaje);
		lecturasDisponibles++;
	}

	public static synchronized boolean permisoEscribir() {
		if (escriturasDisponibles > 0) {
			escriturasDisponibles--;
			return true;
		}
		return false;
	}

	public static synchronized boolean permisoLeer() {
		if (lecturasDisponibles > 0) {
			lecturasDisponibles--;
			return true;
		}
		return false;
	}

	public static synchronized Mensaje retirar() {
		Mensaje ans = mensajes.poll();
		escriturasDisponibles++;
		return ans;
	}

	public static void main(String[] args) {
		CAPACIDAD = 10;
		int clientes = 5;
		int servidores = 2;

		lecturasDisponibles = 0;
		escriturasDisponibles = CAPACIDAD;
		Random rd = new Random();

		for (int i = 0; i < clientes; i++)
			(new Cliente(rd.nextInt(50), i)).start();

		for (int i = 0; i < servidores; i++)
			(new Servidor()).start();
	}
}
