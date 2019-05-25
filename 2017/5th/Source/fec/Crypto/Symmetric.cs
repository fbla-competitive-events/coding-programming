using System.IO;
using System.Security.Cryptography;

namespace Crypto {

    public static class Symmetric {

        #region Database Encryption Operations
        /// <summary>
        /// Encrypt a database file using AES-256 (Rijndael).
        /// </summary>
        /// <param name="dbFileName"> The database file to encrypt. </param>
        /// <param name="encryptedDBFileName"> The name of the encrypted database file. </param>
        /// <param name="password"> The password to use for encryption. </param>
        /// <param name="pbkdf2Iterations"> The iterations for the key derivation function. </param>
        public static void EncryptDB(string dbFileName, string encryptedDBFileName, string password, int pbkdf2Iterations) {

            // The 256-bit password that will be used for the actual encryption.
            byte[] passwordBytes = new byte[32];

            // The 256-bit key used for the computation of the HMAC hashes.
            byte[] hmacKeyBytes = new byte[32];

            // The 128-bit initialization vector used for the encryption.
            byte[] ivBytes = Generator.GenerateBytes(16);

            // The 256-bit salt used to derive the master key.
            byte[] saltBytes = Generator.GenerateBytes(32);

            // The 512-bit derived master key from the password.
            byte[] key = Utilities.DeriveBytes_PBKDF2(password, saltBytes, pbkdf2Iterations, 64);

            // Assign the lower-half 256 bits of the master key as the password.
            for (int i = 0; i < 32; i++) {
                passwordBytes[i] = key[i];
            }

            // Assign the upper-half 256 bits of the master key as the HMAC key.
            for (int i = 32; i < 64; i++) {
                hmacKeyBytes[i-32] = key[i];
            }

            // Create a new Rijndael (AES) object.
            using (RijndaelManaged AES = new RijndaelManaged()) {
                // Set the mode of AES to Cipher Block Chaining.
                AES.Mode = CipherMode.CBC;

                // Set the key size of AES to 256-bits.
                AES.KeySize = 256;

                // Set the block size (IV size) of AES to 128-bits.
                AES.BlockSize = 128;

                // Open a stream to the input database.
                using (FileStream fsInput = File.OpenRead(dbFileName)) {

                    // Open a stream to the encrypted database.
                    using (FileStream fsOutput = new FileStream(encryptedDBFileName, FileMode.Create, FileAccess.ReadWrite, FileShare.None)) {

                        // Skip space for the password HMAC hash, the file HMAC hash, the IV bytes, and the salt bytes.
                        fsOutput.Seek(144, SeekOrigin.Begin);

                        // Create an encryptor from the specified password, IV, and AES object.
                        using (ICryptoTransform aesEncrypt = AES.CreateEncryptor(passwordBytes, ivBytes)) {

                            // Bind a cryptographic stream to the encrypted database output stream.
                            using (CryptoStream cryptoStream = new CryptoStream(fsOutput, aesEncrypt, CryptoStreamMode.Write)) {

                                // Create a buffer for faster encryption.
                                byte[] buffer = new byte[4096];

                                // Perform encryption.
                                int bytesRead = 0;
                                while ((bytesRead = fsInput.Read(buffer, 0, buffer.Length)) > 0) {
                                    cryptoStream.Write(buffer, 0, bytesRead);
                                }
                                cryptoStream.FlushFinalBlock();

                                // Seek to space after the password HMAC and the file HMAC hashes.
                                fsOutput.Seek(96, SeekOrigin.Begin);

                                // Write the IV bytes and the salt bytes.
                                fsOutput.Write(ivBytes, 0, 16);
                                fsOutput.Write(saltBytes, 0, 32);

                                // Seek to space after the password HMAC and the file HMAC hashes.
                                fsOutput.Seek(96, SeekOrigin.Begin);

                                // Hash the cipher text for integrity validation.
                                byte[] fileHmac = Hashing.HashFileHMAC(hmacKeyBytes, fsOutput, Hashing.Algorithms.HMACSHA256);

                                // Hash the password for authentication.
                                byte[] passwordHmac = Hashing.HashHMAC(hmacKeyBytes, passwordBytes, Hashing.Algorithms.HMACSHA512);

                                // Seek to the beginning of the file and write the password HMAC and file HMAC hashes in the space.
                                fsOutput.Seek(0, SeekOrigin.Begin);
                                fsOutput.Write(passwordHmac, 0, 64);
                                fsOutput.Write(fileHmac, 0, 32);
                            }
                        }
                    }
                }
            }
        }

