using System.Data.SQLite;

namespace fec {

    public static class Queries {

        // The database connection object. Used to connect to the database.
        public static readonly SQLiteConnection connection = new SQLiteConnection("Data Source = FEC_DATABASE.db; Version=3; PRAGMA synchronous=off;");

        #region Employee Queries
        // The query used to add a new employee entry.
        public const string addEmployeeQuery = "INSERT INTO Employees (Id, Name, Job, Address, Phone) "
                                             + "VALUES (@id, @name, @job, @address, @phone)";

        // The query used to edit an employee entry.
        public const string editEmployeeQuery = "UPDATE Employees SET Name = @name, Job = @job, Address = @address, "
                                              + "Phone = @phone WHERE Id = @id";

        // The query used to delete an employee entry.
        public const string deleteEmployeeQuery = "DELETE FROM Employees WHERE Id = @id";

        // The query used to get the data of a specific employee entry.
        public const string getEmployeeQuery = "SELECT * FROM Employees WHERE Id = @id";
        #endregion

        #region Employee Schedule Queries
        // The query used to add a new employee schedule entry.
        public const string addEmployeeScheduleQuery = "INSERT INTO [Employee Schedule] (Id, Name, Monday, Tuesday, Wednesday, "
                                                     + "Thursday, Friday, Saturday, Sunday) "
                                                     + "VALUES (@id, @name, @monday, @tuesday, @wednesday, @thursday, @friday, "
                                                     + "@saturday, @sunday)";

        // The query used to edit an employee schedule entry.
        public const string editEmployeeScheduleQuery = "UPDATE [Employee Schedule] SET Monday = @monday, Tuesday = @tuesday, "
                                                      + "Wednesday = @wednesday, Thursday = @thursday, Friday = @friday, "
                                                      + "Saturday = @saturday, Sunday = @sunday WHERE Id = @id";

        // The query used to edit the name in an employee schedule entry.
        public const string editEmployeeScheduleNameQuery = "UPDATE [Employee Schedule] SET Name = @name WHERE Id = @id";

        // The query used to delete an employee schedule entry.
        public const string deleteEmployeeScheduleQuery = "DELETE FROM [Employee Schedule] WHERE Id = @id";

        // The query used to get the data of a specific employee schedule entry.
        public const string getEmployeeScheduleQuery = "SELECT * FROM [Employee Schedule] WHERE Id = @id";

        // The query used to reset the employee schedule values to 'OFF'
        public const string resetEmployeeScheduleQuery = "UPDATE [Employee Schedule] SET Monday = 'OFF', Tuesday = 'OFF', "
                                                         + "Wednesday = 'OFF', Thursday = 'OFF', Friday = 'OFF', "
                                                         + "Saturday = 'OFF', Sunday = 'OFF'";
        #endregion

        #region Customer Queries
        // The query used to add a new customer entry.
        public const string addCustomerQuery = "INSERT INTO Customers (Id, Name, Membership, Phone) "
                                             + "VALUES (@id, @name, @membership, @phone)";

        // The query used to edit new customer entry.
        public const string editCustomerQuery = "UPDATE Customers SET Name = @name, Membership = @membership, Phone = @phone WHERE Id = @id";

        // The query used to delete a customer entry.
        public const string deleteCustomerQuery = "DELETE FROM Customers WHERE Id = @id";

        // The query used to get the data of a specific customer entry.
        public const string getCustomerQuery = "SELECT * FROM Customers WHERE Id = @id";
        #endregion

        #region Customer Attendance Queries
        // The query used to add a new customer attendance entry.
        public const string addCustomerAttendanceQuery = "INSERT INTO [Customer Attendance] (Id, Name, Monday, Tuesday, Wednesday, "
                                                       + "Thursday, Friday, Saturday, Sunday) "
                                                       + "VALUES (@id, @name, @monday, @tuesday, @wednesday, @thursday, @friday, "
                                                       + "@saturday, @sunday)";

        // The query used to edit a customer attendance entry.
        public const string editCustomerAttendanceQuery = "UPDATE [Customer Attendance] SET Monday = @monday, Tuesday = @tuesday, "
                                                        + "Wednesday = @wednesday, Thursday = @thursday, Friday = @friday, "
                                                        + "Saturday = @saturday, Sunday = @sunday WHERE Id = @id";

        // The query used to edit the name of a customer attendance entry.
        public const string editCustomerAttendanceNameQuery = "UPDATE [Customer Attendance] SET Name = @name WHERE Id = @id";

        // The query used to delete a customer attendance entry.
        public const string deleteCustomerAttendanceQuery = "DELETE FROM [Customer Attendance] WHERE Id = @id";

        // The query used to add a new customer attendance entry.
        public const string getCustomerAttendanceQuery = "SELECT * FROM [Customer Attendance] WHERE Id = @id";

        // The query used to reset the customer attendance values to 'N/A'.
        public const string resetCustomerAttendanceQuery = "UPDATE [Customer Attendance] SET Monday = 'N/A', Tuesday = 'N/A', "
                                                         + "Wednesday = 'N/A', Thursday = 'N/A', Friday = 'N/A', "
                                                         + "Saturday = 'N/A', Sunday = 'N/A'";
        #endregion

        #region Utility Queries
        // The query used to compact the database.
        public const string vaccumQuery = "VACUUM";

        // The query used to perform a quick database integrity check.
        public const string integrityCheckQuery = "PRAGMA quick_check";
        #endregion
    }
}
