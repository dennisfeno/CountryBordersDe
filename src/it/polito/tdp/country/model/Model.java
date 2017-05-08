package it.polito.tdp.country.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.country.db.CountryDao;

public class Model {
	
	private UndirectedGraph<Country, DefaultEdge> graph  ;
	private List<Country> countries ; 
	private Map<Country, Country> alberoVisita ;
	
	public Model() {
	}
	
	public List<Country> getCountries() {
		
		if(this.countries == null){ // se non ho ancora inizializzato nulla
			CountryDao dao = new CountryDao();
			countries = dao.listCountry() ; // è una sorta di caching.
		}
		return countries ; 

	}
	public List<Country> getRaggiungibili(Country partenza){
		// visito il grafo
		UndirectedGraph<Country,DefaultEdge> g = this.getGrafo() ;
		BreadthFirstIterator<Country, DefaultEdge> bfi= new BreadthFirstIterator<>(g, partenza) ;
	
		List<Country> list = new ArrayList<>() ;
		Map<Country, Country> albero = new HashMap<>();
//		albero.put(partenza, null) ;
		albero.put(partenza, new Country(-1,"fittizio","fittizio")) ; 
		bfi.addTraversalListener(new CountryTraversalListener(g, albero));

		// posso ricordarmi per ogni vertice il suo papà, così non sbaglio...
		// salvo solamente un riferimento, questo è stato scoperto a partire da.. 
		// es. canada è stato scoperto da USA. 
		// una mappa è il modo più semplice per scrivere questa cosa è la mappa,
		// creo una mappa di <country, country> seguendo la mappa, per come l'ho costruita, ritorno al vertice di partenza.
		
		while (bfi.hasNext()){
			list.add(bfi.next()) ;
		}
		
		this.alberoVisita = albero ;
		//System.out.println(alberoVisita.toString()) ;
		
		return list ;
		/**
		 * come li hai raggiunti ? posso decidere di ri-costruirla. osservo l'iteratore mentre lavora. 
		 * ho dei listener(gestore di eventi) che mi dice cosa è successo durante la visita dell'iteratore
		 * se voglio posso intercettare degli eventi. 
		 * vedi i metori del breadthFirstIterator
		 * posso inserire una classe che ascolti le operazioni di attraversamento
		 */
	}
	
	/**
	 * creo il grafo con il modello 3, solo se il grafo non esiste... 
	 * @return
	 */
	private UndirectedGraph<Country,DefaultEdge> getGrafo() {
		if(this.graph==null){
			this.creaGrafo3();
		}
		return this.graph ;
	}

	/**
	 * Creazione del grafo CountryBorders.
	 * Prima versione: per ogni coppia di vertici, chiedo al database se esiste un arco.
	 */
	public void creaGrafo1() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries() ) ;
	
		// crea gli archi del grafo -- versione 1
		for(Country c1: graph.vertexSet()) {
			for(Country c2: graph.vertexSet()) {
				if(!c1.equals(c2)) {
					if( dao.confinanti(c1, c2) ) {
						graph.addEdge(c1, c2) ;
					}
				}
			}
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Seconda versione: per ogni vertice, chiedo al database la lista dei vertici ad esso confinanti.
	 */
	public void creaGrafo2() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 2
		for(Country c: graph.vertexSet()) {
			List<Country> adiacenti = dao.listAdiacenti(c) ;
			for(Country c2: adiacenti)
				graph.addEdge(c, c2) ;
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Terza versione: una sola volta, chiedo al database l'elenco delle coppie di vertici confinanti.
	 */
	public void creaGrafo3() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 3
		for(CountryPair cp : dao.listCoppieCountryAdiacenti()) {
			graph.addEdge(cp.getC1(), cp.getC2()) ;
		}
	}

	public void printStats() {
		System.out.format("Grafo: Vertici %d, Archi %d\n", graph.vertexSet().size(), graph.edgeSet().size());
	}

	public List<Country> getPercorso(Country destinazione) {
		
		List<Country> percorso = new ArrayList<Country>() ;
		
		//percorso.add(destinazione) ;
		
		Country c = destinazione ;
//		while(c!=null){
		while(c!=new Country(-1,"","")){
			percorso.add(c) ;
			if( alberoVisita.get(c) != null){
				c = alberoVisita.get(c) ;
			}
			else c = null ;
				
			System.out.println(c.toString());
 		}
		
		return percorso ;
	}

}