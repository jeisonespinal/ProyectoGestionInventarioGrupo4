package hn.lacolonia.controller;

import hn.lacolonia.data.CategoriasResponse;
import hn.lacolonia.data.Producto;
import hn.lacolonia.data.ProductosResponse;
import hn.lacolonia.model.DatabaseRepositoryImpl;
import hn.lacolonia.views.productos.ViewModelProductos;

public class InteractorImplProductos implements InteractorProductos {

	private DatabaseRepositoryImpl modelo;
	private ViewModelProductos vista;
	
	public InteractorImplProductos(ViewModelProductos view) {
		super();
		this.vista = view;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 30000L);
	}
	
	@Override
	public void consultarProductos() {
		try {
			ProductosResponse respuesta = this.modelo.consultarProductos();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay productos a mostrar");
			}else {
				this.vista.mostrarProductosEnGrid(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public void crearProductos(Producto nuevo) {
		try {
			boolean creado = this.modelo.crearProductos(nuevo);
			if(creado == true) {
				this.vista.mostrarMensajeExito("Producto creado exitosamente");
			}else {
				this.vista.mostrarMensajeError("Hubo un problema al crear el producto");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public void actualizarProductos(Producto cambiar) {
		try {
			boolean modificado = this.modelo.actualizarProductos(cambiar);
			if(modificado == true) {
				this.vista.mostrarMensajeExito("Producto modificado exitosamente");
			}else {
				this.vista.mostrarMensajeError("Hay un problema al modificar el producto");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
		
	}
	
	@Override
	public void eliminarProductos(String codigo) {
		try {
			boolean eliminado = this.modelo.eliminarProductos(codigo);
			if(eliminado == true) {
				this.vista.mostrarMensajeExito("Producto borrado exitosamente");
			}else {
				this.vista.mostrarMensajeError("Hubo un problema al borrar el producto");
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	@Override
	public void consultarCategorias() {
		try {
			CategoriasResponse respuesta = this.modelo.consultarCategorias();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay categorias a mostrar");
			}else {
				this.vista.mostrarCategoriasEnCombobox(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
		
	}
	
}
