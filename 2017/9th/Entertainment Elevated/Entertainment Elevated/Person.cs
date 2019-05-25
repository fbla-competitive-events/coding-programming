using System.ComponentModel;

namespace Entertainment_Elevated
{
    // Base class for both Customer and Employee classes
    public class Person
    {
        // Set the correct headings for these properties in the GridView
        [DisplayName("First Name")]
        public string FirstName { get; set; }

        [DisplayName("Last Name")]
        public string LastName { get; set; }

        [DisplayName("Email")]
        public string Email { get; set; }

        [DisplayName("Phone Number")]
        public string PhoneNumber { get; set; }

        // Empty Constructor
        public Person() { }

        public Person(string FirstName, string LastName)
        {
            this.FirstName = FirstName;
            this.LastName = LastName;
        }

        public Person(string FirstName, string LastName, string Email, string PhoneNumber)
        {
            this.FirstName = FirstName;
            this.LastName = LastName;
            this.Email = Email;
            this.PhoneNumber = PhoneNumber;
        }

        // Override the standard ToString method to output the full name of the person
        public override string ToString()
        {
            return FirstName + " " + LastName;
        }
    }
}
