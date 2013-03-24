/* 
 * $Id: ProcessModel.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

/**
 * Process model entity class. This class is bound to the
 * <strong>process_model</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * @see Model
 * @see ModelType
 *  
 * @author averab
 * @version 1.0.0
 */
public class ProcessModel extends Model implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 285041684729027371L;
	/** Process model instances. */
	private Set<ProcessInstance> instances;
	/** Set of mappings associated to this process model */ 
	private Set<ProcessModelMapping> mappings;	

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ProcessModel() {		
	}
	/**
	 * Gets the {@link #instances} property.
	 * @return the {@link #instances} property.
	 */
	public Set<ProcessInstance> getInstances() {
		return instances;
	}
	/**
	 * Sets the {@link #instances} property.
	 * @param instances the {@link #instances} property to set.
	 */
	public void setInstances(Set<ProcessInstance> instances) {
		this.instances = instances;
	}
	/**
	 * Gets the {@link #instances} property as an ordered list object.
	 * @return the {@link #instances} property as an ordered list object.
	 */
	public List<ProcessInstance> getInstanceList() {
		if (this.instances == null)
			return null;
		ProcessInstance _instances[] = new ProcessInstance[this.instances.size()];
		this.instances.toArray(_instances);
		Arrays.sort(_instances);		
		return Arrays.asList(_instances);
	}			
	/**
	 * Gets the {@link #mappings} property.
	 * @return the {@link #mappings} property.
	 */
	public Set<ProcessModelMapping> getMappings() {
		return mappings;
	}
	/**
	 * Sets the {@link #mappings} property.
	 * @param mappings the {@link #mappings} property to set.
	 */
	public void setMappings(Set<ProcessModelMapping> mappings) {
		this.mappings = mappings;
	}	
}