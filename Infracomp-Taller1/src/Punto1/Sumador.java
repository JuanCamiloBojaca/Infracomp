package Punto1;

public class Sumador extends Thread {
	private int id;
	private int suma;
	private boolean fin;

	public Sumador(int id) {
		this.id = id;
		this.suma = 0;
		this.fin = false;
	}

	@Override
	public void run() {
		int[] fila = Punto1.getMatriz()[id];
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
}
