package Punto1;

public class Punto1 {
	private static int[][] matriz;
	private static int total = 0;

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

	
	
	public static void main(String[] args) {
		int i;
		int numeroFilas = 10; // Número de filas de la matriz
		Sumador[] t = new Sumador[numeroFilas]; // Arreglo de threads

		crearMatriz(numeroFilas); // Inicializa la matriz
		// Complete aquí el código para crear y activar los threads.

		for (int a = 0; a < numeroFilas; a++) {
			(t[a] = new Sumador(a)).start();
		}

		System.out.println("La suma es: " + total);
	}

}
