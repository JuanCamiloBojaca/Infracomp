package carga;

public class CaseDescription {
	private int numberOfTask;
	private int gapBetweenTask;
	private int numberOfTest;
	private String Name;

	public CaseDescription(int numberOfTask, int gapBetweenTask, int numberOfTest, String name) {
		this.numberOfTask = numberOfTask;
		this.gapBetweenTask = gapBetweenTask;
		this.numberOfTest = numberOfTest;
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public int getGapBetweenTask() {
		return gapBetweenTask;
	}

	public int getNumberOfTask() {
		return numberOfTask;
	}

	public int getNumberOfTest() {
		return numberOfTest;
	}
}