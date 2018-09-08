
import java.util.Random;

public class Cliente extends Thread {
	private Buffer buffer;
	private int nSolicitudes;

	public Cliente(int id, int nSolicitudes, Buffer buffer) {
		this.nSolicitudes = nSolicitudes;
		this.buffer = buffer;
		buffer.nuevoCliente();
		setName("Cliente_" + id);
	}

	@Override
	public void run() {
		Random rd = new Random();
		for (int i = 0; i < nSolicitudes; i++) {
			int numero = rd.nextInt(100);
			Mensaje mensaje = new Mensaje();
			mensaje.setNumero(numero);

			while (!buffer.permisoEscribir()) {
				yield();
			}
			buffer.escribir(mensaje);

			try {
				synchronized (mensaje) {
					mensaje.wait();
					System.out.println(getName() + " mensaje " + numero + " - " + mensaje.getNumero());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.terminarCliente();
	}
}
