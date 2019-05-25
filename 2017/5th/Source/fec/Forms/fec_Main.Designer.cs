
namespace fec {

    partial class fec_Main {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(fec_Main));
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo10 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo9 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo8 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo7 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo6 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo5 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo4 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo3 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo2 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo1 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.CaptionImage captionImage1 = new Syncfusion.Windows.Forms.CaptionImage();
            this.databaseGrid = new Syncfusion.Windows.Forms.Grid.Grouping.GridGroupingControl();
            this.addEmployeeButton = new System.Windows.Forms.Button();
            this.removeButton = new System.Windows.Forms.Button();
            this.toolTip = new Syncfusion.Windows.Forms.Tools.SuperToolTip(this);
            this.searchTextBox = new System.Windows.Forms.TextBox();
            this.searchButton = new System.Windows.Forms.Button();
            this.searchComboBox = new Syncfusion.Windows.Forms.Tools.ComboBoxAdv();
            this.addCustomerButton = new System.Windows.Forms.Button();
            this.searchRangeComboBox = new Syncfusion.Windows.Forms.Tools.ComboBoxAdv();
            this.printGridButton = new System.Windows.Forms.Button();
            this.settingsButton = new System.Windows.Forms.Button();
            this.tableResetButton = new System.Windows.Forms.Button();
            this.gridDocument = new System.Drawing.Printing.PrintDocument();
            this.label1 = new System.Windows.Forms.Label();
            this.encryptionWorker = new System.ComponentModel.BackgroundWorker();
            ((System.ComponentModel.ISupportInitialize)(this.databaseGrid)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.searchComboBox)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.searchRangeComboBox)).BeginInit();
            this.SuspendLayout();
            // 
            // databaseGrid
            // 
            this.databaseGrid.AllowedOptimizations = ((Syncfusion.Grouping.EngineOptimizations)((((Syncfusion.Grouping.EngineOptimizations.DisableCounters | Syncfusion.Grouping.EngineOptimizations.VirtualMode) 
            | Syncfusion.Grouping.EngineOptimizations.PassThroughSort) 
            | Syncfusion.Grouping.EngineOptimizations.RecordsAsDisplayElements)));
            this.databaseGrid.AlphaBlendSelectionColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(94)))), ((int)(((byte)(171)))), ((int)(((byte)(222)))));
            this.databaseGrid.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
            | System.Windows.Forms.AnchorStyles.Left) 
            | System.Windows.Forms.AnchorStyles.Right)));
            this.databaseGrid.Appearance.AnyCell.DropDownStyle = Syncfusion.Windows.Forms.Grid.GridDropDownStyle.Exclusive;
            this.databaseGrid.Appearance.AnyCell.RightToLeft = System.Windows.Forms.RightToLeft.Inherit;
            this.databaseGrid.Appearance.AnyCell.TextAlign = Syncfusion.Windows.Forms.Grid.GridTextAlign.Left;
            this.databaseGrid.BackColor = System.Drawing.SystemColors.Window;
            this.databaseGrid.CacheRecordValues = false;
            this.databaseGrid.ChildGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.databaseGrid.ChildGroupOptions.ShowFilterBar = false;
            this.databaseGrid.ChildGroupOptions.ShowSummaries = false;
            this.databaseGrid.CounterLogic = Syncfusion.Grouping.EngineCounters.YAmount;
            this.databaseGrid.DefaultGridBorderStyle = Syncfusion.Windows.Forms.Grid.GridBorderStyle.Dashed;
            this.databaseGrid.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.databaseGrid.GridOfficeScrollBars = Syncfusion.Windows.Forms.OfficeScrollBars.Metro;
            this.databaseGrid.GridVisualStyles = Syncfusion.Windows.Forms.GridVisualStyles.Metro;
            this.databaseGrid.HorizontalScrollTips = true;
            this.databaseGrid.InvalidateAllWhenListChanged = false;
            this.databaseGrid.Location = new System.Drawing.Point(40, 56);
            this.databaseGrid.MarkColHeader = true;
            this.databaseGrid.MarkRowHeader = true;
            this.databaseGrid.Name = "databaseGrid";
            this.databaseGrid.NestedTableGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.databaseGrid.NestedTableGroupOptions.ShowFilterBar = false;
            this.databaseGrid.NestedTableGroupOptions.ShowGroupHeader = false;
            this.databaseGrid.NestedTableGroupOptions.ShowStackedHeaders = true;
            this.databaseGrid.PrintRowHeader = false;
            this.databaseGrid.ShowCurrentCellBorderBehavior = Syncfusion.Windows.Forms.Grid.GridShowCurrentCellBorder.GrayWhenLostFocus;
            this.databaseGrid.ShowRelationFields = Syncfusion.Grouping.ShowRelationFields.Hide;
            this.databaseGrid.Size = new System.Drawing.Size(687, 422);
            this.databaseGrid.TabIndex = 0;
            this.databaseGrid.TableDescriptor.AllowNew = false;
            this.databaseGrid.TableDescriptor.Appearance.AnyCell.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.databaseGrid.TableDescriptor.Appearance.AnyCell.TextAlign = Syncfusion.Windows.Forms.Grid.GridTextAlign.Default;
            this.databaseGrid.TableDescriptor.TableOptions.CaptionRowHeight = 29;
            this.databaseGrid.TableDescriptor.TableOptions.ColumnHeaderRowHeight = 25;
            this.databaseGrid.TableDescriptor.TableOptions.RecordRowHeight = 32;
            this.databaseGrid.TableOptions.AllowDragColumns = false;
            this.databaseGrid.TableOptions.AllowMultiColumnSort = false;
            this.databaseGrid.TableOptions.AllowSelection = Syncfusion.Windows.Forms.Grid.GridSelectionFlags.AlphaBlend;
            this.databaseGrid.TableOptions.ColumnsMaxLengthFirstNRecords = 0;
            this.databaseGrid.TableOptions.ColumnsMaxLengthStrategy = Syncfusion.Windows.Forms.Grid.Grouping.GridColumnsMaxLengthStrategy.None;
            this.databaseGrid.TableOptions.DefaultColumnWidth = 100;
            this.databaseGrid.TableOptions.DrawTextWithGdiInterop = true;
            this.databaseGrid.TableOptions.IndentWidth = 0;
            this.databaseGrid.TableOptions.ListBoxSelectionMode = System.Windows.Forms.SelectionMode.None;
            this.databaseGrid.TableOptions.RecordPreviewRowHeight = 32;
            this.databaseGrid.TableOptions.RecordRowHeight = 32;
            this.databaseGrid.TableOptions.RowHeaderWidth = 18;
            this.databaseGrid.TableOptions.ShowRowHeader = true;
            this.databaseGrid.TableOptions.ShowTableIndent = true;
            this.databaseGrid.TabStop = false;
            this.databaseGrid.Text = "Database Grid";
            this.databaseGrid.TopLevelGroupOptions.ShowAddNewRecordAfterDetails = false;
            this.databaseGrid.TopLevelGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.databaseGrid.TopLevelGroupOptions.ShowColumnHeaders = true;
            this.databaseGrid.TopLevelGroupOptions.ShowFilterBar = true;
            this.databaseGrid.UseDefaultsForFasterDrawing = true;
            this.databaseGrid.UseRightToLeftCompatibleTextBox = true;
            this.databaseGrid.VersionInfo = "14.3200.0.49";
            this.databaseGrid.RecordValueChanged += new Syncfusion.Grouping.RecordValueChangedEventHandler(this.DatabaseGrid_RecordValueChanged);
            // 
            // addEmployeeButton
            // 
            this.addEmployeeButton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.addEmployeeButton.BackColor = System.Drawing.Color.White;
            this.addEmployeeButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.addEmployeeButton.Image = ((System.Drawing.Image)(resources.GetObject("addEmployeeButton.Image")));
            this.addEmployeeButton.Location = new System.Drawing.Point(745, 111);
            this.addEmployeeButton.Name = "addEmployeeButton";
            this.addEmployeeButton.Size = new System.Drawing.Size(37, 37);
            this.addEmployeeButton.TabIndex = 1;
            this.addEmployeeButton.TabStop = false;
            toolTipInfo10.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo10.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image9")));
            toolTipInfo10.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo10.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo10.Body.Text = "Add a new employee in the database.";
            toolTipInfo10.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo10.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo10.ForeColor = System.Drawing.Color.White;
            toolTipInfo10.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo10.Header.Text = "Add Employee";
            this.toolTip.SetToolTip(this.addEmployeeButton, toolTipInfo10);
            this.addEmployeeButton.UseVisualStyleBackColor = false;
            this.addEmployeeButton.Click += new System.EventHandler(this.AddEmployeeButton_Click);
            // 
            // removeButton
            // 
            this.removeButton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.removeButton.BackColor = System.Drawing.Color.White;
            this.removeButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.removeButton.Image = ((System.Drawing.Image)(resources.GetObject("removeButton.Image")));
            this.removeButton.Location = new System.Drawing.Point(745, 234);
            this.removeButton.Name = "removeButton";
            this.removeButton.Size = new System.Drawing.Size(37, 37);
            this.removeButton.TabIndex = 2;
            this.removeButton.TabStop = false;
            toolTipInfo9.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo9.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image8")));
            toolTipInfo9.Body.Size = new System.Drawing.Size(48, 48);
            toolTipInfo9.Body.Text = "Remove selected entry from the database. (Employees and Customers only).";
            toolTipInfo9.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo9.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo9.ForeColor = System.Drawing.Color.White;
            toolTipInfo9.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo9.Header.Text = "Remove Entry";
            this.toolTip.SetToolTip(this.removeButton, toolTipInfo9);
            this.removeButton.UseVisualStyleBackColor = false;
            this.removeButton.Click += new System.EventHandler(this.RemoveButton_Click);
            // 
            // toolTip
            // 
            this.toolTip.GradientBackGround = false;
            this.toolTip.InitialDelay = 500;
            this.toolTip.MaxWidth = 300;
            this.toolTip.MetroColor = System.Drawing.Color.FromArgb(((int)(((byte)(17)))), ((int)(((byte)(158)))), ((int)(((byte)(218)))));
            this.toolTip.ToolTipDuration = 20;
            this.toolTip.UseFading = Syncfusion.Windows.Forms.Tools.SuperToolTip.FadingType.System;
            this.toolTip.VisualStyle = Syncfusion.Windows.Forms.Tools.SuperToolTip.Appearance.Metro;
            // 
            // searchTextBox
            // 
            this.searchTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.searchTextBox.Location = new System.Drawing.Point(442, 30);
            this.searchTextBox.Name = "searchTextBox";
            this.searchTextBox.Size = new System.Drawing.Size(195, 20);
            this.searchTextBox.TabIndex = 4;
            this.searchTextBox.TabStop = false;
            toolTipInfo8.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo8.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image7")));
            toolTipInfo8.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo8.Body.Text = "The value to search in the database. Must be exact to yield results. To end a sea" +
    "rch, clear this.";
            toolTipInfo8.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo8.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo8.ForeColor = System.Drawing.Color.White;
            toolTipInfo8.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo8.Header.Text = "Search Text Box";
            this.toolTip.SetToolTip(this.searchTextBox, toolTipInfo8);
            this.searchTextBox.TextChanged += new System.EventHandler(this.SearchTextBox_TextChanged);
            // 
            // searchButton
            // 
            this.searchButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.searchButton.BackColor = System.Drawing.Color.LightSalmon;
            this.searchButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.searchButton.ForeColor = System.Drawing.Color.White;
            this.searchButton.Location = new System.Drawing.Point(643, 29);
            this.searchButton.Name = "searchButton";
            this.searchButton.Size = new System.Drawing.Size(84, 21);
            this.searchButton.TabIndex = 5;
            this.searchButton.TabStop = false;
            this.searchButton.Text = "Search";
            toolTipInfo7.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo7.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image6")));
            toolTipInfo7.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo7.Body.Text = "Search the database for the entered entry. Fuzzy searching is supported. To clear" +
    " the search, clear the search text box.";
            toolTipInfo7.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo7.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo7.Footer.Text = "";
            toolTipInfo7.ForeColor = System.Drawing.Color.White;
            toolTipInfo7.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo7.Header.Text = "Search";
            this.toolTip.SetToolTip(this.searchButton, toolTipInfo7);
            this.searchButton.UseVisualStyleBackColor = false;
            this.searchButton.Click += new System.EventHandler(this.SearchButton_Click);
            // 
            // searchComboBox
            // 
            this.searchComboBox.AllowNewText = false;
            this.searchComboBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.searchComboBox.BackColor = System.Drawing.Color.White;
            this.searchComboBox.BeforeTouchSize = new System.Drawing.Size(121, 21);
            this.searchComboBox.DisplayMember = "0";
            this.searchComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.searchComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.searchComboBox.GrayOnReadOnly = false;
            this.searchComboBox.Items.AddRange(new object[] {
            "Id",
            "Name",
            "Job",
            "Address",
            "Phone"});
            this.searchComboBox.Location = new System.Drawing.Point(315, 30);
            this.searchComboBox.Name = "searchComboBox";
            this.searchComboBox.Size = new System.Drawing.Size(121, 21);
            this.searchComboBox.Style = Syncfusion.Windows.Forms.VisualStyle.Metro;
            this.searchComboBox.TabIndex = 6;
            this.searchComboBox.TabStop = false;
            this.searchComboBox.Text = "Id";
            toolTipInfo6.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo6.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image5")));
            toolTipInfo6.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo6.Body.Text = "The entry category to search the database for.";
            toolTipInfo6.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo6.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo6.ForeColor = System.Drawing.Color.White;
            toolTipInfo6.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo6.Header.Text = "Search Category";
            this.toolTip.SetToolTip(this.searchComboBox, toolTipInfo6);
            this.searchComboBox.UseMetroColorsInActiveMode = true;
            // 
            // addCustomerButton
            // 
            this.addCustomerButton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.addCustomerButton.BackColor = System.Drawing.Color.White;
            this.addCustomerButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.addCustomerButton.Image = ((System.Drawing.Image)(resources.GetObject("addCustomerButton.Image")));
            this.addCustomerButton.Location = new System.Drawing.Point(745, 165);
            this.addCustomerButton.Name = "addCustomerButton";
            this.addCustomerButton.Size = new System.Drawing.Size(37, 37);
            this.addCustomerButton.TabIndex = 7;
            this.addCustomerButton.TabStop = false;
            toolTipInfo5.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo5.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image4")));
            toolTipInfo5.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo5.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo5.Body.Text = "Add a new customer in the database.";
            toolTipInfo5.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo5.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo5.ForeColor = System.Drawing.Color.White;
            toolTipInfo5.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo5.Header.Text = "Add Customer";
            this.toolTip.SetToolTip(this.addCustomerButton, toolTipInfo5);
            this.addCustomerButton.UseVisualStyleBackColor = false;
            this.addCustomerButton.Click += new System.EventHandler(this.AddCustomerButton_Click);
            // 
            // searchRangeComboBox
            // 
            this.searchRangeComboBox.AllowNewText = false;
            this.searchRangeComboBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.searchRangeComboBox.BackColor = System.Drawing.Color.White;
            this.searchRangeComboBox.BeforeTouchSize = new System.Drawing.Size(121, 21);
            this.searchRangeComboBox.DisplayMember = "0";
            this.searchRangeComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.searchRangeComboBox.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(161)));
            this.searchRangeComboBox.GrayOnReadOnly = false;
            this.searchRangeComboBox.Items.AddRange(new object[] {
            "All",
            "Employees",
            "Customers"});
            this.searchRangeComboBox.Location = new System.Drawing.Point(188, 30);
            this.searchRangeComboBox.Name = "searchRangeComboBox";
            this.searchRangeComboBox.Size = new System.Drawing.Size(121, 21);
            this.searchRangeComboBox.Style = Syncfusion.Windows.Forms.VisualStyle.Metro;
            this.searchRangeComboBox.TabIndex = 8;
            this.searchRangeComboBox.TabStop = false;
            this.searchRangeComboBox.Text = "All";
            toolTipInfo4.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo4.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image3")));
            toolTipInfo4.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo4.Body.Text = "The tables to search in.";
            toolTipInfo4.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo4.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo4.ForeColor = System.Drawing.Color.White;
            toolTipInfo4.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo4.Header.Text = "Table Range";
            this.toolTip.SetToolTip(this.searchRangeComboBox, toolTipInfo4);
            this.searchRangeComboBox.UseMetroColorsInActiveMode = true;
            this.searchRangeComboBox.SelectedIndexChanged += new System.EventHandler(this.SearchCategoryComboBox_SelectedIndexChanged);
            // 
            // printGridButton
            // 
            this.printGridButton.BackColor = System.Drawing.Color.White;
            this.printGridButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.printGridButton.Image = ((System.Drawing.Image)(resources.GetObject("printGridButton.Image")));
            this.printGridButton.Location = new System.Drawing.Point(87, 13);
            this.printGridButton.Name = "printGridButton";
            this.printGridButton.Size = new System.Drawing.Size(37, 37);
            this.printGridButton.TabIndex = 9;
            this.printGridButton.TabStop = false;
            toolTipInfo3.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo3.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image2")));
            toolTipInfo3.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo3.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo3.Body.Text = "Print a table report from the database.";
            toolTipInfo3.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo3.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo3.ForeColor = System.Drawing.Color.White;
            toolTipInfo3.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo3.Header.Text = "Print Report";
            this.toolTip.SetToolTip(this.printGridButton, toolTipInfo3);
            this.printGridButton.UseVisualStyleBackColor = false;
            this.printGridButton.Click += new System.EventHandler(this.PrintGridButton_Click);
            // 
            // settingsButton
            // 
            this.settingsButton.BackColor = System.Drawing.Color.White;
            this.settingsButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.settingsButton.Image = ((System.Drawing.Image)(resources.GetObject("settingsButton.Image")));
            this.settingsButton.Location = new System.Drawing.Point(40, 13);
            this.settingsButton.Name = "settingsButton";
            this.settingsButton.Size = new System.Drawing.Size(37, 37);
            this.settingsButton.TabIndex = 11;
            this.settingsButton.TabStop = false;
            toolTipInfo2.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo2.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image1")));
            toolTipInfo2.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo2.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.Body.Text = "Open settings and database maintenance.";
            toolTipInfo2.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo2.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.ForeColor = System.Drawing.Color.White;
            toolTipInfo2.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.Header.Text = "Settings and Database Maintenance";
            this.toolTip.SetToolTip(this.settingsButton, toolTipInfo2);
            this.settingsButton.UseVisualStyleBackColor = false;
            this.settingsButton.Click += new System.EventHandler(this.SettingsButton_Click);
            // 
            // tableResetButton
            // 
            this.tableResetButton.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.tableResetButton.BackColor = System.Drawing.Color.White;
            this.tableResetButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.tableResetButton.Image = ((System.Drawing.Image)(resources.GetObject("tableResetButton.Image")));
            this.tableResetButton.Location = new System.Drawing.Point(745, 338);
            this.tableResetButton.Name = "tableResetButton";
            this.tableResetButton.Size = new System.Drawing.Size(37, 37);
            this.tableResetButton.TabIndex = 12;
            this.tableResetButton.TabStop = false;
            toolTipInfo1.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo1.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image")));
            toolTipInfo1.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo1.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Body.Text = "Open Table Reset. This tool allows clearance of some tables which is useful for s" +
    "tarting a new week.";
            toolTipInfo1.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo1.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.ForeColor = System.Drawing.Color.White;
            toolTipInfo1.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Header.Text = "Table Reset";
            this.toolTip.SetToolTip(this.tableResetButton, toolTipInfo1);
            this.tableResetButton.UseVisualStyleBackColor = false;
            this.tableResetButton.Click += new System.EventHandler(this.TableResetButton_Click);
            // 
            // gridDocument
            // 
            this.gridDocument.EndPrint += new System.Drawing.Printing.PrintEventHandler(this.GridDocument_EndPrint);
            this.gridDocument.PrintPage += new System.Drawing.Printing.PrintPageEventHandler(this.GridDocument_PrintPage);
            // 
            // label1
            // 
            this.label1.Anchor = System.Windows.Forms.AnchorStyles.Right;
            this.label1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
            this.label1.Location = new System.Drawing.Point(733, 304);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(60, 2);
            this.label1.TabIndex = 13;
            // 
            // encryptionWorker
            // 
            this.encryptionWorker.WorkerSupportsCancellation = true;
            this.encryptionWorker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.EncryptionWorker_DoWork);
            this.encryptionWorker.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.EncryptionWorker_WorkCompleted);
            // 
            // fec_Main
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CaptionBarColor = System.Drawing.Color.DeepSkyBlue;
            this.CaptionBarHeight = 56;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionFont = new System.Drawing.Font("Microsoft Sans Serif", 9.25F);
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(30, -2);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(60, 60);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(798, 499);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.tableResetButton);
            this.Controls.Add(this.settingsButton);
            this.Controls.Add(this.printGridButton);
            this.Controls.Add(this.searchRangeComboBox);
            this.Controls.Add(this.addCustomerButton);
            this.Controls.Add(this.databaseGrid);
            this.Controls.Add(this.searchComboBox);
            this.Controls.Add(this.searchButton);
            this.Controls.Add(this.searchTextBox);
            this.Controls.Add(this.removeButton);
            this.Controls.Add(this.addEmployeeButton);
            this.Name = "fec_Main";
            this.ShowIcon = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Smile Family Entertainment Center Database";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Fec_Main_FormClosing);
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Fec_Main_FormClosed);
            this.ResizeEnd += new System.EventHandler(this.Fec_Main_ResizeEnd);
            this.SizeChanged += new System.EventHandler(this.Fec_Main_SizeChanged);
            ((System.ComponentModel.ISupportInitialize)(this.databaseGrid)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.searchComboBox)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.searchRangeComboBox)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private Syncfusion.Windows.Forms.Grid.Grouping.GridGroupingControl databaseGrid;
        private System.Windows.Forms.Button addEmployeeButton;
        private System.Windows.Forms.Button removeButton;
        private Syncfusion.Windows.Forms.Tools.SuperToolTip toolTip;
        private System.Windows.Forms.Button searchButton;
        private System.Windows.Forms.TextBox searchTextBox;
        private Syncfusion.Windows.Forms.Tools.ComboBoxAdv searchComboBox;
        private System.Windows.Forms.Button addCustomerButton;
        private Syncfusion.Windows.Forms.Tools.ComboBoxAdv searchRangeComboBox;
        private System.Windows.Forms.Button printGridButton;
        private System.Drawing.Printing.PrintDocument gridDocument;
        private System.Windows.Forms.Button settingsButton;
        private System.Windows.Forms.Button tableResetButton;
        private System.Windows.Forms.Label label1;
        private System.ComponentModel.BackgroundWorker encryptionWorker;
    }
}

