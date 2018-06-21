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

	public String getPlacaVehiculo() {
		return placaVehiculo;
	}

	public void setPlacaVehiculo(String placaVehiculo) {
		this.placaVehiculo = placaVehiculo;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}
	
	

}