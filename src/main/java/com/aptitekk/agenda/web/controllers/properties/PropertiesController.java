package com.aptitekk.agenda.web.controllers.properties;

import com.aptitekk.agenda.core.entity.Property;
import com.aptitekk.agenda.core.properties.PropertyGroup;
import com.aptitekk.agenda.core.properties.PropertyKey;
import com.aptitekk.agenda.core.services.PropertiesService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class PropertiesController implements Serializable {

    @Inject
    private PropertiesService propertiesService;

    private List<PropertyInputGroup> propertyInputGroups;

    @PostConstruct
    private void init() {
        buildPropertyInputGroups();
    }

    private void buildPropertyInputGroups() {
        Map<PropertyGroup, List<Property>> propertyGroupKeyEntityMap = new LinkedHashMap<>();
        propertyInputGroups = new ArrayList<>();

        for (PropertyKey propertyKey : PropertyKey.values()) {
            if (!propertyGroupKeyEntityMap.containsKey(propertyKey.getGroup()))
                propertyGroupKeyEntityMap.put(propertyKey.getGroup(), new ArrayList<>());

            propertyGroupKeyEntityMap.get(propertyKey.getGroup()).add(propertiesService.getPropertyByKey(propertyKey));
        }

        for (Map.Entry<PropertyGroup, List<Property>> entry : propertyGroupKeyEntityMap.entrySet()) {
            propertyInputGroups.add(new PropertyInputGroup(entry.getKey(), entry.getValue()));
        }
    }

    public void saveProperties() {
        //Iterate over the different property input groups (Sections)
        for (PropertyInputGroup propertyInputGroup : propertyInputGroups) {
            String groupClientId = "propertiesEditForm:propertyGroup" + propertyInputGroup.getPropertyGroup().ordinal();

            Map<PropertyKey, String> propertiesInputMap = propertyInputGroup.getPropertiesInputMap();
            List<Property> propertyEntityList = propertyInputGroup.getPropertyEntityList();

            //Validate each property.
            boolean validationFailed = false;
            for (Map.Entry<PropertyKey, String> entry : propertiesInputMap.entrySet()) {
                String propertyClientId = "propertiesEditForm:propertyField" + entry.getKey().ordinal();

                if (entry.getValue() != null && entry.getValue().length() > entry.getKey().getMaxLength()) {
                    FacesContext.getCurrentInstance().addMessage(propertyClientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "This may not be longer than " + entry.getKey().getMaxLength() + " characters."));
                    validationFailed = true;
                }

                if (entry.getKey().getRegex() != null) {
                    if (entry.getValue() == null || !entry.getValue().matches(entry.getKey().getRegex())) {
                        FacesContext.getCurrentInstance().addMessage(propertyClientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, entry.getKey().getRegexMessage()));
                        validationFailed = true;
                    }
                }
            }

            //If there were no validation errors, save the properties for this input group.
            if (!validationFailed) {
                boolean changesMade = false;
                for (Property property : propertyEntityList) {

                    //No change to property; We can skip it. No need to make a query to change nothing.
                    if (propertiesInputMap.get(property.getPropertyKey()).equals(property.getPropertyValue()))
                        continue;

                    changesMade = true;
                    property.setPropertyValue(propertiesInputMap.get(property.getPropertyKey()));
                    try {
                        propertiesService.merge(property);
                    } catch (Exception e) {
                        FacesContext.getCurrentInstance().addMessage("propertiesEditForm", new FacesMessage(FacesMessage.SEVERITY_ERROR, null, "Could not update properties. Internal Server Error."));
                        e.printStackTrace();
                        return;
                    }
                }

                if (changesMade) {
                    FacesContext.getCurrentInstance().addMessage(groupClientId, new FacesMessage(FacesMessage.SEVERITY_INFO, null, "The changes to '" + propertyInputGroup.getPropertyGroup().getFriendlyName() + "' have been saved."));
                    propertyInputGroup.getPropertyGroup().firePropertiesChangedEvent();
                }
            }
        }

        buildPropertyInputGroups();
    }

    public void resetFields() {
        for (PropertyInputGroup propertyInputGroup : propertyInputGroups) {
            propertyInputGroup.resetInputMap();
        }
    }

    public List<PropertyInputGroup> getPropertyInputGroups() {
        return propertyInputGroups;
    }
}
