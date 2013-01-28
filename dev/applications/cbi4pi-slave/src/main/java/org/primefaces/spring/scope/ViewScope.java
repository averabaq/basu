/* 
 * $Id: ViewScope.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package org.primefaces.spring.scope;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * Spring view scope implementation for using the <b>JSF 2.0</b> package
 * implementation with the Spring framework.
 * 
 * <p>
 * This class is based upon the implementation of the Java Specification Request
 * <a href="http://www.jcp.org/en/jsr/detail?id=314">JSR 314 Java Server Faces
 * 2.0.</a>
 * </p>
 * <p>
 * Mojarra is Sun's high performance, battle-tested implementation of JSF, and
 * is used in IBM WebSphereTM, Oracle WebLogicTM, Oracle 10g Application Server,
 * SpringSource dm ServerTM, and other popular enterprise platforms such as
 * JBoss, so important to be mentioned.
 * <p>
 * 
 * For further information about the usage of this package visit the Project web
 * site at the <a href="https://javaserverfaces.dev.java.net">Mojarra
 * Project</a> from sun open source projects web site.
 * 
 * @author cagataycivici
 */
public class ViewScope implements Scope {
	/** View scope name */
	public static final String SCOPE_NAME = "view";
	/** View scope callbacks */
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callbacks";

	public synchronized Object get(String name, ObjectFactory<?> objectFactory) {
		Object instance = getViewMap().get(name);
		if (instance == null) {
			instance = objectFactory.getObject();
			getViewMap().put(name, instance);
		}
		return instance;		
	}

	@SuppressWarnings("unchecked")
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);
		if (instance != null) {
			Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap()
					.get(VIEW_SCOPE_CALLBACKS);
			if (callbacks != null) {
				callbacks.remove(name);
			}
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public void registerDestructionCallback(String name, Runnable runnable) {
		Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap()
				.get(VIEW_SCOPE_CALLBACKS);
		if (callbacks != null) {
			callbacks.put(name, runnable);
		}
	}

	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(
				facesContext);
		return facesRequestAttributes.resolveReference(name);
	}

	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(
				facesContext);
		return facesRequestAttributes.getSessionId() + "-"
				+ facesContext.getViewRoot().getViewId();
	}

	private Map<String, Object> getViewMap() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
	}
}