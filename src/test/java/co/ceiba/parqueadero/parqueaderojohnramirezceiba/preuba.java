package co.ceiba.parqueadero.parqueaderojohnramirezceiba;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class preuba {

	public static void main(String[] args) {
		System.out.println("ENTRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");

		try {
			Date fechaInicial = dateFormat.parse("2016-02-14 01:00:00");
			Date fechaFinal =   dateFormat.parse("2016-02-15 23:20:10");
			int diferencia = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 1000);
			
			int horas = diferencia/3600;
			System.out.println("esta "+horas);
			System.out.println("estaaa "+ horas % 24 );
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
