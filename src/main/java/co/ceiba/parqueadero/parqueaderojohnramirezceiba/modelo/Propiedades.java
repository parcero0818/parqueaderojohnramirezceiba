package co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "propiedades")
public class Propiedades {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nombrePropiedad;
	private String valorPropiedad;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombrePropiedad() {
		return nombrePropiedad;
	}

	public void setNombrePropiedad(String nombrePropiedad) {
		this.nombrePropiedad = nombrePropiedad;
	}

	public String getValorPropiedad() {
		return valorPropiedad;
	}

	public void setValorPropiedad(String valorPropiedad) {
		this.valorPropiedad = valorPropiedad;
	}

}
