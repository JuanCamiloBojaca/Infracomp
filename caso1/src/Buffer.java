import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
	private Queue<Mensaje> mensajes = new LinkedList<>();
	private volatile int lecturasDisponibles;
	private volatile int escriturasDisponibles;
	private volatile int clientesActivos;
	public Buffer(int size) {
		escriturasDisponibles = size;
		lecturasDisponibles = 0;
		clientesActivos = 0;
	}
	public synchronized void nuevoCliente() {
		clientesActivos++;
	}
	public synchronized void terminarCliente() {
		clientesActivos--;
	}
	
	public synchronized boolean hayClientes() {
		return clientesActivos > 0;
	}

	public synchronized boolean permisoEscribir() {
		if (escriturasDisponibles > 0) {
			escriturasDisponibles--;
			return true;
		}
		return false;
	}

	public synchronized void escribir(Mensaje mensaje) {
		mensajes.add(mensaje);
		lecturasDisponibles++;
	}

	public synchronized boolean permisoLeer() {
		if (lecturasDisponibles > 0) {
			lecturasDisponibles--;
			return true;
		}
		return false;
	}

	public synchronized Mensaje retirar() {
		Mensaje ans = mensajes.poll();
		escriturasDisponibles++;
		return ans;
	}
}