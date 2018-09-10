public class Servidor extends Thread {
	private Buffer buffer;

	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		Ext: while (true) {
			while (!buffer.permisoLeer()) {
				yield();
				try {
					sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!buffer.hayClientes())
					break Ext;
			}
			Mensaje mensaje = buffer.retirar();
			mensaje.setNumero(mensaje.getNumero() + 1);
			synchronized (mensaje) {
				mensaje.notify();
			}
		}
	}

}