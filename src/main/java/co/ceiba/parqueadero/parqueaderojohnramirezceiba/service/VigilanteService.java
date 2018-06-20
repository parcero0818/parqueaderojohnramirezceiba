package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.TipoVehiculoEnum;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.AutorizacionExcepcion;
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
					|| ingresoPlacaDiasNoAutorizados(vehiculo, calendar)) {
				return registrar(vehiculo);
			}
		} catch (DisponibilidadExcepcion e) {
			throw new DisponibilidadExcepcion("No hay cupo");
		} catch (AutorizacionExcepcion e) {
			throw new AutorizacionExcepcion("No esta autorizado para ingresar");
		}
		return null;

	}

	public boolean ingresoPlacaDiasNoAutorizados(Vehiculo vehiculo, Calendar calendar) {
		if ((validarDisponibilidadVehiculo(vehiculo)
				&& (verificarPlaca(vehiculo.getPlaca()) && verificarDiaSemana(calendar)))) {
			return true;
		} else if (validarDisponibilidadVehiculo(vehiculo)
				&& !(verificarPlaca(vehiculo.getPlaca()) && verificarDiaSemana(calendar))) {
			throw new AutorizacionExcepcion("");
		}
		return false;
	}

	/**
	 * Se valida la disponibilidad del parqueadero para el tipo de vehiculo que se
	 * va a registrar
	 * 
	 * @param vehiculo
	 * @return
	 */
	public boolean validarDisponibilidadVehiculo(Vehiculo vehiculo) {
		if ((isCarro(vehiculo.getTipoVehiculo())
				&& !parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio))
				|| (isMoto(vehiculo.getTipoVehiculo())
						&& !parqueaderoService.verificarDisponibilidadMoto(tiqueteParqueoRepositorio))) {
			throw new DisponibilidadExcepcion("No hay cupo en el parqueadero para el tipo de vehiculo");
		} else if (!isCarro(vehiculo.getTipoVehiculo()) && !isMoto(vehiculo.getTipoVehiculo())) {
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
	public boolean isCarro(String tipoVehiculo) {
		return tipoVehiculo.equalsIgnoreCase(TipoVehiculoEnum.CARRO.getTipoVehiculo());
	}

	/**
	 * metodo que indica si el vehiculo que quiere entrar al parqueadero es una moto
	 * 
	 * @param vehiculo
	 * @return
	 */
	public boolean isMoto(String tipoVehiculo) {
		return tipoVehiculo.equalsIgnoreCase(TipoVehiculoEnum.MOTO.getTipoVehiculo());
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

	public double calcularValorParqueadero(String placaVehiculo) {
		TiqueteParqueo tiquete = tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(placaVehiculo);
		if (null != tiquete) {
			double costoHoraMoto = Double.parseDouble(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOHORAMOTO.getNombrePropiedad()));
			double costoHoraCarro = Double.parseDouble(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOHORACARRO.getNombrePropiedad()));
			double costoDiaMoto = Double.parseDouble(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTODIAMOTO.getNombrePropiedad()));
			double costoDiaCarro = Double.parseDouble(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTODIACARRO.getNombrePropiedad()));
			int horaMinDia = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.HORASMINDIA.getNombrePropiedad()));
			int horaMaxDia = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.HORASMAXDIA.getNombrePropiedad()));
			int minutosParqueadero = (int) ((new Date().getTime() - tiquete.getFechaEntrada().getTime()) / 1000);
			int horasParqueadero = minutosParqueadero / 3600;
			double costoParqueadero = 0;
			if (horasParqueadero < horaMinDia) {
				if (isCarro(tiquete.getTipoVehiculo())) {
					costoParqueadero = horasParqueadero * costoHoraCarro;
					return costoParqueadero;
				} else if (isMoto(tiquete.getTipoVehiculo())) {
					costoParqueadero = horasParqueadero * costoHoraMoto;
					return costoParqueadero + obtenerCostoAdCilindraje(tiquete);
				}
			} else if (horasParqueadero <= horaMaxDia) {
				if (isCarro(tiquete.getTipoVehiculo())) {
					costoParqueadero = horasParqueadero * costoDiaCarro;
					return costoParqueadero;
				} else if (isMoto(tiquete.getTipoVehiculo())) {
					costoParqueadero = horasParqueadero * costoDiaMoto;
					return costoParqueadero + obtenerCostoAdCilindraje(tiquete);
				}
			} else {
				return calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro, costoHoraMoto,
						costoDiaMoto);
			}
		}
		return 0;
	}

	public double obtenerCostoAdCilindraje(TiqueteParqueo tiquete) {
		int cilindraje = Integer.parseInt(propiedadesRepositorio
				.obtenerValorPropiedad(PropiedadesParqueadero.CILINDRAJECOSTOADICIONAL.getNombrePropiedad()));

		if (tiquete.getCilindrajeVehiculo() > cilindraje) {
			return Double.parseDouble(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOADICIONALCILINDRAJE.getNombrePropiedad()));
		}
		return 0;
	}

	public double calculoCostoEnDias(int horasParqueadero, TiqueteParqueo tiquete, double costoHoraCarro,
			double costoDiaCarro, double costoHoraMoto, double costoDiaMoto) {
		int dias = horasParqueadero / 24;
		int horas = horasParqueadero % 24;
		double costoHoras = 0;
		double costoParqueadero = 0;
		if (isCarro(tiquete.getTipoVehiculo())) {
			if (horas > 0) {
				costoHoras = horas * costoHoraCarro;
			}
			costoParqueadero = (dias * costoDiaCarro) + costoHoras;
			return costoParqueadero;
		} else if (isMoto(tiquete.getTipoVehiculo())) {
			if (horas > 0) {
				costoHoras = horas * costoHoraMoto;
			}
			costoParqueadero = (dias * costoDiaMoto) + costoHoras;
			return costoParqueadero + obtenerCostoAdCilindraje(tiquete);
		}

		return costoParqueadero;
	}

}
