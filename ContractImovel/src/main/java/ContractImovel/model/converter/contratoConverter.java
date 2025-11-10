package ContractImovel.model.converter;

import ContractImovel.model.ContratoLocacao;
import ContractImovel.service.contratoLocacaoService;

import javax.enterprise.inject.spi.CDI;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "contratoConverter", forClass = ContratoLocacao.class)
public class contratoConverter implements Converter<ContratoLocacao> {

    private contratoLocacaoService getContratoService() {
        return CDI.current().select(contratoLocacaoService.class).get();
    }

    @Override
    public ContratoLocacao getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            Long id = Long.valueOf(value);
            contratoLocacaoService service  = getContratoService();
            return service.buscarPorId(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, ContratoLocacao value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}