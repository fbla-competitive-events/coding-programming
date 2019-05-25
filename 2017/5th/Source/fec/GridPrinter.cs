using Syncfusion.Windows.Forms;
using Syncfusion.Windows.Forms.Grid.Grouping;
using System;
using System.Collections;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;

namespace fec {

    public class GridPrinter {

        private readonly GridGroupingControl databaseGrid; // Holds a reference to the database grid.
        private readonly string tableName; // Holds the name of the table to print.
        private readonly DataSet db; // Holds a reference to the DataSet underlying the database grid.

        private PrintPageEventArgs page; // Holds values such as the page height and width.

        private readonly StringFormat format = new StringFormat(); // Used to format the grid rows.
        private ArrayList headersX = new ArrayList(); // Used to save left coordinates of columns.
        private ArrayList columnWidths = new ArrayList(); // Used to save column widths.

        private int rowCounter = 0; // Used as a row counter.
        private bool firstPage = true; // Used to check if the first page is being printed in a session.
        private bool newPage = true; // Used to check whether a new page is being printed.
        private bool morePagesToPrint = false; // Used to control whether there are more pages to print.

        private int rowIndex = 0; // The index of the row currently being printed.

        private float pageHeight; // The height of the page to print on.
        private float pageWidth; // The width of the page to print on.

        private int totalWidth = 0; // Used to hold the total width of the table.
        private int headerHeight = 0; // Used to store the height of the table header.

        private readonly Font drawFont; // The font used for printing.
        private readonly Font colHeaderFont; // The font used for the column header printing.
        private readonly Font headerFont; // The font used for the header printing.
        private readonly Bitmap logo; // The logo of the FEC.

        // Holds the current local computer date.
        private readonly string strDate = DateTime.Now.ToLongDateString() + " "
                                        + DateTime.Now.ToShortTimeString();

        // The number of the page being printed.
        private int pageNumber = 1;


        /// <summary>
        /// The GridPrinter constructor. Initializes the GridPrinter object with passed values.
        /// </summary>
        /// <param name="tblName"> The name of the table to print. </param>
        /// <param name="grid"> The grid displaying the database contents. </param>
        /// <param name="database"> The data set containing the database values. </param>
        /// <param name="ev"> The print event from a PrintPage event. </param>
        public GridPrinter(string tblName, ref GridGroupingControl grid, ref DataSet database) {
            databaseGrid = grid;
            tableName = tblName;
            db = database;

            // Set printing formatting.
            format.Alignment = StringAlignment.Center;
            format.LineAlignment = StringAlignment.Center;
            format.Trimming = StringTrimming.EllipsisCharacter;

            // Set the fonts and the logo on the report.
            FontFamily fontFamily = new FontFamily("Microsoft Sans Serif");
            drawFont = new Font(fontFamily, 8.25f, FontStyle.Regular, GraphicsUnit.Point);
            headerFont = new Font(fontFamily, 8.25f, FontStyle.Bold);
            colHeaderFont = new Font(fontFamily, 8.75f, FontStyle.Regular);
            logo = new Bitmap(Properties.Resources.fecLogo);
        }

        /// <summary>
        /// Sets the PrintPageEvent and the page height and width.
        /// Must be called for each page to be printed.
        /// </summary>
        public void SetPage(ref PrintPageEventArgs ev) {
            page = ev;

            // Set the page height according to page orientation.
            if (firstPage) {
                // If landscape, the height and width are reversed.
                if (ev.PageSettings.Landscape) {
                    pageHeight = page.PageSettings.PrintableArea.Width;
                    pageWidth = page.PageSettings.PrintableArea.Height;
                }
                else {
                    pageHeight = page.PageSettings.PrintableArea.Height;
                    pageWidth = page.PageSettings.PrintableArea.Width;
                }
            }
        }

