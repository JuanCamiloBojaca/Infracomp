
public class T {


	private static int[][] matriz;
	private static int total = 0;

	private int id;
	private int suma;
	public boolean fin;

	
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

	public T (int pId) {
		id = pId;
		suma = 0;
		fin = false;
	}
	public static void main(String[] args) {

		int i;
		int numeroFilas = 10;			//Número de filas de la matriz
		T[] t = new T[numeroFilas];		//Arreglo de threads

		crearMatriz(numeroFilas);		// Inicializa la matriz
		// Complete aquí el código para crear y activar los threads.
	
		for(int a=0; a<numeroFilas;a++){
			new T(i).start();
		}
		
		
		System.out.println("La suma es: " + total);
	}
}
