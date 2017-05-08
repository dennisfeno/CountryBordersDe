package it.polito.tdp.country.model;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

/**
 * la libreria jGraphT ci permette di avere tanti grafi. quale metto in questo caso? 
 * istanzio una di queste tre. in questo caso ho bisogno di un grafo non orientato, ovvero guardo solo la parte sinistra
 * nella parte sinistra, il grafo è pesato o non pesato? in questo caso scelgo il non pesato
 * il peso è un'informazione che non va confusa con quello che è il tipo di arco, come in questo caso, quindi, scelgo il non pesato
 * nel nostro caso prendiamo un grafo semplice non pesato e non orientato
 * 
 * costruisco un oggetto di tipo simpleGraph, parte degli undirectedGraph
 * @author Dennis
 */

public class ModelDennis {

	// private UndirectedGraph<V, E> graph = new SimpleGraph<V, E>(edgeClass) ;
	// V: tipo del vertice
	// E: tipo dell'arco, classe dell'arco.
	
	// ci vengono già definiti il DefaultEdge o il DefaultWeightedEdge
	
	//private UndirectedGraph<Country, DefaultEdge> graph = new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class) ;
	private UndirectedGraph<Country, DefaultEdge> graph ;
	// lascio lo spazio vuoto. il grafo non ha bisogno di crearsi i vertici, gli passiamo noi gli oggetti già fatti
	// nei meandri della classe simpleGraph devo avere la creazione di un oggetto DefaultEdge, passandoglielo come parametro
	// lui saprà qual è il riferimento alla classe che andrò ad utilizzare. Crea un oggetto di tipo parametrico
	// a livello pratico, dovrò passare il riferimento al tipo per creare gli archi.
	// se creo degli archi personalizzati sarà ArcoMio.class.
	
	public ModelDennis(){
		
		graph = new SimpleGraph<>(DefaultEdge.class) ;
		
	}
	
	public void creaGrafo(){
		
		// graph.addVertex() ;
		
		// arg0 --> il grafo, il secondo è una collection di vertici, con tutti i vertici che sono country
		
		// crea i vertici del grafo. 
		
		//Graphs.addAllVertices(graph, dao.listCountry()) ; // aggiungi tutti i vertici
		
		/**
		 * 3 metodi per creare gli archi del grafo.
		 * 1) stupido, meno efficiente, funziona sempre. per ogni coppia di vertici, devono essere collegati o no? 
		 * 		se si aggiungo l'arco, altrimenti non lo aggiungo. in questo caso farò un doppio loop 
		 * 		vantaggio: semplice sia in java che in sql
		 * 		svantaggio: n^2 queries
		 * 2) faccio lavorare un po' di più il db e dato il vertice gli chiedo tutti i confinanti
		 * 		in questo caso il risultato è lo stesso. la query sul db è un po' più complicata, 
		 * 		questa volta la query viene fatta n volte, al posto di nQuadro
		 * 		tendenzialmente direi che conviene, il tempo della 2 query non è n volte più lenta.
		 * 		mi aspetto che non sia così. se riesco mi converrebbe la seconda versione, un po' meno "complessa"
		 * 3) facciamo fare tutto il lavoro al DAO, mi elenchi già belli gli archi da aggiungere.
		 * 		faccia già la query esatta che mi dia tutti i vertici. in questo caso non itero più io sui vertici
		 * 		ma chiedo al dao le coppie di country adiacenti. chiedo già l'elenco degli archi. 
		 * 		qua faccio un numero di addEdge pari al numero di vertici, preciso. in questo caso il modello ha il lavoro facile
		 */
		
		/* metodo 1
		for(Country c1: graph.vertexSet()){
			for(Country c2: graph.vertexSet()){
				if(!c1.equals(c2)){ // perché non ho un loop
					if(dao.confinano(c1,c2){
						graph.addEge(c1,c2) ;
					}
				}
			}
		}
		*/
		/* metodo 2
		for(Country c: graph.vertexSet()){
			List<Country> adiacenti = dao.listAdiacenti(c);
				for(Country c2: adiacenti){
					graph.addEdge(c,c2);
				}
			}
		}
		*/		
		
		// anche così funziona. devo solo avere il metodo del dao fatto per bene
		
		///for(CountryPair cp = dao.listCoppieCountryAdiacenti()){
			///graph.addEdge(cp.getC1, cp.getC2) ;
		///}
		
		//Graphs.addAllEdges(arg0, arg1, arg2)
		
		///BreadthFirstIterator<Country, DefaultEdge> bfv = new BreadthFirstIterator<>(graph, c1) ;
		// iteratore, a partire dal vertice di partenza.
		
		///while(bfv.hasNext()){
			//System.out.println(bfv.next());
			///visited.add(bfv.next);
		///}
		// se mi interessa l'albero di vista
		// mentre l'iteratore genera fa degli eventi. con un ascoltatore posso registrare queste cose. 
		
	}
}
