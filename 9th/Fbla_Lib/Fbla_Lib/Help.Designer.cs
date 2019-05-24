namespace Fbla_Lib
{
    partial class Help
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.listHelp = new System.Windows.Forms.ListBox();
            this.HelpDisplay = new System.Windows.Forms.WebBrowser();
            this.label_JakeSmithForver = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // listHelp
            // 
            this.listHelp.FormattingEnabled = true;
            this.listHelp.Items.AddRange(new object[] {
            "How To Add A New Book",
            "How To Add A New Staff",
            "How To Add A New Student",
            "Checking Out A Staff\'s Book",
            "Checking Out A Student\'s Book",
            "Returning A Staff\'s Book",
            "Returning A Student\'s Book"});
            this.listHelp.Location = new System.Drawing.Point(12, 12);
            this.listHelp.Name = "listHelp";
            this.listHelp.Size = new System.Drawing.Size(188, 394);
            this.listHelp.TabIndex = 0;
            this.listHelp.SelectedIndexChanged += new System.EventHandler(this.listHelp_SelectedIndexChanged);
            // 
            // HelpDisplay
            // 
            this.HelpDisplay.Location = new System.Drawing.Point(206, 12);
            this.HelpDisplay.MinimumSize = new System.Drawing.Size(20, 20);
            this.HelpDisplay.Name = "HelpDisplay";
            this.HelpDisplay.Size = new System.Drawing.Size(466, 394);
            this.HelpDisplay.TabIndex = 1;
            // 
            // label_JakeSmithForver
            // 
            this.label_JakeSmithForver.AutoSize = true;
            this.label_JakeSmithForver.Location = new System.Drawing.Point(562, 409);
            this.label_JakeSmithForver.Name = "label_JakeSmithForver";
            this.label_JakeSmithForver.Size = new System.Drawing.Size(110, 13);
            this.label_JakeSmithForver.TabIndex = 4;
            this.label_JakeSmithForver.Text = "(c) Jacob Smith 2018-";
            // 
            // Help
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(684, 430);
            this.Controls.Add(this.label_JakeSmithForver);
            this.Controls.Add(this.HelpDisplay);
            this.Controls.Add(this.listHelp);
            this.Name = "Help";
            this.Text = "WHS Library Help Center";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox listHelp;
        private System.Windows.Forms.WebBrowser HelpDisplay;
        private System.Windows.Forms.Label label_JakeSmithForver;
    }
}