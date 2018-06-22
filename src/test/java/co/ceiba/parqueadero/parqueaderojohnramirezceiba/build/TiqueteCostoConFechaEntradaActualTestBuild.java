package co.ceiba.parqueadero.parqueaderojohnramirezceiba.build;

import java.util.Date;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;

public class TiqueteCostoConFechaEntradaActualTestBuild {

	private String placaVehiculo;
	private String tipoVehiculo;
	private int cilindrajeVehiculo;
	private Date fechaEntrada;

	public TiqueteCostoConFechaEntradaActualTestBuild() {
		this.placaVehiculo = "PZV-283";
		this.tipoVehiculo = "carro";
		this.cilindrajeVehiculo = 200;
		this.fechaEntrada = new Date();
	}

	public String getPlacaVehiculo() {
		return placaVehiculo;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public int getCilindrajeVehiculo() {
		return cilindrajeVehiculo;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public TiqueteParqueo build() {
		return new TiqueteParqueo(this.placaVehiculo, this.tipoVehiculo, this.cilindrajeVehiculo, this.fechaEntrada);
	}

}
