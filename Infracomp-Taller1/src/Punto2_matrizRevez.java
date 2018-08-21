public class Punto2_matrizRevez extends Thread {
	private static int[][] matriz;
	private static int maxTotal = 0;

	private int id;
	private volatile boolean fin;

	public Punto2_matrizRevez(int id) {
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

		long ini = System.currentTimeMillis();

		Punto2_matrizRevez[] threads = new Punto2_matrizRevez[numeroFilas];
		for (int a = 0; a < numeroFilas; a++)
			(threads[a] = new Punto2_matrizRevez(a)).start();

		for (Punto2_matrizRevez sum : threads)
			while (!sum.termino())
				;

		System.out.println(System.currentTimeMillis() - ini);

		System.out.println("el maximo es: " + maxTotal);
		System.out.println("La respuesta debe ser: " + (numeroFilas * numeroFilas - 1));
	}

	private static void crearMatriz(int n) {
		matriz = new int[n][n];
		int valor = 0;

		for (int i = n - 1; i >= 0; i--) {
			for (int j = n - 1; j >= 0; j--) {
				matriz[i][j] = valor;
				valor++;
			}
		}
	}
}
