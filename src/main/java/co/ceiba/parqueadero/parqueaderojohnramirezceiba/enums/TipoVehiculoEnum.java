package co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums;

public enum TipoVehiculoEnum {

	CARRO("Carro"), MOTO("Moto");

	private String tipoVehiculo;

	private TipoVehiculoEnum(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

}
