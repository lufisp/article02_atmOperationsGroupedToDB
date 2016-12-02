package hbaseAdo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Component;

public class HbaseDAO {

	protected Configuration conf;
	protected Connection connection;
	protected Admin admin;
	protected boolean _DEBUG = false;

	public HbaseDAO() {
		try {
			conf = HBaseConfiguration.create();
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
		} catch (IOException e) {

		}
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Map<String, Map<String, String>> getAllRowsUnderFamiliys(String _tableName,String... _columnFamily) {
		HashMap<String, Map<String, String>> resultMap = new HashMap<>();
		try {
			Table table = connection.getTable(TableName.valueOf(_tableName));
			Scan scan = new Scan();
			for (String columnFam : _columnFamily)
				scan.addFamily(Bytes.toBytes(columnFam));
			ResultScanner resultScanner = table.getScanner(scan);
			for (Result result : resultScanner) {
				HashMap<String, String> mapValues = new HashMap<>();
				for (byte[] columnFamily : result.getMap().keySet()) {
					for (byte[] column : result.getFamilyMap(columnFamily).keySet()) {
						mapValues.put(Bytes.toString(column), Bytes.toString(result.getValue(columnFamily, column)));
					}
				}
				resultMap.put(Bytes.toString(result.getRow()), mapValues);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	public static final void main(String... args) {
		HbaseDAO hbDAO = new HbaseDAO();
		System.out.println(hbDAO.getAllRowsUnderFamiliys("atm:AtmTotalCash", "Total", "GeoLoc"));

	}

}