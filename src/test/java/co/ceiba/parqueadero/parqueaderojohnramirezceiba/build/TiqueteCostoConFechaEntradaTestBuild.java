package co.ceiba.parqueadero.parqueaderojohnramirezceiba.build;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;

public class TiqueteCostoConFechaEntradaTestBuild {

	private String placaVehiculo;
	private String tipoVehiculo;
	private int cilindrajeVehiculo;
	private Date fechaEntrada;

	
	public TiqueteCostoConFechaEntradaTestBuild() throws ParseException {
		this.placaVehiculo = "PZV-283";
		this.tipoVehiculo = "carro";
		this.cilindrajeVehiculo = 200;
		DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fecha = "2018-06-21 13:35:51";
		this.fechaEntrada = formatoFecha.parse(fecha);
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