        /// <summary>
        /// Print the selected table.
        /// </summary>
        public void Print() {

            // Used to get/set the grid cell height.
            int cellHeight = 0;

            try {

                // Set the left page margin.
                int leftMargin = page.MarginBounds.Left;

                // Holds the vertical distance from the top margin.
                int offsetY = page.MarginBounds.Top;

                // If the first page is being printed se the cell width and header height.
                if (firstPage) {
                    int leftDistance = leftMargin;
                    int tempWidth = 0;

                    foreach (GridColumnDescriptor col in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {
                        totalWidth += col.Width;
                    }

                    // Calculate and set the cell width and header height.
                    foreach (GridColumnDescriptor GridCol in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {
                        tempWidth = (int)(Math.Floor(GridCol.Width /
                            (double)totalWidth * totalWidth *
                            (page.MarginBounds.Width / (double)totalWidth)));

                        headerHeight = (int)(page.Graphics.MeasureString(GridCol.HeaderText,
                            headerFont, tempWidth).Height) + 11;

                        // Save width and height of headers.
                        headersX.Add(leftDistance);
                        columnWidths.Add(tempWidth);

                        leftDistance += tempWidth;
                    }
                }

                // Print all grid rows.
                while (rowCounter <= db.Tables[tableName].Rows.Count - 1) {

                    // Set the cell height.
                    cellHeight = databaseGrid.TableOptions.RecordRowHeight + 5;
                    int index = 0;

                    if (newPage) {

                        #region Header & Page Number Drawing
                        // Draw the FEC logo in the page header.
                        page.Graphics.DrawImage(logo, new Point(50, 15));

                        // Draw the table name in the page header.
                        page.Graphics.DrawString(tableName,
                            headerFont,
                            Brushes.Black, page.MarginBounds.Left,
                            page.MarginBounds.Top - page.Graphics.MeasureString(tableName,
                            headerFont,
                            page.MarginBounds.Width).Height - 13);

                        // Draw the current date and time in the page header.
                        page.Graphics.DrawString(strDate,
                                                headerFont, Brushes.Black,
                                                page.MarginBounds.Left +
                                                (page.MarginBounds.Width - page.Graphics.MeasureString(strDate,
                                                headerFont,
                                                page.MarginBounds.Width).Width),
                                                page.MarginBounds.Top - page.Graphics.MeasureString(tableName,
                                                headerFont, page.MarginBounds.Width).Height - 13);

                        // If this is not the first page draw the page number.
                        if (!firstPage) {
                            page.Graphics.DrawString(pageNumber.ToString(),
                                                        drawFont,
                                                        Brushes.Black,
                                                        pageWidth / 2,
                                                        pageHeight - page.MarginBounds.Top / 2);
                        }
                        #endregion

                        offsetY = page.MarginBounds.Top;

                        #region Table Column Drawing
                        // Draw table columns.
                        foreach (GridColumnDescriptor GridCol in databaseGrid.GetTable(tableName).TableDescriptor.Columns) {

                            // Draw table top.
                            page.Graphics.FillRectangle(new SolidBrush(Color.DeepSkyBlue),
                                new Rectangle((int)headersX[index], offsetY,
                                (int)columnWidths[index], headerHeight));

                            // Draw table borders.
                            page.Graphics.DrawRectangle(Pens.SkyBlue,
                                new Rectangle((int)headersX[index], offsetY,
                                (int)columnWidths[index], headerHeight));

                            // Draw column headers.
                            page.Graphics.DrawString(GridCol.HeaderText,
                                colHeaderFont,
                                new SolidBrush(Color.White),
                                new RectangleF((int)headersX[index], offsetY,
                                (int)columnWidths[index], headerHeight), format);

                            index++;
                        }
                        #endregion

                        offsetY += headerHeight;
                        newPage = false;
                    }
                    // Holds the x coordinate of current the cell to be printed.
                    int xValue = leftMargin;

                    // Holds the width of the current cell to be printed.
                    int width;

                    #region Table Column Content Drawing
                    //Draw the contents of the table columns.            
                    for (; rowIndex < db.Tables[tableName].Rows.Count; rowIndex++) {
                        for (int colIndex = 0; colIndex < databaseGrid.GetTable(tableName).TableDescriptor.Columns.Count; colIndex++) {

                            width = (int)(Math.Floor(databaseGrid.GetTable(tableName).TableDescriptor.Columns[colIndex].Width /
                            (double)totalWidth * totalWidth *
                            (page.MarginBounds.Width / (double)totalWidth)));


                            // Draw cell contents.
                            page.Graphics.DrawString(db.Tables[tableName].Rows[rowCounter][colIndex].ToString(),
                                drawFont,
                                new SolidBrush(Color.Black),
                                new RectangleF(xValue,
                                offsetY,
                                width, cellHeight),
                                format);

                            // Draw cell borders.
                            page.Graphics.DrawRectangle(Pens.SkyBlue,
                                new Rectangle(xValue, offsetY,
                                width, cellHeight));

                            // Add the current cell width to the xValue, so that next cell gets printed next to the current cell.
                            xValue += width;
                        }

                        // Reset the xValue.
                        xValue = leftMargin;

                        // Add to topMargin the current cellHeight so that the next row is printed below the current one.
                        offsetY += cellHeight;

                        rowCounter++;

                        // Check top see whether the current page settings allows more rows to print.
                        if (offsetY >= pageHeight - page.MarginBounds.Top) {
                            newPage = true;
                            morePagesToPrint = true;
                            rowIndex++;

                            // If this is the first page draw the page number.
                            if (firstPage) {
                                page.Graphics.DrawString(pageNumber.ToString(),
                                                        drawFont,
                                                        Brushes.Black,
                                                        pageWidth / 2,
                                                        pageHeight - page.MarginBounds.Top / 2);
                            }

                            firstPage = false;

                            pageNumber++;

                            break;
                        }
                    }
                    #endregion

                    if (morePagesToPrint) break;
                }

                // If more lines exist, print another page. Recalls the PrintPage event.
                if (morePagesToPrint && rowCounter != db.Tables[tableName].Rows.Count) {
                    page.HasMorePages = true;
                    morePagesToPrint = false;
                }
                else {
                    page.HasMorePages = false;
                }
            }
            catch (Exception ex) {
                MessageBoxAdv.Show(ex.Message, "Error");
            }
        }
    }
}