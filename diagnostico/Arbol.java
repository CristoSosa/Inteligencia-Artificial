package diagnostico;

public class Arbol {
	private Nodo raiz;
	
	public Arbol() {
		raiz = null;
	}
	//Metodos
	
	
	//Metodo vacio() boolean
	public boolean vacio () {
		return raiz == null;
	}
	
	//buscarNodo (nombre)
	public Nodo buscarNodo(String nombre) {
		return buscarPreorden(raiz, nombre);
	}
	
	private Nodo buscarPreorden(Nodo actual, String nombre) {
		if(actual==null) return null;
		
		if(actual.nombre.equals(nombre)) return actual;
		
		Nodo izquierda = buscarPreorden(actual.izquierdo, nombre);
		if(izquierda != null) return izquierda;
		
		return buscarPreorden(actual.derecho, nombre);
	}
	
}
