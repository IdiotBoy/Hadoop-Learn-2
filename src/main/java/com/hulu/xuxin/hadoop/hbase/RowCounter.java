package com.hulu.xuxin.hadoop.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class RowCounter {
	static final String NAME = "rowcounter";
	
	static class RowCounterMapper extends TableMapper<ImmutableBytesWritable, Result> {
		public static enum Counters { ROWS }
		
		@Override
		public void map(ImmutableBytesWritable row, Result values, Context context)
			throws IOException {
			for (Cell cell : values.listCells()) {
				if (cell.getValueArray().length > 0) {
					context.getCounter(Counters.ROWS).increment(1);
					break;
				}
			}
		}
	}
	
	public static Job createSubmittableJob(Configuration conf, String[] args)
			throws IOException{
		String tableName = args[0];
		Job job = new Job(conf, NAME + "_" + tableName);
		job.setJarByClass(RowCounter.class);
		
		StringBuilder sb = new StringBuilder();
		final int columnOffset = 1;
		for (int i = columnOffset; i < args.length; i++) {
			if (i > columnOffset) {
				sb.append(" ");
			}
			sb.append(args[i]);
		}
		Scan scan = new Scan();
		scan.setFilter(new FirstKeyOnlyFilter());
		if (sb.length() > 0) {
			for (String columnName : sb.toString().split(" ")) {
				String[] fields = columnName.split(":");
				if (fields.length == 1)
					scan.addFamily(Bytes.toBytes(fields[0]));
				else
					scan.addColumn(Bytes.toBytes(fields[0]), Bytes.toBytes(fields[1]));
			}
		}
		
		job.setOutputFormatClass(NullOutputFormat.class);
		TableMapReduceUtil.initTableMapperJob(tableName, scan, RowCounterMapper.class,
				ImmutableBytesWritable.class, Result.class, job);
		job.setNumReduceTasks(0);
		return job;
	}
	
	public static void main(String[] args) throws Exception {
		Configuration config = new Configuration();
		config.set("hbase.zookeeper.quorum",
			"xuxin-test5.server.hulu.com,xuxin-test6.server.hulu.com,xuxin-test7.server.hulu.com");
		String[] otherArgs = new GenericOptionsParser(config, args).getRemainingArgs();
		if (otherArgs.length < 1) {
			System.err.println("ERROR: Wrong number of parameters: " + args.length);
			System.err.println("Usage: RowCounter <tablename> [<column1> <column2>...]");
			System.exit(-1);
		}
		Job job = createSubmittableJob(config, otherArgs);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
