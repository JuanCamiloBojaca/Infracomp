package caso1;

import java.util.Random;

public class Cliente extends Thread {
	private int nSolicitudes;

	public Cliente(int nSolicitudes, int i) {
		this.nSolicitudes = nSolicitudes;
		setName("cliente_" + i);
	}

	@Override
	public void run() {
		Random rd = new Random();
		for (int i = 0; i < nSolicitudes; i++) {
			int numero = rd.nextInt(100);
			Mensaje mensaje = new Mensaje();
			mensaje.setNumero(numero);

			while (!Buffer.permisoEscribir()) {
				yield();
			}
			Buffer.escribir(mensaje);

			try {
				synchronized (mensaje) {
					mensaje.wait();
					System.out.println(getName() + " mensaje " + numero + " - " + mensaje.getNumero());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
