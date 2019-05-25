namespace Entertainment_Elevated
{
    partial class HelpForm
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
            this.HelpFormPanel = new System.Windows.Forms.Panel();
            this.MenuButton = new System.Windows.Forms.Button();
            this.HelpLabel = new System.Windows.Forms.Label();
            this.HelpFormPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // HelpFormPanel
            // 
            this.HelpFormPanel.Controls.Add(this.HelpLabel);
            this.HelpFormPanel.Controls.Add(this.MenuButton);
            this.HelpFormPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.HelpFormPanel.Location = new System.Drawing.Point(0, 0);
            this.HelpFormPanel.Name = "HelpFormPanel";
            this.HelpFormPanel.Size = new System.Drawing.Size(584, 461);
            this.HelpFormPanel.TabIndex = 0;
            // 
            // MenuButton
            // 
            this.MenuButton.Location = new System.Drawing.Point(12, 426);
            this.MenuButton.Name = "MenuButton";
            this.MenuButton.Size = new System.Drawing.Size(75, 23);
            this.MenuButton.TabIndex = 1;
            this.MenuButton.Text = "Menu";
            this.MenuButton.UseVisualStyleBackColor = true;
            this.MenuButton.Click += new System.EventHandler(this.MenuButton_Click);
            // 
            // HelpLabel
            // 
            this.HelpLabel.AutoSize = true;
            this.HelpLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 14.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.HelpLabel.Location = new System.Drawing.Point(3, 56);
            this.HelpLabel.Name = "HelpLabel";
            this.HelpLabel.Size = new System.Drawing.Size(60, 24);
            this.HelpLabel.TabIndex = 2;
            this.HelpLabel.Text = "label1";
            // 
            // HelpForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(584, 461);
            this.Controls.Add(this.HelpFormPanel);
            this.Name = "HelpForm";
            this.Text = "HelpForm";
            this.HelpFormPanel.ResumeLayout(false);
            this.HelpFormPanel.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        public System.Windows.Forms.Panel HelpFormPanel;
        private System.Windows.Forms.Button MenuButton;
        private System.Windows.Forms.Label HelpLabel;
    }
}