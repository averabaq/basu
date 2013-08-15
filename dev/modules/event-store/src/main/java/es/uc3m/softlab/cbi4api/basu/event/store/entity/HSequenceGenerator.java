/* 
 * $Id: HSource.java,v 1.0 2013-01-27 23:29:05 averab Exp $
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
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * HSequenceGenerator entity class. This class is bound to the
 * <strong>sequence_generator</strong> table at the data base through 
 * the EJB3 persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Sequence generator identifier.</li>
 * <li><b>seq</b>: Sequence generator next value.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-store.SequenceGenerator")
@Table(name="sequence_generator", schema="event_store")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
public class HSequenceGenerator implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 501248234613460L;
	/** sequence generator table type */
	public enum Type { EVENT, PROCESS_INSTANCE, ACTIVITY_INSTANCE, MODEL }
	/** Sequence generator identifier. */
	private Type id;
	/** Sequence generator next. */
	private long seq;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public HSequenceGenerator() {		
	}
	/**
	 * Creates a new object with default property values. 
	 * @param type table type identifier.
	 * @param sequence sequence value.
	 */
	public HSequenceGenerator(Type type, long sequence) {
		this.id = type;
		this.seq = sequence;
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	@Column(name="id", length=150, nullable=false)
	public Type getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set.
	 */
	public void setId(Type id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #seq} property.
	 * @return the seq property.
	 */
	@Column(name="seq", nullable=false)
	public long getSeq() {
		return seq;
	}
	/**
	 * Sets the {@link #seq} property.
	 * @param seq the {@link #seq} to set
	 */
	public void setSeq(long seq) {
		this.seq = seq;
	}
	/**
	 * Gets the next sequence stored at {@link #seq} property.
	 * @return the the next sequence.
	 */	
	@Transient
	synchronized public long getNextSeq() {		
		increase();
		return seq;
	}
	/**
	 * Increase the sequence.
	 */
	@Transient
	synchronized private void increase() {
		this.seq++;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HSequenceGenerator (");
		builder.append(id);
		builder.append(")[");
		builder.append(seq);
		builder.append("]: ");
		builder.append(seq);
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
		HSequenceGenerator other = (HSequenceGenerator) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
