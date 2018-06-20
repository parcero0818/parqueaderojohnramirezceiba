package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Calendar;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;

public interface IVigilanteService {

	public TiqueteParqueo registrarIngreso(Vehiculo vehiculo, Calendar calendar);

	public double calcularValorParqueadero(String placaVehiculo);

}