        /// <summary>
        /// Decrypt a database file.
        /// </summary>
        /// <param name="encryptedDBFileName"> The database file to decrypt. </param>
        /// <param name="dbFileName"> The file name of the decrypted database file. </param>
        /// <param name="password"> The password to use for decryption. </param>
        /// <param name="pbkdf2Iterations"> The iterations for the key derivation function. </param>
        public static void DecryptDB(string encryptedDBFileName, string dbFileName, string password, int pbkdf2Iterations) {

            // The 256-bit password that will be used for the actual decryption.
            byte[] passwordBytes = new byte[32];

            // The 256-bit key used for the computation of the HMAC hashes.
            byte[] hmacKeyBytes = new byte[32];

            // The 256-bit salt used to derive the master key.
            byte[] saltBytes = Utilities.GetSalt(encryptedDBFileName);

            // The 512-bit derived master key from the password.
            byte[] key = Utilities.DeriveBytes_PBKDF2(password, saltBytes, pbkdf2Iterations, 64);

            // Assign the lower-half 256 bits of the master key as the password.
            for (int i = 0; i < 32; i++) {
                passwordBytes[i] = key[i];
            }

            // Assign the upper-half 256 bits of the master key as the HMAC key.
            for (int i = 32; i < 64; i++) {
                hmacKeyBytes[i-32] = key[i];
            }

            // Create a new Rijndael (AES) object.
            using (RijndaelManaged AES = new RijndaelManaged()) {
                // Set the mode of AES to Cipher Block Chaining.
                AES.Mode = CipherMode.CBC;

                // Set the key size of AES to 256-bits.
                AES.KeySize = 256;

                // Set the block size (IV size) of AES to 128-bits.
                AES.BlockSize = 128;

                // Open a stream to the encrypted database.
                using (FileStream fsInput = File.OpenRead(encryptedDBFileName)) {

                    // Holds the bytes of the password HMAC hash.
                    byte[] passwordHmac = new byte[64];

                    // Holds the bytes of the cipher text HMAC hash.
                    byte[] fileHmac = new byte[32];

                    // Holds the initialization vector to be used for decryption.
                    byte[] ivBytes = new byte[16];

                    // Read the password HMAC hash.
                    fsInput.Read(passwordHmac, 0, 64);

                    // Calculate the HMAC hash of the provided password.
                    byte[] actualKeyHmac = Hashing.HashHMAC(hmacKeyBytes, passwordBytes, Hashing.Algorithms.HMACSHA512);

                    // If the HMAC of the provided password and the password HMAC written in the file match, then the provided password is valid.
                    if (ByteArraysEqual(actualKeyHmac, passwordHmac)) {

                        // Read the file HMAC hash.
                        fsInput.Read(fileHmac, 0, 32);

                        // Read the IV bytes written in the encrypted database file.
                        fsInput.Read(ivBytes, 0, 16);

                        // Seek to space after the password HMAC and file HMAC hashes.
                        fsInput.Seek(96, SeekOrigin.Begin);

                        // Calculate the HMAC hash of the actual cipher text of the encrypted database.
                        byte[] actualFileHmac = Hashing.HashFileHMAC(hmacKeyBytes, fsInput, Hashing.Algorithms.HMACSHA256);

                        // Seek to the cipher text.
                        fsInput.Seek(144, SeekOrigin.Begin);

                        // If the HMAC of the actual cipher text and the HMAC of the cipher text written in the file match, then the encrypted database is authentic.
                        if (ByteArraysEqual(actualFileHmac, fileHmac)) {

                            // Create a decryptor from the specified password, IV, and AES object.
                            using (ICryptoTransform aesDecrypt = AES.CreateDecryptor(passwordBytes, ivBytes)) {

                                // Bind a cryptographic stream to the encrypted database input stream.
                                using (CryptoStream cryptoStream = new CryptoStream(fsInput, aesDecrypt, CryptoStreamMode.Read)) {

                                    // Create a database file to hold the decrypted contents of the encrypted database.
                                    using (FileStream fsOutput = new FileStream(dbFileName, FileMode.Create)) {

                                        // Create a buffer for faster decryption.
                                        byte[] buffer = new byte[4096];

                                        // Perform decryption.
                                        int bytesRead = 0;
                                        while ((bytesRead = cryptoStream.Read(buffer, 0, buffer.Length)) > 0) {
                                            fsOutput.Write(buffer, 0, bytesRead);
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            throw new CryptographicException("Database authenticity could not be verified.");
                        }
                    }
                    else {
                        throw new CryptographicException("Invalid password.");
                    }
                }
            }
        }

        /// <summary>
        /// Checks whether two byte arrays have the same contents.
        /// </summary>
        /// <param name="b1"> The first array. </param>
        /// <param name="b2"> The second array. </param>
        /// <returns> Whether the two byte arrays have the same contents. </returns>
        private static bool ByteArraysEqual(byte[] b1, byte[] b2) {
            // If either array references are null, return false.
            if (b1 == null || b2 == null) {
                return false;
            }

            // If the arrays are not of equal length, then they are not equal.
            if (b1.Length != b2.Length) {
                return false;
            }

            // Compare the two arrays.
            for (int i = 0; i < b1.Length; i++) {
                if (b1[i] != b2[i]) {
                    return false;
                }
            }

            return true;
        }
        #endregion
    }
}