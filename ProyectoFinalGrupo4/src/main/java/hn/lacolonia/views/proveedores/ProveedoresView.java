package hn.lacolonia.views.proveedores;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;

import hn.lacolonia.controller.InteractorImplProveedores;
import hn.lacolonia.controller.InteractorProveedores;
import hn.lacolonia.data.Producto;
import hn.lacolonia.data.Proveedor;
import hn.lacolonia.views.MainLayout;
import hn.lacolonia.views.productos.ProductosView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@PageTitle("Proveedores")
@Route(value = "proveedores", layout = MainLayout.class)
@Uses(Icon.class)
public class ProveedoresView extends Div implements /*BeforeEnterObserver*/ ViewModelProveedores {

	/*private final String SUPPLIER_IDPROVEEDOR = "idproveedor";
    private final String SUPPLIER_EDIT_ROUTE_TEMPLATE = "proveedores/%s/edit";*/
    
    private Grid<Proveedor> grid = new Grid<>(Proveedor.class, false);
    
    private Filters filters;
    
    private Proveedor proveedorSeleccionado;
    private List<Proveedor> elementos;
    private InteractorProveedores controlador;
    
    public ProveedoresView() {
        setSizeFull();
        addClassNames("proveedores-View");
        
        filters = new Filters(() -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
        
        controlador = new InteractorImplProveedores(this);
        elementos = new ArrayList<>();
        controlador.consultarProveedores();
        
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }
    
    public static class Filters extends Div implements Specification<Proveedor> {
    	
    	private TextField idproveedor = new TextField("ID");
        private TextField nombre = new TextField("Nombre");
        private TextField direccion = new TextField("Direccion");
        private TextField telefono = new TextField("Telefono");
        
        public Filters(Runnable onSearch) {
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            
            // Action buttons
            Button cancelBtn = new Button("Cancelar", new Icon(VaadinIcon.CLOSE_CIRCLE));
            cancelBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
            cancelBtn.setId("btn_cancelar");
            cancelBtn.addClickListener(e -> {
            	idproveedor.clear();
                nombre.clear();
                direccion.clear();
                telefono.clear();
                onSearch.run();
            });
            
            Button searchBtn = new Button("Buscar", new Icon(VaadinIcon.SEARCH));
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());
            
            Div actions = new Div(searchBtn, cancelBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            idproveedor.setId("txt_idproveedor");
            idproveedor.setPrefixComponent(VaadinIcon.ELLIPSIS_DOTS_H.create());
            
            nombre.setId("txt_nombre");
            nombre.setPrefixComponent(VaadinIcon.CLIPBOARD_USER.create());
            
            direccion.setId("txt_direccion");
            direccion.setPrefixComponent(VaadinIcon.MAP_MARKER.create());
            
            telefono.setId("txt_telefono");
            telefono.setPrefixComponent(VaadinIcon.PHONE.create());
            
            add(idproveedor, nombre, direccion, telefono, actions);
            
            idproveedor.addValueChangeListener(event -> onSearch.run());
            nombre.addValueChangeListener(event -> onSearch.run());
            direccion.addValueChangeListener(event -> onSearch.run());
            telefono.addValueChangeListener(event -> onSearch.run());
            
        }
        
        public List<Proveedor> applyFilters(List<Proveedor> proveedores) {
            return proveedores.stream()
                .filter(proveedor -> idMatches(proveedor) && nombreMatches(proveedor) &&
                    direccionMatches(proveedor) && telefonoMatches(proveedor))
                .collect(Collectors.toList());
        }
        
        private boolean idMatches(Proveedor proveedor) {
            String idFilter = String.valueOf(idproveedor.getValue()).toLowerCase();
            return String.valueOf(proveedor.getIdproveedor()).toLowerCase().contains(idFilter);
        }
        
        private boolean nombreMatches(Proveedor proveedor) {
            String nombreFilter = nombre.getValue().toLowerCase();
            return proveedor.getNombre().toLowerCase().contains(nombreFilter);
        }
        
        private boolean direccionMatches(Proveedor proveedor) {
            String direccionFilter = direccion.getValue().toLowerCase();
            return proveedor.getDireccion().toLowerCase().contains(direccionFilter);
        }
        
        private boolean telefonoMatches(Proveedor proveedor) {
            String telefonoFilter = telefono.getValue().toLowerCase();
            return proveedor.getTelefono().toLowerCase().contains(telefonoFilter);
        }
        
		@Override
        public Predicate toPredicate(Root<Proveedor> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (idproveedor != null) {
                String idString = String.valueOf(idproveedor);
                Predicate idMatch = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("idproveedor").as(String.class)),
                    idString + "%"
                );
                predicates.add(idMatch);
            }
            
            if (!nombre.isEmpty()) {
                String lowerCaseFilter = nombre.getValue().toLowerCase();
                Predicate nameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");
                predicates.add(nameMatch);
            }
            
            if (!direccion.isEmpty()) {
                String lowerCaseFilter = direccion.getValue().toLowerCase();
                Predicate direccionMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");
                predicates.add(direccionMatch);
            }
            
            if (!telefono.isEmpty()) {
                String databaseColumn = "telefono";
                String ignore = "- ()";

                String lowerCaseFilter = ignoreCharacters(ignore, telefono.getValue().toLowerCase());
                Predicate telefonoMatch = criteriaBuilder.like(
                        ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
                        "%" + lowerCaseFilter + "%");
                predicates.add(telefonoMatch);
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            
        }
		
        private String ignoreCharacters(String characters, String in) {
            String result = in;
            for (int i = 0; i < characters.length(); i++) {
                result = result.replace("" + characters.charAt(i), "");
            }
            return result;
        }
        
        private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
                Expression<String> inExpression) {
            Expression<String> expression = inExpression;
            for (int i = 0; i < characters.length(); i++) {
                expression = criteriaBuilder.function("replace", String.class, expression,
                        criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
            }
            return expression;
        }

    }
    
    private Component createGrid() {
        grid = new Grid<>(Proveedor.class, false);
        grid.addColumn("idproveedor").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("direccion").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
     
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        return grid;
    }

    @Override
    public void mostrarProveedoresEnGrid(List<Proveedor> items) {
        List<Proveedor> proveedoresFiltrados = filters.applyFilters(items);
        Collection<Proveedor> itemsCollection = proveedoresFiltrados;
        grid.setItems(itemsCollection);
        this.elementos = items;
    }
    
    @Override
	public void mostrarMensajeError(String mensaje) {
		Notification.show(mensaje);
	}

    private void refreshGrid() {
    	grid.select(null);
        grid.getDataProvider().refreshAll();
        this.controlador.consultarProveedores();
    }

	/*@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		
	}*/
    
    /*private void clearForm() {
    	populateForm(null);
	}*/
    
    /*private void populateForm(Proveedor value) {
    	this.proveedorSeleccionado = value;
    	if(value != null) {
    		idproveedor.setValue(Double.valueOf(value.getIdproveedor()));
    		nombre.setValue(value.getNombre());
    		direccion.setValue(value.getDireccion());
    		telefono.setValue(value.getTelefono());
    	}else {
    		idproveedor.setValue(0.0);
    		nombre.setValue("");
    		direccion.setValue("");
    		telefono.setValue("");
    	}
    }*/
}
