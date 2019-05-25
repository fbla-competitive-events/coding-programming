using System;

namespace Entertainment_Elevated
{
    public class StoreVisit
    {
        public DateTime StartTime { get; set; }

        public DateTime EndTime { get; set; }

        public StoreVisit() { }

        // StartTime is the time when the person enters the store
        public StoreVisit(DateTime StartTime)
        {
            this.StartTime = StartTime;
        }

        public StoreVisit(DateTime StartTime, DateTime EndTime)
        {
            this.StartTime = StartTime;
            this.EndTime = EndTime;
        }
        
        // Return decimal values rather than double values
        // Decimal values hold exact values, but double values could possible produce inaccuracies
        // Due to the IEEE floating point formating
        public decimal NumberHours()
        {
            TimeSpan timeSpan = EndTime.Subtract(StartTime);

            // Calculate the number of hours of the visit by taking the number of hours
            // Then adding on the number of minutes
            // Use an 'm' suffix to convert 60 to a decimal
            // This forces the / operator to be a decimal operator to calculate the current decimal to return
            return timeSpan.Hours + (timeSpan.Minutes / 60m);
        }
    }
}
