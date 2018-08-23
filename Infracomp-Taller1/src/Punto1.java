/**
 * 
 * @author jc.bojaca
 *
 */
public class Punto1 extends Thread {
	private static int[][] matriz;
	private static int total = 0;

	private int id;
	private int suma;
	private volatile boolean fin;

	public Punto1(int id) {
		this.id = id;
		this.suma = 0;
		this.fin = false;
	}

	@Override
	public void run() {
		int[] fila = matriz[id];
		for (int i = 0; i < fila.length; i++)
			suma += fila[i];
		fin = true;
	}

	public int getSuma() {
		return suma;
	}

	public boolean termino() {
		return fin;
	}

	public static void main(String[] args) {
		int numeroFilas = 100;
		crearMatriz(numeroFilas);

		Punto1[] threads = new Punto1[numeroFilas];
		for (int a = 0; a < numeroFilas; a++)
			(threads[a] = new Punto1(a)).start();

		for (Punto1 sum : threads)
			while (!sum.termino())
				;

		for (Punto1 sum : threads)
			total += sum.getSuma();

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
