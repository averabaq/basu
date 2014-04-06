/* 
 * $Id: ProcessInstance.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;

/**
 * Process instance entity class. This class is bound to the
 * <strong>process_instance</strong> table at the data warehouse 
 * star diagram and managed through the EJB3 persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Process instance's identifier.</li>
 * <li><b>name</b>: Process instance's name.</li>
 * <li><b>description</b>: Process instance's description.</li>
 * <li><b>model</b>: Process instance's model.</li>
 * <li><b>correlator_id</b>: Process instance correlation identifier.</li>
 * <li><b>instance_src_id</b>: Process instance identifier at source.</li>
 * <li><b>source</b>: Process instance's source.</li> 
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-dw.ProcessInstance")
@Table(name="dw_process_instance", schema="event_dw")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
public class ProcessInstance implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 588874541115534614L;
	/** Process instance's identifier. */
	private Long id;
	/** Process instance's name. */
	private String name;
	/** Process instance's description. */
	private String description;
	/** Process instance's model */
	private ProcessModel model;
	/** Process instance identifier at source */
	private String instanceSrcId;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ProcessInstance() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	public Long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
	@Column(name="name", length=200, updatable=false)
	public String getName() {
		return name;
	}
	/**
	 * Sets the {@link #name} property.
	 * @param name the {@link #name} property to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the {@link #description} property.
	 * @return the {@link #description} property.
	 */
	@Column(name="description", columnDefinition="text", updatable=false)
	public String getDescription() {
		return description;
	}
	/**
	 * Sets the {@link #description} property.
	 * @param description the {@link #description} property to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="model", updatable=false)
	public ProcessModel getModel() {
		return model;
	}
	/**
	 * Sets the {@link #model} property.
	 * @param model the {@link #model} property to set.
	 */
	public void setModel(ProcessModel model) {
		this.model = model;
	}
	/**
	 * Gets the {@link #instanceSrcId} property.
	 * @return the {@link #instanceSrcId} property.
	 */
	@Column(name="instance_src_id", length=250, updatable=false)
	public String getInstanceSrcId() {
		return instanceSrcId;
	}
	/**
	 * Sets the {@link #instanceSrcId} property.
	 * @param instanceSrcId the {@link #instanceSrcId} property to set.
	 */
	public void setInstanceSrcId(String instanceSrcId) {
		this.instanceSrcId = instanceSrcId;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProcessInstance (");
		builder.append(id);
		builder.append("): ");
		builder.append(name);
		return builder.toString();
	}
	/**
	 * Returns a hash code value for the object.
	 * @return a hash code value for this object.
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	/**
	 * Indicates whether some other object is "equal to" this one.
	 * @param obj the reference object with which to compare.
	 * @return true if this object is the same as the obj argument; false otherwise.
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessInstance other = (ProcessInstance) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
