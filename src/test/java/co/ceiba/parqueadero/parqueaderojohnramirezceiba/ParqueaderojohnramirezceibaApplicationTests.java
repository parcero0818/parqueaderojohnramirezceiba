package co.ceiba.parqueadero.parqueaderojohnramirezceiba;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoConFechaEntradaActualTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoParqueaderoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestPlacaPermitidaBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestVehiculoNoPermitBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.AutorizacionExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.RegistroExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.TipoVehiculoExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Tiquete;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.PropiedadesRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.TiqueteParqueoRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.rest.ParqueaderoRest;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.service.ParqueaderoService;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.service.VigilanteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParqueaderojohnramirezceibaApplicationTests {
	@Autowired
	VigilanteService vigilanteService;
	@Autowired
	ParqueaderoService parqueaderoService;
	@Autowired
	PropiedadesRepositorio propiedadesRepositorio;

	@Autowired
	ParqueaderoRest parqueaderoRest;

	@Before
	public void setup() {
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void parqueaderoDisponibleCarros() {
		// Arrange
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		when(tiqueteParqueoRepositorio.cantidadCarrosParqueados()).thenReturn(19);
		// Act
		boolean disponible = parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio);
		// Assert
		Assert.assertTrue(disponible);
	}

	@Test
	public void parqueaderoNoDisponibleCarros() {
		// Arrange
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		when(tiqueteParqueoRepositorio.cantidadCarrosParqueados()).thenReturn(20);
		// Act
		try {
			parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio);

		} catch (DisponibilidadExcepcion e) {
			// Assert
			Assert.assertEquals("No hay cupo", e.getMessage());
		}
	}

	@Test
	public void parqueaderoDisponibleMotos() {
		// Arrange
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		when(tiqueteParqueoRepositorio.cantidadMotosParqueados()).thenReturn(9);
		// Act
		boolean disponible = parqueaderoService.verificarDisponibilidadMoto(tiqueteParqueoRepositorio);
		// Assert
		Assert.assertTrue(disponible);
	}

	@Test
	public void parqueaderoNoDisponibleMotos() {
		// Arrange
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		when(tiqueteParqueoRepositorio.cantidadMotosParqueados()).thenReturn(10);
		// Act
		try {
			parqueaderoService.verificarDisponibilidadMoto(tiqueteParqueoRepositorio);
		} catch (DisponibilidadExcepcion e) {
			// TODO: handle exception
			// Assert
			Assert.assertEquals("No hay cupo", e.getMessage());
		}
	}

	@Test
	public void placaPermitida() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestBuild().build();
		// Act
		boolean validarPlaca = vigilanteService.verificarPlaca(vehiculo.getPlaca());
		// Assert
		Assert.assertTrue(validarPlaca);
	}

	@Test
	public void placaNoPermitida() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		// Act
		boolean validarPlaca = vigilanteService.verificarPlaca(vehiculo.getPlaca());
		// Assert
		Assert.assertFalse(validarPlaca);
	}

	@Test
	public void verificarDomingo() {
		// Arrange
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		// Act
		boolean isDomingo = vigilanteService.verificarDiaSemana(calendar);
		// Assert
		Assert.assertTrue(isDomingo);
	}

	@Test
	public void verificarLunes() {
		// Arrange
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// Act
		boolean isLunes = vigilanteService.verificarDiaSemana(calendar);
		// Assert
		Assert.assertTrue(isLunes);
	}

	@Test
	public void verificarOtroDia() {
		// Arrange
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		// Act
		boolean noEsLunesNiDomingo = vigilanteService.verificarDiaSemana(calendar);
		// Assert
		Assert.assertFalse(noEsLunesNiDomingo);
	}

	@Test
	public void obtenerPropiedad() {
		// Arrange
		String nombrePropiedad = PropiedadesParqueadero.CANTCARROSPERMITIDOS.getNombrePropiedad();
		// Act
		String carros = parqueaderoService.obtenerValorPropiedad(nombrePropiedad);
		// Assert
		Assert.assertEquals("20", carros);
	}

	@Test
	public void validarDisponibilidadVehiculo() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(false);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();

		// Act
		try {
			vigilanteService.validarDisponibilidadVehiculo(vehiculo);
		} catch (DisponibilidadExcepcion e) {
			// Assert
			Assert.assertEquals("No hay cupo en el parqueadero para el tipo de vehiculo", e.getMessage());

		}
	}

	@Test
	public void validarVehiculoNoPermitido() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(false);
		Vehiculo vehiculo = new VehiculoTestVehiculoNoPermitBuild().build();

		// Act
		try {
			vigilanteService.validarDisponibilidadVehiculo(vehiculo);
		} catch (TipoVehiculoExcepcion e) {
			// Assert
			Assert.assertEquals("Tipo de vehiculo no permitido", e.getMessage());

		}
	}

	@Test
	public void registrarIngresBds() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		// Act
		TiqueteParqueo tiquete = vigilanteService.registrar(vehiculo);
		// Assert
		Assert.assertNotNull(tiquete);
	}

	@Test
	public void registrarIngresoVehicNoDisponible() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(false);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();
		//when(vigilanteService.obtenerVehiculoPorPlacaSalida(vehiculo.getPlaca())).thenReturn(null);
		// Act
		try {
			vigilanteService.registrarIngreso(vehiculo, calendar);
		} catch (DisponibilidadExcepcion e) {
			// Assert
			Assert.assertEquals("No hay cupo en el parqueadero para el tipo de vehiculo", e.getMessage());

		}
	}

	@Test
	public void registroPlacasAutorizadas() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);

		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();

		when(propiedadesRepositorio.obtenerValorPropiedad("placas")).thenReturn("p,x");

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		// Act
		boolean autorizado = vigilanteService.ingresoPlacaDiasNoAutorizados(vehiculo, calendar);
		// Assert
		Assert.assertTrue(autorizado);

	}

	@Test
	public void registrarIngresoPlcasNoAutorizadas() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);

		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();

		when(propiedadesRepositorio.obtenerValorPropiedad("placas")).thenReturn("p,x");

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		// Act
		try {
			vigilanteService.ingresoPlacaDiasNoAutorizados(vehiculo, calendar);
		} catch (AutorizacionExcepcion e) {
			// Assert
			Assert.assertEquals("No esta autorizado", e.getMessage());

		}
	}

	@Test
	public void costoHorasCarroParqueo() {
		// Arrange
		int horasParqueadero = 6;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();

		// Act
		int costoHoras = vigilanteService.costoHorasParqueo(horasParqueadero, costoHoraCarro, costoHoraMoto, tiquete);
		// Assert
		Assert.assertEquals(6000, costoHoras);
	}

	@Test
	public void costoHorasMotoParqueo() {
		// Arrange
		int horasParqueadero = 6;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		// Act
		int costoHoras = vigilanteService.costoHorasParqueo(horasParqueadero, costoHoraCarro, costoHoraMoto, tiquete);
		// Assert
		Assert.assertEquals(3000, costoHoras);
	}

	@Test
	public void costoCarroDiaParqueo() {
		// Arrange
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(8000, costoDia);
	}

	@Test
	public void costoMotoDiaParqueo() {
		// Arrange
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(4000, costoDia);
	}

	@Test
	public void costoMotoAltoCilindrajeParqueo() {
		// Arrange
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		tiquete.setCilindrajeVehiculo(600);
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(6000, costoDia);
	}

	@Test
	public void costoParqueoDiasMotoAltoCilindraje() {
		// Arragne
		int horasParqueadero = 26;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		tiquete.setCilindrajeVehiculo(600);

		// Act
		int costoParqueo = vigilanteService.calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
				costoHoraMoto, costoDiaMoto);
		// Assert
		Assert.assertEquals(7000, costoParqueo);
	}

	@Test
	public void costoParqueoDiasMotoBajoCilindraje() {
		// Arragne
		int horasParqueadero = 26;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");

		// Act
		int costoParqueo = vigilanteService.calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
				costoHoraMoto, costoDiaMoto);
		// Assert
		Assert.assertEquals(5000, costoParqueo);
	}

	@Test
	public void costoParqueoDiasCarro() {
		// Arragne
		int horasParqueadero = 26;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();

		// Act
		int costoParqueo = vigilanteService.calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
				costoHoraMoto, costoDiaMoto);
		// Assert
		Assert.assertEquals(10000, costoParqueo);
	}

	@Test
	public void costoParqueoDiasMotoHoras() {
		// Arragne
		int horasParqueadero = 35;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		// Act
		int costoParqueo = vigilanteService.calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
				costoHoraMoto, costoDiaMoto);
		// Assert
		Assert.assertEquals(8000, costoParqueo);
	}

	@Test
	public void costoParqueoDiasCarroHoras() {
		// Arragne
		int horasParqueadero = 35;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		// Act
		int costoParqueo = vigilanteService.calculoCostoEnDias(horasParqueadero, tiquete, costoHoraCarro, costoDiaCarro,
				costoHoraMoto, costoDiaMoto);
		// Assert
		Assert.assertEquals(16000, costoParqueo);
	}

	@Test
	public void costoParqueaderoMenosUnaHoraCarro() {
		// Arrange
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		// Act
		int costoParqueadero = vigilanteService.costoMenorUnaHora(tiquete, costoHoraCarro, costoHoraMoto);
		// Assert
		Assert.assertEquals(1000, costoParqueadero);
	}

	@Test
	public void costoParqueaderoMenosUnaHoraMotod() {
		// Arrange
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		int costoHoraCarro = 1000;
		int costoHoraMoto = 500;
		// Act
		int costoParqueadero = vigilanteService.costoMenorUnaHora(tiquete, costoHoraCarro, costoHoraMoto);

		// Assert
		Assert.assertEquals(500, costoParqueadero);
	}

	@Test
	public void costoParqueadero() {
		// Arragne
		TiqueteParqueo tiquete = new TiqueteCostoConFechaEntradaActualTestBuild().build();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tiquete.getFechaEntrada());
		calendar.add(Calendar.HOUR, -26);
		tiquete.setFechaEntrada(calendar.getTime());
		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(tiquete);
		// Assert
		Assert.assertEquals(10000, tiqueteCosto.getCostoParqueo());
	}

	@Test
	public void listaVehiculosParqueados() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);

		// when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		// Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		// when(vigilanteService.obtenerVehiculoPorPlacaSalida(vehiculo.getPlaca())).thenReturn(null);
		TiqueteParqueo tiqueteCosto = new TiqueteCostoParqueaderoTestBuild().build();
		List<TiqueteParqueo> listaTiquetes = new ArrayList<>();
		listaTiquetes.add(tiqueteCosto);
		when(tiqueteParqueoRepositorio.vehiculosParqueados()).thenReturn(listaTiquetes);
		// Act
		List<Tiquete> tiquetes = vigilanteService.vehiculosParqueados();
		// Assert
		Assert.assertTrue(tiquetes.size() > 0);
	}

	@Test
	public void vehiculosParqueadosRest() {
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		VigilanteService vigilanteService = mock(VigilanteService.class);
		// when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		// Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		// when(vigilanteService.obtenerVehiculoPorPlacaSalida(vehiculo.getPlaca())).thenReturn(null);
		TiqueteParqueo tiqueteCosto = new TiqueteCostoParqueaderoTestBuild().build();
		List<TiqueteParqueo> listaTiquetes = new ArrayList<>();
		listaTiquetes.add(tiqueteCosto);

		List<Tiquete> listaTiq = new ArrayList<>();
		Tiquete tiqu = new Tiquete("XXX-123", "carro", null);
		listaTiq.add(tiqu);
		when(tiqueteParqueoRepositorio.vehiculosParqueados()).thenReturn(listaTiquetes);
		when(vigilanteService.vehiculosParqueados()).thenReturn(listaTiq);
		// Act
		List<Tiquete> tiquetes = parqueaderoRest.vehiculosParqueados();
		// Asert
		Assert.assertTrue(tiquetes.size() > 0);

	}
	
	//Registra en base de datos
	@Test
	public void registroVehiculoRest() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(false);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		//when(vigilanteService.verificarPlacaRegistro(vehiculo.getPlaca())).thenReturn(null);
		// Act
		try {
			parqueaderoRest.registrarIngresoVehiculo(vehiculo);
		} catch (RuntimeException e) {
			// Assert
			Assert.assertEquals("No hay cupo", e.getMessage());

		}
	}

	@Test
	public void validarPlacaRegistroRest() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);

		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();

		when(tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(vehiculo.getPlaca())).thenReturn(tiquete);

		// Act
		try {
			vigilanteService.verificarPlacaRegistro(vehiculo.getPlaca());
		} catch (RegistroExcepcion e) {
			// Assert
			Assert.assertEquals("El carro ya se encuentra en el parqueadero", e.getMessage());

		}
	}

	@Test
	public void ingresoVehicRegistrado() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();

		when(tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(vehiculo.getPlaca())).thenReturn(tiquete);
		// Act
		try {
			vigilanteService.registrarIngreso(vehiculo, calendar);
		} catch (RegistroExcepcion e) {
			// Assert
			Assert.assertEquals("El carro ya se encuentra en el parqueadero", e.getMessage());

		}
	}

	@Test
	public void ingresoVehic() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();

		when(tiqueteParqueoRepositorio.obtenerVehiculoPorPlaca(vehiculo.getPlaca())).thenReturn(null);
		when(propiedadesRepositorio.obtenerValorPropiedad("placas")).thenReturn("p,x");
		// Act
		try {
			vigilanteService.registrarIngreso(vehiculo, calendar);
		} catch (AutorizacionExcepcion e) {
			// Assert
			Assert.assertEquals("No esta autorizado", e.getMessage());
		}

	}

	@Test
	public void costoParqueaderoUnaHora() throws ParseException {
		// Arragne
		int costoHoraCarro = 1000;
		TiqueteParqueo tiquete = new TiqueteCostoConFechaEntradaActualTestBuild().build();

		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(tiquete);
		// Assert
		Assert.assertEquals(costoHoraCarro, tiqueteCosto.getCostoParqueo());
	}

	@Test
	public void costoParqueaderoVariasHoras() {
		// Arragne
		TiqueteParqueo tiquete = new TiqueteCostoConFechaEntradaActualTestBuild().build();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tiquete.getFechaEntrada());
		calendar.add(Calendar.HOUR, -6);

		tiquete.setFechaEntrada(calendar.getTime());
		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(tiquete);
		// Assert
		Assert.assertEquals(6000, tiqueteCosto.getCostoParqueo());
	}

	@Test
	public void costoParqueaderoUnDia() {
		// Arragne
		int costoDiaCarro = 8000;
		TiqueteParqueo tiquete = new TiqueteCostoConFechaEntradaActualTestBuild().build();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(tiquete.getFechaEntrada());
		calendar.add(Calendar.HOUR, -14);

		tiquete.setFechaEntrada(calendar.getTime());
		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(tiquete);
		// Assert
		Assert.assertEquals(costoDiaCarro, tiqueteCosto.getCostoParqueo());
	}
	
	@Test
	public void obtenerTrm() {
		//Arrange
		// Act
		Float trm = vigilanteService.obtenerTrm();
		// Asert
		Assert.assertTrue(trm > 0);

	}
	
	@Test
	public void obtenerTrmRest() {
		//Arrange
		// Act
		Float trm = parqueaderoRest.trm();
		// Asert
		Assert.assertTrue(trm > 0);

	}

}
