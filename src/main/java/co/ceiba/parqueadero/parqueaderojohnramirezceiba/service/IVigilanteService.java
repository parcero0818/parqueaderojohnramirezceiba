package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Calendar;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;

public interface IVigilanteService {

	public boolean registrarIngreso(Vehiculo vehiculo, Calendar calendar);

	public boolean verificarDiaSemana(Calendar calendar);

	public boolean verificarPlaca(String placa);

}
