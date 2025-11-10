package ContractImovel.model.converter;

import ContractImovel.model.Fiador;
import ContractImovel.service.fiadorService;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "fiadorConverter", forClass = Fiador.class)
public class fiadorConverter implements Converter<Fiador> {

    private fiadorService getFiadorService() {
        return CDI.current().select(fiadorService.class).get();
    }

    @Override
    public Fiador getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            fiadorService service = getFiadorService();
            return service.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Fiador value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}