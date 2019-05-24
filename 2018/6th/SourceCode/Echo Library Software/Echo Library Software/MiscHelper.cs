using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;
using System.Xml.Serialization;

namespace Echo_Library_Software
{
    /// <summary>
    /// Static class used for all sorts of handy things.
    /// </summary>
    static class MiscHelper
    {
        public static UserSettings userSettings = new UserSettings();

        public static uint FNVHash(string str)
        {
            const uint fnv_prime = 0x811C9DC5;
            uint _hash = 0;
            uint i = 0;

            for (i = 0; i < str.Length; i++)
            {
                _hash *= fnv_prime;
                _hash ^= ((byte)str[(int)i]);
            }

            return _hash;
        }

        /// <summary>
        /// Little algorithm that generates a random string of digits and letters.
        /// </summary>
        /// <param name="length"></param>
        /// <returns></returns>
        public static string EchoHash(int length)
        {
            string _hash = "";
            Random _rnd = new Random();

            for (int i = 0; i < length; i++)
            {
                //Generate a value that is 0 or 1. If 0, the next character will be a digit. 
                //If 1, the next character will be a letter.
                int _nextCharType = _rnd.Next(0, 2);

                char _nextChar;

                //Add digit to string.
                if (_nextCharType == 0)
                {
                    //Generate random digit.
                    _nextChar = (char)_rnd.Next(48, 58);

                    _hash += _nextChar;
                }
                //Add letter to string.
                else if (_nextCharType == 1)
                {
                    _nextChar = (char)_rnd.Next(65, 91);

                    _hash += _nextChar;
                }
            }

            return _hash;
        }

        public static void SerializeObjectToXML<T>(T item, string FilePath)
        {
            XmlSerializer _xs = new XmlSerializer(typeof(T));
            using (StreamWriter _sw = new StreamWriter(FilePath))
            {
                _xs.Serialize(_sw, item);
            }
        }

        public static T DeserializeXMLFileToObject<T>(string XmlFilename)
        {
            T returnObject = default(T);
            if (string.IsNullOrEmpty(XmlFilename)) return default(T);

            try
            {
                
                XmlSerializer _xs = new XmlSerializer(typeof(T));
                using (StreamReader _sr = new StreamReader(XmlFilename))
                {
                    returnObject = (T)_xs.Deserialize(_sr);
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine("Deserializer has broken -- " + ex);
            }
            return returnObject;
        }
    }
}
