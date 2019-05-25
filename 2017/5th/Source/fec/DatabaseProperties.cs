using System.Security;

namespace fec {

    public static class DatabaseProperties {

        // The name of the database file.
        public const string DATABASE_NAME = "FEC_DATABASE.db";

        // The name of the encrypted database file.
        public const string ENCRYPTED_DATABASE_NAME = "FEC_DATABASE.db.enc";

        // The key deriving iterations.
        public const int PBKDF2_ITERATIONS = 100000;

        // Holds the database password in encrypted memory.
        public static SecureString password = new SecureString();
    }
}
