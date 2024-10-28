package userengagement;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ProductMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");
        
        // For user activity (4 fields): UserID, ProductID, ProductCategory, ActivityType
        if (fields.length == 4) {
            String productCategory = fields[2];
            String activityType = fields[3];

            if (!activityType.isEmpty()) {
                // Emit interaction
                context.write(new Text(productCategory), new Text("interaction:1"));
            }
        }

        // For transactions (3 fields): UserID, ProductID, ProductCategory
        if (fields.length == 3) {
            String productCategory = fields[2];
            // Emit purchase
            context.write(new Text(productCategory), new Text("purchase:1"));
        }
    }
}
