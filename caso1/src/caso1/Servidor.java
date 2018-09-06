package caso1;

public class Servidor extends Thread {

	@Override
	public void run() {
		while (true) {
			while (!Buffer.permisoLeer()) {
				yield();
			}
			Mensaje mensaje = Buffer.retirar();
			mensaje.setNumero(mensaje.getNumero() + 1);
			synchronized (mensaje) {
				mensaje.notify();
			}
		}
	}

}
