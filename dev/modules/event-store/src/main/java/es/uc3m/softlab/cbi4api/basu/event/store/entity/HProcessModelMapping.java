/* 
 * $Id: HProcessModelMapping.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.entity;

import es.uc3m.softlab.cbi4api.basu.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 * Process model mapping entity class. This class is bound to the
 * <strong>process_model_mapping</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>model</b>: Process model associated to this map.</li>
 * <li><b>mapping</b>: Mapping associated to this model.</li>
 * <li><b>sequence</b>: Order of sequence for this process model at the current map.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-store.ProcessModelMapping")
@Table(name="process_model_mapping", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@NamedQueries({
	@NamedQuery(name="getProcessMappingSequence", 
		        query="from event-store.ProcessModelMapping m where m.mapping = :mapping order by m.sequence asc")
})
public class HProcessModelMapping implements Comparable<HProcessModelMapping>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 19293806384692541L;
	/** Process model mapped */
	private long model;
	/** Process mapping model */
	private long mapping;
	/** Sequence of process mapping order. */
	private int sequence;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HProcessModelMapping() {		
	}
	/**
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
	@Id
	@Column(name="model", nullable=false)
	public long getModel() {
		return model;
	}
	/**
	 * Sets the {@link #model} property.
	 * @param model the {@link #model} property to set.
	 */
	public void setModel(long model) {
		this.model = model;
	}
	/**
	 * Gets the {@link #mapping} property.
	 * @return the {@link #mapping} property.
	 */	
	@Column(name="mapping", nullable=false)
	public long getMapping() {
		return mapping;
	}
	/**
	 * Sets the {@link #mapping} property.
	 * @param mapping the {@link #mapping} property to set.
	 */
	public void setMapping(long mapping) {
		this.mapping = mapping;
	}
	/**
	 * Gets the {@link #sequence} property.
	 * @return the {@link #sequence} property.
	 */
	@Column(name="sequence", nullable=false)
	public int getSequence() {
		return sequence;
	}
	/**
	 * Sets the {@link #sequence} property.
	 * @param sequence the {@link #sequence} property to set.
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	/**
	 * Compares this object with the specified object for order. Returns a negative integer, zero, or a positive 
	 * integer as this object is less than, equal to, or greater than the specified object. 
	 * The implementor must ensure sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y. (This implies that 
	 * x.compareTo(y) must throw an exception iff y.compareTo(x) throws an exception.)
	 * The implementor must also ensure that the relation is transitive: (x.compareTo(y)>0 && y.compareTo(z)>0) 
	 * implies x.compareTo(z)>0. Finally, the implementor must ensure that x.compareTo(y)==0 implies that 
	 * sgn(x.compareTo(z)) == sgn(y.compareTo(z)), for all z. 
	 * 
	 * @param mapping the object to be compared. 
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater 
	 * than the specified object. 
	 */
	@Override
	public int compareTo(HProcessModelMapping mapping) {
		if (mapping == null)
			return -1;		
		return Integer.valueOf(sequence).compareTo(mapping.getSequence()) ;
	}	
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(mapping + ": " + model);
		builder.append("]: ");
		builder.append(sequence);
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
		result = prime * result + (int) (model ^ (model >>> 32));
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
		HProcessModelMapping other = (HProcessModelMapping) obj;
		if (model != other.model)
			return false;
		return true;
	}
}