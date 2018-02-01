package com.sv.tumi.view.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import com.sv.tumi.db.entity.Personal;

@FacesConverter(value = "empConverter")
public class PickListEmpConverter implements Converter {
  @Override
  public Object getAsObject(FacesContext fc, UIComponent comp, String value) {
      @SuppressWarnings("unchecked")
	DualListModel<Personal> model = (DualListModel<Personal>) ((PickList) comp).getValue();
      for (Personal employee : model.getSource()) {
          if (employee.getCodigo().toString().equalsIgnoreCase(value)) {
              return employee;
          }
      }
      for (Personal employee : model.getTarget()) {
          if (employee.getCodigo().toString().equalsIgnoreCase(value)) {
              return employee;
          }
      }
      return null;
  }

  @Override
  public String getAsString(FacesContext fc, UIComponent comp, Object value) {
      return ((Personal) value).getCodigo().toString();
  }
}