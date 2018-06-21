package co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "propiedades")
public class Propiedades {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String nombrePropiedad;
	String valorPropiedad;

}
