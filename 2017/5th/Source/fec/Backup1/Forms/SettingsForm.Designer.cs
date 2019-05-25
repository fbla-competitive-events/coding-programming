namespace fec {
    partial class SettingsForm {
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(SettingsForm));
            Syncfusion.Windows.Forms.Tools.ToolTipInfo toolTipInfo1 = new Syncfusion.Windows.Forms.Tools.ToolTipInfo();
            Syncfusion.Windows.Forms.CaptionImage captionImage1 = new Syncfusion.Windows.Forms.CaptionImage();
            this.compactDatabaseButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.toolTip = new Syncfusion.Windows.Forms.Tools.SuperToolTip(this);
            this.SuspendLayout();
            // 
            // compactDatabaseButton
            // 
            this.compactDatabaseButton.BackColor = System.Drawing.Color.White;
            this.compactDatabaseButton.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.compactDatabaseButton.Image = ((System.Drawing.Image)(resources.GetObject("compactDatabaseButton.Image")));
            this.compactDatabaseButton.Location = new System.Drawing.Point(154, 75);
            this.compactDatabaseButton.Name = "compactDatabaseButton";
            this.compactDatabaseButton.Size = new System.Drawing.Size(93, 88);
            this.compactDatabaseButton.TabIndex = 10;
            toolTipInfo1.BackColor = System.Drawing.Color.Coral;
            toolTipInfo1.Body.ForeColor = System.Drawing.Color.White;
            toolTipInfo1.Body.Image = ((System.Drawing.Image)(resources.GetObject("resource.Image")));
            toolTipInfo1.Body.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Body.Text = "Rebuild the database and repack it so as to use minimal disk space. Should be don" +
    "e on a regular basis.";
            toolTipInfo1.Body.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
            toolTipInfo1.Footer.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Header.ForeColor = System.Drawing.Color.White;
            toolTipInfo1.Header.Size = new System.Drawing.Size(20, 20);
            toolTipInfo1.Header.Text = "Compact Database";
            this.toolTip.SetToolTip(this.compactDatabaseButton, toolTipInfo1);
            this.compactDatabaseButton.UseVisualStyleBackColor = false;
            this.compactDatabaseButton.Click += new System.EventHandler(this.compactDatabaseButton_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F);
            this.label1.Location = new System.Drawing.Point(151, 59);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(98, 13);
            this.label1.TabIndex = 11;
            this.label1.Text = "Compact Database";
            // 
            // toolTip
            // 
            this.toolTip.GradientBackGround = false;
            this.toolTip.InitialDelay = 500;
            this.toolTip.MaxWidth = 300;
            this.toolTip.MetroColor = System.Drawing.Color.Coral;
            this.toolTip.ToolTipDuration = 20;
            this.toolTip.UseFading = Syncfusion.Windows.Forms.Tools.SuperToolTip.FadingType.System;
            this.toolTip.VisualStyle = Syncfusion.Windows.Forms.Tools.SuperToolTip.Appearance.Metro;
            // 
            // SettingsForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.CaptionBarColor = System.Drawing.Color.Coral;
            this.CaptionBarHeight = 42;
            this.CaptionButtonColor = System.Drawing.Color.White;
            this.CaptionButtonHoverColor = System.Drawing.Color.LightGray;
            this.CaptionForeColor = System.Drawing.Color.White;
            captionImage1.BackColor = System.Drawing.Color.Transparent;
            captionImage1.Image = ((System.Drawing.Image)(resources.GetObject("captionImage1.Image")));
            captionImage1.Location = new System.Drawing.Point(18, -3);
            captionImage1.Name = "CaptionImage1";
            captionImage1.Size = new System.Drawing.Size(48, 48);
            this.CaptionImages.Add(captionImage1);
            this.ClientSize = new System.Drawing.Size(419, 253);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.compactDatabaseButton);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MetroColor = System.Drawing.Color.Coral;
            this.Name = "SettingsForm";
            this.ShowIcon = false;
            this.ShowMaximizeBox = false;
            this.ShowMinimizeBox = false;
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterParent;
            this.Text = "Settings & Database Maintenance";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button compactDatabaseButton;
        private System.Windows.Forms.Label label1;
        private Syncfusion.Windows.Forms.Tools.SuperToolTip toolTip;
    }
}