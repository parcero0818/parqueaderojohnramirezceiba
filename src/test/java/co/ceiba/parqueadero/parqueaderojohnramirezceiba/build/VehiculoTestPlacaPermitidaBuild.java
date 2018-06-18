package co.ceiba.parqueadero.parqueaderojohnramirezceiba.build;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;

public class VehiculoTestPlacaPermitidaBuild {

	private String placa;
	private int cilindraje;
	private String tipoVehiculo;

	public VehiculoTestPlacaPermitidaBuild() {
		this.placa = "PZV-283";
		this.cilindraje = 220;
		this.tipoVehiculo = "Carro";
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

	public void setCilindraje(int cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Vehiculo build() {
		return new Vehiculo(placa, cilindraje, tipoVehiculo);
	}

}
