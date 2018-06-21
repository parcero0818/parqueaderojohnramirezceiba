package co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo;

import java.util.Date;

public class Tiquete {

	String placaVehiculo;
	String tipoVehiculo;
	Date fechaEntrada;

	public Tiquete(String placaVehiculo, String tipoVehiculo, Date fechaEntrada) {
		super();
		this.placaVehiculo = placaVehiculo;
		this.tipoVehiculo = tipoVehiculo;
		this.fechaEntrada = fechaEntrada;
	}

}