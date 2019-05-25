using System.Collections.Generic;
using System.ComponentModel;

namespace Entertainment_Elevated
{
    // Inherit from the Person subclass
    public class Employee : Person
    {
        // Set the correct headings for these properties in the GridVieW
        [DisplayName("Position")]
        public string Position { get;  set; }

        [DisplayName("Payrate")]
        public decimal Payrate { get; set; }
        
        public List<Shift> Shifts { get; set; }
        
        // Inherit the empty constructor from the base, but also initialize the shift list
        public Employee() : base()
        {
            Shifts = new List<Shift>();
        }

        public Employee(string FirstName, string LastName) : base(FirstName, LastName)
        {
            Shifts = new List<Shift>();
        }

        // Inherit from the base constructor, but add on the properties of position and payrate special to this class
        public Employee(string FirstName, string LastName, string PhoneNumber, string Email, string Position, decimal Payrate) 
            : base(FirstName, LastName, Email, PhoneNumber)
        {
            this.Position = Position;
            this.Payrate = Payrate;
            Shifts = new List<Shift>();
        }
    }
}
