/* 
 * $Id: ProcessModel.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;

/**
 * Process model entity class. This class is bound to the
 * <strong>process_model</strong> table at the data warehouse 
 * star diagram and managed through the EJB3 persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Process model's identifier.</li>
 * <li><b>name</b>: Process model's name.</li>
 * <li><b>source</b>: Process model's source.</li>
 * <li><b>description</b>: Process model's description.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-dw.ProcessModel")
@Table(name="dw_process_model", schema="event_dw")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
public class ProcessModel implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 4684582003300562014L;
	/** Process model identifier. */
	private Long id;
	/** Process model name. */
	private String name;
	/** Process model source. */
	private String source;	
	/** Process model description. */
	private String description;
	/** Process model instances. */
	private Set<ProcessInstance> instances;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ProcessModel() {		
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
	@Column(name="name", length=250, updatable=false)
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
	 * Gets the {@link #source} property.
	 * @return the {@link #source} property.
	 */
	@Column(name="source", length=250, updatable=false)
	public String getSource() {
		return source;
	}
	/**
	 * Sets the {@link #source} property.
	 * @param source the {@link #source} property to set.
	 */
	public void setSource(String source) {
		this.source = source;
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
	 * Gets the {@link #instances} property.
	 * @return the {@link #instances} property.
	 */
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch=FetchType.LAZY, mappedBy="model", targetEntity=ProcessInstance.class)
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
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Process Model [");
		builder.append(id);
		builder.append("]{");
		builder.append(source);
		builder.append("}:");		
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
		ProcessModel other = (ProcessModel) obj;
		if (id != other.id)
			return false;
		return true;
	}
}