package ContractImovel.model.converter;

import ContractImovel.model.Usuario;
import ContractImovel.service.usuarioService;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "usuarioConverter", forClass = Usuario.class)
public class usuarioConverter implements Converter<Usuario> {
    
    private usuarioService getUsuarioService() {
        return CDI.current().select(usuarioService.class).get();
    } 
    
    @Override
    public Usuario getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            usuarioService service = getUsuarioService();
            return service.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Usuario value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }

}
