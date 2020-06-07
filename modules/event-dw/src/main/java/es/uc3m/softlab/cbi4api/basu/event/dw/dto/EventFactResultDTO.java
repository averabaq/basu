/* 
 * $Id: EventFactResultDTO.java,v 1.0 2012-03-28 12:29:27 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.dw.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Data transfer object for representing event fact data retrieved from
 * dynamic queries.
 * 
 * @author averab
 */
public class EventFactResultDTO implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 5943712032471673487L;
    /** Event fact instance */
	private Long id;
    /** Event fact name */
	private String name;    
    /** Event fact source */
	private String source;
    /** Event fact model */
	private String model;
	/** Event fact start time */
	private Date startTime;
    /** Event fact end time */
	private Date endTime;
    /** Event fact measure of turn around time in milliseconds */
	private Double turnAround;
    /** Event fact measure of waiting time in milliseconds */
	private Double wait;
    /** Event fact measure of change over time in milliseconds */
	private Double changeOver;
    /** Event fact measure of processing time in milliseconds */
	private Double processing;
    /** Event fact measure of suspend time in milliseconds */
	private Double suspend;    
        
	/**
	 * Creates a new object with null property values. 	 
	 */
	public EventFactResultDTO() {		
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
	 * Gets the {@link #source} property.
	 * @return the {@link #source} property.
	 */
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
	 * Gets the {@link #model} property.
	 * @return the {@link #model} property.
	 */
	public String getModel() {
		return model;
	}
	/**
	 * Sets the {@link #model} property.
	 * @param model the {@link #model} property to set.
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * Gets the {@link #startTime} property.
	 * @return the {@link #startTime} property.
	 */
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
	public Double getTurnAround() {
		return turnAround;
	}
	/**
	 * Sets the {@link #turnAround} property.
	 * @param turnAround the {@link #turnAround} property to set.
	 */
	public void setTurnAround(Double turnAround) {
		this.turnAround = turnAround;
	}
	/**
	 * Gets the {@link #wait} property.
	 * @return the {@link #wait} property.
	 */
	public Double getWait() {
		return wait;
	}
	/**
	 * Sets the {@link #wait} property.
	 * @param wait the {@link #wait} property to set.
	 */
	public void setWait(Double wait) {
		this.wait = wait;
	}
	/**
	 * Gets the {@link #changeOver} property.
	 * @return the {@link #changeOver} property.
	 */
	public Double getChangeOver() {
		return changeOver;
	}
	/**
	 * Sets the {@link #changeOver} property.
	 * @param changeOver the {@link #changeOver} property to set.
	 */
	public void setChangeOver(Double changeOver) {
		this.changeOver = changeOver;
	}
	/**
	 * Gets the {@link #processing} property.
	 * @return the {@link #processing} property.
	 */
	public Double getProcessing() {
		return processing;
	}
	/**
	 * Sets the {@link #processing} property.
	 * @param processing the {@link #processing} property to set.
	 */
	public void setProcessing(Double processing) {
		this.processing = processing;
	}
	/**
	 * Gets the {@link #suspend} property.
	 * @return the {@link #suspend} property.
	 */
	public Double getSuspend() {
		return suspend;
	}
	/**
	 * Sets the {@link #suspend} property.
	 * @param suspend the {@link #suspend} property to set.
	 */
	public void setSuspend(Double suspend) {
		this.suspend = suspend;
	}	
}