package hn.lacolonia.data;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import java.text.NumberFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ProductosReport implements JRDataSource {

	private List<Producto> productos;
	private int counter = -1;
	private int maxCounter = 0;
	
	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
		this.maxCounter = this.productos.size()-1;
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
		if("CODIGO".equals(jrField.getName())) {
			return productos.get(counter).getCodigo();
		}else if("NOMBRE".equals(jrField.getName())) {
			return productos.get(counter).getNombre();
		}else if ("PRECIO".equals(jrField.getName())) {
			    double precio = productos.get(counter).getPrecio();
			    NumberFormat nf = NumberFormat.getCurrencyInstance();
			    String precioTexto = nf.format(precio);
			    return precioTexto;
		}else if("CATEGORIA".equals(jrField.getName())) {
			return productos.get(counter).getNombre_categoria();
		}else if("FECHA VENCIMIENTO".equals(jrField.getName())) {
			Date fecha_vencimiento = productos.get(counter).getFecha_vencimiento();
		    String fecha_vencimientoTexto = convertirFechaATexto(fecha_vencimiento);
		    return fecha_vencimientoTexto;
		}
		return "";
	}

	private String convertirFechaATexto(Date fecha_vencimiento) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    return sdf.format(fecha_vencimiento);
	}
}
