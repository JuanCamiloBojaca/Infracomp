import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new FileReader("./data/Values.txt"))) {
			Buffer buffer = new Buffer(Integer.parseInt(br.readLine().split(":")[1].trim()));

			int serversSize = Integer.parseInt(br.readLine().split(":")[1].trim());
			Servidor[] servers = new Servidor[serversSize];
			for (int i = 0; i < serversSize; i++)
				servers[i] = new Servidor(i, buffer);
			
			int clientsSize = Integer.parseInt(br.readLine().split(":")[1].trim());
			Cliente[] clients = new Cliente[clientsSize];
			for (int i = 0; i < clientsSize; i++)
				(clients[i] = new Cliente(i, Integer.parseInt(br.readLine()), buffer)).start();

			for (Servidor servidor : servers)
				servidor.start();

			for (Cliente cliente : clients)
				cliente.join();

			for (Servidor servidor : servers)
				servidor.join();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
