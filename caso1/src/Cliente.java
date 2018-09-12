//manda solicitudes
public class Cliente extends Thread {
	private Buffer buffer;
	private int nSolicitudes;

	// constructor
	public Cliente(int id, int nSolicitudes, Buffer buffer) {
		this.nSolicitudes = nSolicitudes;
		this.buffer = buffer;
		buffer.nuevoCliente();
		setName("Cliente_" + id);
	}

	@Override
	public void run() {
		for (int i = 0; i < nSolicitudes; i++) {
			int numero = i + 1;
			Mensaje mensaje = new Mensaje(); // se crea el mensaje
			mensaje.setNumero(numero); // se le pone id

			while (!buffer.permisoEscribir()) {
				yield(); // si no hay permisos para escribir, vuelva a la cola de procesos
				try {
					sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			buffer.escribir(mensaje); // manda solicitud al buffer

			try {
				synchronized (mensaje) {
					mensaje.wait(); // espera al notify en el servidor
				}
				System.out.println(getName() + " mensaje " + numero + " - " + mensaje.getNumero());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.terminarCliente(); // cierra el thread
		System.out.println(getName() + " termino");
	}
}