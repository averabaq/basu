/* 
 * $Id: EventFact.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.star.fact;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uc3m.softlab.cbi4api.basu.event.dw.StaticResources;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ActivityInstance;
import es.uc3m.softlab.cbi4api.basu.event.dw.star.dimension.ProcessInstance;


/**
 * Event fact entity class. This class is bound to the
 * <strong>event_fact</strong> table at the data warehouse 
 * star diagram and managed through the EJB3 persistence layer. <br/>
 * Its fields are the following:
 * <ul>
 * <li><b>id</b>: Event fact's identifier.</li>
 * <li><b>mapping</b>: Event fact's process mapping dimension.</li>
 * <li><b>model</b>: Event fact's process model dimension.</li>
 * <li><b>instance</b>: Event fact's process instance dimension.</li>
 * <li><b>start_time</b>: Event fact's start_time fact.</li>
 * <li><b>end_time</b>: Event fact's end time fact.</li>
 * <li><b>runtime</b>: Event fact's runtime time measure.</li>
 * <li><b>turnaround</b>: Event fact's turnaround time measure.</li>
 * <li><b>wait</b>: Event fact's wait time measure.</li>
 * <li><b>change_over</b>: Event fact's change over  time measure.</li>
 * <li><b>processing</b>: Event fact's processing time measure.</li>
 * <li><b>suspend</b>: Event fact's suspend time measure.</li>
 * </ul>
 * 
 * @author averab
 */
@Entity(name="event-dw.EventFact")
@Table(name="dw_event_fact", schema="event_dw")
@PersistenceUnit(name=StaticResources.PERSISTENCE_NAME_EVENT_DATA_WAREHOUSE, unitName=StaticResources.PERSISTENCE_UNIT_NAME_EVENT_DATA_WAREHOUSE)
public class EventFact implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1656021381876773190L;
	/** Event identifier */
    protected long id;
    /** Event process instance */
    protected ProcessInstance process;
    /** Event activity instance */
    protected ActivityInstance activity;    
    /** Fact start time */
    protected Date startTime;
    /** Fact end time */
    protected Date endTime;
    /** Fact measure of turn around time in milliseconds */
    protected Long turnAround;
    /** Fact measure of waiting time in milliseconds */
    protected Long wait;
    /** Fact measure of change over time in milliseconds */
    protected Long changeOver;
    /** Fact measure of processing time in milliseconds */
    protected Long processing;
    /** Fact measure of suspend time in milliseconds */
    protected Long suspend;
    /** Fact measure of turn around time in milliseconds */
    protected Byte successful;
    /** Fact measure of waiting time in milliseconds */
    protected Byte running;
    /** Fact measure of change over time in milliseconds */
    protected Byte aborted;
    /** Fact measure of processing time in milliseconds */
    protected Byte failed;

    /**
	 * Creates a new object with null property values. 	 
	 */
	public EventFact() {		
	}
	/**
	 * Gets the {@link #id} property.
	 * @return the {@link #id} property.
	 */
	@Id
	@Column(name="id")
    @GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}
	/**
	 * Sets the {@link #id} property.
	 * @param id the {@link #id} property to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Gets the {@link #process} property.
	 * @return the {@link #process} property.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="process", nullable=false, updatable=false)	
	public ProcessInstance getProcess() {
		return process;
	}
	/**
	 * Sets the {@link #process} property.
	 * @param process the {@link #process} property to set.
	 */
	public void setProcess(ProcessInstance process) {
		this.process = process;
	}
	/**
	 * Gets the {@link #activity} property.
	 * @return the {@link #activity} property.
	 */
	@ManyToOne(cascade={CascadeType.REFRESH})
	@JoinColumn(name="activity", nullable=true, updatable=false)	
	public ActivityInstance getActivity() {
		return activity;
	}
	/**
	 * Sets the {@link #activity} property.
	 * @param activity the {@link #activity} property to set.
	 */
	public void setActivity(ActivityInstance activity) {
		this.activity = activity;
	}
	/**
	 * Gets the {@link #startTime} property.
	 * @return the {@link #startTime} property.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time", updatable=false)
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * Sets the {@link #startTime} property.
	 * @param startTime the {@link #startTime} property to set.
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * Gets the {@link #endTime} property.
	 * @return the {@link #endTime} property.
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time", updatable=false)
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * Sets the {@link #endTime} property.
	 * @param endTime the {@link #endTime} property to set.
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * Gets the {@link #turnAround} property.
	 * @return the {@link #turnAround} property.
	 */
	@Column(name="turnaround", updatable=false)
	public Long getTurnAround() {
		return turnAround;
	}
	/**
	 * Sets the {@link #turnAround} property.
	 * @param turnAround the {@link #turnAround} property to set.
	 */
	public void setTurnAround(Long turnAround) {
		this.turnAround = turnAround;
	}
	/**
	 * Gets the {@link #wait} property.
	 * @return the {@link #wait} property.
	 */
	@Column(name="wait", updatable=false)
	public Long getWait() {
		return wait;
	}
	/**
	 * Sets the {@link #wait} property.
	 * @param wait the {@link #wait} property to set.
	 */
	public void setWait(Long wait) {
		this.wait = wait;
	}
	/**
	 * Gets the {@link #changeOver} property.
	 * @return the {@link #changeOver} property.
	 */
	@Column(name="change_over", updatable=false)
	public Long getChangeOver() {
		return changeOver;
	}
	/**
	 * Sets the {@link #changeOver} property.
	 * @param changeOver the {@link #changeOver} property to set.
	 */
	public void setChangeOver(Long changeOver) {
		this.changeOver = changeOver;
	}
	/**
	 * Gets the {@link #processing} property.
	 * @return the {@link #processing} property.
	 */
	@Column(name="processing", updatable=false)
	public Long getProcessing() {
		return processing;
	}
	/**
	 * Sets the {@link #processing} property.
	 * @param processing the {@link #processing} property to set.
	 */
	public void setProcessing(Long processing) {
		this.processing = processing;
	}
	/**
	 * Gets the {@link #suspend} property.
	 * @return the {@link #suspend} property.
	 */
	@Column(name="suspend", updatable=false)
	public Long getSuspend() {
		return suspend;
	}
	/**
	 * Sets the {@link #suspend} property.
	 * @param suspend the {@link #suspend} property to set.
	 */
	public void setSuspend(Long suspend) {
		this.suspend = suspend;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Event Fact [id=");
		builder.append(id);
		builder.append(", process=");
		builder.append(process);
		builder.append(", activity=");
		builder.append(activity);
		builder.append("] (");
		builder.append(startTime);
		builder.append("," + endTime);
		builder.append("): {");
		builder.append(", turnaround=");
		builder.append(turnAround);
		builder.append(", wait=");
		builder.append(wait);
		builder.append(", changeOver=");
		builder.append(changeOver);
		builder.append(", suspend=");
		builder.append(suspend);
		builder.append(", processing=");
		builder.append(processing);
		builder.append("}");
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
		EventFact other = (EventFact) obj;
		if (id != other.id)
			return false;
		return true;
	}
}