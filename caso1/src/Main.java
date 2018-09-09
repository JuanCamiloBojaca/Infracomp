import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		try (BufferedReader br = new BufferedReader(new FileReader("./data/Values.txt"))) {
			int bufferSize = Integer.parseInt(br.readLine().split(":")[1].trim());
			int serversSize = Integer.parseInt(br.readLine().split(":")[1].trim());
			Servidor[] servers = new Servidor[serversSize];
			int clientsSize = Integer.parseInt(br.readLine().split(":")[1].trim());
			Cliente[] clients = new Cliente[clientsSize];

			Buffer buffer = new Buffer(bufferSize, clientsSize);

			for (int i = 0; i < clientsSize; i++)
				(clients[i] = new Cliente(i + 1, Integer.parseInt(br.readLine()), buffer)).start();

			for (int i = 0; i < serversSize; i++)
				(servers[i] = new Servidor(i + 1, buffer)).start();
			
			for (Cliente cliente : clients)
				cliente.join();

			System.out.println("termiino el proceso de clientes");
			
			for (Servidor servidor : servers)
				servidor.join();

			System.out.println("asd");
			

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
