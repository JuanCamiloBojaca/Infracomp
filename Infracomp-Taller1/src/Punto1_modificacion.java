
public class Punto1_modificacion extends Thread {
	private static int[][] matriz;
	private static int total = 0;

	private int id;
	private int suma;
	private volatile boolean fin;

	public Punto1_modificacion(int id) {
		this.id = id;
		this.suma = 0;
		this.fin = false;
	}

	@Override
	public void run() {
		int[] fila = matriz[id];
		for (int i = 0; i < fila.length; i++)
			suma += fila[i];

		add(suma);
		fin = true;
	}

	public boolean termino() {
		return fin;
	}

	public static synchronized void add(int suma) {
		total += suma;
	}

	public static void main(String[] args) {
		int numeroFilas = 100;
		crearMatriz(numeroFilas);

		Punto1_modificacion[] threads = new Punto1_modificacion[numeroFilas];
		for (int a = 0; a < numeroFilas; a++)
			(threads[a] = new Punto1_modificacion(a)).start();

		for (Punto1_modificacion sum : threads)
			while (!sum.termino())
				;

		System.out.println("La suma es: " + total);
		System.out.println(
				"La respuesta debe ser: " + ((numeroFilas * numeroFilas) * (numeroFilas * numeroFilas - 1)) / 2);
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
}
