import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class Job2 {
	
	public static class Map2 extends Mapper<Object, Text, Text, Text> {
		private Text key1 = new Text();
		private Text value1 = new Text();
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			String element[] = null;
			element = line.split("	");
			String[] keyset = element[0].split("/");
			key1.set(keyset[0]);
			value1.set(element[1]);
		//	System.out.println(key1+" "+value1);
			context.write(key1, value1);
		}
	}

	public static class Reduce2 extends Reducer<Text, Text, Text, Text> {
		private Text stockName = new Text();
		private Text vol = new Text();
		double volatility = 0;
		public void reduce(Text key, Iterable<Text> values,Context context) throws IOException, InterruptedException {			
			double N = 0;
			double sum = 0;
			double y = 0;
			List<String> list1 = new ArrayList<String>();
			for (Text value:values){
			//	System.out.println(Double.valueOf(value.toString()));
				sum = sum + Double.valueOf(value.toString());
				list1.add(new String(value.toString()));
				N += 1;
			//	System.out.println(value+ "   "+key);
			}
		
			//System.out.println(key + "   N: "+N);
			double mean = sum/N;
			//System.out.println("Mean: "+ mean);
			
			for (Object x:list1){
				
				y = y + ((Double.valueOf(x.toString()) - mean)*(Double.valueOf(x.toString()) - mean));
				
			}
		//	System.out.println("Variance: "+ y);
			if(N == 1){
				volatility = 0;
			} else {
			volatility = Math.sqrt(y/(N-1));
			}
		//	System.out.println(volatility);
			if(volatility!=0){
			vol.set(Double.toString(volatility));
			stockName.set(key);
			context.write(vol,stockName);
			}
		}
	}
}
