package Punto1;

public class Punto1 {
	private static int[][] matriz;
	private static int total = 0;

	public static int[][] getMatriz() {
		return matriz;
	}

	public static void main(String[] args) {
		int numeroFilas = 10;
		crearMatriz(numeroFilas);

		Sumador[] threads = new Sumador[numeroFilas];
		for (int a = 0; a < numeroFilas; a++)
			(threads[a] = new Sumador(a)).start();

		for (Sumador sum : threads) {
			while (!sum.termino())
				;
			total += sum.getSuma();
		}

		System.out.println("La suma es: " + total);
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
