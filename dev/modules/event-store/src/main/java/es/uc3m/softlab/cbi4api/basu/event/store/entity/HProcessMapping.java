/* 
 * $Id: HProcessMapping.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.entity;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Process mapping entity class. This class is bound to the
 * <strong>process_mapping</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Process mapping identifier.</li>
 * <li><b>name</b>: Process mapping name.</li>
 * <li><b>description</b>: Process mapping description.</li>
 * </ul>
 * 
 * @see ProcessModelMapping
 * @see Model
 *  
 * @author averab
 * @version 1.0.0
 */
@Entity(name="event-store.ProcessMapping")
@Table(name="process_mapping", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@SequenceGenerator(name="process_mapping_sequence", sequenceName="process_mapping_id_seq")
public class HProcessMapping implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 496374620364126398L;
	/** Process mapping model identifier. */
	private long id;
	/** Process mapping model name. */
	private String name;
	/** Process mapping model description. */
	private String description;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HProcessMapping() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set.
	 */
	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="process_mapping_sequence")
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
	@Column(name="name", length=200, nullable=false)
	public String getName() {
		return name;
	}
	/**
	 * Sets the {@link #name} property.
	 * @param name the {@link #name} property to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Gets the {@link #description} property.
	 * @return the {@link #description} property.
	 */
	@Column(name="description", columnDefinition="text")
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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Process Mapping [");
		builder.append(id);
		builder.append("]: ");		
		builder.append(name);
		return builder.toString();
	}
	/**
	 * Returns a hash code value for the object.
	 * @return a hash code value for this object.
	 * @see java.lang.Object#hashCode()
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
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HProcessMapping other = (HProcessMapping) obj;
		if (id != other.id)
			return false;
		return true;
	}
}