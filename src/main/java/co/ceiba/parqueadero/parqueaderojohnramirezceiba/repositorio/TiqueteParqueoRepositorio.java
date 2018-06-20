package co.ceiba.parqueadero.parqueaderojohnramirezceiba.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import co.ceiba.parqueadero.parqueaderojohnramirezceiba.entidades.TiqueteParqueo;

public interface TiqueteParqueoRepositorio extends JpaRepository<TiqueteParqueo, Long> {

	@Query("select count(*) from tiqueteParqueo t where lower(t.tipoVehiculo)=lower('carro') and t.fechaSalida is null")
	Integer cantidadCarrosParqueados();

	@Query("select count(*) from tiqueteParqueo t where lower(t.tipoVehiculo)=lower('moto') and t.fechaSalida is null")
	Integer cantidadMotosParqueados();

	@Query("select t from tiqueteParqueo t where lower(t.placaVehiculo)=lower(?1)")
	TiqueteParqueo obtenerVehiculoPorPlaca(String placaVehiculo);
	
	@Query("select t from tiqueteParqueo t where t.fechaSalida is null")
	List<TiqueteParqueo> vehiculosParqueados();

}
