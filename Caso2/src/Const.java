public enum Const {
	hola("HOLA"), algoritmos("ALGORITMOS"), ok("OK"), error("ERROR"), sep(":");

	private String value;

	private Const(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}