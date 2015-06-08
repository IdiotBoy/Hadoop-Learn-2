package com.hulu.xuxin.hadoop.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;


public class ExampleClient {
	
	public static void main(String[] args) throws IOException {
		//Configuration config = HBaseConfiguration.create();
		Configuration config = new Configuration();
		config.set("hbase.zookeeper.quorum",
			"xuxin-test5.server.hulu.com,xuxin-test6.server.hulu.com,xuxin-test7.server.hulu.com");
		
		// create table
		HBaseAdmin admin = new HBaseAdmin(config);
		
		TableName tablename = TableName.valueOf("test");
		HTableDescriptor htd = new HTableDescriptor(tablename);
		HColumnDescriptor hcd = new HColumnDescriptor("data");
		htd.addFamily(hcd);
		admin.createTable(htd);
		HTableDescriptor[] tables = admin.listTables();
		if (tables.length != 1 || tablename != tables[0].getTableName()) {
			throw new IOException("Failed create of table");
		}
		
		//
		HTable table = new HTable(config, tablename);
		byte[] row1 = Bytes.toBytes("row1");
		Put p1 = new Put(row1);
		byte[] databytes = Bytes.toBytes("data");
		p1.add(databytes, Bytes.toBytes("1"), Bytes.toBytes("value1"));
		table.put(p1);
		Get g = new Get(row1);
		Result result = table.get(g);
		System.out.println("Get: " + result);
		Scan scan = new Scan();
		ResultScanner scanner = table.getScanner(scan);
		try {
			for (Result r : scanner) {
				System.out.println("Scan: " + r);
			}
		} finally {
			scanner.close();
		}
		
		admin.disableTable(tablename);
		admin.deleteTable(tablename);
	}

}
