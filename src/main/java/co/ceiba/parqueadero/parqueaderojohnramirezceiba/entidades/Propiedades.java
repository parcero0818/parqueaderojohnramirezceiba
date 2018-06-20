package co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades;

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

	public String getNombrePropiedad() {
		return nombrePropiedad;
	}

	public String getValorPropiedad() {
		return valorPropiedad;
	}

}
