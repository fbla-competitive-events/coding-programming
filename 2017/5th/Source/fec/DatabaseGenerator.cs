using System.Data.SQLite;

namespace fec {

    public static class DatabaseGenerator {

        /// <summary>
        /// Create a database and populate it with tables.
        /// </summary>
        public static void GenerateDB() {

            // Create the database file.
            SQLiteConnection.CreateFile(DatabaseProperties.DATABASE_NAME);

            // The query used to create the 'Employees' table.
            string employeeTableCreationQuery = "CREATE TABLE Employees (Id INTEGER PRIMARY KEY, Name TEXT, Job TEXT, Address TEXT, Phone BIGINT)";

            // The query used to create the 'Employee Schedule' table.
            string employeeScheduleTableCreationQuery = "CREATE TABLE [Employee Schedule] (Id INTEGER PRIMARY KEY, Name TEXT, "
                                                      + "Monday TEXT, Tuesday TEXT, Wednesday TEXT, "
                                                      + "Thursday TEXT, Friday TEXT, Saturday TEXT, "
                                                      + "Sunday TEXT)";

            // The query used to create the 'Customers' table.
            string customerTableCreationQuery = "CREATE TABLE Customers (Id INTEGER PRIMARY KEY, Name TEXT, Membership TEXT, Phone BIGINT)";

            // The query used to create the 'Customer Attendance' table.
            string customerAttendanceTableCreationQuery = "CREATE TABLE [Customer Attendance] (Id INTEGER PRIMARY KEY, Name TEXT, "
                                                        + "Monday TEXT, Tuesday TEXT, Wednesday TEXT, "
                                                        + "Thursday TEXT, Friday TEXT, Saturday TEXT, "
                                                        + "Sunday TEXT)";
            

            SQLiteCommand employeeTableCreationCommand = new SQLiteCommand(employeeTableCreationQuery, Queries.connection);
            SQLiteCommand employeeScheduleTableCreationCommand = new SQLiteCommand(employeeScheduleTableCreationQuery, Queries.connection);
            SQLiteCommand customerTableCreationCommand = new SQLiteCommand(customerTableCreationQuery, Queries.connection);
            SQLiteCommand customerAttendanceTableCreationCommand = new SQLiteCommand(customerAttendanceTableCreationQuery, Queries.connection);

            // Open a connection to the database and execute the queries. 
            // Close the connection when done.
            Queries.connection.Open();
            employeeTableCreationCommand.ExecuteNonQuery();
            employeeScheduleTableCreationCommand.ExecuteNonQuery();
            customerTableCreationCommand.ExecuteNonQuery();
            customerAttendanceTableCreationCommand.ExecuteNonQuery();
            Queries.connection.Close();
        }
    }
}
