//recibe solicitudes
public class Servidor extends Thread {
	private Buffer buffer;

	// constructor
	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		Ext: while (true) { 
			while (!buffer.permisoLeer()) {
				yield(); // si no hay permisos para leer, vaya a la cola de procesos y espere
				if (!buffer.hayClientes())
					break Ext; // si no hay clientes, termina el while(true)
			}
			Mensaje mensaje = buffer.retirar(); // saque el mensaje del buffer
			mensaje.setNumero(mensaje.getNumero() + 1); // modifica el mensaje
			synchronized (mensaje) {
				mensaje.notify(); // notifique que el mensaje termino y se puede imprimir, se notifica al cliente
			}
		}
	}

}