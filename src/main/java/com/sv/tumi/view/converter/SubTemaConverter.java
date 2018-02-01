package com.sv.tumi.view.converter;


import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.sv.tumi.db.entity.Subtema;
import com.sv.tumi.view.bean.SolicitudCapacitacionBean;

@FacesConverter(value = "subtemaConverter")
public class SubTemaConverter implements Converter {
  
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
        return bean.getSubTemaForConverter(Integer.valueOf(id));
    }

  @Override
  public String getAsString(FacesContext fc, UIComponent comp, Object value) {
	  
	  if(value instanceof Subtema){
		  return ((Subtema) value).getCodigo().toString();
	  } else {
		return null;
	}	  
      
  }
}