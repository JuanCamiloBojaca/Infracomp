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

	public boolean termino() {
		return fin;
	}
}
