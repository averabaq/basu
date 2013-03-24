/* 
 * $Id: ProcessMapping.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.io.Serializable;

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
public class ProcessMapping implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 496374620364126398L;
	/** Process mapping model identifier. */
	private Long id;
	/** Process mapping model name. */
	private String name;
	/** Process mapping model description. */
	private String description;
	/** Set of process model mappings associated to this model */ 
	private Set<ProcessModelMapping> mappings;	

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ProcessMapping() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
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
	/**
	 * Gets the {@link #mappings} property as an ordered list object.
	 * @return the {@link #mappings} property as an ordered list object.
	 */
	public List<ProcessModelMapping> getMappingsList() {
		if (this.mappings == null)
			return null;
		ProcessModelMapping _mappings[] = new ProcessModelMapping[this.mappings.size()];
		this.mappings.toArray(_mappings);
		Arrays.sort(_mappings);		
		return Arrays.asList(_mappings);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProcessMapping other = (ProcessMapping) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}