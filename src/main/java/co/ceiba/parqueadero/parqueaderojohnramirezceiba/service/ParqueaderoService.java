package co.ceiba.parqueadero.parqueaderojohnramirezceiba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.enums.PropiedadesParqueadero;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.excepcion.DisponibilidadExcepcion;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.PropiedadesRepositorio;
import co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio.TiqueteParqueoRepositorio;
@Controller
public class ParqueaderoService implements IParqueaderoService{
	
	@Autowired
	PropiedadesRepositorio propiedadesRepositorio;

	public boolean verificarDisponibilidadCarro(TiqueteParqueoRepositorio tiqueteParqueoRepositorio) {
		int cantidadCarrosPermitidos = Integer.parseInt(obtenerValorPropiedad(PropiedadesParqueadero.CANTCARROSPERMITIDOS.getNombrePropiedad()));
		if (cantidadCarrosParqueados(tiqueteParqueoRepositorio) >= cantidadCarrosPermitidos) {
			throw new DisponibilidadExcepcion("No hay cupo");
		}
		return true;
	}

	public boolean verificarDisponibilidadMoto(TiqueteParqueoRepositorio tiqueteParqueoRepositorio) {
		int cantidadMotosPermitidos = Integer.parseInt(obtenerValorPropiedad(PropiedadesParqueadero.CANTMOTOSPERMITIDOS.getNombrePropiedad()));
		if (cantidadMotosParqueados(tiqueteParqueoRepositorio) >= cantidadMotosPermitidos) {
			throw new DisponibilidadExcepcion("No hay cupo");
		}
		return true;
	}
	
	public String obtenerValorPropiedad(String nombrePropiedad) {
		return propiedadesRepositorio.obtenerValorPropiedad(nombrePropiedad);
	}
	
	public int cantidadMotosParqueados(TiqueteParqueoRepositorio tiqueteParqueoRepositorio) {
		return tiqueteParqueoRepositorio.cantidadMotosParqueados();
	}
	
	public int cantidadCarrosParqueados(TiqueteParqueoRepositorio tiqueteParqueoRepositorio) {
		return tiqueteParqueoRepositorio.cantidadCarrosParqueados();
	}
}
