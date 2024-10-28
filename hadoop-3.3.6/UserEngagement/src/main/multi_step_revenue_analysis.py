from mrjob.job import MRJob
from mrjob.step import MRStep

class MRMultiStepRevenue(MRJob):

    def mapper(self, _, line):
        fields = line.split(',')
        
        # Skip the header row
        if fields[0] == 'ProductID':  # Adjust based on your actual header content
            return
        
        try:
            revenue = float(fields[5])  # Assuming RevenueGenerated is the 6th field
            product_id = fields[0]
            category = fields[2]
            yield (product_id, category), revenue
        except (IndexError, ValueError) as e:
            self.increment_counter('Mapper', 'InvalidValue', 1)  # Optional: Increment counter for invalid values
            return  # Skip invalid lines

    def reducer_total_revenue(self, key, values):
        total_revenue = sum(values)
        yield key, total_revenue

    def reducer_average_revenue(self, key, values):
        total_revenue = sum(values)
        count = sum(1 for _ in values)  # Count the number of values
        if count > 0:
            average_revenue = total_revenue / count
        else:
            average_revenue = 0  # Default value if no counts

        yield key[1], (key[0], average_revenue)

    def reducer_top_products(self, category, values):
        sorted_products = sorted(values, key=lambda x: x[1], reverse=True)
        for product in sorted_products[:3]:  # Get top 3 products
            yield category, product

    def steps(self):
        return [
            MRStep(mapper=self.mapper,
                    reducer=self.reducer_total_revenue),
            MRStep(reducer=self.reducer_average_revenue),
            MRStep(reducer=self.reducer_top_products)
        ]

if __name__ == '__main__':
    MRMultiStepRevenue.run()
