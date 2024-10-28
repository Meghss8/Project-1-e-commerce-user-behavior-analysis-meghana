package userengagement;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ConversionRateMapper extends Mapper<Object, Text, Text, IntWritable> {
    private Text productCategory = new Text();
    private IntWritable one = new IntWritable(1);
    private IntWritable zero = new IntWritable(0);

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");

        // Assuming fields[2] is ProductCategory and fields[1] indicates interaction/purchase
        if (fields.length > 2) {
            String category = fields[2];
            String interactionType = fields[1]; // "interaction" or "purchase"
            
            productCategory.set(category);
            if ("purchase".equals(interactionType)) {
                context.write(productCategory, one); // Emit for purchase
            } else {
                context.write(productCategory, zero); // Emit for interaction
            }
        }
    }
}
