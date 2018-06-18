package co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "tiqueteParqueo")
public class TiqueteParqueo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String placaVehiculo;
	private String tipoVehiculo;
	private int cilindrajeVehiculo;
	private Date fechaEntrada;
	private Date fechaSalida;
	private int costoParqueo;

	public TiqueteParqueo() {

	}

	public TiqueteParqueo(String placaVehiculo, String tipoVehiculo, int cilindrajeVehiculo, Date fechaEntrada,
			Date fechaSalida, int costoParqueo) {
		super();
		this.placaVehiculo = placaVehiculo;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindrajeVehiculo = cilindrajeVehiculo;
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.costoParqueo = costoParqueo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getCilindrajeVehiculo() {
		return cilindrajeVehiculo;
	}

	public void setCilindrajeVehiculo(int cilindrajeVehiculo) {
		this.cilindrajeVehiculo = cilindrajeVehiculo;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public void setFechaEntrada(Date fechaEntrada) {
		this.fechaEntrada = fechaEntrada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	public int getCostoParqueo() {
		return costoParqueo;
	}

	public void setCostoParqueo(int costoParqueo) {
		this.costoParqueo = costoParqueo;
	}

}
