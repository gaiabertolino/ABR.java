/** @author Gaia Bertolino */
package Bertolino.ABR;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class AlberoBinarioDiRicerca<T extends Comparable<? super T>> implements CollezioneOrdinata<T>{
	
	private static class Nodo<E>{
		E info;
		Nodo<E> fS, fD;
		int cfs, cfd;
	}//nodo
	private Nodo<T> radice=null;
	private Stack<Nodo<T>> sbilanciati = new Stack<>();
	
	public int size() {
		return size(radice);
	}//size
	private int size( Nodo<T> radice ) {
		if( radice==null ) return 0;
		return 1+size( radice.fS )+size( radice.fD );
	}//size
	
	public void clear() {
		radice=null;
	}//clear
	
	public boolean contains( T x ) {
		return contains(radice,x);
	}
	private boolean contains( Nodo<T> radice, T e ) {
		if( radice==null ) return false;
		if( radice.info.equals(e) ) return true;
		if( radice.info.compareTo(e)>0 ) return contains( radice.fS, e );
		return contains( radice.fD, e );
	}//contains
	
	public boolean bilanciato(Nodo<T> radice){
		if(radice== null)
			return true;
		return Math.abs(radice.cfd - radice.cfs) <= 1;
	}//bilanciato 

	private Nodo<T> insert(Nodo<T> radice, List<T> lista, int inizio, int fine) {
		if (fine<=inizio) {
			return radice; 
		}
		int med = (inizio+fine)/2;
		radice = new Nodo<T>();
		
		radice.info = lista.get(med);

		radice.fD = insert(radice.fD, lista, med+1, fine);
		radice.fS = insert(radice.fS, lista, inizio, med);
		
		if (radice.fD!= null)
			radice.cfd = 1 + radice.fD.cfd + radice.fD.cfs;
		else
			radice.cfd = 0;
		if (radice.fS!= null)
			radice.cfs = 1 + radice.fS.cfd + radice.fS.cfs;
		else
			radice.cfs = 0;
		
		return radice;
	}//insert
	
	public void add( T x ) {
		sbilanciati = new Stack<>();
		radice=add(radice,x);
		if (!sbilanciati.isEmpty()) {
			List<T> lista = new LinkedList<>();
			inOrder(lista);
			radice = sbilanciati.pop();
			radice.fD = null;
			radice.fS = null;
			radice.cfd = 0;
			radice.cfs = 0;
			radice = insert(radice, lista, 0, lista.size());
		}
	}
	private Nodo<T> add( Nodo<T> radice, T x ){
		if( radice==null ) {
			Nodo<T> n=new Nodo<>();
			n.info=x; n.fS=null; n.fD=null;			
			return n;
		}
		if( radice.info.compareTo(x)>0 ) {
			radice.fS=add( radice.fS, x );
			radice.cfs++;
			
			if (!bilanciato(radice)) {
				sbilanciati.push(radice);
			}
			return radice;
		}
		radice.fD=add( radice.fD, x );
		radice.cfd++;
		if (!bilanciato(radice)) {
			sbilanciati.push(radice);
		}
		return radice;
	}//add

	public void remove( T x) {
		sbilanciati = new Stack<>();
		radice=remove( radice, x );
		if (!sbilanciati.isEmpty()) {
			List<T> lista = new LinkedList<>();
			inOrder(lista);
			radice = sbilanciati.pop();
			radice.fD = null;
			radice.fS = null;
			radice.cfd = 0;
			radice.cfs = 0;
			radice = insert(radice, lista, 0, lista.size());
		}
	}
	private Nodo<T> remove( Nodo<T> radice, T x ){
		if( radice==null ) return null;
		if( radice.info.compareTo(x)>0 ) {
			radice.fS=remove( radice.fS,x );
			radice.cfs--;
			if (!bilanciato(radice)) {
				sbilanciati.push(radice);
			}
			return radice;
		}
		if( radice.info.compareTo(x)<0 ) {
			radice.fD=remove( radice.fD, x );
			radice.cfd--;
			if (!bilanciato(radice)) {
				sbilanciati.push(radice);
			}
			return radice;
		}
		if( radice.fS==null && radice.fD==null ) {
			return null;
		}
		if( radice.fS==null ) {
			return radice.fD;
		}
		if( radice.fD==null ) { 
			return radice.fS;
		}
		if( radice.fD.fS==null ) {
			radice.info=radice.fD.info;
			radice.fD=radice.fD.fD; 
			return radice;
		}
		Nodo<T> padre=radice.fD; 
		Nodo<T> figlio=padre.fS;
		while( figlio.fS!=null ) {
			padre=figlio; figlio=figlio.fS;
		}
		radice.info=figlio.info;
		padre.fS=figlio.fD; 
		radice.cfd--;
		return radice;
	}//remove
	
	
	public void inOrder( List<T> lis ) {
		inOrder( radice, lis );
	}
	private void inOrder( Nodo<T> radice, List<T> lis ) {
		if( radice!=null ) {
			inOrder( radice.fS, lis );
			lis.add( radice.info );
			inOrder( radice.fD, lis ); 
		}
	}//inOrder
	
	public void preOrder( List<T> lis ) {
		preOrder(radice,lis);
	}
	private void preOrder( Nodo<T> radice, List<T> lis ) {
		if( radice!=null ) {
			lis.add(radice.info); 
			preOrder( radice.fS, lis );
			preOrder( radice.fD, lis );
		}
	}//preOrder
	
	public void postOrder( List<T> lis ) {
		postOrder( radice, lis );
	}
	private void postOrder( Nodo<T> radice, List<T> lis ) {
		if( radice!=null ) {
			postOrder( radice.fS, lis );
			postOrder( radice.fD, lis );
			lis.add( radice.info );
		}
	}//postOrder
	
	public T get( T x ) {
		return get( radice,x );
	}
	private T get( Nodo<T> radice, T e ) {
		if( radice==null ) return null;
		if( radice.info.equals(e) ) return radice.info;
		if( radice.info.compareTo(e)>0 ) return get( radice.fS, e );
		return get( radice.fD, e );
	}//get
	
	public boolean isEmpty() { return radice==null; }
	public boolean isFull() { return false; }
	
	public Iterator<T> iterator(){ 
		return new ABRIterator(); 
	}//iterator()
	
	private class ABRIterator implements Iterator<T>{
		Stack<Nodo<T>> nodi = new Stack<>();
		T cor;
		
		public ABRIterator() {
			ABRIt(radice);
		}//ABRIterator
		
		private void ABRIt(Nodo<T> radice) {
			if (radice == null)
				return;
			nodi.add(radice);
			ABRIt(radice.fS);
		}//ABRIterator

		@Override
		public boolean hasNext() {
			if (nodi.isEmpty())
				return false;
			return true;
		}//hasNext

		@Override
		public T next() {
			Nodo<T> next = nodi.pop();
			ABRIt(next.fD);
			cor = next.info;
			return cor;
		}//next
		
		@Override
		public void remove() {
			AlberoBinarioDiRicerca.this.remove(cor);
			cor = null;
			nodi = new Stack<>();
			ABRIt(radice);
		}//remove
		
	}//ABRIterator
	
	public String toString() {
		StringBuilder sb=new StringBuilder(100);
		sb.append('[');
		Iterator<T> it=this.iterator();
		while( it.hasNext() ) {
			sb.append( it.next() );
			if( it.hasNext() ) sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}//toString
	
	public boolean equals( Object x ) {
		if( !(x instanceof AlberoBinarioDiRicerca) ) return false;
		if( x==this ) return true;
		@SuppressWarnings("unchecked")
		AlberoBinarioDiRicerca<T> abr=(AlberoBinarioDiRicerca<T>)x;
		return equals( this.radice, abr.radice );
	}//equals
	
	private boolean equals( Nodo<T> r1, Nodo<T> r2 ) {
		if( r1==null && r2==null ) return true;
		if( r1==null || r2==null ) return false;
		if( !r1.info.equals(r2.info) ) return false;
		return equals( r1.fS, r2.fS ) && equals( r1.fD, r2.fD );
	}//equals
	
	public int hashCode() {
		return toString().hashCode();
	}//hashCode
	
	public AlberoBinarioDiRicerca<T> copy(){
		AlberoBinarioDiRicerca<T> copia = new AlberoBinarioDiRicerca<>();
		return copy(radice, copia);
	}//copy
	
	private AlberoBinarioDiRicerca<T> copy(Nodo<T> radice, AlberoBinarioDiRicerca<T> copia){
		if (radice == null)
			return null;
		copia.add(radice.info);
		copy(radice.fS, copia);
		copy(radice.fD, copia);
		return copia;
	}
	
	public void build( T[] a ) {
		clear();
		List<T> lista = new ArrayList<>();
		inOrder(lista);
		for (int i=0; i<lista.size(); i++)
			a[i] = lista.get(i);
	}//build
	
	public int altezza() {
		return altezza(radice);
	}
	private int altezza(Nodo<T> radice) {
		if (radice == null)
			return -1;
		if (radice.cfd > radice.cfs) {
			return 1+altezza(radice.fD);
		}
		return 1+altezza(radice.fS);
	}//altezza

	public void visitaPerLivelli( List<T> visitati ) {
		if( radice==null ) return;
		LinkedList<Nodo<T>> coda=new LinkedList<>();
		coda.addLast( radice );
		while( !coda.isEmpty() ) {
			Nodo<T> n=coda.removeFirst();
			//visita n
			visitati.add(n.info);
			if( n.fS!=null ) coda.addLast( n.fS );
			if( n.fD!=null ) coda.addLast( n.fD );
		}
	}//visitaPerLivelli
	
	public static void main(String[] args) {
		
		AlberoBinarioDiRicerca<Integer> b = new AlberoBinarioDiRicerca <>();
		b.add(50);
		System.out.println("Ho aggiunto 50. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nNon è stato necessario ribilanciare\n");
		
		b.add(37);
		System.out.println("Ho aggiunto 37. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nNon è stato necessario ribilanciare\n");
		
		b.add(-1);
		System.out.println("Ho aggiunto -1. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nE' stato necessario ribilanciare\n");
		
		b.remove(50);
		System.out.println("Ho rimosso 50. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nNon è stato necessario ribilanciare\n");
		
		b.add(4);
		System.out.println("Ho aggiunto 4. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nE' stato necessario ribilanciare\n");
		
		b.add(27);
		System.out.println("Ho aggiunto 27. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nNon è stato necessario ribilanciare\n");
		
		b.remove(4);
		System.out.println("Ho rimosso 4. "
				+ "\nOra b è: " + b +
				".\nLa radice principale è: " + b.radice.info +
				".\nA sinistra vi sono " + b.radice.cfs + " nodi e a destra ve ne sono " + b.radice.cfd 
				+ "\nNon è stato necessario ribilanciare\n");
		
		System.out.println("La lista prima dello svuotamento tramite iteratore è: " + b);
		Iterator<Integer> i = b.iterator();
		while (i.hasNext()) {
			i.next();
			i.remove();
		}
		System.out.println("La lista dopo lo svuotamento tramite iteratore è: " + b);
	}
		
}//AlberoBinarioDiRicerca
