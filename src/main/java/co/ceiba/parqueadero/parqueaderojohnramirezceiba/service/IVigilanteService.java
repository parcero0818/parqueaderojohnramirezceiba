package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Calendar;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;

public interface IVigilanteService {

	public TiqueteParqueo registrarIngreso(Vehiculo vehiculo, Calendar calendar);

	public TiqueteParqueo calcularValorParqueadero(String placaVehiculo);

}
