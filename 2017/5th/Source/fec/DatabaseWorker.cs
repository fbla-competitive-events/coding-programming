using System;
using System.Data.SQLite;

namespace fec {

    public static class DatabaseWorker {

        // Used for data sharing.
        public static int tempId;

        #region Employee Methods
        /// <summary>
        /// Add a new employee entry to the database.
        /// </summary>
        /// <param name="employeeName"> The name of the employee. </param>
        /// <param name="job"> The job of the employee. </param>
        /// <param name="address"> The address of the employee. </param>
        /// <param name="phone"> The phone of the employee. </param>
        /// <param name="workDays"> The days the employee works in the FEC. </param>
        public static void AddEmployee(int employeeId, string employeeName, string job, string address, string phone, string[] schedule) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@job", job);
            command.Parameters.AddWithValue("@address", address);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            AddEmployeeSchedule(employeeId, employeeName, schedule);
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit an employee entry in the database.
        /// </summary>
        /// <param name="id"> The id of the employee entry to edit. </param>
        /// <param name="employeeName"> The new name of the employee. </param>
        /// <param name="job"> The new job of the employee. </param>
        /// <param name="address"> The new address of the employee. </param>
        /// <param name="phone"> The new phone of the employee. </param>
        /// <param name="workDays"> The new work days of the employee. </param>
        public static void EditEmployee(int id, string employeeName, string job, string address, string phone) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@job", job);
            command.Parameters.AddWithValue("@address", address);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            EditEmployeeScheduleName(id, employeeName);
            Queries.connection.Close();
        }

        /// <summary>
        /// Remove the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee to remove. </param>
        public static void RemoveEmployee(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            RemoveEmployeeSchedule(id);
            Queries.connection.Close();
        }

