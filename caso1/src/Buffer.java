public class Buffer {
	private Mensaje[] colaMensajes;
	public volatile int lecturasDisponibles;
	public volatile int escriturasDisponibles;
	public volatile int clientesActivos;
	private int mod, escritura, lectura;

	public Buffer(int size, int clientes) {
		escriturasDisponibles = size;
		lecturasDisponibles = 0;
		clientesActivos = clientes;
		colaMensajes = new Mensaje[size + 1];
		mod = size + 1;
		escritura = 0;
		lectura = 0;
	}

	public void escribir(Mensaje mensaje) {
		colaMensajes[escritura] = mensaje;
		escritura = (escritura + 1) % mod;
	}

	public Mensaje retirar() {
		Mensaje ans = colaMensajes[lectura];
		lectura = (lectura + 1) % mod;
		return ans;
	}
}
