package co.ceiba.parqueadero.parqueaderojohnramirezceiba;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoConFechaEntradaTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoParqueaderoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestPlacaPermitidaBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestVehiculoNoPermitBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.AutorizacionExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
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
		when(vigilanteService.obtenerVehiculoPorPlacaSalida(vehiculo.getPlaca())).thenReturn(null);
		// Act
		try {
			vigilanteService.registrarIngreso(vehiculo, calendar);
		} catch (DisponibilidadExcepcion e) {
			// Assert
			Assert.assertEquals("No hay cupo en el parqueadero para el tipo de vehiculo", e.getMessage());

		}
	}

	@Test
	public void registrarIngresoVehiculo() {
		// Arrange
		VigilanteService vigilanteService = mock(VigilanteService.class);
		TiqueteParqueoRepositorio tiqueteParqueoRepositorio = mock(TiqueteParqueoRepositorio.class);
		ParqueaderoService parqueaderoService = mock(ParqueaderoService.class);
		PropiedadesRepositorio propiedadesRepositorio = mock(PropiedadesRepositorio.class);
		vigilanteService = new VigilanteService(tiqueteParqueoRepositorio, parqueaderoService, propiedadesRepositorio);
		when(parqueaderoService.verificarDisponibilidadCarro(tiqueteParqueoRepositorio)).thenReturn(true);
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		when(vigilanteService.obtenerVehiculoPorPlacaSalida(vehiculo.getPlaca())).thenReturn(null);

		when(propiedadesRepositorio.obtenerValorPropiedad("placas")).thenReturn("z,x");

		// when(vigilanteService.verificarPlaca(vehiculo.getPlaca())).thenReturn(false);
		Calendar calendar = Calendar.getInstance();

		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		when(vigilanteService.registrar(vehiculo)).thenReturn(tiquete);

		// Act
		vigilanteService.registrarIngreso(vehiculo, calendar);
		// Assert
		Assert.assertEquals("PZV-283", tiquete.getPlacaVehiculo());
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
	public void costoParqueadero() throws ParseException {
		// Arragne
		TiqueteParqueo tiquete = new TiqueteCostoConFechaEntradaTestBuild().build();

		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(tiquete);
		// Assert
		Assert.assertEquals(tiquete.getPlacaVehiculo(), tiqueteCosto.getPlacaVehiculo());
	}

	@Test
	public void listaVehiculosParqueados() {
		// Arrange

		// Act
		List<Tiquete> tiquetes = vigilanteService.vehiculosParqueados();
		// Assert
		Assert.assertNotNull(tiquetes);
	}

	@Test
	public void vehiculosParqueadosRest() {
		// Arrange

		// Act
		List<Tiquete> tiquetes = parqueaderoRest.vehiculosParqueados();
		if (tiquetes.size() <= 0) {
			Assert.assertNull(tiquetes);
		} else {
			Assert.assertNotNull(tiquetes);
		}

	}

}
