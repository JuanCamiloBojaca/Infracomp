package logic;

import java.io.PrintStream;
import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class CpuLog extends Thread {
	private Boolean fin = false;
	private int delay;
	private PrintStream file;

	public CpuLog(int delay, PrintStream file) {
		this.file = file;
		this.delay = delay;
	}

	@Override
	public void run() {
		try {
			synchronized (fin) {
				while (!fin) {
					file.println(getSystemCpuLoad());
					sleep(delay);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void fin() {
		fin = true;
	}

	public static double getSystemCpuLoad() throws Exception {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		AttributeList list = mbs.getAttributes(name, new String[] { "SystemCpuLoad" });
		if (list.isEmpty())
			return Double.NaN;
		Attribute att = (Attribute) list.get(0);
		Double value = (Double) att.getValue();
		// usually takes a couple of seconds before we get real values
		if (value == -1.0)
			return Double.NaN;
		// returns a percentage value with 1 decimal point precision
		return ((int) (value * 1000) / 10.0);
	}

}
