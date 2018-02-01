package com.sv.tumi.view.converter;


import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.sv.tumi.db.entity.Especialidad;
import com.sv.tumi.view.bean.SolicitudCapacitacionBean;

@FacesConverter(value = "especialidadConverter")
public class EspecialidadConverter implements Converter {
  
	@Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String id) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{solicitudCapacitacionBean}", SolicitudCapacitacionBean.class);
        
        if(id.equalsIgnoreCase("")){
        	return null;
        }

        SolicitudCapacitacionBean bean = (SolicitudCapacitacionBean)vex.getValue(ctx.getELContext());
        
        try {
        	return bean.getEspecialidadForConverter(Integer.valueOf(id));
		} catch (Exception e) {
			return null;
		}
        
        
    }

  @Override
  public String getAsString(FacesContext fc, UIComponent comp, Object value) {
	  
	  if(value instanceof Especialidad){
		  return ((Especialidad) value).getCodigo().toString();
	  } else {
		return null;
	}	  
      
  }
}