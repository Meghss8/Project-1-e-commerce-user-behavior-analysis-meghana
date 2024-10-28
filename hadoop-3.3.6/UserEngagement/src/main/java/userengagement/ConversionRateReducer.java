package userengagement;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ConversionRateReducer extends Reducer<Text, IntWritable, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int purchaseCount = 0;
        int interactionCount = 0;

        for (IntWritable val : values) {
            if (val.get() == 1) {
                purchaseCount++;
            } else {
                interactionCount++;
            }
        }

        String conversionRate = (interactionCount > 0) 
            ? String.format("%.2f", (double) purchaseCount / interactionCount) 
            : "0.00";
        
        context.write(key, new Text("Purchases: " + purchaseCount + ", Interactions: " + interactionCount + ", Conversion Rate: " + conversionRate));
    }
}
