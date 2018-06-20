package co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo;

import java.util.Date;

public class Tiquete {

	private String placaVehiculo;
	private String tipoVehiculo;
	private Date fechaEntrada;

	public Tiquete(String placaVehiculo, String tipoVehiculo, Date fechaEntrada) {
		super();
		this.placaVehiculo = placaVehiculo;
		this.tipoVehiculo = tipoVehiculo;
		this.fechaEntrada = fechaEntrada;
	}

	public String getPlacaVehiculo() {
		return placaVehiculo;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

}
