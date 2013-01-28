/* 
 * $Id: ActivityModel.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.entity;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;

import java.util.Set;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

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
@Entity(name="event-store.ActivityModel")
@Table(name="activity_model", schema="event_store")
@DiscriminatorValue(value="activity")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
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
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch=FetchType.LAZY, mappedBy="model", targetEntity=ActivityInstance.class)
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