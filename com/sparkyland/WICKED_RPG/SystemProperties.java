import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.*; // .com.ms./security/permissions/

public class SystemProperties extends Applet
{
	Button popertiesButton;
	DieFrame frame;
	String properties[] = { "spartique.version", "java.version", "java.class.version", "java.vendor", 
		"java.vendor.url", "os.name", "os.version", "os.arch", "file.separator", "path.separator", "line.separator" };

    public void init()
	{
		popertiesButton = new Button("Properties");
		this.add(popertiesButton);
	}
	public void start()
	{
		super.start();
	}
    public boolean action(Event e, Object arg)
	{
		System.out.println(e.target);
		if ( e.target ==  popertiesButton )
		{
			frame = new DieFrame("System Properties");
			TextArea textArea = new TextArea("",20,50);

			String property = new String();
			String value = new String();

			try
			{
				System.out.println("Get the property1: ");
				Properties poperties = System.getProperties();
				System.out.println("Get the property2: ");

				for (int i = 0; i < properties.length; i++ )
				{
					try
					{
						property = properties[i];
						System.out.println("Get this property: " + property);
						value = (String)( poperties.getProperty( property ) );
					}
					catch (Throwable ex)
					{
						System.out.println("This property: " + property + " is unavailable");
						value = "N/A";
					}
					textArea.appendText( property + "\t\t" + value + "\n" );
				}
			}
			catch (Throwable ex)
			{
				textArea.appendText( "System Properties not available" );
			}

			//frame.addNotify(this);
			frame.add(textArea);
			frame.pack();
			frame.show();
        }
		return true;
    }
	public void stop()
	{
		System.out.println("Close.");
		frame.dispose();
	}
}

class DieFrame extends Frame implements ActionListener {

    DieFrame(String s)
	{
		super(s);
		Button cancel = new Button("Close");
		cancel.addActionListener(this);
		add("South", cancel );
    }

    public void actionPerformed(ActionEvent e)
	{
		System.out.println("Close.");
		setVisible(false);
		dispose();
    }
}