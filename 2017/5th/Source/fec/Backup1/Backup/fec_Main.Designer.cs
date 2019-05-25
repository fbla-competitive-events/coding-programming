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
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo2 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo1 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.CaptionImage captionImage1 = new Syncfusion.Windows.Forms.CaptionImage();
            this.employeeGrid = new Syncfusion.Windows.Forms.Grid.Grouping.GridGroupingControl();
            this.addEmployeeButton = new System.Windows.Forms.Button();
            this.removeEmployeeButton = new System.Windows.Forms.Button();
            this.toolTip = new Syncfusion.Windows.Forms.Tools.SuperToolTip(this);
            this.button1 = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.employeeGrid)).BeginInit();
            this.SuspendLayout();
            // 
            // employeeGrid
            // 
            this.employeeGrid.AllowedOptimizations = ((Syncfusion.Grouping.EngineOptimizations)((((Syncfusion.Grouping.EngineOptimizations.DisableCounters | Syncfusion.Grouping.EngineOptimizations.VirtualMode) 
            | Syncfusion.Grouping.EngineOptimizations.PassThroughSort) 
            | Syncfusion.Grouping.EngineOptimizations.RecordsAsDisplayElements)));
            this.employeeGrid.AlphaBlendSelectionColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(94)))), ((int)(((byte)(171)))), ((int)(((byte)(222)))));
            this.employeeGrid.BackColor = System.Drawing.SystemColors.Window;
            this.employeeGrid.CacheRecordValues = false;
            this.employeeGrid.ChildGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.employeeGrid.ChildGroupOptions.ShowFilterBar = true;
            this.employeeGrid.CounterLogic = Syncfusion.Grouping.EngineCounters.YAmount;
            this.employeeGrid.GridOfficeScrollBars = Syncfusion.Windows.Forms.OfficeScrollBars.Metro;
            this.employeeGrid.GridVisualStyles = Syncfusion.Windows.Forms.GridVisualStyles.Metro;
            this.employeeGrid.HorizontalScrollTips = true;
            this.employeeGrid.Location = new System.Drawing.Point(29, 21);
            this.employeeGrid.MarkColHeader = true;
            this.employeeGrid.Name = "employeeGrid";
            this.employeeGrid.NestedTableGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.employeeGrid.NestedTableGroupOptions.ShowFilterBar = true;
            this.employeeGrid.NestedTableGroupOptions.ShowStackedHeaders = true;
            this.employeeGrid.Office2007ScrollBars = true;
            this.employeeGrid.ShowCurrentCellBorderBehavior = Syncfusion.Windows.Forms.Grid.GridShowCurrentCellBorder.GrayWhenLostFocus;
            this.employeeGrid.ShowRelationFields = Syncfusion.Grouping.ShowRelationFields.Hide;
            this.employeeGrid.Size = new System.Drawing.Size(586, 369);
            this.employeeGrid.TabIndex = 0;
            this.employeeGrid.TableDescriptor.AllowNew = false;
            this.employeeGrid.TableDescriptor.TableOptions.CaptionRowHeight = 29;
            this.employeeGrid.TableDescriptor.TableOptions.ColumnHeaderRowHeight = 25;
            this.employeeGrid.TableDescriptor.TableOptions.RecordRowHeight = 25;
            this.employeeGrid.TableOptions.DefaultColumnWidth = 469;
            this.employeeGrid.TableOptions.IndentWidth = 0;
            this.employeeGrid.TableOptions.RecordPreviewRowHeight = 32;
            this.employeeGrid.TableOptions.RecordRowHeight = 32;
            this.employeeGrid.TableOptions.RowHeaderWidth = 18;
            this.employeeGrid.TableOptions.ShowRowHeader = true;
            this.employeeGrid.TableOptions.ShowTableIndent = true;
            this.employeeGrid.Text = "gridGroupingControl1";
            this.employeeGrid.TopLevelGroupOptions.ShowAddNewRecordAfterDetails = false;
            this.employeeGrid.TopLevelGroupOptions.ShowAddNewRecordBeforeDetails = false;
            this.employeeGrid.TopLevelGroupOptions.ShowColumnHeaders = true;
            this.employeeGrid.TopLevelGroupOptions.ShowFilterBar = true;
            this.employeeGrid.UseDefaultsForFasterDrawing = true;
            this.employeeGrid.UseRightToLeftCompatibleTextBox = true;
            this.employeeGrid.VersionInfo = "14.3200.0.49";
            this.employeeGrid.RecordValueChanging += new Syncfusion.Grouping.RecordValueChangingEventHandler(this.employeeGrid_RecordValueChanging);
            this.employeeGrid.RecordValueChanged += new Syncfusion.Grouping.RecordValueChangedEventHandler(this.employeeGrid_RecordValueChanged);
            // 
            // addEmployeeButton
            // 
            this.addEmployeeButton.Image = ((System.Drawing.Image)(resources.GetObject("addEmployeeButton.Image")));
            this.addEmployeeButton.Location = new System.Drawing.Point(634, 82);
            this.addEmployeeButton.Name = "addEmployeeButton";
            this.addEmployeeButton.Size = new System.Drawing.Size(37, 37);
            this.addEmployeeButton.TabIndex = 1;
            toolTipInfo2.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo2.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image1")));
            toolTipInfo2.Body.ImageScalingSize = new System.Drawing.Size(48, 48);
            toolTipInfo2.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.Body.Text = "Add a new employee in the database.";
            toolTipInfo2.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo2.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.ForeColor = System.Drawing.Color.White;
            toolTipInfo2.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo2.Header.Text = "Add Employee";
            this.toolTip.SetToolTip(this.addEmployeeButton, toolTipInfo2);
            this.addEmployeeButton.UseVisualStyleBackColor = true;
            this.addEmployeeButton.Click += new System.EventHandler(this.addEmployeeButton_Click);
            // 
            // removeEmployeeButton
            // 
            this.removeEmployeeButton.Image = ((System.Drawing.Image)(resources.GetObject("removeEmployeeButton.Image")));
            this.removeEmployeeButton.Location = new System.Drawing.Point(634, 138);
            this.removeEmployeeButton.Name = "removeEmployeeButton";
            this.removeEmployeeButton.Size = new System.Drawing.Size(37, 37);
            this.removeEmployeeButton.TabIndex = 2;
            toolTipInfo1.BackColor = System.Drawing.Color.DeepSkyBlue;
            toolTipInfo1.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image")));
            toolTipInfo1.Body.Size = new System.Drawing.Size(48, 48);
            toolTipInfo1.Body.Text = "Remove selected employee records from the database.";
            toolTipInfo1.Body.TextMargin = new System.Windows.Forms.Padding(0, 18, 0, 0);
            toolTipInfo1.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.ForeColor = System.Drawing.Color.White;
            toolTipInfo1.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Header.Text = "Remove Employee(s)";
            this.toolTip.SetToolTip(this.removeEmployeeButton, toolTipInfo1);
            this.removeEmployeeButton.UseVisualStyleBackColor = true;
            this.removeEmployeeButton.Click += new System.EventHandler(this.removeEmployeeButton_Click);
            // 
            // toolTip
            // 
            this.toolTip.GradientBackGround = false;
            this.toolTip.MaxWidth = 300;
            this.toolTip.MetroColor = System.Drawing.Color.FromArgb(((int)(((byte)(17)))), ((int)(((byte)(158)))), ((int)(((byte)(218)))));
            this.toolTip.VisualStyle = Syncfusion.Windows.Forms.Tools.SuperToolTip.Appearance.Metro;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(616, 395);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 3;
            this.button1.Text = "button1";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // fec_Main
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CaptionBarColor = System.Drawing.Color.DeepSkyBlue;
            this.CaptionBarHeight = 52;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(30, -3);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(60, 60);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(693, 416);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.removeEmployeeButton);
            this.Controls.Add(this.addEmployeeButton);
            this.Controls.Add(this.employeeGrid);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "fec_Main";
            this.ShowIcon = false;
            this.Text = "Smile Family Entertainment Center Database";
            ((System.ComponentModel.ISupportInitialize)(this.employeeGrid)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private Syncfusion.Windows.Forms.Grid.Grouping.GridGroupingControl employeeGrid;
        private System.Windows.Forms.Button addEmployeeButton;
        private System.Windows.Forms.Button removeEmployeeButton;
        private Syncfusion.Windows.Forms.Tools.SuperToolTip toolTip;
        private System.Windows.Forms.Button button1;
    }
}

