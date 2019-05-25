using System;
using System.IO;
using System.Runtime.InteropServices;
using System.Security;
using System.Security.Cryptography;

namespace Crypto {

    public static class Utilities {

        /// <summary>
        /// Derive bytes from password using PBKDF2.
        /// </summary>
        /// <param name="password"> The password to derive bytes from. </param>
        /// <param name="salt"> The salt to use. </param>
        /// <param name="iterations"> The iterations of PBKDF2. </param>
        /// <param name="returnBytes"> The number of bytes to return. </param>
        /// <returns> The derived key bytes. </returns>
        public static byte[] DeriveBytes_PBKDF2(string password, byte[] salt, int iterations, int returnBytes) {
            Rfc2898DeriveBytes keyBytes = new Rfc2898DeriveBytes(password, salt, iterations);
            return keyBytes.GetBytes(returnBytes);
        }

        /// <summary>
        /// Get the salt from an encrypted database file.
        /// </summary>
        /// <param name="encryptedDBFileName"> The file name of the encrypte database from which to get the salt from. </param>
        /// <returns> The read salt. </returns>
        public static byte[] GetSalt(string encryptedDBFileName) {
            byte[] salt = new byte[32];

            using (FileStream fsInput = File.OpenRead(encryptedDBFileName)) {
                fsInput.Seek(112, SeekOrigin.Begin);
                fsInput.Read(salt, 0, 32);
            }           

            return salt;
        }

        /// <summary>
        /// Convert SecureString to string by using unmanaged memory marshalling.
        /// </summary>
        /// <param name="value"> The Secure String object to decrypt and convert to a string. </param>
        /// <returns> The SecureString converted to a string. </returns>
        public static string SecureStringToString(SecureString value) {
            IntPtr valuePtr = IntPtr.Zero;

            try {
                // Copy the contents of the managed SecureString to unmanaged memory and decrypt it.
                valuePtr = Marshal.SecureStringToGlobalAllocUnicode(value);

                return Marshal.PtrToStringUni(valuePtr);
            }
            finally {
                // Free unmanaged string pointer.
                Marshal.ZeroFreeGlobalAllocUnicode(valuePtr);
            }
        }
    }
}
