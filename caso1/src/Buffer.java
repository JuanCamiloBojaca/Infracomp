import java.lang.reflect.Array;

public class Buffer {
	private Queue<Mensaje> queue;
	private volatile int n;
	private Object N = new Object();
	private final int LIMIT;
	private Object esc = new Object();
	private Object ret = new Object();

	public Buffer(int size, int clientes) {
		queue = new Queue<>(size, Mensaje.class);
		LIMIT = size;
		n = 0;
	}

	public void escribir(Mensaje mensaje) {
		synchronized (esc) {
			boolean a;
			synchronized (N) {
				a = n == LIMIT;
			}
			while (a) {
				Thread.yield();
				synchronized (N) {
					a = n == LIMIT;
				}
			}
			queue.add(mensaje);
			synchronized (N) {
				n++;
			}
		}
	}

	public Mensaje retirar() {
		synchronized (ret) {
			boolean a;
			synchronized (N) {
				a = n == 0;
			}
			while (a) {
				Thread.yield();
				synchronized (N) {
					a = n == 0;
				}
			}
			synchronized (N) {
				n--;
			}
			return queue.take();
		}

	}

	public class Queue<T> {
		private final int mod;
		private volatile int escritura;
		private volatile int lectura;
		private T[] array;

		@SuppressWarnings("unchecked")
		public Queue(int size, Class<T> clazz) {
			array = (T[]) Array.newInstance(clazz, size + 1);
			mod = size + 1;
			escritura = 0;
			lectura = 0;
		}

		public synchronized boolean isEmpty() {
			return escritura == lectura;
		}

		public synchronized boolean isFull() {
			return (escritura + 1) % mod == lectura;
		}

		public synchronized void add(T t) {
			array[escritura] = t;
			escritura = (escritura + 1) % mod;
		}

		public synchronized T take() {
			T t = array[lectura];
			lectura = (lectura + 1) % mod;
			return t;
		}
	}
}
