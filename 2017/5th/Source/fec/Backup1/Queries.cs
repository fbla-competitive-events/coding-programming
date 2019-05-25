using System.Data.SQLite;

namespace fec {

    public static class Queries {

        public static readonly SQLiteConnection connection = new SQLiteConnection("Data Source = FEC_DATABASE.db; Version=3; PRAGMA synchronous=off;");

        public const string addEmployeeQuery = "INSERT INTO Employees (Id, Name, Job, Address, Phone) "
                                                + "VALUES (@id, @name, @job, @address, @phone)";

        public const string fillEmployeeGridQuery = "SELECT * FROM Employees";

        public const string editEmployeeQuery = "UPDATE Employees SET Name = @name, Job = @job, Address = @address, "
                                                 + "Phone = @phone WHERE Id = @id";

        public const string deleteEmployeeQuery = "DELETE FROM Employees WHERE Id = @id";

        public const string getEmployeeQuery = "SELECT * FROM Employees WHERE Id = @id";

        public const string addEmployeeScheduleQuery = "INSERT INTO [Employee Schedule] (Id, Name, Monday, Tuesday, Wednesday, "
                                                       + "Thursday, Friday, Saturday, Sunday) "
                                                       + "VALUES (@id, @name, @monday, @tuesday, @wednesday, @thursday, @friday, "
                                                       + "@saturday, @sunday)";

        public const string editEmployeeScheduleQuery = "UPDATE [Employee Schedule] SET Monday = @monday, Tuesday = @tuesday, "
                                                        + "Wednesday = @wednesday, Thursday = @thursday, Friday = @friday, "
                                                        + "Saturday = @saturday, Sunday = @sunday WHERE Id = @id";

        public const string editEmployeeScheduleNameQuery = "UPDATE [Employee Schedule] SET Name = @name WHERE Id = @id";

        public const string removeEmployeeScheduleQuery = "DELETE FROM [Employee Schedule] WHERE Id = @id";

        public const string getEmployeeScheduleQuery = "SELECT * FROM [Employee Schedule] WHERE Id = @id";


        public const string addCustomerQuery = "INSERT INTO Customers (Id, Name, Membership, Phone) "
                                               + "VALUES (@id, @name, @membership, @phone)";

        public const string editCustomerQuery = "UPDATE Customers SET Name = @name, Membership = @membership, Phone = @phone WHERE Id = @id";

        public const string getCustomerQuery = "SELECT * FROM Customers WHERE Id = @id";

        public const string getCustomerAttendanceQuery = "SELECT * FROM [Customer Attendance] WHERE Id = @id";

        public const string addCustomerAttendanceQuery = "INSERT INTO [Customer Attendance] (Id, Name, Monday, Tuesday, Wednesday, "
                                                         + "Thursday, Friday, Saturday, Sunday) "
                                                         + "VALUES (@id, @name, @monday, @tuesday, @wednesday, @thursday, @friday, "
                                                         + "@saturday, @sunday)";

        public const string editCustomerAttendanceQuery = "UPDATE [Customer Attendance] SET Monday = @monday, Tuesday = @tuesday, "
                                                          + "Wednesday = @wednesday, Thursday = @thursday, Friday = @friday, "
                                                          + "Saturday = @saturday, Sunday = @sunday WHERE Id = @id";

        public const string editCustomerAttendanceNameQuery = "UPDATE [Customer Attendance] SET Name = @name WHERE Id = @id";

        public const string deleteCustomerQuery = "DELETE FROM Customers WHERE Id = @id";

        public const string deleleCustomerAttendanceQuery = "DELETE FROM [Customer Attendance] WHERE Id = @id";

        public const string vaccumQuery = "VACUUM";
    }
}
