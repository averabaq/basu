/* 
 * $Id: MapReduceCorrelation.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.basu.event.store.mapred;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.persistence.Table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.HFileOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable ;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.log4j.Logger;

import es.uc3m.softlab.cbi4api.basu.event.store.domain.EventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HEvent;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HEventCorrelation;
import es.uc3m.softlab.cbi4api.basu.event.store.entity.HProcessInstance;

/**
 * 
 * @author averab
 * @version 1.0.0
 */
public class MapReduceCorrelation {
    /** Logger for tracing */
    private static Logger logger = Logger.getLogger(MapReduceCorrelation.class);
	/** */
	private static final int CACHING = 500;
	/** */
	private static final String MAP_REDUCE_CORRELATOR_PATH = "/var/cbi4api/basu/tmp/mapred/correlator";
	private static final String MAP_REDUCE_COR_EVENT_PATH = MAP_REDUCE_CORRELATOR_PATH + "/event";
	private static final String MAP_REDUCE_COR_MODEL_PATH = MAP_REDUCE_CORRELATOR_PATH + "/model";
	private static final String MAP_REDUCE_COR_EVENT_CORRELATION_PATH = MAP_REDUCE_CORRELATOR_PATH + "/event-correlation";
	/** */
	private static final int MAPRED_TIMEOUT = 1000;
	private static final int NUM_RED_TASKS = 1;
	private static final byte[] CF_EVENT_CORRELATION = "event_correlation".getBytes();	
	private static final byte[] CF_EVENT = "event".getBytes();	
	private static final byte[] CF_PRC_INSTANCE = "process_instance".getBytes();
	private static final byte[] ATTR_EVENT_ID = "event_id".getBytes();
	private static final byte[] ATTR_MODEL_ID = "model".getBytes();
	private static final byte[] ATTR_ID = "id".getBytes();
	private static final byte[] ATTR_PRC_INST = "process_instance".getBytes();	
	private static final byte[] ATTR_ACT_INST = "activity_instance".getBytes();	
	private static final byte[] ATTR_KEY = "key".getBytes();	
	private static final byte[] ATTR_VALUE = "value".getBytes();
	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class ModelFilterMapper extends TableMapper<LongWritable, Text>  {	
		/**
		 * 
		 */
		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			BigInteger binstance = new BigInteger(value.getValue(CF_PRC_INSTANCE, ATTR_ID));
			Text  modelId = new Text("X");
			LongWritable instanceId = new LongWritable (binstance.longValue());
			context.write(instanceId, modelId);
		}
	}
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventFilterMapper extends TableMapper<LongWritable, Text>  {	
		/**
		 * 
		 */
		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			BigInteger bevent = new BigInteger(value.getValue(CF_EVENT, ATTR_EVENT_ID));
			BigInteger bprocessInstance = new BigInteger(value.getValue(CF_EVENT, ATTR_PRC_INST));
			Text _event = new Text(String.valueOf(bevent.longValue()));
			LongWritable  _processInstance = new LongWritable (bprocessInstance.longValue());
			context.write(_processInstance, _event);
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventFilterCombiner extends Reducer<LongWritable, LongWritable, LongWritable, Text> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			Iterator<LongWritable> iterator = values.iterator();
			while (iterator.hasNext()) {
				_buf.append(iterator.next());
				if (iterator.hasNext())
					_buf.append(":");			
			}	
			Text _value = new Text(_buf.toString());
			context.write(key, _value);
		}
	}		
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorFilterMapper extends TableMapper<LongWritable , Text> {	
		/**
		 * 
		 */
		@Override
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			BigInteger bevent = new BigInteger(value.getValue(CF_EVENT_CORRELATION, ATTR_EVENT_ID));
			String bkey = new String(value.getValue(CF_EVENT_CORRELATION, ATTR_KEY));
			LongWritable  _event = new LongWritable (bevent.longValue());
			Text _key = new Text(bkey);
			context.write(_event, _key);
		}
	}			
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorMapper extends Mapper<Text, Text, LongWritable, LongWritable> {	
		private final static LongWritable one = new LongWritable (1L); 
		/**
		 * 
		 */
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {			
			LongWritable _key = new LongWritable(Long.valueOf(key.toString()));
			context.write(_key, one);
			
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorCombiner extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException { 
			long sum = 0;			
			for (LongWritable value : values) {
				sum += value.get();
			}
			context.write(key, new LongWritable(sum));
		}
	}		
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventCorrelatorReducer extends Reducer<LongWritable, LongWritable, LongWritable, LongWritable> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException { 
			Configuration conf = context.getConfiguration();
			int size = conf.getInt("correlationKeysSize", -1);
			
			long sum = 0;			
			for (LongWritable value : values) {
				sum += value.get();
			}
			// emits if and only if the eventId satisfies all correlation values
			if (sum == size)
				context.write(key, new LongWritable(sum));
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class ModelEventMapper extends Mapper<Text, Text, LongWritable, Text> {	
		/**
		 * 
		 */
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {			
			LongWritable _key = new LongWritable(Long.valueOf(key.toString()));
			context.write(_key, value);			
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class ModelEventCombiner extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			Iterator<Text> iterator = values.iterator();
			while (iterator.hasNext()) {
				_buf.append(iterator.next());
				if (iterator.hasNext())
					_buf.append(":");			
			}	
			Text _value = new Text(_buf.toString());
			context.write(key, _value);
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class ModelEventReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			Iterator<Text> iterator = values.iterator();
			while (iterator.hasNext()) {
				_buf.append(iterator.next());
				if (iterator.hasNext())
					_buf.append(":");			
			}				
			if (_buf.toString().contains(":X")) {
				String[] tokens = _buf.toString().split(":X");
				for (String eventId : tokens[0].split(":")) {
					LongWritable _value = new LongWritable(Long.valueOf(eventId));
					context.write(_value, new Text(String.valueOf(key)));	
				}
			} else if (_buf.toString().contains("X:")) {
				String[] tokens = _buf.toString().split("X:");
				if (tokens.length > 1) {					
					for (String eventId : tokens[1].split(":")) {
						LongWritable _value = new LongWritable(Long.valueOf(eventId));
						context.write(_value, new Text(String.valueOf(key)));	
					}
				}
			}
		}
	}		
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventFinalMapper extends Mapper<Text, Text, LongWritable, Text> {	
		/**
		 * 
		 */
		@Override
		public void map(Text key, Text value, Context context) throws IOException, InterruptedException {			
			LongWritable _key = new LongWritable(Long.valueOf(key.toString()));
			context.write(_key, value);			
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventFinalCombiner extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			for (Text text : values) {
				_buf.append(text);
			}
			Text _value = new Text(_buf.toString());
			context.write(key, _value);
		}
	}	
	/**
	 * 
	 * @author averab
	 *
	 */
	public static class EventFinalReducer extends Reducer<LongWritable, Text, LongWritable, Text> {
		/**
		 * 
		 */
		@Override
		public void reduce(LongWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException { 
			StringBuffer _buf = new StringBuffer();
			Iterator<Text> iterator = values.iterator();
			while (iterator.hasNext()) {
				_buf.append(iterator.next());
				if (iterator.hasNext())
					_buf.append(":");			
			}	
			String[] tokens = _buf.toString().split(":");
			if (_buf.toString().contains(":")) {
				LongWritable _key = new LongWritable(Long.valueOf(tokens[0]));
				context.write(_key, new Text(String.valueOf(key.get())));
			}
		}
	}	
	/**
	 * 
	 * @param modelId
	 * @param correlations
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	public static void perform(final long modelId, final Set<EventCorrelation> correlations) throws IOException, ClassNotFoundException, InterruptedException {
		// session identifier for the current map reduce process 
		String sessionId = UUID.randomUUID().toString();
		logger.debug("Session ID for map reduce job (correlation) [" + sessionId + "]");
		// asynchronous job for filtering model_id-process_instances
		Job modelJob = modelFilterStage(modelId, sessionId);
		logger.debug("Map model filter job (correlation) of session [" + sessionId + "] completed.");
		// asynchronous job for filtering event_id-process_instances
		Job eventJob = eventFilterStage(sessionId);
		logger.debug("Map event filter job (correlation) of session [" + sessionId + "] completed.");
		// synchronous job for reducing model and event_id into process_instances
		Job modelEventReducerJob = modelEventReducerStage(sessionId); 
		logger.debug("Reduce model event job (correlation) of session [" + sessionId + "] completed.");		
		
		// synchronous job for filtering event_id-key
		List<ControlledJob> eventCorrelationFilterControlledJobList = new ArrayList<ControlledJob>();
		for (EventCorrelation correlation : correlations) {
			Job eventCorrelationFilterJob = eventCorrelatorFilterStage(correlation, sessionId);
			eventCorrelationFilterControlledJobList.add(new ControlledJob(eventCorrelationFilterJob, null));
			logger.debug("Map event correlation filter job (correlation) over the key " + correlation.getKey() + " and session [" + sessionId + "] completed.");
		}
		// synchronous job for reducing event_id that succeed the correlation set
		Job eventCorrelationReducerJob = eventCorrelationReducerStage(correlations, sessionId);
		// synchronous job for reducing final event_id that succeed the correlation set, model and process_instance
		Job eventReducerJob = eventReducerFinalStage(sessionId);
		
		// controlled jobs definition
		ControlledJob modelControlledJob = new ControlledJob(modelJob, null);
		ControlledJob eventControlledJob = new ControlledJob(eventJob, null);
		List<ControlledJob> modelEventJobList = new ArrayList<ControlledJob>();
		modelEventJobList.add(modelControlledJob);
		modelEventJobList.add(eventControlledJob);
		ControlledJob modelEventReducerControlledJob = new ControlledJob(modelEventReducerJob, modelEventJobList);		
		ControlledJob eventCorrelationReducerControlledJob = new ControlledJob(eventCorrelationReducerJob, eventCorrelationFilterControlledJobList);
		List<ControlledJob> eventCorrelationControlledJobList = new ArrayList<ControlledJob>();
		eventCorrelationControlledJobList.add(modelEventReducerControlledJob);
		eventCorrelationControlledJobList.add(eventCorrelationReducerControlledJob);
		
		List<ControlledJob> eventReducerJobList = new ArrayList<ControlledJob>();
		eventReducerJobList.addAll(eventCorrelationControlledJobList);
		eventReducerJobList.addAll(modelEventJobList);
		ControlledJob eventReducerControlledJob = new ControlledJob(eventReducerJob, eventReducerJobList);
		
		// sets the job dependency control
		JobControl jobControl = new JobControl("corrleationJobGroup"); 		
		jobControl.addJob(modelControlledJob);
		jobControl.addJob(eventControlledJob);
		jobControl.addJob(modelEventReducerControlledJob);
		jobControl.addJobCollection(eventCorrelationFilterControlledJobList);
		jobControl.addJob(eventCorrelationReducerControlledJob);
		jobControl.addJobCollection(eventReducerJobList);
		jobControl.addJob(eventReducerControlledJob);
		
		// starts map-reduce process
		Thread thread = new Thread(jobControl);
		thread.start();
		while (!jobControl.allFinished()); 
		
		// read the result back	
	    Long instance = getResult(eventReducerJob.getConfiguration(), new Path(MAP_REDUCE_CORRELATOR_PATH + "/reduce/" + sessionId));
	    logger.fatal("PROCESS INSTANCE: " + instance);
		logger.fatal("Map-Reduce correlation job of session [" + sessionId + "] completed.");		
	}
	/**
	 * 
	 * @param configuration
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Long getResult(Configuration configuration, Path path) throws IOException {
		Long instanceId = null;
		FileSystem fileSystem = null;
		BufferedReader bufferedReader = null;
		try {
			fileSystem = FileSystem.get(configuration);
			FileStatus[] fileStatus = fileSystem.listStatus(path, new PathFilter() {
				public boolean accept(Path _path) {
					return _path.toString().contains("part-");
				}
			});  
			
			for (FileStatus status : fileStatus) {
				Path _path = status.getPath();
				bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(_path)));		        
				String line = bufferedReader.readLine();
				while (line != null) {
					StringTokenizer tokenizer = new StringTokenizer(line);
					String key = tokenizer.nextToken();
					if (key != null) {
						instanceId = Long.valueOf(key);
					}
					line = bufferedReader.readLine();					
				}
			}		    
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileSystem != null)
				fileSystem.close();
		}
		return instanceId;
	}
	/**
	 * 
	 * @param modelId
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static Job modelFilterStage(final long modelId, final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		config.setInt("mapreduce.task.timeout", MAPRED_TIMEOUT);
		
		Job job = new Job(config,"correlation-model-filter");
		// class that contains mapper and reducer
		job.setJarByClass(MapReduceCorrelation.class);     

		Scan scan = new Scan();
		scan.setCaching(CACHING);      
		scan.setCacheBlocks(false);		
		SingleColumnValueFilter filter = new SingleColumnValueFilter(
			CF_PRC_INSTANCE,
			ATTR_MODEL_ID,
			CompareOp.EQUAL,
			Bytes.toBytes(modelId));
		scan.setFilter(filter);

		// gets the process instance table name
		if (!HProcessInstance.class.isAnnotationPresent(Table.class))
			throw new RuntimeException("Cannot retrieved the process instance table name from annotated class. Table persistence annotation is not defined");
		Table table = HProcessInstance.class.getAnnotation(Table.class);				
		
		TableMapReduceUtil.initTableMapperJob(
			table.name(),        
			scan,                 
			ModelFilterMapper.class, 
			LongWritable.class,               
			Text.class,               
			job);

		HFileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_MODEL_PATH + "/" + sessionId)); 
		//job.submit();		
		return job;		
	}
	/**
	 * 
	 * @param modelId
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static Job eventFilterStage(String sessionId) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		config.setInt("mapreduce.task.timeout", MAPRED_TIMEOUT);
		
		Job job = new Job(config,"correlation-event-filter");
		// class that contains mapper and reducer
		job.setJarByClass(MapReduceCorrelation.class);     

		Scan scan = new Scan();
		scan.setCaching(CACHING);      
		scan.setCacheBlocks(false);		
		
		SingleColumnValueFilter filter = new SingleColumnValueFilter(
			CF_EVENT,
			ATTR_ACT_INST,
			CompareOp.EQUAL,
			Bytes.toBytes(Long.MIN_VALUE));
		filter.setFilterIfMissing(false);
		scan.setFilter(filter);
		
		// gets the process instance table name
		if (!HEvent.class.isAnnotationPresent(Table.class))
			throw new RuntimeException("Cannot retrieved the event table name from annotated class. Table persistence annotation is not defined");
		Table table = HEvent.class.getAnnotation(Table.class);				
		
		TableMapReduceUtil.initTableMapperJob(
			table.name(),        
			scan,                 
			EventFilterMapper.class,
			LongWritable.class,     
			Text.class,     
			job);

		job.setCombinerClass(EventFilterCombiner.class);
		HFileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_PATH + "/" + sessionId)); 
		//job.submit();
		return job;			
	}
	/**
	 * 
	 * @param modelId
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static Job eventCorrelatorFilterStage(final EventCorrelation correlation, String sessionId) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = HBaseConfiguration.create();
		
		Job job = new Job(config, "correlation-event-correlation-filter");
		// class that contains mapper and reducer
		job.setJarByClass(MapReduceCorrelation.class);     

		Scan scan = new Scan();
		scan.setCaching(CACHING);      
		scan.setCacheBlocks(false);		
		
		// filters event-correlation on pairs (key, value)
		FilterList filterList = new FilterList();
		SingleColumnValueFilter keyFilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_KEY,
			CompareOp.EQUAL,
			Bytes.toBytes(correlation.getKey()));
		SingleColumnValueFilter valueFilter = new SingleColumnValueFilter(
			CF_EVENT_CORRELATION,
			ATTR_VALUE,
			CompareOp.EQUAL,
			Bytes.toBytes(correlation.getValue()));		
		filterList.addFilter(keyFilter);
		filterList.addFilter(valueFilter);		
		scan.setFilter(filterList);		
        
		// gets the event table name
		if (!HEventCorrelation.class.isAnnotationPresent(Table.class))
			throw new RuntimeException("Cannot retrieved the process instance table name from annotated class. Table persistence annotation is not defined");
		Table table = HEventCorrelation.class.getAnnotation(Table.class);				
		
		TableMapReduceUtil.initTableMapperJob(
			table.name(),         
			scan,                 
			EventCorrelatorFilterMapper.class, 
			LongWritable.class,                
			Text.class,                         
			job);
		
		job.setNumReduceTasks(NUM_RED_TASKS);  
		HFileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/" + sessionId + "/" + correlation.getKey()));  

		//job.submit();
		return job;
	}	
	/**
	 * 
	 * @param correlations
	 * @param sessionId
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	private static Job modelEventReducerStage(final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {	
		Configuration conf = new Configuration();

		Job job = new Job(conf, "correlation-model-event-reducer");
		job.setJarByClass(MapReduceCorrelation.class); 
		job.setNumReduceTasks(NUM_RED_TASKS);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);			
		
		job.setMapperClass(ModelEventMapper.class);
		job.setCombinerClass(ModelEventCombiner.class);
		job.setReducerClass(ModelEventReducer.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_MODEL_PATH + "/" + sessionId));
		FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_EVENT_PATH + "/" + sessionId));
		
		FileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_PATH + "/reduce/" + sessionId));

		//job.submit();
		return job;
	}	
	/**
	 * 
	 * @param correlations
	 * @param sessionId
	 * @throws InterruptedException 
	 * @throws ClassNotFoundException 
	 */
	private static Job eventCorrelationReducerStage(final Set<EventCorrelation> correlations, final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {	
		Configuration conf = new Configuration();
		conf.setInt("correlationKeysSize", correlations.size());

		Job job = new Job(conf, "correlation-event-correlation-reducer");
		job.setJarByClass(MapReduceCorrelation.class); 
		job.setNumReduceTasks(NUM_RED_TASKS);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);		
		
		job.setMapperClass(EventCorrelatorMapper.class);
		job.setCombinerClass(EventCorrelatorCombiner.class);
		job.setReducerClass(EventCorrelatorReducer.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		for (EventCorrelation eventCorrelation : correlations) {
			FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/" + sessionId + "/" + eventCorrelation.getKey()));
		}
		FileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/reduce/" + sessionId));

		//job.submit();
		return job;
	}
	/**
	 * 
	 * @param correlations
	 * @param sessionId
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 */
	private static Job eventReducerFinalStage(final String sessionId) throws IOException, ClassNotFoundException, InterruptedException {	
		Configuration conf = new Configuration();

		Job job = new Job(conf, "correlation-event-reducer");
		job.setJarByClass(MapReduceCorrelation.class); 
		job.setNumReduceTasks(NUM_RED_TASKS);
		
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);		
		
		job.setMapperClass(EventFinalMapper.class);
		job.setCombinerClass(EventFinalCombiner.class);
		job.setReducerClass(EventFinalReducer.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_EVENT_PATH + "/reduce/" + sessionId));
		FileInputFormat.addInputPath(job, new Path(MAP_REDUCE_COR_EVENT_CORRELATION_PATH + "/reduce/" + sessionId));
		
		FileOutputFormat.setOutputPath(job, new Path(MAP_REDUCE_CORRELATOR_PATH + "/reduce/" + sessionId));

		//job.submit();
		return job;
	}	
}