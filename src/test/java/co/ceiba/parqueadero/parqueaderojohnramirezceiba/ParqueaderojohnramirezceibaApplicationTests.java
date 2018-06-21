package co.ceiba.parqueadero.parqueaderojohnramirezceiba;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoConFechaEntradaTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.TiqueteCostoParqueaderoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoMotoTestPlacaPermitidaBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestPlacaPermitidaBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.build.VehiculoTestVehiculoNoPermitBuild;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
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
	public void validarVehiculoNoPermitido() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestVehiculoNoPermitBuild().build();
		// Act
		boolean isPermitido = vigilanteService.validarDisponibilidadVehiculo(vehiculo);
		// Assert
		Assert.assertFalse(isPermitido);
	}

	/*@Test
	public void validarVehiculoMoto() {
		// Arrange
		Vehiculo vehiculo = new VehiculoMotoTestPlacaPermitidaBuild().build();
		// Act
		boolean isPermitido = vigilanteService.validarDisponibilidadVehiculo(vehiculo);
		// Assert
		Assert.assertTrue(isPermitido);
	}*/

	@Test
	public void registrarIngresBds() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		// Act
		TiqueteParqueo tiquete = vigilanteService.registrar(vehiculo);
		// Assert
		Assert.assertNotNull(tiquete);
	}

	/*@Test
	public void registrarIngresoVehiculo() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();
		// Act
		TiqueteParqueo tiquete = vigilanteService.registrarIngreso(vehiculo, calendar);
		// Assert
		Assert.assertNotNull(tiquete);
	}

	@Test
	public void registrarIngresoVehiculoMoto() {
		// Arrange
		Vehiculo vehiculo = new VehiculoMotoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();
		VigilanteService vigilanteServiceMock = mock(VigilanteService.class);
		when(vigilanteServiceMock.validarDisponibilidadVehiculo(vehiculo)).thenReturn(true);
		
		// Act
		TiqueteParqueo tiquete = vigilanteServiceMock.registrarIngreso(vehiculo, calendar);
		// Assert
		Assert.assertNotNull(tiquete);
	}

	@Test
	public void registrarVehiculoNoDisponible() {
		// Arrange
		Vehiculo vehiculo = new VehiculoMotoTestPlacaPermitidaBuild().build();
		Calendar calendar = Calendar.getInstance();
		VigilanteService vigilanteServiceMock = mock(VigilanteService.class);
		when(vigilanteServiceMock.validarDisponibilidadVehiculo(vehiculo)).thenReturn(false);
		// Act
		TiqueteParqueo tiquete = vigilanteServiceMock.registrarIngreso(vehiculo, calendar);
		// Assert
		Assert.assertEquals(null, tiquete);
	}

	@Test
	public void registrarVehiculoRest() {
		// Arrange
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		ResponseEntity<HttpStatus> httpResponse = parqueaderoRest.registrarIngresoVehiculo(vehiculo);

		Assert.assertEquals(HttpStatus.OK, httpResponse.getStatusCode());
	}*/

	// CALCULO COSTO PARQUEADERO
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
	public void costoDiaParqueo() {
		// Arrange
		int horasParqueadero = 6;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(horasParqueadero, costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(48000, costoDia);
	}

	@Test
	public void costoMotoParqueo() {
		// Arrange
		int horasParqueadero = 6;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(horasParqueadero, costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(24000, costoDia);
	}

	@Test
	public void costoMotoAltoCilindrajeParqueo() {
		// Arrange
		int horasParqueadero = 6;
		int costoDiaCarro = 8000;
		int costoDiaMoto = 4000;
		TiqueteParqueo tiquete = new TiqueteCostoParqueaderoTestBuild().build();
		tiquete.setTipoVehiculo("moto");
		tiquete.setCilindrajeVehiculo(600);
		// Act
		int costoDia = vigilanteService.costoUnDiaParqueo(horasParqueadero, costoDiaCarro, costoDiaMoto, tiquete);
		// Assert
		Assert.assertEquals(26000, costoDia);
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
	public void costoParqueadero() {
		// Arragne
		Vehiculo vehiculo = new VehiculoTestPlacaPermitidaBuild().build();
		vehiculo.setPlaca("XXL-456");
		vigilanteService.registrar(vehiculo);
		// Act
		TiqueteParqueo tiqueteCosto = vigilanteService.calcularValorParqueadero(vehiculo.getPlaca());
		// Assert
		Assert.assertNotNull(tiqueteCosto);
	}

	@Test
	public void listaVehiculosParqueados() {
		// Arrange

		// Act
		List<Tiquete> tiquetes = vigilanteService.vehiculosParqueados();
		// Assert
		Assert.assertNotNull(tiquetes);
	}

}
