package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	

	/*
	 * Nella Classe Base Albun è presente un valore "DURATA"
	 * che nelle tabelle non c'è ma che ci è utile aggiungere 
	 * per richieste future!
	 * (Salviamo durata dell'album)
	 */
	
	/*
	 * Durata nel database di ogni track sono in millisecondi(Int)
	 * Conviene lavorare in Minuti(Double)
	 */
	
	
	private Graph<Album, DefaultEdge> grafo;
	
	//per la ricorsione:
	private int dimMax;
	private List<Album> setMax;
	
	public void creaGrafo(Double durata) {
		
		this.grafo = new SimpleGraph<Album,DefaultEdge>(DefaultEdge.class);
		ItunesDAO dao = new ItunesDAO();
		
		//Creo Vertici
		Graphs.addAllVertices(this.grafo, dao.albumConDurata(durata));
		
		List<Arco> archi = dao.albumPlaylist();
		//Creo mappa di indici String(idAlbum)-Album
		Map<Integer, Album> mappa = new HashMap<Integer, Album>();
		for(Album a : this.grafo.vertexSet()) {
			mappa.put(a.getAlbumId(), a);
		}
		//Creo Archi
		for(Arco arco : archi) {
			if(mappa.containsKey(arco.getA1Id()) && mappa.containsKey(arco.getA2Id()) 
					&& arco.getA1Id()!=(arco.getA2Id())) {
				this.grafo.addEdge(mappa.get(arco.getA1Id()), mappa.get(arco.getA2Id()));
			}
		}
		
		System.out.println("Vertici: "+this.grafo.vertexSet().size());
		System.out.println("Archi: "+this.grafo.edgeSet().size());
	}
	
	public List<Album> getAlbum() {
		
		List<Album> listaAlbum = new ArrayList<Album>(this.grafo.vertexSet());
		Collections.sort(listaAlbum);
		return listaAlbum;
	}
	
	public Set<Album> getComponenteConnessa(Album album) {
		
		/* 
		 * Possiamo utilizzare il Connectivity Inspector: connectedSetOf(V vertex)
		 * ritorna una lista Set di tutti i vertici "vicini di casa" al mio vertice selezionato
		 *
		 * Altro modo è con il pathExists(V sourceVertex, V targetVertex)
		 */
	
		ConnectivityInspector<Album, DefaultEdge> connInsp = new ConnectivityInspector<>(this.grafo);
		//ritorno la mia componente connessa
		return connInsp.connectedSetOf(album);
				
	}
	
	public Set<Album> ricercaMassimo(Album album, double dTot) {
		
		if(album.getDurata()>dTot) {
			//non esiste una soluzione
			return null;
		}
		
		List<Album>  parziale = new ArrayList<Album>();
		parziale.add(album);
		//devo passargli tutti gli album della mia componente connessa a 'album'
		List<Album> tutti = new ArrayList<Album>(getComponenteConnessa(album));
		tutti.remove(album);
		
		dimMax=1; //poichè parte già con l'album selezionato
		setMax= new ArrayList<Album>(parziale);
		
		cerca(parziale, 1, album.getDurata(), dTot, tutti);
		
		return new HashSet<Album>(setMax); //poichè devo tornare un Set
	}
	
	private void cerca(List<Album> parziale, int livello,double durataParziale, double dTot, List<Album> tutti) {
		
		//double durataParziale=calcolaDurata(parziale); glielo passo da sopra
		
		//Caso Ottimo
		if(parziale.size()>dimMax) {
			dimMax=parziale.size();
			setMax=new ArrayList<>(parziale);
		}
		
		//provo ad aggiungere al parziale tutti album disponibili
		for(Album nuovo : tutti) {
			if(parziale.contains(nuovo)==false && durataParziale+nuovo.getDurata()<=dTot) {
				//aggiungo il mio album
				parziale.add(nuovo);
				cerca(parziale, livello+1, durataParziale+nuovo.getDurata(), dTot, tutti);
				parziale.remove(parziale.size()-1);
			}
		}
		//la  ricorsone finirà quando 'if' sarà falso
	}
	
}
