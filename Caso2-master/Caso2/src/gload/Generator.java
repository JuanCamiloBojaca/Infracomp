package gload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import entrega2.Cliente;
import entrega2.Config;
import uniandes.gload.core.LoadGenerator;
import uniandes.gload.core.Task;

public class Generator {
	private LoadGenerator generator;
	private Config config = new Config();
	private int numberOfTask;
	private int gapBetweenTask;
	private int numberOfTest;

	public Generator(List<CaseDescription> cases) throws Exception {
		firstClient();

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh-mm");
		String path = "./Report/" + format.format(new Date());
		(new File(path)).mkdirs();

		for (CaseDescription des : cases) {
			numberOfTask = des.getNumberOfTask();
			numberOfTest = des.getNumberOfTest();
			gapBetweenTask = des.getGapBetweenTask();

			String pathCase = path + "/" + des.getName();
			(new File(pathCase)).mkdirs();
			runCase(pathCase);
		}
	}

	private void runCase(String basePath) throws FileNotFoundException, IOException, InterruptedException {
		String verification = basePath + "/verificacion.txt";
		String consulta = basePath + "/consula.txt";
		String otros = basePath + "/otros.txt";

		try (PrintStream ver = new PrintStream(getFile(verification));
				PrintStream con = new PrintStream(getFile(consulta));
				PrintStream otr = new PrintStream(getFile(otros))) {

			Task work = createTask(config, ver, con);
			generator = new LoadGenerator(basePath, numberOfTask, work, gapBetweenTask);

			for (int i = 0; i < numberOfTest; i++) {
				System.out.println("-----------CASO " + (i + 1) + "-----------");
				otr.println("-----------CASO " + (i + 1) + "-----------");
				otr.println(System.currentTimeMillis());
				con.println("-----------CASO " + (i + 1) + "-----------");
				ver.println("-----------CASO " + (i + 1) + "-----------");
				runTest(ver, con, otr);
				System.out.println("----------------------------");
				Thread.sleep(3000);
			}

			con.flush();
			otr.flush();
			ver.flush();
		}
	}

	private void firstClient() throws Exception {
		Cliente cliente = new Cliente(config, System.out, System.out, System.out);
		cliente.protocolo((int) Math.random() * 100);
	}

	private void runTest(PrintStream ver, PrintStream con, PrintStream otr) throws FileNotFoundException, IOException {
		ClientTask.clear();

		generator.generate();

		while (ClientTask.numberOfClients() < numberOfTask) {
			Thread.yield();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		otr.println(ClientTask.bien + ";" + ClientTask.mal);
	}

	private Task createTask(Config config, PrintStream streamVerificacion, PrintStream streamConsulta) {
		return new ClientTask(config, streamVerificacion, streamConsulta);
	}

	public static File getFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists())
			file.createNewFile();
		return file;
	}

	public static void main(String[] args) {
		try {
			List<CaseDescription> set = new LinkedList<>();
			set.add(new CaseDescription(20, 100, 10, "20_100"));
			set.add(new CaseDescription(10, 100, 10, "10_100"));
			set.add(new CaseDescription(5, 100, 10, "5_100"));
			new Generator(set);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
