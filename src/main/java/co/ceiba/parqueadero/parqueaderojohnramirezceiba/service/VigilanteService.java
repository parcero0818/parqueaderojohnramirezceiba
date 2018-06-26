package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.TipoVehiculoEnum;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.AutorizacionExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.NoEncontradoExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.RegistroExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.TipoVehiculoExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Tiquete;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.PropiedadesRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.TiqueteParqueoRepositorio;
import co.ceiba.soaptrm.consumotrm.impl.Itrm;
import co.ceiba.soaptrm.consumotrm.impl.trmImpl;
import co.com.sc.nexura.superfinanciera.action.generic.services.trm.action.TcrmResponse;

@Controller
public class VigilanteService implements IVigilanteService {
	@Autowired
	TiqueteParqueoRepositorio tiqueteParqueoRepositorio;
	@Autowired
	ParqueaderoService parqueaderoService;
	@Autowired
	PropiedadesRepositorio propiedadesRepositorio;

	private final Logger logger = Logger.getLogger(VigilanteService.class.getName());

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
			throw new AutorizacionExcepcion("No esta autorizado para ingresar");
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
				costoParqueadero = costoUnDiaParqueo(costoDiaCarro, costoDiaMoto, tiquete);
			} else {
				costoParqueadero = calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
						costoHoraMoto, costoDiaMoto);
			}
		}
		return registrarSalidaVehiculo(tiquete, costoParqueadero);
	}

	public TiqueteParqueo obtenerVehiculoPorPlacaSalida(String placaVehiculo) {
		if (null == tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(placaVehiculo)) {
			throw new NoEncontradoExcepcion("El vehiculo no se encuentra en el parqueadero");
		}
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
			Tiquete tiquete = new Tiquete(tiqueteParqueadero.getPlacaVehiculo().toUpperCase(),
					tiqueteParqueadero.getTipoVehiculo().toLowerCase(), tiqueteParqueadero.getFechaEntrada());
			vehiculos.add(tiquete);
		}
		return vehiculos;
	}

	public Float obtenerTrm() {
		XMLGregorianCalendar xmlGregorianCalendar = null;
		GregorianCalendar gregorianCalendar = new GregorianCalendar();

		try {
			gregorianCalendar.setTime(new Date());
			DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
			xmlGregorianCalendar = dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			logger.info("Excepcion " + e.getMessage());
		}
		Itrm trm = new trmImpl(
				"https://www.superfinanciera.gov.co/SuperfinancieraWebServiceTRM/TCRMServicesWebService/TCRMServicesWebService");
		TcrmResponse r = trm.queryTCRM(xmlGregorianCalendar);
		return r.getValue();
	}

}
