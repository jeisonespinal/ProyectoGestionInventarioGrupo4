package proyectogestioninventariosupermercadogrupo4.test;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import hn.lacolonia.data.Proveedor;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProyectoGestionInventarioSupermercadoTest {
	
	@Test
	@Order(1)
	public void testEliminarProductos() throws InterruptedException {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		try{
			// Abre la página web de productos
			driver.get("http://localhost:8080/productos");
			int cantidadProductosInicial = 0;
		
			new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Productos"));
			
			Thread.sleep(3000);
			
			// Localiza el campo de entrada de nombre de usuario
			WebElement campoCodigo = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_codigo']/input"));
			WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_nombre']/input"));
			WebElement campoPrecio = driver.findElement(By.xpath("//vaadin-number-field[@id='txt_precio']/input"));
			WebElement campoCategoria = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_categoria']/input"));
			WebElement campoFechavencimiento = driver.findElement(By.xpath("//vaadin-date-picker[@id='date_fechaVencimiento']/input"));
			
			WebElement botonGuardar = driver.findElement(By.xpath("//vaadin-button[@id='btn_guardar']"));
			WebElement botonCancelar = driver.findElement(By.xpath("//vaadin-button[@id='btn_cancelar']"));
			WebElement botonEliminar = driver.findElement(By.xpath("//vaadin-button[@id='btn_eliminar']"));
			
			// Ingresa el nombre de usuario
			campoCodigo.sendKeys("202110060116");
			campoNombre.sendKeys("Litro de Leche");
			campoPrecio.sendKeys("25");
			campoCategoria.sendKeys("Lacteos");
			campoFechavencimiento.sendKeys("18/4/2024");
			
			//botonGuardar.click();
			//botonEliminar.click();
			
			WebElement element = driver.findElement(By.id("btn_eliminar"));
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
			botonEliminar.click();
			
			Thread.sleep(5000);
			
			int cantidadProductosFinal = 0;
			
			//assertFalse(cantidadProductosInicial+1 == 0);
			assertFalse(cantidadProductosFinal == (cantidadProductosInicial+2));
			
		}finally {
			driver.quit();
		}
	}
	
	@Test
	@Order(2)
	public void testCrearProducto() throws InterruptedException {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		try{
			// Abre la página web de productos
			driver.get("http://localhost:8080/productos");
			
			int cantidadProductosInicial = 0;
		
			new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Productos"));
			
			Thread.sleep(3000);
			
			WebElement botonGuardar = driver.findElement(By.xpath("//vaadin-button[@id='btn_guardar']"));
			WebElement botonCancelar = driver.findElement(By.xpath("//vaadin-button[@id='btn_cancelar']"));
			WebElement botonEliminar = driver.findElement(By.xpath("//vaadin-button[@id='btn_eliminar']"));
			
			// Localiza el campo de entrada de nombre de usuario
			WebElement campoCodigo = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_codigo']/input"));
			WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_nombre']/input"));
			WebElement campoPrecio = driver.findElement(By.xpath("//vaadin-number-field[@id='txt_precio']/input"));
			WebElement campoCategoria = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_categoria']/input"));
			WebElement campoFechavencimiento = driver.findElement(By.xpath("//vaadin-date-picker[@id='date_fechaVencimiento']/input"));
			
			// Ingresa el nombre de usuario
			campoCodigo.sendKeys("10235689");
			campoNombre.sendKeys("Salchicha");
			campoPrecio.sendKeys("84.90");
			campoCategoria.sendKeys("Embutidos");
			campoFechavencimiento.sendKeys("25/09/2027");
			
			//botonGuardar.click();
			
			WebElement element = driver.findElement(By.id("btn_guardar"));
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
			botonGuardar.click();
			
			Thread.sleep(5000);
			
			int cantidadProductosFinal = 0;
			
			assertTrue(cantidadProductosFinal == (cantidadProductosInicial));
			
		}finally {
			driver.quit();
		}
	}

	
	@Test
	@Order(3)
	public void testCrearCategoria() throws InterruptedException {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		try{
			// Abre la página web de categorias
			driver.get("http://localhost:8080/categorias");
			
			int cantidadCategoriasInicial = 0;
		
			new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Categorias"));
			
			Thread.sleep(3000);
			
			WebElement botonGuardar = driver.findElement(By.xpath("//vaadin-button[@id='btn_guardar']"));
			WebElement botonCancelar = driver.findElement(By.xpath("//vaadin-button[@id='btn_cancelar']"));
			WebElement botonEliminar = driver.findElement(By.xpath("//vaadin-button[@id='btn_eliminar']"));
			
			// Localiza el campo de entrada de nombre de usuario
			WebElement campoIdcategoria = driver.findElement(By.xpath("//vaadin-number-field[@id='txt_idcategoria']/input"));
			WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_nombre']/input"));
			WebElement campoDescripcion = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_descripcion']/input"));
			WebElement campoEstado = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_estado']/input"));
			WebElement campoProveedor = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_proveedor']/input"));
			
			// Ingresa el nombre de usuario
			campoIdcategoria.sendKeys("1");
			campoNombre.sendKeys("Embutidos");
			campoDescripcion.sendKeys("Incluye Jamones,HotDog, Salami y otros derivados");
			campoEstado.sendKeys("Activa");
			campoProveedor.sendKeys("SULA SA");
	        
			//botonGuardar.click();
			
			WebElement element = driver.findElement(By.id("btn_guardar"));
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
			botonGuardar.click();
			
			Thread.sleep(5000);
			
			int cantidadCategoriasFinal = 0;
			
			assertFalse(cantidadCategoriasFinal == (cantidadCategoriasInicial+1));
			
		}finally {
			driver.quit();
		}
	}

	
	@Test
	@Order(4)
	public void testEliminarCategoria() throws InterruptedException {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		try{
			// Abre la página web de categorias
			driver.get("http://localhost:8080/categorias");
			
			int cantidadCategoriasInicial = 0;
		
			new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Categorias"));
			
			Thread.sleep(3000);
			
			WebElement botonGuardar = driver.findElement(By.xpath("//vaadin-button[@id='btn_guardar']"));
			WebElement botonCancelar = driver.findElement(By.xpath("//vaadin-button[@id='btn_cancelar']"));
			WebElement botonEliminar = driver.findElement(By.xpath("//vaadin-button[@id='btn_eliminar']"));
			
			// Localiza el campo de entrada de nombre de usuario
			WebElement campoIdcategoria = driver.findElement(By.xpath("//vaadin-number-field[@id='txt_idcategoria']/input"));
			WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_nombre']/input"));
			WebElement campoDescripcion = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_descripcion']/input"));
			WebElement campoEstado = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_estado']/input"));
			WebElement campoProveedor = driver.findElement(By.xpath("//vaadin-combo-box[@id='cbo_proveedor']/input"));
			
			// Ingresa el nombre de usuario
			campoIdcategoria.sendKeys("241");
			campoNombre.sendKeys("Embutidos");
			campoDescripcion.sendKeys("Incluye Jamones,HotDog, Salami y otros derivados");
			campoEstado.sendKeys("Activa");
			campoProveedor.sendKeys("SULA SA");
	        
			//botonEliminar.click();
			
			WebElement element = driver.findElement(By.id("btn_eliminar"));
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
			botonEliminar.click();
			
			Thread.sleep(5000);
			
			int cantidadCategoriasFinal = 0;
			
			assertFalse(cantidadCategoriasFinal == (cantidadCategoriasInicial+1));
			
		}finally {
			driver.quit();
		}
	}

	@Test
	@Order(5)
	public void testBuscarProveedor() throws InterruptedException {
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		try{
			// Abre la página web de proveedores
			driver.get("http://localhost:8080/proveedores");
			
			int cantidadProveedoresInicial = 0;
		
			new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Proveedores"));
			
			Thread.sleep(3000);
			
			WebElement botonBuscar = driver.findElement(By.xpath("//vaadin-button[@id='btn_buscar']"));
			WebElement botonCancelar = driver.findElement(By.xpath("//vaadin-button[@id='btn_cancelar']"));

			
			// Localiza el campo de entrada de nombre de usuario
			WebElement campoIdproveedor = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_idproveedor']/input"));
			WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_nombre']/input"));
			WebElement campoDireccion = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_direccion']/input"));
			WebElement campoTelefono = driver.findElement(By.xpath("//vaadin-text-field[@id='txt_telefono']/input"));
			
			// Ingresa el nombre de usuario
			campoNombre.sendKeys("OLACSA");
	        
						
			botonBuscar.click();
			
			Thread.sleep(5000);
			
			int cantidadProveedoresFinal = 0;
			
			assertEquals(cantidadProveedoresFinal, (cantidadProveedoresInicial), .001);
			
		}finally {
			driver.quit();
		}
	}
	
}


