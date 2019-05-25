using Syncfusion.Windows.Forms.Grid.Grouping;
using System;
using System.Collections;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.Windows.Forms;

namespace fec {
    public class GridPrinter {

        private readonly GridGroupingControl databaseGrid;
        private string tableName;
        private PrintPageEventArgs e;
        private DataSet db;

        /// <summary>
        /// The GridPrinter constructor. Initializes the GridPrinter object with passed values.
        /// </summary>
        /// <param name="tblName"> The name of the table to print. </param>
        /// <param name="grid"> The grid displaying the database contents. </param>
        /// <param name="database"> The data object containing the database values. </param>
        /// <param name="ev"> The print event from a PrintPage event. </param>
        public GridPrinter(string tblName, ref GridGroupingControl grid, ref DataSet database, PrintPageEventArgs ev) {
            databaseGrid = grid;
            tableName = tblName;
            e = ev;
            db = database;
        }

        /// <summary>
        /// Print the selected table
        /// </summary>
        public void Print() {
            int cellHeight = 0; //Used to get/set the grid cell height
            int totalWidth = 0;
            int rowCounter = 0;//Used as a row counter
            bool isFirstPage = true; // Used to check whether the first page is being printed
            bool isNewPage = true;// Used to check whether a new page is being printed
            int headerHeight = 0; // Used to store the header height

            StringFormat strFormat = new StringFormat(); //Used to format the grid rows.
            ArrayList columnsLeft = new ArrayList();//Used to save left coordinates of columns
            ArrayList columnWidths = new ArrayList(); //Used to save column widths

            Font drawFont = databaseGrid.Font;
            Font headerFont = new Font(drawFont, FontStyle.Bold);
            Bitmap image = new Bitmap(Properties.Resources.fecLogo);


            strFormat.Alignment = StringAlignment.Center;
            strFormat.LineAlignment = StringAlignment.Center;
            strFormat.Trimming = StringTrimming.EllipsisCharacter;

            // Calculate total table width
            totalWidth = 0;
            foreach (GridColumnDescriptor col in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {
                totalWidth += col.Width;
            }

            try {
                // Set the left page margin
                int leftMargin = e.MarginBounds.Left;

                // Set the top page margin
                int topMargin = e.MarginBounds.Top;

                // Used to check whether there are more pages to print
                bool morePagesToPrint = false;

                int tempWidth = 0;

                // For the first page to be printed, set the cell width and header height
                if (isFirstPage) {
                    foreach (GridColumnDescriptor GridCol in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {
                        tempWidth = (int)(Math.Floor(GridCol.Width /
                            (double)totalWidth * totalWidth *
                            (e.MarginBounds.Width / (double)totalWidth)));

                        headerHeight = (int)(e.Graphics.MeasureString(GridCol.HeaderText,
                            drawFont, tempWidth).Height) + 11;

                        // Save width and height of headers
                        columnsLeft.Add(leftMargin);
                        columnWidths.Add(tempWidth);
                        leftMargin += tempWidth;
                    }
                }

                // Print all grid rows
                while (rowCounter <= db.Tables[tableName].Rows.Count - 1) {

                    // Set the cell height
                    cellHeight = databaseGrid.TableOptions.RecordRowHeight + 5;
                    int iCount = 0;

                    // Check top see whether the current page settings allows more rows to print
                    if (topMargin + cellHeight >= e.MarginBounds.Height + e.MarginBounds.Top) {
                        isNewPage = true;
                        isFirstPage = false;
                        morePagesToPrint = true;
                        break;
                    } 
                    else {
                        if (isNewPage) {

                            e.Graphics.DrawImage(image, new Point(50, 15));

                            //Draw report header
                            e.Graphics.DrawString(tableName,
                                headerFont,
                                Brushes.Black, e.MarginBounds.Left,
                                e.MarginBounds.Top - e.Graphics.MeasureString(tableName,
                                headerFont,
                                e.MarginBounds.Width).Height - 13);

                            string strDate = DateTime.Now.ToLongDateString() + " " +
                                DateTime.Now.ToShortTimeString();

                            // Draw the current date and time in the header
                            e.Graphics.DrawString(strDate,
                                                    headerFont, Brushes.Black,
                                                    e.MarginBounds.Left +
                                                    (e.MarginBounds.Width - e.Graphics.MeasureString(strDate,
                                                    headerFont,
                                                    e.MarginBounds.Width).Width),
                                                    e.MarginBounds.Top - e.Graphics.MeasureString(tableName,
                                                    headerFont, e.MarginBounds.Width).Height - 13);

                            // Draw table columns                 
                            topMargin = e.MarginBounds.Top;
                            foreach (GridColumnDescriptor GridCol in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {

                                // Draw table top
                                e.Graphics.FillRectangle(new SolidBrush(Color.DeepSkyBlue),
                                    new Rectangle((int)columnsLeft[iCount], topMargin,
                                    (int)columnWidths[iCount], headerHeight));

                                // Draw table borders
                                e.Graphics.DrawRectangle(Pens.SkyBlue,
                                    new Rectangle((int)columnsLeft[iCount], topMargin,
                                    (int)columnWidths[iCount], headerHeight));

                                // Draw column headers
                                e.Graphics.DrawString(GridCol.HeaderText,
                                    drawFont,
                                    new SolidBrush(Color.White),
                                    new RectangleF((int)columnsLeft[iCount], topMargin,
                                    (int)columnWidths[iCount], headerHeight), strFormat);
                                iCount++;
                            }
                            isFirstPage = false;
                            topMargin += headerHeight;
                        }

                        // Holds the x coordinate of current the cell to be printed
                        int xValue = (int)columnsLeft[0];

                        // Holds the width of the current cell to be printed
                        int width;

                        //Draw the contents of the table columns                
                        for (int rowIndex = 0;rowIndex < db.Tables[tableName].Rows.Count;rowIndex++) {
                            for (int colIndex = 0;colIndex < databaseGrid.GetTable(tableName).TableDescriptor.Columns.Count;colIndex++) {

                                width = (int)(Math.Floor(databaseGrid.GetTable(tableName).TableDescriptor.Columns[colIndex].Width /
                                (double)totalWidth * totalWidth *
                                (e.MarginBounds.Width / (double)totalWidth)));


                                // Draw cell contents
                                e.Graphics.DrawString(db.Tables[tableName].Rows[rowCounter][colIndex].ToString(),
                                    drawFont,
                                    new SolidBrush(Color.Black),
                                    new RectangleF(xValue,
                                    topMargin,
                                    width, cellHeight),
                                    strFormat);

                                // Draw cell borders 
                                e.Graphics.DrawRectangle(Pens.SkyBlue,
                                    new Rectangle(xValue, topMargin,
                                    width, cellHeight));

                                // Add the current cell width to the xValue, so that next cell gets printed next to the current cell
                                xValue += width;
                            }

                            // Reset the xValue
                            xValue = (int)columnsLeft[0];

                            rowCounter++;

                            // Add to topMargin the current cellHeight so that the next row is printed below the current one
                            topMargin += cellHeight;
                        }
                    }
                }

                //I f more lines exist, print another page.
                if (morePagesToPrint) {
                    e.HasMorePages = true;
                } else {
                    e.HasMorePages = false;
                }
            } catch (Exception exc) {
                MessageBox.Show(exc.Message, "Error", MessageBoxButtons.OK,
                   MessageBoxIcon.Error);
            }
        }
    }
}


