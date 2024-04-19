package hn.lacolonia.views.categorias;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import hn.lacolonia.controller.InteractorCategorias;
import hn.lacolonia.controller.InteractorImplCategorias;
import hn.lacolonia.data.Categoria;
import hn.lacolonia.data.Producto;
import hn.lacolonia.data.ProductosReport;
import hn.lacolonia.data.Proveedor;
import hn.lacolonia.data.SamplePerson;
import hn.lacolonia.data.CategoriasReport;
import hn.lacolonia.services.ReportGenerator;
import hn.lacolonia.views.MainLayout;
import hn.lacolonia.views.productos.ProductosView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Categorias")
@Route(value = "categorias/:idcategoria?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class CategoriasView extends Div implements BeforeEnterObserver, ViewModelCategorias {

	private final String CATEGORY_IDCATEGORIA = "idcategoria";
    private final String CATEGORY_EDIT_ROUTE_TEMPLATE = "categorias/%s/edit";

    private final Grid<Categoria> grid = new Grid<>(Categoria.class, false);

    private NumberField idcategoria;
    private TextField nombre;
    private TextField descripcion;
    private ComboBox<String> estado;
    private ComboBox<Proveedor> proveedor;

    private final Button cancel = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_CIRCLE));
    private final Button save = new Button("Guardar", new Icon(VaadinIcon.CHECK_CIRCLE));
    private final Button eliminar = new Button("Eliminar", new Icon(VaadinIcon.TRASH));

    private Categoria categoriaSeleccionada;
    private List<Categoria> elementos;
    private List<Proveedor> proveedores;
    private InteractorCategorias controlador;
    private Proveedor proveedorSeleccionado;

    public CategoriasView() {
        addClassNames("categorias-view");
        
        controlador = new InteractorImplCategorias(this);
        elementos = new ArrayList<>();
        proveedores = new ArrayList<>();
        
        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idcategoria").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);
        grid.addColumn("estado").setAutoWidth(true);
        grid.addColumn("nombre_proveedor").setAutoWidth(true).setHeader("Proveedor");

        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CATEGORY_EDIT_ROUTE_TEMPLATE, event.getValue().getIdcategoria()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CategoriasView.class);
            }
        });
        
        GridContextMenu<Categoria> menu = grid.addContextMenu();
        GridMenuItem<Categoria> assign = menu.addItem("Exportar");
        GridMenuItem<Categoria> menueliminar = menu.addItem("Eliminar");
        menueliminar.addMenuItemClickListener( e -> {
        });
        
        //assign.addComponentAsFirst(createIcon(VaadinIcon.FILE_REFRESH));
        
        GridSubMenu<Categoria> exportSubMenu = assign.getSubMenu();
        exportSubMenu.addItem("Portable Document Format (.pdf)", event -> {
        	Notification.show("Generando reporte PDF...");
        	generarReporte();
        });

        controlador.consultarCategorias();
        controlador.consultarProveedores();
        
        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.categoriaSeleccionada == null) {
                	//CREACION
                    this.categoriaSeleccionada = new Categoria();
                    
                    this.categoriaSeleccionada.setNombre(nombre.getValue());
                    this.categoriaSeleccionada.setDescripcion(descripcion.getValue());
                    this.categoriaSeleccionada.setEstado(estado.getValue());
                    this.categoriaSeleccionada.setProveedor(proveedor.getValue().getIdproveedor());
                    
                    this.controlador.crearCategorias(categoriaSeleccionada);
                    
                }else {
                	//ACTUALIZACION
                	this.categoriaSeleccionada.setIdcategoria(idcategoria.getValue().intValue());
                	this.categoriaSeleccionada.setNombre(nombre.getValue());
                	this.categoriaSeleccionada.setDescripcion(descripcion.getValue());
                    this.categoriaSeleccionada.setEstado(estado.getValue());
                    this.categoriaSeleccionada.setProveedor(proveedor.getValue().getIdproveedor());
                    
                    this.controlador.actualizarCategorias(categoriaSeleccionada);
                }
                clearForm();
                refreshGrid();
                UI.getCurrent().navigate(CategoriasView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        
        eliminar.addClickListener(e -> {
        	if(this.categoriaSeleccionada == null) {
        		mostrarMensajeError("Seleccione una categoria para poder eliminarla");
        	}else {
        		this.controlador.eliminarCategorias(categoriaSeleccionada.getIdcategoria());
        		 clearForm();
                 refreshGrid();
                 UI.getCurrent().navigate(ProductosView.class);
        	}
        });
    }
    
    private void generarReporte() {
    	ReportGenerator generador = new ReportGenerator();
    	CategoriasReport datasource = new CategoriasReport();
    	datasource.setCategorias(elementos);
    	Map<String, Object> parameters = new HashMap<>();
    	if(elementos.size() % 2 == 0) {
    		parameters.put("FIRMA", "firma1.png");
    	}else {
    		parameters.put("FIRMA", "firma2.png");
    	}
    	
    	boolean generado = generador.generarReportePDF("reporte_categorias", parameters, datasource);
    	if(generado) {
    		String ubicacion = generador.getReportPath();
    		Anchor url = new Anchor(ubicacion, "Abrir Reporte");
    		url.setTarget("_blank");
    		
    		Notification notification = new Notification(url);
    	    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    	    notification.setPosition(Position.MIDDLE);
    	    notification.setDuration(15000);
    	    notification.open();
    	}else {
    		mostrarMensajeError("Ocurrió un problema al generar el reporte:(");
    	}
	}

	private Component createIcon(VaadinIcon vaadinIcon) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)")
                .set("margin-inline-end", "var(--lumo-space-s")
                .set("padding", "var(--lumo-space-xs");
        return icon;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> idCategoria = event.getRouteParameters().get(CATEGORY_IDCATEGORIA).map(Long::parseLong);
        if (idCategoria.isPresent()) {
        	Categoria categoriaObtenida = obtenerCategoria(idCategoria.get());
            if (categoriaObtenida != null) {
                populateForm(categoriaObtenida);
            } else {
                Notification.show(
                		String.format("La categoria con id = %s no existe", idCategoria.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(CategoriasView.class);
            }
        }
    }
    
    private Categoria obtenerCategoria(Long idcategoria) {
    	Categoria encontrada = null;
		for(Categoria cat: elementos) {
			if(cat.getIdcategoria() == idcategoria) {
				encontrada = cat;
				break;
			}
			
		}
		return encontrada;
	}

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        idcategoria = new NumberField("ID");
        idcategoria.setId("txt_idcategoria");
        idcategoria.setPrefixComponent(VaadinIcon.ELLIPSIS_DOTS_H.create());
        
        nombre = new TextField("Nombre");
        nombre.setId("txt_nombre");
        nombre.setPrefixComponent(VaadinIcon.LIST_UL.create());
        
        descripcion = new TextField("Descripcion");
        descripcion.setId("txt_descripcion");
        descripcion.setPrefixComponent(VaadinIcon.FILE_TEXT_O.create());
        
        estado = new ComboBox<>("Estado");
        estado.setItems("Activa", "Inactiva");
        estado.setId("cbo_estado");
        estado.setPrefixComponent(VaadinIcon.CLIPBOARD_CHECK.create());
        
        proveedor = new ComboBox<>("Proveedor");
        proveedor.setId("cbo_proveedor");
        proveedor.setPrefixComponent(VaadinIcon.TRUCK.create());
        proveedor.setItemLabelGenerator(Proveedor::getNombre);
        
        formLayout.add(idcategoria, nombre, descripcion, estado, proveedor);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        cancel.setId("btn_cancelar");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.setId("btn_guardar");
        eliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        eliminar.setId("btn_eliminar");
        
        buttonLayout.add(save, cancel, eliminar);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
        this.controlador.consultarCategorias();
        this.controlador.consultarProveedores();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Categoria value) {
    	this.categoriaSeleccionada = value;
        if(value != null) {
        	idcategoria.setValue(Double.valueOf(value.getIdcategoria()));
            nombre.setValue(value.getNombre());
            descripcion.setValue(value.getDescripcion());
            estado.setValue(value.getEstado());
            
            proveedorSeleccionado = buscarProveedor(value.getProveedor());
            proveedor.setValue(proveedorSeleccionado);
        }else {
        	idcategoria.setValue(0.0);
            nombre.setValue("");
            descripcion.setValue("");
            estado.setValue("");
            proveedor.clear();
	    }
	}
    
    private Proveedor buscarProveedor(int idProveedor) {
		Proveedor encontrado = null;
		for(Proveedor prov: proveedores) {
			if(prov.getIdproveedor() == idProveedor) {
				encontrado = prov;
				break;
			}
		}
		return encontrado;
	}

	@Override
	public void mostrarCategoriasEnGrid(List<Categoria> items) {
		Collection<Categoria> itemsCollection = items;
		grid.setItems(itemsCollection);
		this.elementos = items;
	}

	@Override
	public void mostrarMensajeError(String mensaje) {
		Notification n = Notification.show(mensaje);
		n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
	}
	
	@Override
	public void mostrarProveedoresEnCombobox(List<Proveedor> items) {
		Collection<Proveedor> itemsCollection = items;
		proveedores = items;
		proveedor.setItems(items);	
	}
	
	@Override
	public void mostrarMensajeExito(String mensaje) {
		Notification notification = new Notification();
	    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

	    Icon icon = VaadinIcon.CHECK_CIRCLE.create();

	    var layout = new HorizontalLayout(icon, new Text(mensaje));
	    layout.setAlignItems(FlexComponent.Alignment.CENTER);

	    notification.add(layout);
		
	    notification.setPosition(Position.MIDDLE);
	    notification.setDuration(5000);
	    notification.open();
	}
}
