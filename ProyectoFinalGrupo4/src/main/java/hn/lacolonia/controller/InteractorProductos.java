package hn.lacolonia.controller;

import hn.lacolonia.data.Producto;

public interface InteractorProductos {

	void consultarProductos();
	void consultarCategorias();
	void crearProductos(Producto nuevo);
	void actualizarProductos(Producto cambiar);
	void eliminarProductos(String codigo);
}
