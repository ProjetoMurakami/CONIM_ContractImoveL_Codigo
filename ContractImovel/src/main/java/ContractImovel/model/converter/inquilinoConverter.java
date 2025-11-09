package ContractImovel.model.converter;

import ContractImovel.model.Inquilino;
import ContractImovel.service.inquilinoService;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "inquilinoConverter", forClass = Inquilino.class)
public class inquilinoConverter implements Converter<Inquilino> {

    private inquilinoService getInquilinoService() {
        return CDI.current().select(inquilinoService.class).get();
    }

    @Override
    public Inquilino getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            inquilinoService service = getInquilinoService();
            return service.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Inquilino value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}