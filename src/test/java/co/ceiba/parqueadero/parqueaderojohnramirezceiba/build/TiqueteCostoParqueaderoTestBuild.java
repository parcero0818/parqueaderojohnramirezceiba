package co.ceiba.parqueadero.parqueaderojohnramirezceiba.build;

import java.util.Date;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;

public class TiqueteCostoParqueaderoTestBuild {

	private String placaVehiculo;
	private String tipoVehiculo;
	private int cilindrajeVehiculo;
	private Date fechaEntrada;

	
	public TiqueteCostoParqueaderoTestBuild() {
		this.placaVehiculo = "PZV-283";
		this.tipoVehiculo = "carro";
		this.cilindrajeVehiculo = 200;
		
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
	
	public TiqueteParqueo build() {
		return  new TiqueteParqueo(this.placaVehiculo, this.tipoVehiculo, this.cilindrajeVehiculo, this.fechaEntrada);
	}

}
