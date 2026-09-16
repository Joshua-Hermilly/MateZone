using MateZone.controleur;
using System.Windows;

namespace MateZone
{
    /// <summary>
    /// Interaction logic for App.xaml
    /// </summary>
    public partial class App : Application
    {
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);
            
            // Le contrôleur crée et gère la fenêtre
            _ = new Controleur();
        }
    }
}
