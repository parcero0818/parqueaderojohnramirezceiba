package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Calendar;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;

public interface IVigilanteService {

	public TiqueteParqueo registrarIngreso(Vehiculo vehiculo, Calendar calendar);

	public boolean verificarDiaSemana(Calendar calendar);

	public boolean verificarPlaca(String placa);

	public boolean validarDisponibilidadVehiculo(Vehiculo vehiculo);

	public TiqueteParqueo registrar(Vehiculo vehiculo);

}
