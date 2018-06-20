package co.ceiba.parqueadero.parqueaderojohnramirezceiba.rest;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Tiquete;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Vehiculo;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.PropiedadesRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.TiqueteParqueoRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.service.VigilanteService;

@RequestMapping("/parqueadero")
@RestController
public class ParqueaderoRest {

	@Autowired
	VigilanteService vigilanteService;

	@Autowired
	TiqueteParqueoRepositorio tiqueteParqueoRepositorio;

	@Autowired
	PropiedadesRepositorio propiedadesRepositorio;

	@PostMapping(value = "/registrarIngreso")
	public ResponseEntity<HttpStatus> registrarIngresoVehiculo(@RequestBody Vehiculo vehiculo) {
		try {
			Calendar calendar = Calendar.getInstance();
			TiqueteParqueo tiquete = vigilanteService.registrarIngreso(vehiculo, calendar);
			if (null != tiquete) {
				return new ResponseEntity<>(HttpStatus.OK);
			}

		} catch (DisponibilidadExcepcion e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/registrarSalida")
	public int registrarSalidaVehiculo(@RequestParam("placaVehiculo") String placaVehiculo) {
		return vigilanteService.calcularValorParqueadero(placaVehiculo);
	}

	@GetMapping(value = "/vehiculosParqueados")
	public List<Tiquete> vehiculosParqueados() {
		return vigilanteService.vehiculosParqueados();
	}

}
