package com.hulu.xuxin.hadoop.hdfs;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

public class ListStatus {

	// hdfs://elsaudnn002.prod.hulu.com/
	public static void main(String[] args) throws IOException {
		String uri = args[0];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(URI.create(uri), conf);
		
		Path[] paths = new Path[args.length];
		for (int i = 0; i < paths.length; i++) {
			paths[i] = new Path(args[i]);
		}
		FileStatus[] status = fs.listStatus(paths);
		Path[] listedPaths = FileUtil.stat2Paths(status);
		for (Path path : listedPaths) {
			System.out.println(path);
		}
	}

}
