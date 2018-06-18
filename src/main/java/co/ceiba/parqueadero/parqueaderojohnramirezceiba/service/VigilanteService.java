package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.TipoVehiculoEnum;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.PropiedadesRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.TiqueteParqueoRepositorio;

@Controller
public class VigilanteService implements IVigilanteService {
	@Autowired
	TiqueteParqueoRepositorio tiqueteParqueoRepositorio;
	@Autowired
	ParqueaderoService parqueaderoService;
	@Autowired
	PropiedadesRepositorio propiedadesRepositorio;
	TiqueteParqueo tiqueteParqueo;

	public TiqueteParqueo registrarIngreso(Vehiculo vehiculo, Calendar calendar) {
		try {
			if ((validarDisponibilidadVehiculo(vehiculo) && !verificarPlaca(vehiculo.getPlaca()))
					|| (validarDisponibilidadVehiculo(vehiculo)
							&& (verificarPlaca(vehiculo.getPlaca()) && verificarDiaSemana(calendar)))) {
				return registrar(vehiculo);
			}
		} catch (DisponibilidadExcepcion e) {
			throw new DisponibilidadExcepcion("No hay cupo");
		}
		return null;

	}

	/**
	 * Se valida la disponibilidad del parqueadero para el tipo de vehiculo que se
	 * va a registrar
	 * 
	 * @param vehiculo
	 * @return
	 */
	public boolean validarDisponibilidadVehiculo(Vehiculo vehiculo) {
		if ((isCarro(vehiculo) && !parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio))
				|| (isMoto(vehiculo) && !parqueaderoService.verificarDisponibilidadMoto(tiqueteParqueoRepositorio))) {
			throw new DisponibilidadExcepcion("No hay cupo en el parqueadero para el tipo de vehiculo");
		}else if(!isCarro(vehiculo)  && !isMoto(vehiculo)) {
			return false;
		}
		return true;
	}

	/**
	 * metodo para registrar el ingreso del vehiculo al parqueadero
	 * 
	 * @param vehiculo
	 * @return
	 */
	public TiqueteParqueo registrar(Vehiculo vehiculo) {
		tiqueteParqueo = new TiqueteParqueo(vehiculo.getPlaca(), vehiculo.getTipoVehiculo(), vehiculo.getCilindraje(),
				new Date(), null, 0);
		return tiqueteParqueoRepositorio.save(tiqueteParqueo);
	}

	/**
	 * metodo que indica si el vehiculo que quiere ingresar al parqueadero es un
	 * carro
	 * 
	 * @param vehiculo
	 * @return
	 */
	public boolean isCarro(Vehiculo vehiculo) {
		return vehiculo.getTipoVehiculo().equalsIgnoreCase(TipoVehiculoEnum.CARRO.getTipoVehiculo());
	}

	/**
	 * metodo que indica si el vehiculo que quiere entrar al parqueadero es una moto
	 * 
	 * @param vehiculo
	 * @return
	 */
	public boolean isMoto(Vehiculo vehiculo) {
		return vehiculo.getTipoVehiculo().equalsIgnoreCase(TipoVehiculoEnum.MOTO.getTipoVehiculo());
	}

	/**
	 * metodo que india si el dia actual es un domingo o un lunes
	 */
	public boolean verificarDiaSemana(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}

	/**
	 * metodo para verificar si la placa el vehiculo puede ingresar o se debe
	 * validar el dia en el que puede ingresar
	 */
	public boolean verificarPlaca(String placa) {
		String placasBd = propiedadesRepositorio
				.obtenerValorPropiedad(PropiedadesParqueadero.PLACASPERMITIDAS.getNombrePropiedad());
		String[] placas = placasBd.toLowerCase().split(",");
		return Arrays.asList(placas).contains(placa.toLowerCase().substring(0, 1));
	}

}
