/* 
 * $Id: LazyProcessInstancesDataModel.java,v 1.4 2013-01-26 21:47:15 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4pi.slave.web.mvc.view.event.store;

import es.uc3m.softlab.cbi4pi.slave.event.store.entity.ProcessInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Class user bean controller under the <i>Mojarra Project</i> for JSF 2.0 implementation.
 * 
 * This class uses the <b>JSF 2.0</b> package implementation developed by the Mojarra Project. 
 * <p> It implements the Java Specification Request
 * <a href="http://www.jcp.org/en/jsr/detail?id=314">JSR 314 Java Server Faces 2.0.</a>
 * </p><p>Mojarra is Sun's high performance, battle-tested implementation of JSF, 
 * and is used in IBM WebSphereTM, Oracle WebLogicTM, Oracle 10g Application Server, 
 * SpringSource dm ServerTM, and other popular enterprise platforms such as JBoss, 
 * so important to be mentioned.<p>
 * 
 * For further information about the usage of this package visit the Project web site 
 * at the <a href="https://javaserverfaces.dev.java.net">Mojarra Project</a> from sun
 * open source projects web site.
 * 
 * @author averab
 * @version 1.0
 */
public class LazyProcessInstancesDataModel extends LazyDataModel<ProcessInstance> implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 3318644566775111178L;
	/** Logger for tracing. */
	private static final Logger logger = Logger.getLogger(LazyProcessInstancesDataModel.class);	
	/** Data source for partial data loading */
	private List<ProcessInstance> dataSource;  

	/**
	 * Creates a new object with a default values for the properties passed by
	 * arguments.
	 */
	public LazyProcessInstancesDataModel() {
		dataSource = new ArrayList<ProcessInstance>(); 
	}
	/**
	 * Creates a new object with an initial data source property passed by
	 * arguments.
	 * 
	 * @param dataSource data source used for lazy data loading.
	 */
	public LazyProcessInstancesDataModel(List<ProcessInstance> dataSource) {	
		this.dataSource = dataSource;
	}
	/**
	 * Gets the {@link #dataSource} property.
	 * @return the {@link #dataSource} property.
	 */
	public List<ProcessInstance> getDataSource() {
		return dataSource;
	}
	/**
	 * Sets the {@link #dataSource} property.
	 * @param dataSource the {@link #dataSource} property to set.
	 */
	public void setDataSource(List<ProcessInstance> dataSource) {
		this.dataSource = dataSource;
	}
	/**
	 * Gets the row object data associated to the row key passed by arguments.
	 * @param rowKey row key to get the object data.  
	 * @return the object data located in the row by the given key.
	 */
	@Override  
	public ProcessInstance getRowData(String rowKey) {  
		for(ProcessInstance instance : dataSource) {  
			if(instance.getId() == Long.valueOf(rowKey))  
				return instance;  
		}  
		return null;  
	}  
	/**
	 * Gets the row key identifier of the process instance passed by arguments.
	 * 
	 * @param instance process instance to get the row key to be uniquely 
	 * identified.
	 * @return instance the row key identifier.
	 */
	@Override  
	public Object getRowKey(ProcessInstance instance) {  
		return instance.getId();  
	}  	
	/**
	 * Loads the lazy data by returning the data source list applying the 
	 * current filters and orders.
	 * 
	 * @param first index for the first row in the lazy load data subset. 
	 * @param pageSize page size of the lazy load data subset.
	 * @param sortField sort field of the lazy load data subset.
	 * @param sortOrder sort order of the lazy load data subset.
	 * @param filters filters applied on the lazy load data subset.
	 * @return 
	 */
	@Override
	public List<ProcessInstance> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {  
		List<ProcessInstance> data = new ArrayList<ProcessInstance>();  
		
		/* perform the lazy filter */  
		for(ProcessInstance instance : dataSource) {  
			boolean match = true;  

			for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {  
				try {  
					String filterProperty = it.next();  
					logger.debug("filter property: " + filterProperty);
					String filterValue = filters.get(filterProperty);  
					logger.debug("filter value: " + filterValue);
					String fieldValue = null;
					String filterMethod = "get" + StringUtils.capitalize(filterProperty);  
					logger.debug("filter method: " + filterMethod);
					fieldValue = String.valueOf(instance.getClass().getMethod(filterMethod).invoke(instance));
					logger.debug("field value: " + fieldValue);
					
					if(filterValue == null || fieldValue.toUpperCase().startsWith(filterValue.toUpperCase())) {  
						match = true;  
					}  
					else {  
						match = false;  
						break;  
					}  
				} catch(Exception ex) {  
					match = false;  
				}   
			}  

			if(match) {  
				data.add(instance);  
			}  
		}  

		/* 
		 * The sort function is disabled 
		 * 
		 * if(sortField != null) {  
		 *    Collections.sort(data, new LazySorter(sortField, sortOrder));  
		 * }  
		 */

		/* rowCount */  
		int dataSize = data.size();  
		this.setRowCount(dataSize);  

		/* paginate */  
		if (dataSize > pageSize) {  
			try {  
				return data.subList(first, first + pageSize);  
			}  
			catch(IndexOutOfBoundsException e) {  
				return data.subList(first, first + (dataSize % pageSize));  
			}  
		} else {  
			return data;  
		}  
	}  
}