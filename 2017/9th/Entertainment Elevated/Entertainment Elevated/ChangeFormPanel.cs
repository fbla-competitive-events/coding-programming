using System.Windows.Forms;

namespace Entertainment_Elevated
{
    public static class ChangeFormPanel
    {
        // This method is what allows the program to switch between different panels
        // Easier on the user as there is only one window, not multiple windows
        // This method is also used in many different files to switch between panels

        // This method takes in an object sender, which is used to find the form
        // This will always be a button that is clicked

        // The use of a generic method allows all the different types of forms with panels to be generalized
        // This type, T, must inherit from the two classes listed below as well as have an empty constructor
        public static void ChangeFormPanels<T>(object sender)
            where T : Form, IPanelForm, new()
        {
            // The button is the sender, so this Control will be the button
            Control obj = (Control)sender;

            // This is done so the form of the button can be found
            // This form will always be the blank, GeneralForm so an implicit conversion can be done
            GeneralForm general = (GeneralForm)obj.FindForm();

            // A form of our wanted screen is created
            T form = new T();

            // All the controls on the GeneralForm are cleared so that the new controls can be added
            general.Controls.Clear();

            // Add the wanted panel onto the GeneralForm to display it
            general.Controls.Add(form.Panel());
        }
    }
}
