
public class Servidor extends Thread {
	private Buffer buffer;

	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		Ext: while (buffer.hayClientes()) {
			
			while (!buffer.permisoLeer()) {
				if(!buffer.hayClientes())
					break Ext;
				yield();
			}
			Mensaje mensaje = buffer.retirar();
			mensaje.setNumero(mensaje.getNumero() + 1);
			synchronized (mensaje) {
				mensaje.notify();
			}
		}
	}

}
