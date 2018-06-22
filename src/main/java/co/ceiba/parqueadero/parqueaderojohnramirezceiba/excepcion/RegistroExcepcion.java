package co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion;

public class RegistroExcepcion extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegistroExcepcion(String message) {
		super(message);
	}
}
