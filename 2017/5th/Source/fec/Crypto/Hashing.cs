using System.IO;
using System.Security.Cryptography;

namespace Crypto {

    public static class Hashing {

        public struct Algorithms {
            public static readonly KeyedHashAlgorithm HMACSHA256 = new HMACSHA256();
            public static readonly KeyedHashAlgorithm HMACSHA512 = new HMACSHA512();
        }

        #region HMAC Hash Operations
        /// <summary>
        /// Hash a value with an HMAC algorithm.
        /// </summary>
        /// <param name="value"> The value to hash. </param>
        /// <returns> The hashed value bytes. </returns>
        public static byte[] HashHMAC(byte[] key, byte[] value, KeyedHashAlgorithm algorithm) {
            algorithm.Key = key;

            byte[] result = algorithm.ComputeHash(value);

            return result;
        }

        /// <summary>
        /// Hash a file with an HMAC algorithm taking key in bytes.
        /// </summary>
        /// <param name="value"> The file to hash. </param>
        /// <returns> The hashed file bytes. </returns>
        public static byte[] HashFileHMAC(byte[] key, Stream file, KeyedHashAlgorithm algorithm) {
            algorithm.Key = key;

            byte[] result = algorithm.ComputeHash(file);

            return result;
        }
        #endregion
    }
}