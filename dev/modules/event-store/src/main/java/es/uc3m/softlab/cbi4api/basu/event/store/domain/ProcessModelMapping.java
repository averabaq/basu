/* 
 * $Id: ProcessModelMapping.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.domain;

import java.io.Serializable;

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
public class ProcessModelMapping implements Comparable<ProcessModelMapping>, Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 19293806384692541L;
	/** Process model mapped */
	private ProcessModel model;
	/** Process mapping model */
	private ProcessMapping mapping;
	/** Sequence of process mapping order. */
	private int sequence;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public ProcessModelMapping() {		
	}
	/**
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
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
	 * Gets the {@link #mapping} property.
	 * @return the {@link #mapping} property.
	 */	
	public ProcessMapping getMapping() {
		return mapping;
	}
	/**
	 * Sets the {@link #mapping} property.
	 * @param mapping the {@link #mapping} property to set.
	 */
	public void setMapping(ProcessMapping mapping) {
		this.mapping = mapping;
	}
	/**
	 * Gets the {@link #sequence} property.
	 * @return the {@link #sequence} property.
	 */
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
	public int compareTo(ProcessModelMapping mapping) {
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
		result = prime * result + ((model == null) ? 0 : model.hashCode());
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
		ProcessModelMapping other = (ProcessModelMapping) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		return true;
	}
}