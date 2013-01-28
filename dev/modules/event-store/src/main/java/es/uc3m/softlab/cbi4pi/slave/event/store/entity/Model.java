/* 
 * $Id: Model.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.event.store.entity;

import es.uc3m.softlab.cbi4pi.slave.event.store.StaticResources;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.UniqueConstraint;

/**
 * Model entity class. This class is bound to the
 * <strong>model</strong> table at the data base through the EJB3
 * persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Model's identifier.</li>
 * <li><b>type</b>: Model's type (or level) related to process/activity.</li>
 * <li><b>name</b>: Model's name.</li>
 * <li><b>description</b>: Model's description.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-store.Model")
@Table(name="model", schema="event_store", 
	   uniqueConstraints={@UniqueConstraint(columnNames={"model_src_id", "source"})})
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_STORE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_STORE)
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
@SequenceGenerator(name="model_sequence", sequenceName="model_id_seq")
public class Model implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 19293806384692541L;
	/** Model identifier. */
	private Long id;
	/** Model type. */
	private ModelType type;
	/** Model source definition identifier. */
	private String modelSrcId;	
	/** Model name. */
	private String name;
	/** Model source. */
	private Source source;	
	/** Model description. */
	private String description;

	/**
	 * Creates a new object with null property values. 	 
	 */
	public Model() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="model_sequence")
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #type} property.
	 * @return the {@link #type} property.
	 */
	@Column(name="type", length=50, nullable=false, updatable=false)
	public ModelType getType() {
		return type;
	}
	/**
	 * Sets the {@link #type} property.
	 * @param type the {@link #type} property to set.
	 */
	public void setType(ModelType type) {
		this.type = type;
	}
	/**
	 * Gets the {@link #name} property.
	 * @return the {@link #name} property.
	 */
	@Column(name="name", length=250, nullable=false)
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
	 * Gets the {@link #modelSrcId} property.
	 * @return the {@link #modelSrcId} property.
	 */
	@Column(name="model_src_id", length=250, nullable=false, updatable=false)
	public String getModelSrcId() {
		return modelSrcId;
	}
	/**
	 * Sets the {@link #modelSrcId} property.
	 * @param modelSrcId the {@link #modelSrcId} property to set.
	 */
	public void setModelSrcId(String modelSrcId) {
		this.modelSrcId = modelSrcId;
	}
	/**
	 * Gets the {@link #source} property.
	 * @return the {@link #source} property.
	 */
	@ManyToOne(optional=false, cascade=CascadeType.REFRESH)
	@JoinColumn(name="source", nullable=false, updatable=false)	
	public Source getSource() {
		return source;
	}
	/**
	 * Sets the {@link #source} property.
	 * @param source the {@link #source} property to set.
	 */
	public void setSource(Source source) {
		this.source = source;
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
		builder.append("Model [");
		builder.append(type);
		builder.append("](");
		builder.append(id);
		builder.append("){");
		builder.append(source);
		builder.append("}:");		
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
		Model other = (Model) obj;
		if (id != other.id)
			return false;
		return true;
	}
}