package hn.lacolonia.model;

import hn.lacolonia.data.Categoria;
import hn.lacolonia.data.CategoriasResponse;
import hn.lacolonia.data.Producto;
import hn.lacolonia.data.ProductosResponse;
import hn.lacolonia.data.ProveedoresResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DatabaseRepository {
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@GET("/pls/apex/pav2_202110060116/pgis/productos")
	Call<ProductosResponse> consultarProductos();
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@POST("/pls/apex/pav2_202110060116/pgis/productos")
	Call<ResponseBody> crearProductos(@Body Producto nuevo);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@PUT("/pls/apex/pav2_202110060116/pgis/productos")
	Call<ResponseBody> actualizarProductos(@Body Producto cambiar);

	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@DELETE("/pls/apex/pav2_202110060116/pgis/productos")
	Call<ResponseBody> eliminarProductos(@Query("codigo") String codigo);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@GET("/pls/apex/pav2_202110060116/pgis/categorias")
	Call<CategoriasResponse> consultarCategorias();
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@POST("/pls/apex/pav2_202110060116/pgis/categorias")
	Call<ResponseBody> crearCategorias(@Body Categoria nueva);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@PUT("/pls/apex/pav2_202110060116/pgis/categorias")
	Call<ResponseBody> actualizarCategorias(@Body Categoria cambiar);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})
	@DELETE("/pls/apex/pav2_202110060116/pgis/categorias")
	Call<ResponseBody> eliminarCategorias(@Query("idcategoria") Integer idcategoria);
	
	@Headers({
	    "Accept: application/json",
	    "User-Agent: Retrofit-Sample-App"
	})    
	@GET("/pls/apex/pav2_202110060116/pgis/proveedores")
	Call<ProveedoresResponse> consultarProveedores();
}
