/* 
 * $Id: ActivityModel.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;

/**
 * Activity model entity class. This class is bound to the
 * <strong>activity_model</strong> table at the data warehouse 
 * star diagram and managed through the EJB3 persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Activity model's identifier.</li>
 * <li><b>name</b>: Activity model's name.</li>
 * <li><b>source</b>: Activity model's source.</li>
 * <li><b>description</b>: Activity model's description.</li>
 * </ul>
 * 
 * @author averab
 */            
@Entity(name="event-dw.ActivityModel")
@Table(name="dw_activity_model", schema="event_dw")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
public class ActivityModel implements Serializable {		
	/** Serial Version UID */
	private static final long serialVersionUID = 1471839569108177712L;
	/** Activity model identifier. */
	private Long id;
	/** Activity model name. */
	private String name;
	/** Activity model source. */
	private String source;	
	/** Activity model description. */
	private String description;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ActivityModel() {		
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
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Activity Model [");
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
		ActivityModel other = (ActivityModel) obj;
		if (id != other.id)
			return false;
		return true;
	}
}