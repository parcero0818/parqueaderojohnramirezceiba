package co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums;

public enum PropiedadesParqueadero {

	COSTOHORACARRO("valorHoraCarro"),
	COSTOHORAMOTO("valorHoraMoto"),
	COSTODIACARRO("valorDiaCarro"),
	COSTODIAMOTO("valorDiaMoto"),
	CANTCARROSPERMITIDOS("cantidadCarros"),
	CANTMOTOSPERMITIDOS("cantidadMotos"),
	PLACASPERMITIDAS("placas");

	private String nombrePropiedad;

	private PropiedadesParqueadero(String nombrePropiead) {
		this.nombrePropiedad = nombrePropiead;
	}

	public String getNombrePropiedad() {
		return nombrePropiedad;
	}
	
}
