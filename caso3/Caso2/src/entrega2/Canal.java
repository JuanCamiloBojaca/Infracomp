package entrega2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Canal implements AutoCloseable {
	private PrintStream out;
	private BufferedReader in;

	public Canal(Socket socket) throws IOException {
		out = new PrintStream(socket.getOutputStream());
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void send(CharSequence charSequence) {
		out.println(charSequence);
	}

	public void send(Const cons) {
		out.println(cons.getValue());
	}

	public CharSequence recive() throws IOException {
		return in.readLine();
	}

	@Override
	public void close() throws Exception {
		out.close();
		in.close();
	}
}
