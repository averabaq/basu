/* 
 * $Id: ActivityModel.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.domain;

import java.util.Set;
import java.io.Serializable;

/**
 * Activity model entity class. This class is bound to the
 * <strong>activity_model</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * @see Model
 * @see ModelType
 *  
 * @author averab
 * @version 1.0.0
 */
public class ActivityModel extends Model implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 394640468306418701L;
	/** Model activity instances. */
	private Set<ActivityInstance> instances;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ActivityModel() {		
	}
	/**
	 * Gets the {@link #instances} property.
	 * @return the {@link #instances} property.
	 */
	public Set<ActivityInstance> getInstances() {
		return instances;
	}
	/**
	 * Sets the {@link #instances} property.
	 * @param instances the {@link #instances} property to set.
	 */
	public void setInstances(Set<ActivityInstance> instances) {
		this.instances = instances;
	}
}