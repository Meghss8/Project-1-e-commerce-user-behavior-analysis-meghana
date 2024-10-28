package userengagement;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ProductReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        int interactions = 0;
        int purchases = 0;

        for (Text value : values) {
            String[] parts = value.toString().split(":");
            if (parts.length == 2) {
                if (parts[0].equals("interaction")) {
                    interactions += Integer.parseInt(parts[1]);
                } else if (parts[0].equals("purchase")) {
                    purchases += Integer.parseInt(parts[1]);
                }
            }
        }

        // Calculate conversion rate
        double conversionRate = interactions == 0 ? 0 : (double) purchases / interactions;
        context.write(key, new Text("Conversion Rate: " + String.format("%.2f", conversionRate)));
    }
}
