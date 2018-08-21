
public class Punto2_RepartidorId extends Thread {
	private static int[][] matriz;
	private static int maxTotal = 0;

	private IDs id;
	private volatile boolean fin;

	public Punto2_RepartidorId(IDs id) {
		this.id = id;
		this.fin = false;
	}

	@Override
	public void run() {
		int fil = id.getId();
		int max = 0;
		while (fil != -1) {
			int[] fila = matriz[fil];
			for (int i = 0; i < fila.length; i++) {
				if (fila[i] > max)
					max = fila[i];
			}

			max(max);
			fil = id.getId();
		}
		fin = true;
	}

	public static void max(int val) {
		if (val > maxTotal)
			maxTotal = val;
	}

	public boolean termino() {
		return fin;
	}

	public static void main(String[] args) {
		int numeroFilas = 1000 * 10;
		crearMatriz(numeroFilas);

		long ini = System.currentTimeMillis();
		IDs ids = new IDs(numeroFilas);

		int nprocesos = Runtime.getRuntime().availableProcessors();
		Punto2_RepartidorId[] threads = new Punto2_RepartidorId[nprocesos];
		for (int a = 0; a < nprocesos; a++)
			(threads[a] = new Punto2_RepartidorId(ids)).start();

		for (Punto2_RepartidorId sum : threads) {
			while (!sum.termino()) {
				// sdf
			}
		}

		System.out.println(System.currentTimeMillis() - ini);

		System.out.println("el maximo es: " + maxTotal);
		System.out.println("La respuesta debe ser: " + (numeroFilas * numeroFilas - 1));
	}

	private static void crearMatriz(int n) {
		matriz = new int[n][n];
		int valor = 0;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matriz[i][j] = valor;
				valor++;
			}
		}
	}

	static class IDs {
		private int i = 0;
		private final int filas;

		public IDs(int filas) {
			this.filas = filas;
		}

		public synchronized int getId() {
			if (i < filas)
				return i++;
			return -1;
		}
	}
}
