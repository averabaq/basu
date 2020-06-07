/* 
 * $Id: XESMap.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.xes.event.publisher;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import es.uc3m.softlab.cbi4api.basu.xes.event.publisher.xsd.xes.map.BPAFState;

/**
 * XES conversor map.
 * 
 * @author averab
 */
public class XESMap implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 36027840328420L;
	/** Semantic Map */
    private Map<XESMapType, Map<String,Set<BPAFMapType>>> semantic;
    /** Correlation conversion map */
    private Map<XESMapType, Set<String>> correlation;
    /** Payload conversion map */
    private Map<XESMapType, Set<String>> payload;
    /** State transition conversion map */
    private Map<String, BPAFStateXesMap> state;
    /** State transition key conversion */
    private String transitionKey;

    /**
     * Creates a new XES map object.
     */
    public XESMap() {    
    }
	/**
	 * Gets the {@link #semantic} property.
	 * @return the semantic property.
	 */
	public Map<XESMapType, Map<String, Set<BPAFMapType>>> getSemantic() {
		return semantic;
	}
	/**
	 * Sets the {@link #semantic} property.
	 * @param semantic the {@link #semantic} to set
	 */
	public void setSemantic(Map<XESMapType, Map<String, Set<BPAFMapType>>> semantic) {
		this.semantic = semantic;
	}
	/**
	 * Gets the {@link #state} property.
	 * @return the state property.
	 */
	public Map<String, BPAFStateXesMap> getState() {
		return state;
	}
	/**
	 * Sets the {@link #state} property.
	 * @param state the {@link #state} to set
	 */
	public void setState(Map<String, BPAFStateXesMap> state) {
		this.state = state;
	}
	/**
	 * Gets the {@link #correlation} property.
	 * @return the correlation property.
	 */
	public Map<XESMapType, Set<String>> getCorrelation() {
		return correlation;
	}
	/**
	 * Sets the {@link #correlation} property.
	 * @param correlation the {@link #correlation} to set
	 */
	public void setCorrelation(Map<XESMapType, Set<String>> correlation) {
		this.correlation = correlation;
	}
	/**
	 * Gets the {@link #payload} property.
	 * @return the payload property.
	 */
	public Map<XESMapType, Set<String>> getPayload() {
		return payload;
	}
	/**
	 * Sets the {@link #payload} property.
	 * @param payload the {@link #payload} to set
	 */
	public void setPayload(Map<XESMapType, Set<String>> payload) {
		this.payload = payload;
	}
	/**
	 * Gets the {@link #transitionKey} property.
	 * @return the transitionKey property.
	 */
	public String getTransitionKey() {
		return transitionKey;
	}
	/**
	 * Sets the {@link #transitionKey} property.
	 * @param transitionKey the {@link #transitionKey} to set
	 */
	public void setTransitionKey(String transitionKey) {
		this.transitionKey = transitionKey;
	}
	/**
	 * Returns a string representation of the object.
	 * @return string representation of the object.
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("XESMap [semantic=");
		builder.append(semantic);
		builder.append(", correlation=");
		builder.append(correlation);
		builder.append(", payload=");
		builder.append(payload);
		builder.append(", state=");
		builder.append(state);
		builder.append(", transitionKey=");
		builder.append(transitionKey);
		builder.append("]");
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
		result = prime * result
				+ ((semantic == null) ? 0 : semantic.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		XESMap other = (XESMap) obj;
		if (semantic == null) {
			if (other.semantic != null)
				return false;
		} else if (!semantic.equals(other.semantic))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	/**
	 * BPAF state conversion XES map for mapping between XES state transitions
	 * and its BPAF (previous, current) equivalent states.
	 * @author averab
	 */
	public class BPAFStateXesMap {
		/** XES state conversion source for previous BPAF state */
		private BPAFState source;
		/** XES state conversion target for previous BPAF current */
		private BPAFState target;
		
		/**
		 * Initializes the class with empty property values.
		 */
		public BPAFStateXesMap() {			
		}
		/**
		 * Initializes the class with default property values.
		 * @param source source BPAF state conversion for previous state
		 * @param target target BPAF state conversion for current state
		 */
		public BPAFStateXesMap(BPAFState source, BPAFState target) {
			this.source = source;
			this.target = target;
			
		}
		/**
		 * Gets the {@link #source} property.
		 * @return the source property.
		 */
		public BPAFState getSource() {
			return source;
		}
		/**
		 * Sets the {@link #source} property.
		 * @param source the {@link #source} to set
		 */
		public void setSource(BPAFState source) {
			this.source = source;
		}
		/**
		 * Gets the {@link #target} property.
		 * @return the target property.
		 */
		public BPAFState getTarget() {
			return target;
		}
		/**
		 * Sets the {@link #target} property.
		 * @param target the {@link #target} to set
		 */
		public void setTarget(BPAFState target) {
			this.target = target;
		}
		/**
		 * Returns a string representation of the object.
		 * @return string representation of the object.
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("BPAFStateXesMap [source=");
			builder.append(source);
			builder.append(", target=");
			builder.append(target);
			builder.append("]");
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
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((source == null) ? 0 : source.hashCode());
			result = prime * result
					+ ((target == null) ? 0 : target.hashCode());
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
			BPAFStateXesMap other = (BPAFStateXesMap) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			if (target == null) {
				if (other.target != null)
					return false;
			} else if (!target.equals(other.target))
				return false;
			return true;
		}
		private XESMap getOuterType() {
			return XESMap.this;
		}
	}
}