        /// <summary>
        /// Get the employee entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get data for. </param>
        /// <returns> The data of the employee entry. </returns>
        public static string[] GetEmployeeEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getEmployeeQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[5];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[0].ToString();
                    values[1] = reader[1].ToString();
                    values[2] = reader[2].ToString();
                    values[3] = reader[3].ToString();
                    values[4] = reader[4].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }
        #endregion

        #region Employee Schedule Methods
        /// <summary>
        /// Add a new employee schedule entry to the database.
        /// This method is meant to be called from the AddEmployee method.
        /// </summary>
        /// <param name="employeeId"> The id of the employee. </param>
        /// <param name="employeeName"> The name of the employee for whom this schedule is for. </param>
        /// <param name="scheduleValues"> The values of the schedule. </param>
        private static void AddEmployeeSchedule(int employeeId, string employeeName, string[] scheduleValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@name", employeeName);
            command.Parameters.AddWithValue("@monday", scheduleValues[0]);
            command.Parameters.AddWithValue("@tuesday", scheduleValues[1]);
            command.Parameters.AddWithValue("@wednesday", scheduleValues[2]);
            command.Parameters.AddWithValue("@thursday", scheduleValues[3]);
            command.Parameters.AddWithValue("@friday", scheduleValues[4]);
            command.Parameters.AddWithValue("@saturday", scheduleValues[5]);
            command.Parameters.AddWithValue("@sunday", scheduleValues[6]);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Edit an employee schedule entry in the database.
        /// </summary>
        /// <param name="employeeId"> The id of the employee whose schedule is to be changed. </param>
        /// <param name="scheduleValues"> The new schedule values. </param>
        public static void EditEmployeeSchedule(int employeeId, string[] scheduleValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", employeeId);
            command.Parameters.AddWithValue("@monday", scheduleValues[0]);
            command.Parameters.AddWithValue("@tuesday", scheduleValues[1]);
            command.Parameters.AddWithValue("@wednesday", scheduleValues[2]);
            command.Parameters.AddWithValue("@thursday", scheduleValues[3]);
            command.Parameters.AddWithValue("@friday", scheduleValues[4]);
            command.Parameters.AddWithValue("@saturday", scheduleValues[5]);
            command.Parameters.AddWithValue("@sunday", scheduleValues[6]);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit the name field of an Employee Schedule entry.
        /// This method is meant to be called from the EditEmployee method.
        /// </summary>
        /// <param name="id"> The id of the employee schedule to edit. </param>
        /// <param name="employeeName"> The new name assigned to the schedule. </param>
        private static void EditEmployeeScheduleName(int id, string employeeName) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editEmployeeScheduleNameQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", employeeName);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Remove the schedule of the employee with the specified id.
        /// This method is meant to be called from the RemoveEmployee method.
        /// </summary>
        /// <param name="id"> The id of the employe whose schedule to remove. </param>
        private static void RemoveEmployeeSchedule(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Get the schedule data of the employee with the specified id.
        /// </summary>
        /// <param name="id"> The id of the employee for whom to get the schedule data. </param>
        /// <returns> The schedule data of the employee. </returns>
        public static string[] GetEmployeeScheduleEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[9];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    for (int i = 0;i < 9;i++) {
                        values[i] = reader[i].ToString();
                    }
                }
            }
            Queries.connection.Close();

            return values;
        }

        /// <summary>
        /// Reset the values of the employee schedule table to 'OFF'.
        /// </summary>
        public static void ResetEmployeeSchedule() {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.resetEmployeeScheduleQuery;
            command.Connection = Queries.connection;

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }
        #endregion

        #region Customer Methods
        /// <summary>
        /// Add a new customer entry to the database.
        /// </summary>
        /// <param name="customerId"> The id of the customer. </param>
        /// <param name="customerName"> The name of the customer. </param>
        /// <param name="membership"> The membership of the customer. </param>
        /// <param name="phone"> The phone of the customer. </param>
        /// <param name="attedance"> The attendance values of the customer. </param>
        public static void AddCustomer(int customerId, string customerName, string membership, string phone, string[] attedance) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", customerId);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@membership", membership);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            AddCustomerAttendance(customerId, customerName, attedance);
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit a customer entry in the database.
        /// </summary>
        /// <param name="id"> The id of the customer entry to edit. </param>
        /// <param name="customerName"> The new name of the customer. </param>
        /// <param name="membership"> The new membership of the customer. </param>
        /// <param name="phone"> The new phone of the customer. </param>
        public static void EditCustomer(int id, string customerName, string membership, string phone) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@membership", membership);
            command.Parameters.AddWithValue("@phone", Convert.ToInt64(phone));

            Queries.connection.Open();
            command.ExecuteNonQuery();
            EditCustomerAttendanceName(id, customerName);
            Queries.connection.Close();
        }

        /// <summary>
        /// Remove the customer entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the customer to remove. </param>
        public static void RemoveCustomer(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            RemoveCustomerAttendance(id);
            Queries.connection.Close();
        }

        /// <summary>
        /// Get the customer entry with the specified id from the database.
        /// </summary>
        /// <param name="id"> The id of the customer for whom to get data for. </param>
        /// <returns> The data of the customer entry. </returns>
        public static string[] GetCustomerEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getCustomerQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[4];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    values[0] = reader[0].ToString();
                    values[1] = reader[1].ToString();
                    values[2] = reader[2].ToString();
                    values[3] = reader[3].ToString();
                }
            }
            Queries.connection.Close();

            return values;
        }
        #endregion

        #region Customer Attendance Methods
        /// <summary>
        /// Add a new customer attendance entry to the database.
        /// This method is peant to be called from the AddCustomer method.
        /// </summary>
        /// <param name="customerId"> The id of the customer. </param>
        /// <param name="customerName"> The name of the customer. </param>
        /// <param name="attendanceValues"> The attendance values of the customer.</param>
        private static void AddCustomerAttendance(int customerId, string customerName, string[] attendanceValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.addCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", customerId);
            command.Parameters.AddWithValue("@name", customerName);
            command.Parameters.AddWithValue("@monday", attendanceValues[0]);
            command.Parameters.AddWithValue("@tuesday", attendanceValues[1]);
            command.Parameters.AddWithValue("@wednesday", attendanceValues[2]);
            command.Parameters.AddWithValue("@thursday", attendanceValues[3]);
            command.Parameters.AddWithValue("@friday", attendanceValues[4]);
            command.Parameters.AddWithValue("@saturday", attendanceValues[5]);
            command.Parameters.AddWithValue("@sunday", attendanceValues[6]);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Edit a customer attendance entry in the database.
        /// </summary>
        /// <param name="id"> The id of the customer. </param>
        /// <param name="attendanceValues"> The new attendance values of the customer. </param>
        public static void EditCustomerAttendance(int id, string[] attendanceValues) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@monday", attendanceValues[0]);
            command.Parameters.AddWithValue("@tuesday", attendanceValues[1]);
            command.Parameters.AddWithValue("@wednesday", attendanceValues[2]);
            command.Parameters.AddWithValue("@thursday", attendanceValues[3]);
            command.Parameters.AddWithValue("@friday", attendanceValues[4]);
            command.Parameters.AddWithValue("@saturday", attendanceValues[5]);
            command.Parameters.AddWithValue("@sunday", attendanceValues[6]);

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }

        /// <summary>
        /// Edit the name field of a customer attendance entry, 
        /// </summary>
        /// <param name="id"> The id of the customer. </param>
        /// <param name="customerName"> The new name of the customer. </param>
        private static void EditCustomerAttendanceName(int id, string customerName) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.editCustomerAttendanceNameQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);
            command.Parameters.AddWithValue("@name", customerName);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Remove the attendance entry for the customer with the specified id from the database.
        /// This method is meant to be called from the RemoveCustomer method.
        /// </summary>
        /// <param name="id"> The id of the customer to remove the attendance for. </param>
        private static void RemoveCustomerAttendance(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.deleteCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            command.ExecuteNonQuery();
        }

        /// <summary>
        /// Get a customer attendance entry from the database.
        /// </summary>
        /// <param name="id"> The id of the customer. </param>
        /// <returns> The customer attendance data. </returns>
        public static string[] GetCustomerAttendanceEntry(int id) {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.getCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            command.Parameters.AddWithValue("@id", id);

            string[] values = new string[9];

            Queries.connection.Open();
            using (SQLiteDataReader reader = command.ExecuteReader()) {
                while (reader.Read()) {
                    for (int i = 0;i < 9;i++) {
                        values[i] = reader[i].ToString();
                    }
                }
            }
            Queries.connection.Close();

            return values;
        }

        /// <summary>
        /// Reset the values of the customer attendance table to 'N/A'.
        /// </summary>
        public static void ResetCustomerAttendance() {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.resetCustomerAttendanceQuery;
            command.Connection = Queries.connection;

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }
        #endregion

        #region Utilities
        /// <summary>
        /// Check if an employee or customer with the specified id exists in the database.
        /// </summary>
        /// <param name="id"> The id of the employee or customer to search for. </param>
        /// <returns> Whether an entry with the specified id exists. </returns>
        public static bool IdExists(int id) {
            SQLiteCommand commandEmployee = new SQLiteCommand();
            SQLiteCommand commandCustomer = new SQLiteCommand();

            commandEmployee.CommandType = System.Data.CommandType.Text;
            commandEmployee.CommandText = Queries.getEmployeeQuery;
            commandEmployee.Connection = Queries.connection;

            commandCustomer.CommandType = System.Data.CommandType.Text;
            commandCustomer.CommandText = Queries.getCustomerQuery;
            commandCustomer.Connection = Queries.connection;

            commandEmployee.Parameters.AddWithValue("@id", id);
            commandCustomer.Parameters.AddWithValue("@id", id);

            Queries.connection.Open();
            try {
                commandEmployee.ExecuteNonQuery();
                commandCustomer.ExecuteNonQuery();
            }
            catch (Exception) {
                Queries.connection.Close();
                return true;
            }
            Queries.connection.Close();
            return false;
        }

        /// <summary>
        /// Restructure and compact the database so as to use minimum disk space.
        /// </summary>
        public static void CompactDatabase() {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.vaccumQuery;
            command.Connection = Queries.connection;

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }

        /// <summary>
        /// Perform a database integrity check.
        /// </summary>
        public static void CheckIntegrity() {
            SQLiteCommand command = new SQLiteCommand();

            command.CommandType = System.Data.CommandType.Text;
            command.CommandText = Queries.integrityCheckQuery;
            command.Connection = Queries.connection;

            Queries.connection.Open();
            command.ExecuteNonQuery();
            Queries.connection.Close();
        }
        #endregion
    }
}
