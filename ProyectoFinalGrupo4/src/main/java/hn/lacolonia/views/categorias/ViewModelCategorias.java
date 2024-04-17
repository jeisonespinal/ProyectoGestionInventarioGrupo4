package hn.lacolonia.views.categorias;

import java.util.List;

import hn.lacolonia.data.Categoria;
import hn.lacolonia.data.Proveedor;

public interface ViewModelCategorias {

	void mostrarCategoriasEnGrid(List<Categoria> items);
	void mostrarProveedoresEnCombobox(List<Proveedor> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
