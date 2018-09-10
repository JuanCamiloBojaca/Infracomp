public class Servidor extends Thread {
	private Buffer buffer;

	public Servidor(int id, Buffer buffer) {
		this.buffer = buffer;
		setName("Servidor_" + id);
	}

	@Override
	public void run() {
		try {
			Mensaje mensaje = null;
			while (true) {
				mensaje = buffer.retirar();

				synchronized (mensaje) {
					mensaje.setNumero(mensaje.getNumero() + 1);
					mensaje.notify();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
