package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.TipoVehiculoEnum;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.AutorizacionExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.RegistroExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.TipoVehiculoExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Tiquete;
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

	public VigilanteService(TiqueteParqueoRepositorio tiqueteParqueoRepositorio, ParqueaderoService parqueaderoService,
			PropiedadesRepositorio propiedadesRepositorio) {
		this.tiqueteParqueoRepositorio = tiqueteParqueoRepositorio;
		this.parqueaderoService = parqueaderoService;
		this.propiedadesRepositorio = propiedadesRepositorio;
	}

	public TiqueteParqueo registrarIngreso(Vehiculo vehiculo, Calendar calendar) {
		validarDisponibilidadVehiculo(vehiculo);
		verificarPlacaRegistro(vehiculo.getPlaca());
		if ((!verificarPlaca(vehiculo.getPlaca())) || ingresoPlacaDiasNoAutorizados(vehiculo, calendar)) {
			return registrar(vehiculo);
		}
		return null;

	}

	public boolean ingresoPlacaDiasNoAutorizados(Vehiculo vehiculo, Calendar calendar) {
		if ((verificarPlaca(vehiculo.getPlaca()) && verificarDiaSemana(calendar))) {
			return true;
		} else if (!(verificarPlaca(vehiculo.getPlaca()) && verificarDiaSemana(calendar))) {
			throw new AutorizacionExcepcion("No esta autorizado");
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
	public void validarDisponibilidadVehiculo(Vehiculo vehiculo) {
		if ((isCarro(vehiculo.getTipoVehiculo())
				&& !parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio))
				|| (isMoto(vehiculo.getTipoVehiculo())
						&& !parqueaderoService.verificarDisponibilidadMoto(tiqueteParqueoRepositorio))) {
			throw new DisponibilidadExcepcion("No hay cupo en el parqueadero para el tipo de vehiculo");
		} else if (!isCarro(vehiculo.getTipoVehiculo()) && !isMoto(vehiculo.getTipoVehiculo())) {
			throw new TipoVehiculoExcepcion("Tipo de vehiculo no permitido");
		}
	}

	/**
	 * metodo para registrar el ingreso del vehiculo al parqueadero
	 * 
	 * @param vehiculo
	 * @return
	 */
	public TiqueteParqueo registrar(Vehiculo vehiculo) {
		TiqueteParqueo tiqueteParqueo = new TiqueteParqueo(vehiculo.getPlaca(), vehiculo.getTipoVehiculo(),
				vehiculo.getCilindraje(), new Date(), null, 0);
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

	public TiqueteParqueo calcularValorParqueadero(TiqueteParqueo tiquete) {

		int costoParqueadero = 0;
		if (null != tiquete) {
			int costoHoraMoto = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOHORAMOTO.getNombrePropiedad()));
			int costoHoraCarro = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOHORACARRO.getNombrePropiedad()));
			int costoDiaMoto = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTODIAMOTO.getNombrePropiedad()));
			int costoDiaCarro = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTODIACARRO.getNombrePropiedad()));
			int horaMinDia = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.HORASMINDIA.getNombrePropiedad()));
			int horaMaxDia = Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.HORASMAXDIA.getNombrePropiedad()));
			int minutosParqueadero = (int) ((new Date().getTime() - tiquete.getFechaEntrada().getTime()) / 1000);
			int horasParqueadero = minutosParqueadero / 3600;

			if (horasParqueadero == 0) {
				costoParqueadero = costoMenorUnaHora(tiquete, costoHoraCarro, costoHoraMoto);
			} else if (horasParqueadero < horaMinDia) {
				costoParqueadero = costoHorasParqueo(horasParqueadero, costoHoraCarro, costoHoraMoto, tiquete);
			} else if (horasParqueadero <= horaMaxDia) {
				costoUnDiaParqueo(costoDiaCarro, costoDiaMoto, tiquete);
			} else {
				costoParqueadero = calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
						costoHoraMoto, costoDiaMoto);
			}
		}
		return registrarSalidaVehiculo(tiquete, costoParqueadero);
	}

	public TiqueteParqueo obtenerVehiculoPorPlacaSalida(String placaVehiculo) {
		return tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(placaVehiculo);
	}

	public void verificarPlacaRegistro(String placaVehiculo) {
		if (null != tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(placaVehiculo)) {
			throw new RegistroExcepcion("El carro ya se encuentra en el parqueadero");
		}
	}

	public int costoHorasParqueo(int horasParqueadero, int costoHoraCarro, int costoHoraMoto, TiqueteParqueo tiquete) {
		int costoParqueadero = 0;
		if (isCarro(tiquete.getTipoVehiculo())) {
			costoParqueadero = horasParqueadero * costoHoraCarro;
		} else if (isMoto(tiquete.getTipoVehiculo())) {
			costoParqueadero = horasParqueadero * costoHoraMoto;
			costoParqueadero += obtenerCostoAdCilindraje(tiquete);
		}
		return costoParqueadero;
	}

	public int costoUnDiaParqueo(int costoDiaCarro, int costoDiaMoto, TiqueteParqueo tiquete) {
		int costoParqueadero = 0;
		if (isCarro(tiquete.getTipoVehiculo())) {
			costoParqueadero = costoDiaCarro;
		} else if (isMoto(tiquete.getTipoVehiculo())) {
			costoParqueadero = costoDiaMoto;
			costoParqueadero += obtenerCostoAdCilindraje(tiquete);
		}
		return costoParqueadero;
	}

	public int obtenerCostoAdCilindraje(TiqueteParqueo tiquete) {
		int cilindraje = Integer.parseInt(propiedadesRepositorio
				.obtenerValorPropiedad(PropiedadesParqueadero.CILINDRAJECOSTOADICIONAL.getNombrePropiedad()));

		if (tiquete.getCilindrajeVehiculo() > cilindraje) {
			return Integer.parseInt(propiedadesRepositorio
					.obtenerValorPropiedad(PropiedadesParqueadero.COSTOADICIONALCILINDRAJE.getNombrePropiedad()));
		}
		return 0;
	}

	public int calculoCostoEnDias(int horasParqueadero, TiqueteParqueo tiquete, int costoHoraCarro, int costoDiaCarro,
			int costoHoraMoto, int costoDiaMoto) {
		int dias = horasParqueadero / 24;
		int horas = horasParqueadero % 24;
		int costoHoras = 0;
		int costoParqueadero = 0;
		if (isCarro(tiquete.getTipoVehiculo())) {
			if (horas > 0 && horas < 9) {
				costoHoras = horas * costoHoraCarro;
			} else {
				costoHoras = costoDiaCarro;
			}
			costoParqueadero = (dias * costoDiaCarro) + costoHoras;
		} else if (isMoto(tiquete.getTipoVehiculo())) {
			if (horas > 0 && horas < 9) {
				costoHoras = horas * costoHoraMoto;
			} else {
				costoHoras = costoDiaMoto;
			}
			costoParqueadero = (dias * costoDiaMoto) + costoHoras;
			costoParqueadero += obtenerCostoAdCilindraje(tiquete);
		}
		return costoParqueadero;
	}

	public TiqueteParqueo registrarSalidaVehiculo(TiqueteParqueo tiquete, int costoParqueo) {
		tiquete.setFechaSalida(new Date());
		tiquete.setCostoParqueo(costoParqueo);
		tiqueteParqueoRepositorio.save(tiquete);
		return tiquete;
	}

	public int costoMenorUnaHora(TiqueteParqueo tiquete, int costoHoraCarro, int costoHoraMoto) {
		int costoParqueadero = 0;
		if (isCarro(tiquete.getTipoVehiculo())) {
			costoParqueadero = costoHoraCarro;
		} else if (isMoto(tiquete.getTipoVehiculo())) {
			costoParqueadero = costoHoraMoto;
			costoParqueadero += obtenerCostoAdCilindraje(tiquete);
		}
		return costoParqueadero;
	}

	public List<Tiquete> vehiculosParqueados() {
		List<TiqueteParqueo> listaVehiculos = tiqueteParqueoRepositorio.vehiculosParqueados();
		List<Tiquete> vehiculos = new ArrayList<>();
		for (TiqueteParqueo tiqueteParqueadero : listaVehiculos) {
			Tiquete tiquete = new Tiquete(tiqueteParqueadero.getPlacaVehiculo(), tiqueteParqueadero.getTipoVehiculo(),
					tiqueteParqueadero.getFechaEntrada());
			vehiculos.add(tiquete);
		}
		return vehiculos;
	}

}
