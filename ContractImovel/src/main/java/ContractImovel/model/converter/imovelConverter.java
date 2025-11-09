package ContractImovel.model.converter;

import ContractImovel.model.Imovel;
import ContractImovel.service.imovelService;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "imovelConverter", forClass = Imovel.class)
public class imovelConverter implements Converter<Imovel> {

    private imovelService getImovelService() {
        return CDI.current().select(imovelService.class).get();
    }

    @Override
    public Imovel getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            Long id = Long.valueOf(value);
            imovelService service = getImovelService();
            return service.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Imovel value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}