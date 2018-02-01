package com.sv.tumi.view.converter;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

 
@FacesValidator("dateRangeValidator")
public class PrimeDateRangeValidator implements Validator {
     
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        /*if (value == null) {
            return;
        }
         
        //Leave the null handling of startDate to required="true"
        Object startDateValue = component.getAttributes().get("startDate");
        if (startDateValue==null) {
            return;
        }
         
        Date startDate = (Date)startDateValue;
        Date endDate = (Date)value; 
        if (endDate.before(startDate)) {
            throw new ValidatorException(
                    FacesMessageUtil.newBundledFacesMessage(FacesMessage.SEVERITY_ERROR, "", "msg.dateRange", ((Calendar)component).getLabel(), startDate));
        }*/
    	
    	 if (value == null) {
             return; // Let required="true" handle.
         }

         UIInput startDateComponent = (UIInput) component.getAttributes().get("startDateComponent");

         if (!startDateComponent.isValid()) {
             return; // Already invalidated. Don't care about it then.
         }

         Date startDate = (Date) startDateComponent.getValue();

         if (startDate == null) {
             return; // Let required="true" handle.
         }

         Date endDate = (Date) value;

         if (startDate.after(endDate)) {
             startDateComponent.setValid(false);
             throw new ValidatorException(new FacesMessage(
                 FacesMessage.SEVERITY_ERROR, "Start date may not be after end date.", null));
         }
    }
}