package co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo;

public class Vehiculo {

	private String placa;
	private int cilindraje;
	private String tipoVehiculo;
	
	public Vehiculo() {
	}

	public Vehiculo(String placa, int cilindraje, String tipoVehiculo) {
		super();
		this.placa = placa;
		this.cilindraje = cilindraje;
		this.tipoVehiculo = tipoVehiculo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public int getCilindraje() {
		return cilindraje;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

}
