package hn.lacolonia.data;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CategoriasReport implements JRDataSource {
	
	private List<Categoria> categorias;
	private int counter = -1;
	private int maxCounter = 0;

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
		this.maxCounter = this.categorias.size()-1;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public int getMaxCounter() {
		return maxCounter;
	}

	public void setMaxCounter(int maxCounter) {
		this.maxCounter = maxCounter;
	}

	@Override
	public boolean next() throws JRException {
		if(counter < maxCounter) {
			counter++;
			return true;
		}
		return false;
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		if("ID".equals(jrField.getName())) {
			int idCategoria = categorias.get(counter).getIdcategoria();
		    String idCategoriaTexto = String.valueOf(idCategoria);
		    return idCategoriaTexto;
		}else if("NOMBRE".equals(jrField.getName())) {
			return categorias.get(counter).getNombre();
		}else if ("DESCRIPCION".equals(jrField.getName())) {
			return categorias.get(counter).getDescripcion();
		}else if("ESTADO".equals(jrField.getName())) {
			return categorias.get(counter).getEstado();
		}else if("PROVEEDOR".equals(jrField.getName())) {
			return categorias.get(counter).getNombre_proveedor();
		}
		return "";
	}

}
