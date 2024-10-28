package userengagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PurchaseTimeMapper extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text hourCategory = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        // Split the line into fields
        String[] fields = value.toString().split(",");

        // Check if the line has the expected number of fields (adjust based on your CSV structure)
        if (fields.length > 6) {
            try {
                // Assuming the timestamp is in the 7th field
                String timestamp = fields[6];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(timestamp);
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
                String hour = hourFormat.format(date);

                // Assuming the product category is in the 3rd field
                String productCategory = fields[2];

                // Emit the hour and product category
                hourCategory.set(hour + "_" + productCategory);
                context.write(hourCategory, one);
            } catch (Exception e) {
                // Handle parsing exceptions (log if necessary)
            }
        }
    }
}
