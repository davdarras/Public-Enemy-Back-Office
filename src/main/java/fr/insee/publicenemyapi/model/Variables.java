package fr.insee.publicenemyapi.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Maps the variables element in the XML file
 */
@XmlRootElement(name="Variables")
@XmlAccessorType(XmlAccessType.FIELD)
public class Variables {
	
	public Variables() {
		super();
	}
	
	public Variables(List<Object> variables) {
		super();
		this.variables = variables;
	}

	@XmlAnyElement
    private List<Object> variables;
	

	/**
	 * @return the variables
	 */
	public List<Object> getVariables() {
		return variables;
	}

	/**
	 * @param allVariable the variables to set
	 */
	public void setVariables(List<Object> variables) {
		this.variables = variables;
	}

	
}
