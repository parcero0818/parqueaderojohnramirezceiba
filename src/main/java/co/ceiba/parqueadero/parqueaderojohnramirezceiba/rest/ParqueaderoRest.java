package co.ceiba.parqueadero.parqueaderojohnramirezceiba.rest;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;
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
	
	@CrossOrigin
	@PostMapping(value = "/registrarIngreso")
	public String registrarIngresoVehiculo(@RequestBody Vehiculo vehiculo) {
		try {
			Calendar calendar = Calendar.getInstance();
			TiqueteParqueo tiquete = vigilanteService.registrarIngreso(vehiculo, calendar);
			if (null != tiquete) {
				return "Registro Exitoso";
			}

		} catch (RuntimeException e) {
			return e.getMessage();
		}
		return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
	}

	@CrossOrigin
	@GetMapping(value = "/registrarSalida")
	public TiqueteParqueo registrarSalidaVehiculo(@RequestParam("placaVehiculo") String placaVehiculo) {
		TiqueteParqueo tiquete = vigilanteService.obtenerVehiculoPorPlacaSalida(placaVehiculo);
		if(tiquete != null) {
			return vigilanteService.calcularValorParqueadero(tiquete);
		}
		return null;
		
	}

	@CrossOrigin
	@GetMapping(value = "/vehiculosParqueados")
	public List<Tiquete> vehiculosParqueados() {
		return vigilanteService.vehiculosParqueados();
	}
	
	@CrossOrigin
	@GetMapping(value = "/trm")
	public Float trm() {
		return vigilanteService.obtenerTrm();
	}

}
