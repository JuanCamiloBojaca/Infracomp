
public class Servidor extends Thread {
	private volatile boolean permiso;
	private Buffer buffer;

	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setPriority(NORM_PRIORITY + 1);
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		Mensaje mensaje = null;
		Ext: while (true) {
			boolean a;

			synchronized (buffer) {
				permiso = buffer.lecturasDisponibles == 0;
				if (!permiso)
					buffer.lecturasDisponibles--;

			}
			while (permiso) {
				synchronized (buffer) {
					a = buffer.clientesActivos == 0;
				}
				if (a)
					break Ext;
				yield();
				synchronized (buffer) {
					permiso = buffer.lecturasDisponibles == 0;
					if (!permiso)
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
