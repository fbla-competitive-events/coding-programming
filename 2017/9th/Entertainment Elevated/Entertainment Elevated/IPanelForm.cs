using System.Windows.Forms;

namespace Entertainment_Elevated
{
    // This is the interface used to generalize all the forms that use panels
    public interface IPanelForm
    {
        // Method that returns the panel of the form
        Panel Panel();
    }
}
