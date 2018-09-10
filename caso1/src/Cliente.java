
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
		Mensaje mensaje;
		try {
			for (int i = 0; i < nSolicitudes; i++) {

				mensaje = new Mensaje(i + 1);
				buffer.escribir(mensaje);

				synchronized (mensaje) {
					mensaje.wait();
				}
				System.out.println(getName() + "(" + (i + 1) + "," + mensaje.getNumero() + ")");
			}

			System.out.println("fin " + getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
