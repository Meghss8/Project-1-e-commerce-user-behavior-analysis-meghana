import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PurchaseTimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalPurchases = 0;

        // Sum up the total purchases for each key (hour + product category)
        for (IntWritable value : values) {
            totalPurchases += value.get();
        }

        // Emit the hour and product category with the total purchases
        context.write(key, new IntWritable(totalPurchases));
    }
}
