
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

				boolean a;
				synchronized (buffer) {
					a = buffer.escriturasDisponibles == 0;
					if (!a)
						buffer.escriturasDisponibles--;

				}
				while (a) {
					yield();
					synchronized (buffer) {
						a = buffer.escriturasDisponibles == 0;
						if (!a)
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
