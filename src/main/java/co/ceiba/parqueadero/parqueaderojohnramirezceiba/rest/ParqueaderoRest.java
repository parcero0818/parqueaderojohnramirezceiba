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
import org.springframework.web.bind.annotation.RestController;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.Propiedades;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.modelo.TiqueteParqueo;
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

	@PostMapping(value = "/parqueaderos")
	public ResponseEntity<HttpStatus> parqueaderos(@RequestBody Vehiculo vehiculo) {
		Calendar calendar = Calendar.getInstance();
		TiqueteParqueo tiquete = vigilanteService.registrarIngreso(vehiculo, calendar);
		if (null != tiquete) {
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/getparqueaderos")
	public List<TiqueteParqueo> getParqueaderos() {
		return tiqueteParqueoRepositorio.findAll();
	}
	
	@GetMapping(value = "/getPropiedades")
	public List<Propiedades> getPropiedades() {
		return propiedadesRepositorio.findAll();
	}
	
}
