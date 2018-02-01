package com.sv.tumi.view.converter;


import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.sv.tumi.db.entity.Curso;
import com.sv.tumi.view.bean.SolicitudCapacitacionBean;

@FacesConverter(value = "cursoConverter")
public class CursoConverter implements Converter {
  
	@Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String id) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{solicitudCapacitacionBean}", SolicitudCapacitacionBean.class);
        
        if(id.equalsIgnoreCase("")){
        	return null;
        }
        
        try {
        	SolicitudCapacitacionBean bean = (SolicitudCapacitacionBean)vex.getValue(ctx.getELContext());
            return bean.getCursoForConverter(Integer.valueOf(id));
		} catch (Exception e) {
			return null;// TODO: handle exception
		}
        

        
    }

  @Override
  public String getAsString(FacesContext fc, UIComponent comp, Object value) {
	  
	  if(value instanceof Curso){
		  return ((Curso) value).getCodigo().toString();
	  } else {
		return null;
	}	  
      
  }
}