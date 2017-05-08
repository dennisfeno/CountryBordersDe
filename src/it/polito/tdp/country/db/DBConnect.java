package it.polito.tdp.country.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.DataSources;

public class DBConnect {
	
	private static String jdbcURL = "jdbc:mysql://localhost/countries?user=root" ;
	private static DataSource ds ; // static perché ce n'è uno solo...
	
	public static Connection getConnection() {
		
		if(ds==null){
			// crea il dataSource.
			try {
				ds = DataSources.pooledDataSource(DataSources.unpooledDataSource(jdbcURL)) ;
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(1); // chiuditi perché tanto non posso farci nulla. 
			}
			
			//pooled DataSource prende un unpooled e lo trasforma in pooled. 
			// non parlo più con il driver jDBC, è c3p0 che ci pensa.
		}
		
		try {
			Connection c = ds.getConnection() ; //jdbcURL non serve più. ciò che manca è la creazione del dataSource. 
			/**
			 * l'oggetto dataSource non lo devo creare qui, ma devo crearlo una volta sola e riuso sempre lo stesso
			 */
			return c ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		// esiste un metodo che si chiama destroy per cancellare e ripulire il ds.
		
	}

}