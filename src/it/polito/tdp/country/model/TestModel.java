package it.polito.tdp.country.model;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model() ;
		
		long start, end ;
		
		System.out.println("Creo il grafo CountryBorders usando i 3 metodi");
		
		start=System.nanoTime() ;
		//m.creaGrafo1();
		end=System.nanoTime() ;
		System.out.format("Metodo 1: %d ms\n", (end-start)/1000000) ;
		m.printStats();
		
		/**
		 * il primo metodo è lentino.. sta facendo un sacco di volte la stessa query
		 * noi stiamo facendo tante query, ho 218 country, 218*217 query (47000 query) 
		 * per ciascuna apro la connessione, faccio la query e chiudo. subito dopo ne apro un'altra
		 * per fare un'altra query. a livello di sistema operativo apro un socket per comunicare con mySqL
		 * una volta chiuso un socket il S.O. ci mette un pochino per liberarlo, mettiamo che ci metta 10microS per liberarne uno
		 * se ci mette 2microS per crearne nuovi, ho un tasso differente per creare/liberare risorse. il numero di socket
		 * aperti cresce fino a raggiungere il limite del sistema operativo.
		 * 
		 * abusa delle risorse di sistema. 
		 */
		
		start=System.nanoTime() ;
		m.creaGrafo2();
		end=System.nanoTime() ;
		System.out.format("Metodo 2: %d ms\n", (end-start)/1000000) ;
		m.printStats();

		/**
		 * il metodo 2) e 3) costruiscono un grafo "uguale", con tempi differenti. 
		 * perché per il metodo 1) non c'è speranza? 
		 * per una query del metodo 2 ho un millisecondo di delay, * 218 stati.
		 * non mi aspetto quindi 1 secondo di tempo speso, ma 218ms, come mai ho un secondo?
		 * chi mangia l'80% del tempo? sicuramente non è l'addEdge, ma lo perdo nella 
		 * GetConnection(). da un lato (metodo 1) ci fa morire il programma, dall'altro (metodo 2) 
		 * consuma l'80% del tempo. haidiSQL mantiene una connessione aperta e non spende altre volte il tempo
		 * per effettuare la connessione (così come ogni altro programma di gestione del DB).
		 * DB ottimizzato per fare tante query su poche connessioni di lunga durata. 
		 */
		
		start=System.nanoTime() ;
		m.creaGrafo3();
		end=System.nanoTime() ;
		System.out.format("Metodo 3: %d ms\n", (end-start)/1000000) ;
		m.printStats();
		
		/**
		 * come si evita questo problema? facciamo anche noi come fa heidiSQL .. apriamo anche noi una connessione
		 * e manteniamola aperta per tutta la durata del programma. viene creato un altro tipo di problemi: timeOut della connessione
		 * per fortuna questo problema è già risolto. è un meccanismo che si chiama ConnectionPooling. 
		 * il principio è quello di avere qualcuno che gestisca un pool di connessioni. quando il programma parte apre un po' di connessioni
		 * dentro le connessioni non si fa nulla. il dao a questo punto prende in prestito una delle connessioni dal pool
		 * ci fossero quindi anche molte query da eseguire insieme, il tempo di set-up viene cancellato. anziché pensare
		 * che il dao apra una connessione, penseremo che la prenda in prestito dalle connessioni già aperte. è una libreria 
		 * che gestisce in maniera autonoma in modo da garantire la massima efficienza. l'interfaccia che utilizzeremo si chiama
		 * dataSource. mi piacerebbe trovare una libreria che implementi l'interfaccia DataSource. ci viene in aiuto
		 * c3p0. questa libreria fa connectionPooling. statementPooling: si accorge che sto facendo la query simile alla precedente
		 * e dice al DB che è la stessa. il DB non deve più fare l'analisi della query, ma solo più l'esecuzione. 
		 * la classe datasource contiene un metodo che contiene la getConnection(), ritorna lo stesso tipo di connessione, 
		 * anche se l'oggetto sarà un pochino differente. 
		 * dentro la classe DBConnection() non devo usare il driverManager, ma utilizzare la  libreria per farmi dare i dataSource
		 * ho 2 modi per farlo:
		 * classe factory: classe con metodo statico che crea oggetti. i metodi factory restituiscono un oggetto. restituisce un oggetto che 
		 * implementi datasource. 
		 */

	}

}
