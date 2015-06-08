package com.hulu.xuxin.hadoop.hdfs;

import java.io.*;
import java.net.URL;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.*;

public class URLCat {
	static {
		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
	}
	
	public static void main(String[] args) throws Exception {
		String uri = args[0];
		InputStream in = null;
		try {
			in = new URL(uri).openStream();
			IOUtils.copyBytes(in, System.out, 4096, false);
		} finally {
			IOUtils.closeStream(in);
		}
		
	}
}
