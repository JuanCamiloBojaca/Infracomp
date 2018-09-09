
public class Cliente extends Thread {
	private Buffer buffer;
	private int nSolicitudes;
	public volatile boolean permiso;

	public Cliente(int id, int nSolicitudes, Buffer buffer) {
		this.nSolicitudes = nSolicitudes;
		this.buffer = buffer;
		setName("Cliente_" + id);
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < nSolicitudes; i++) {
				int numero = i + 1;
				Mensaje mensaje = new Mensaje();
				mensaje.setNumero(numero);

				synchronized (buffer) {
					permiso = buffer.escriturasDisponibles == 0;
					if (!permiso)
						buffer.escriturasDisponibles--;

				}
				while (permiso) {
					yield();
					synchronized (buffer) {
						permiso = buffer.escriturasDisponibles == 0;
						if (!permiso)
							buffer.escriturasDisponibles--;
					}
				}
				synchronized (buffer) {
					buffer.escribir(mensaje);
					buffer.lecturasDisponibles++;
				}

				synchronized (mensaje) {
					mensaje.wait();
				}
				System.out.println(getName() + " (" + numero + " -> " + mensaje.getNumero() + ")");
			}

			synchronized (buffer) {
				buffer.clientesActivos--;
			}

			System.out.println("fin " + getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
