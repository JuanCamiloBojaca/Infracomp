
public class Punto2 extends Thread {
	private static int[][] matriz;
	private static int maxTotal = 0;

	private int id;
	private boolean fin;

	public Punto2(int id) {
		this.id = id;
		this.fin = false;
	}

	@Override
	public void run() {
		int max = 0;
		int[] fila = matriz[id];
		for (int i = 0; i < fila.length; i++) {
			if (fila[i] > max)
				max = fila[i];
		}
		max(max);
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
		int numeroFilas = 100;
		crearMatriz(numeroFilas);

		Punto2[] threads = new Punto2[numeroFilas];
		for (int a = 0; a < numeroFilas; a++)
			(threads[a] = new Punto2(a)).start();

		for (Punto2 sum : threads)
			while (!sum.termino())
				;

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
}
