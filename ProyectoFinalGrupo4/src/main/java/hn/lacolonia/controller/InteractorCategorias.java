package hn.lacolonia.controller;

import hn.lacolonia.data.Categoria;

public interface InteractorCategorias {
	
	void consultarCategorias();
	void consultarProveedores();
	void crearCategorias(Categoria nueva);
	void actualizarCategorias(Categoria cambiar);
	void eliminarCategorias(Integer idcategoria);
}
