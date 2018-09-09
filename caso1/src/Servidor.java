
public class Servidor extends Thread {
	public volatile boolean permiso;
	private Buffer buffer;

	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		Mensaje mensaje = null;
		Ext: while (true) {
			boolean a, b;
			synchronized (buffer) {
				a = buffer.lecturasDisponibles == 0;
				if (!a)
					buffer.lecturasDisponibles--;

			}
			while (a) {
				synchronized (buffer) {
					b = buffer.clientesActivos == 0;
				}
				if (b)
					break Ext;
				yield();
				synchronized (buffer) {
					a = buffer.lecturasDisponibles == 0;
					if (!a)
						buffer.lecturasDisponibles--;
				}
			}

			synchronized (buffer) {
				mensaje = buffer.retirar();
				buffer.escriturasDisponibles++;
			}

			synchronized (mensaje) {
				mensaje.setNumero(mensaje.getNumero() + 1);
				mensaje.notify();
			}
		}
		// System.out.println(getName());
	}

}
