using System;
using System.Collections.Generic;

namespace Entertainment_Elevated
{
    // Inherit this class from the Person class
    public class Customer : Person
    {
        // List of StoreVisits for each unique time this customer visits the FEC
        public List<StoreVisit> StoreVisits { get; private set; } = new List<StoreVisit>();

        // Inherit the empty constructor from the base class
        public Customer() : base()
        {
        }

        // Inherit the constructor from the base class
        public Customer(string FirstName, string LastName, string Email, string PhoneNumber) : base(FirstName, LastName, Email, PhoneNumber)
        {
        }

        public override string ToString()
        {
            return FirstName + " " + LastName + " - " + StoreVisits.Count.ToString();
        }
    }
}
